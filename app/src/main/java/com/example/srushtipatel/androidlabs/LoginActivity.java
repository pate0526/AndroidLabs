package com.example.srushtipatel.androidlabs;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
    private static final String MY_PREF = "MY_PREF";
    private static final String DEFAULT_EMAIL_KEY = "DEFAULT_EMAIL_KEY";
    protected static final String ACTIVITY_NAME = "LoginActivity";
    private Button loginButton;
    private EditText emailEditText;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        loginButton = findViewById(R.id.login_button);
        emailEditText = findViewById(R.id.email_edit_text);

        sharedPref = getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);

        final String defaultEmail = sharedPref.getString(DEFAULT_EMAIL_KEY, "email@domain.com");
        emailEditText.setText(defaultEmail);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(DEFAULT_EMAIL_KEY, email);
                editor.commit();

                Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }
    @Override
    protected void onStop()
    {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
