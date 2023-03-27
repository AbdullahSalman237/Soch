package com.example.soch;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

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
    private ScoreHandler scoreHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        scoreHandler=new ScoreHandler(Report.this);
        SQLiteDatabase db = scoreHandler.getReadableDatabase();
        barChart = (BarChart) findViewById(R.id.barchart);
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);

        ArrayList<BarEntry> entries = new ArrayList<>();
        ScoreHandler handler = new ScoreHandler(this);
        Cursor cursor = handler.getAllScores();
        if (cursor.moveToFirst()) {
            float i = 0;
            do {
                String score = cursor.getString(0);
                Toast.makeText(this,score,Toast.LENGTH_LONG);
                int s= Integer.valueOf(score);
                entries.add(new BarEntry(i++, s));
            } while (cursor.moveToNext());
        }
        else
        {
            Toast.makeText(this,"No data available",Toast.LENGTH_LONG);
        }
        cursor.close();
        db.close();

// Create a data set
        BarDataSet dataSet = new BarDataSet(entries, "Score");
        dataSet.setColor(Color.BLUE);


// Refresh the chart periodically
        final Handler handler1 = new Handler();
        final int delay = 5000; // milliseconds
        handler1.postDelayed(new Runnable() {
            public void run() {
                // Repeat the data fetching and chart update code here
                handler1.postDelayed(this, delay);
            }
        }, delay);


    }
}