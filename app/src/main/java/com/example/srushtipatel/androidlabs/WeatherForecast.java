    package com.example.srushtipatel.androidlabs;

    import android.app.Activity;
    import android.content.Context;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.util.Log;
    import android.util.Xml;
    import android.view.View;
    import android.widget.ImageView;
    import android.widget.ProgressBar;
    import android.widget.TextView;

    import org.xmlpull.v1.XmlPullParser;
    import org.xmlpull.v1.XmlPullParserException;
    import org.xmlpull.v1.XmlPullParserFactory;

    import java.io.File;
    import java.io.FileInputStream;
    import java.io.FileNotFoundException;
    import java.io.FileOutputStream;
    import java.io.IOException;
    import java.io.InputStream;
    import java.net.HttpURLConnection;
    import java.net.MalformedURLException;
    import java.net.URL;

    public class WeatherForecast extends Activity {

        protected static final String URL_STRING = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
        protected static final String URL_IMAGE = "http://openweathermap.org/img/w/";
        protected static final String ACTIVITY_NAME = "WeatherForecast";
        private ProgressBar progressBar;
        private TextView currentTemperature;
        private TextView minTempTemperature;
        private TextView maxTempTemperature;
        private TextView windSpeed2;
        private ImageView image1;
        private TextView targetLocation;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_weather_forecast);

            targetLocation = findViewById(R.id.targetLocation);
            progressBar = findViewById(R.id.progress);
            this.progressBar.setVisibility(View.VISIBLE);
            currentTemperature = findViewById(R.id.currentTemp);
            minTempTemperature = findViewById(R.id.minTemp);
            maxTempTemperature = findViewById(R.id.maxTemp);
            windSpeed2 = findViewById(R.id.windSpeed);
            image1 = findViewById(R.id.image);

            new ForecastQuery().execute(null, null, null);
        }


        public class ForecastQuery extends AsyncTask<String, Integer, String> {

           private String currentTemp = "";
            private String minTemp = "";
            private String maxTemp = "";
            private String windSpeed = "";
            String iconName = "";
            private Bitmap image;
            private String currentLocation ="";

    
            @Override
            protected String doInBackground(String... strings) {
                InputStream inputStream = null;
                try {
                    //connect to Server:
                    URL url = new URL(URL_STRING);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setReadTimeout(10000 /* milliseconds */);
                    urlConnection.setConnectTimeout(15000 /* milliseconds */);
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);
                    // Starts the query
                    urlConnection.connect();
                    inputStream  = urlConnection.getInputStream();
                    inputStream = urlConnection.getInputStream();
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(inputStream, null);

                    int eventType = parser.getEventType();
                    boolean set = false;
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_TAG) {
                            if (parser.getName().equalsIgnoreCase("current")) {
                                set = true;
                            } else if (parser.getName().equalsIgnoreCase("city") && set) {
                                currentLocation = parser.getAttributeValue(null, "name");
                            } else if (parser.getName().equalsIgnoreCase("temperature") && set) {
                                currentTemp = parser.getAttributeValue(null, "value");
                                publishProgress(25);
                                minTemp = parser.getAttributeValue(null, "min");
                                publishProgress(50);
                                maxTemp = parser.getAttributeValue(null, "max");
                                publishProgress(75);}
                            else if(parser.getName().equals("speed")) {
                                windSpeed = parser.getAttributeValue(null, "value");
                                publishProgress(750);
                            } else if (parser.getName().equalsIgnoreCase("weather") && set) {
                                iconName = parser.getAttributeValue(null, "icon") + ".png";
                                File file = getBaseContext().getFileStreamPath(iconName);
                                if (!file.exists()) {
                                    saveImage(iconName);
                                } else {
                                    Log.i(ACTIVITY_NAME, "Saved icon, " + iconName + " is displayed.");
                                    try {
                                        FileInputStream in = new FileInputStream(file);
                                        image = BitmapFactory.decodeStream(in);
                                    } catch (FileNotFoundException e) {
                                        Log.i(ACTIVITY_NAME, "Saved icon, " + iconName + " is not found.");
                                    }
                                }
                                publishProgress(100);

                            }
                        } else if (eventType == XmlPullParser.END_TAG) {
                            if (parser.getName().equalsIgnoreCase("current"))
                                set = false;
                        }
                        eventType = parser.next();
                    }

                } catch (IOException e) {
                    Log.i(ACTIVITY_NAME, "IOException: " + e.getMessage());
                } catch (XmlPullParserException e) {
                    Log.i(ACTIVITY_NAME, "XmlPullParserException: " + e.getMessage());
                } finally {
                    if (inputStream != null)
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            Log.i(ACTIVITY_NAME, "IOException: " + e.getMessage());
                        }
                    return null;
                }
            }


            @Override
            public void onProgressUpdate(Integer ... args) //update your GUI
            {
                super.onProgressUpdate(args);
               progressBar.setVisibility(View.VISIBLE);
               progressBar.setProgress(args[0]);
                if (args[0] == 100) {

                }
            }

            public void onPostExecute(String result)  // doInBackground has finished
            {
                super.onPostExecute(result);
                targetLocation.setText("Weather report for " + currentLocation);
                currentTemperature.setText("Current Temperature "+currentTemp);
                minTempTemperature.setText("Minimum Temperature "+minTemp);
                maxTempTemperature.setText("Maximum Temperature "+maxTemp);
                windSpeed2.setText("Wind speed"+windSpeed);
                image1.setImageBitmap(image);
                progressBar.setVisibility(View.INVISIBLE);
            }
            private void saveImage(String fname) {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(URL_IMAGE + fname);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        image = BitmapFactory.decodeStream(connection.getInputStream());
                        FileOutputStream outputStream = openFileOutput(fname, Context.MODE_PRIVATE);
                        image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                        Log.i(ACTIVITY_NAME, "Weather icon, " + fname + " is downloaded and displayed.");
                    } else
                        Log.i(ACTIVITY_NAME, "Can't connect to the weather icon for downloading.");
                } catch (Exception e) {
                    Log.i(ACTIVITY_NAME, "weather icon download error: " + e.getMessage());
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

            }       }
    }
