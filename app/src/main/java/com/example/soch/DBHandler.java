package com.example.soch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "Soch";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "PatientDetails";
    private static final String TABLE_IMAGE = "ImageTable";
    private static final String NAME_COL = "name";
    private static final String AGE_COL = "age";
    private static final String MED_COL = "med";
    private static final String IMAGE_ID = "image_id";
    private static final String option2 = "option2";
    private static final String option3 = "option3";
    private static final String option4 = "option4";
    private static final String IMAGE_BITMAP = "image_bitmap";

    // creating a constructor for our database handler.
    public DBHandler(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_IMAGE_TABLE = "CREATE TABLE " + TABLE_IMAGE + "("

                + IMAGE_ID + " TEXT,"
                + option2 + " TEXT,"
                + option3 + " TEXT,"
                + option4 + " TEXT,"
                + IMAGE_BITMAP + " TEXT )";
        db.execSQL(CREATE_IMAGE_TABLE);

        String query = "CREATE TABLE " + TABLE_NAME + " ("

                + NAME_COL + " TEXT,"
                + AGE_COL + " TEXT,"
                + MED_COL + " TEXT)";


        db.execSQL(query);
    }
    public void addUser(String PersonName, String PersonAge, String PersonMed) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COL, PersonName);
        values.put(AGE_COL, PersonAge);
        values.put(MED_COL, PersonMed);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void insetImage(Drawable dbDrawable, String imageId, String o2, String o3, String o4) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IMAGE_ID, imageId);
        values.put(option2,o2);
        values.put(option3,o3);
        values.put(option4,o4);
        Bitmap bitmap = ((BitmapDrawable)dbDrawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        values.put(IMAGE_BITMAP, stream.toByteArray());
        db.insert(TABLE_IMAGE, null, values);
        db.close();
    }

    public Image[] getImage() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor2;
        cursor2 = db.rawQuery("SELECT * FROM "+TABLE_IMAGE,null);
        int i=0;
        Image[] imageHelper;
        int size=0;

        if (cursor2.moveToFirst()) {
            do {
            size++;
            } while (cursor2.moveToNext());
        }
        imageHelper=new Image[size];
        if (cursor2.moveToFirst()) {
            do {
                imageHelper[i]=new Image();
                imageHelper[i].setImageId(cursor2.getString(0));
                imageHelper[i].setOption2(cursor2.getString(1));
                imageHelper[i].setOption3(cursor2.getString(2));
                imageHelper[i].setOption4(cursor2.getString(3));
                imageHelper[i].setImageByteArray(cursor2.getBlob(4));
                i++;
            } while (cursor2.moveToNext());
        }

        cursor2.close();
        db.close();
        return imageHelper;


    }

    public boolean checkData(){
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst())
            return true;
        else return false;


    }
    public boolean checkImages(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor2;
        cursor2 = db.rawQuery("SELECT * FROM "+TABLE_IMAGE,null);
        if ( cursor2.moveToFirst())
            return true;
        else return false;
    }

    public Cursor RetrieveData(){
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorCourses = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        return cursorCourses;

    }
    public void OnUpgradeUser()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        String query = "CREATE TABLE " + TABLE_NAME + " ("

                + NAME_COL + " TEXT,"
                + AGE_COL + " TEXT,"
                + MED_COL + " TEXT)";


        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);
        onCreate(db);
    }
}