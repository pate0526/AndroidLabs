package com.example.srushtipatel.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class ChatWindow extends Activity {

    protected static final String ACTIVITY_NAME = "ChatWindow";
    private  ListView chatListView;
    private  EditText e1;
    private Button b1;
    private List<String> chatMessages;
    private ChatDatabaseHelper chatdatabase;
    private Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        Log.i("ChatWindow", "ChatWindow onCreate()");
        ctx = this;
        chatdatabase = new ChatDatabaseHelper(ctx);

        chatMessages = chatdatabase.getAllMessages();
        chatListView = findViewById(R.id.myList);
        e1 = (EditText) findViewById(R.id.editText);
        b1 = findViewById(R.id.send);

        ChatAdapter messageAdapter = new ChatAdapter(this);
        chatListView.setAdapter(messageAdapter);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = e1.getText().toString();

                chatdatabase.insertMessage(message);
                chatMessages.add(message);
                messageAdapter.notifyDataSetChanged();
                e1.setText("");


            }
        });

    }

    protected void onDestroy() {
        super.onDestroy();
        chatdatabase.close();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

    private class ChatAdapter extends ArrayAdapter<String> {

        public ChatAdapter(Context ctx) {

            super(ctx, 0);

        }

        public int getCount() {
            return chatMessages.size();
        }

        public String getItem(int position) {
            return chatMessages.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;

            if (position % 2 == 0)
               result = inflater.inflate(R.layout.chat_row_incoming, null);

            else

                result = inflater.inflate(R.layout.chat_row_outgoing, null);

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position)); // get the string at position

            return result;

        }

        public long getItemId(int position) {
            return position;
        }
    }
}