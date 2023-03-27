package com.example.soch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ScoreHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "Score";
    private static final int DB_VERSION = 1;
    //table for scores

    private static final String TABLE_NAMES = "scores";
    private static final String RESULT = "result";
    private static final String DATE_OF_QUIZ = "date";

    // creating a constructor for our database handler.
    public ScoreHandler(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_SCORES_TABLE = "CREATE TABLE " + TABLE_NAMES + " ("
                + DATE_OF_QUIZ + " TEXT,"
                + RESULT + " TEXT)";
        db.execSQL(CREATE_SCORES_TABLE);
    }

    // Insert score into the table
    public void addScore(String date, String score) {
        SQLiteDatabase db2 = this.getWritableDatabase();
        db2.isOpen();
        ContentValues values = new ContentValues();
        values.put(RESULT, score);
        values.put(DATE_OF_QUIZ, date);
        try {
            db2.beginTransaction();
            db2.insert(TABLE_NAMES, null, values);
            db2.setTransactionSuccessful();
        } finally {
            db2.endTransaction();
        }
        db2.close();

    }

    // Get all scores from the table
    public Cursor getAllScores() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor2;
        cursor2 = db.rawQuery("SELECT * FROM "+TABLE_NAMES,null);

        return cursor2;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAMES);
        String q ="CREATE TABLE "+ TABLE_NAMES + " ("
                + DATE_OF_QUIZ + " TEXT,"
                + RESULT + " TEXT)";

        db.execSQL(q);

    }
}
