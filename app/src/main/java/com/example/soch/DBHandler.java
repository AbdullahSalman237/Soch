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

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "Soch";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "PatientDetails";
    private static final String TABLE_IMAGE = "ImageTable";

    // below variable is for our course name column
    private static final String NAME_COL = "name";

    // below variable id for our course duration column.
    private static final String AGE_COL = "age";

    // below variable for our course description column.
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

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        String CREATE_IMAGE_TABLE = "CREATE TABLE " + TABLE_IMAGE + "("

                + IMAGE_ID + " TEXT,"
                + option2 + " TEXT,"
                + option3 + " TEXT,"
                + option4 + " TEXT,"
                + IMAGE_BITMAP + " TEXT )";
        db.execSQL(CREATE_IMAGE_TABLE);

        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("

                + NAME_COL + " TEXT,"
                + AGE_COL + " TEXT,"
                + MED_COL + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }
    public void addNewCourse(String PersonName, String PersonAge, String PersonMed) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(NAME_COL, PersonName);
        values.put(AGE_COL, PersonAge);
        values.put(MED_COL, PersonMed);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
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

        imageHelper=new Image[24];

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
    public void OnUpgrade(){

        onUpgrade(this.getReadableDatabase(),1,1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE);
        onCreate(db);
    }
}