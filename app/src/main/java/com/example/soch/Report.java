package com.example.soch;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

public class Report extends AppCompatActivity {


    private DBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        dbHandler= new DBHandler(Report.this);
        Cursor c = dbHandler.getAllScores();
        if (c.moveToFirst())
        {
            do {
                Toast.makeText(this, c.getString(0), Toast.LENGTH_SHORT).show();
            }while (c.moveToNext());
        }else Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show();

    }
}