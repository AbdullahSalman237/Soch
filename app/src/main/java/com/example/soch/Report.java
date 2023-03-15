package com.example.soch;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.IndexedValue;

public class Report extends AppCompatActivity {

    BarChart barChart;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        barChart = (BarChart) findViewById(R.id.barchart);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);

        ArrayList<BarEntry> entries = new ArrayList<>();
        Cursor cursor = dbHandler.getAllScores();
        if (cursor.moveToFirst()) {
            float i = 0;
            do {
                int score = Integer.parseInt(cursor.getString(0));
                entries.add(new BarEntry(i++, score));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

// Create a data set
        BarDataSet dataSet = new BarDataSet(entries, "Score");
        dataSet.setColor(Color.BLUE);


// Refresh the chart periodically
        final Handler handler = new Handler();
        final int delay = 5000; // milliseconds
        handler.postDelayed(new Runnable() {
            public void run() {
                // Repeat the data fetching and chart update code here
                handler.postDelayed(this, delay);
            }
        }, delay);


    }
}