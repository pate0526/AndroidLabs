package com.example.srushtipatel.androidlabs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    private static final String ACTIVITY_NAME = "ChatDatabaseHelper";
    private static final String DATABASE_NAME = "Messages.db";
    private static int VERSION_NUM = 1;
    private static final String TABLE_NAME = "CHAT";
    private final static String KEY_ID = "KeyId";
    private final static String KEY_MESSAGE = "keymessage";
    private final static String[] ALL_COLUMNS = new String[]{KEY_ID, KEY_MESSAGE};


    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_MESSAGE + " TEXT);");
        Log.i(ACTIVITY_NAME, "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(ACTIVITY_NAME, "Calling onUpgrade");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertMessage(String message) {

        final ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_MESSAGE, message);
        final SQLiteDatabase db = getWritableDatabase();
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result > 0) {
            Log.i(ACTIVITY_NAME, "Data Inserted Successfully" + result);
        } else {
            Log.i(ACTIVITY_NAME, "Data Insert failed" + result);
        }
    }

    public List<String> getAllMessages() {
        final SQLiteDatabase db = this.getWritableDatabase();
        final List<String> messages = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, ALL_COLUMNS, null, null, null, null, null);
        Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + cursor.getColumnCount());

        while (cursor.moveToNext()) {
            final String msg = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + msg);
            messages.add(msg);
        }

        for (int i = 0; i < cursor.getColumnCount(); i++) {
            Log.i(ACTIVITY_NAME, "Cursor’s column Name =" + cursor.getColumnName(i));
        }

        cursor.close();
        return messages;
    }

}

