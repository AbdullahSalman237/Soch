package com.example.soch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MedicationHandler extends SQLiteOpenHelper {


    //medication time ka table
    private static final String DB_NAME = "Medicine";
    private static final int DB_VERSION = 1;
    private static final String MED_NAME = "array_table";
    private static final String MED_COLUMN = "array_value";

    public MedicationHandler(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + MED_NAME + "("

                + MED_COLUMN + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    //adding the medication times
    public void addArrayValue(String textView1Value) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MED_COLUMN, textView1Value);

        db.insert(MED_NAME, null, values);
        db.close();
    }


    //retrieving the medication times
    public Cursor getArrayValues() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + MED_NAME, null);
        if(cursor==null)
        {
            return null;
        }
        return cursor;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + MED_NAME);

        onCreate(db);

    }
}
