package com.example.soch;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Report extends AppCompatActivity {


    private DBHandler dbHandler;

    private TextView total_quiz, avg,name,age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getReport();
    }
    void getReport()
    {
        name=findViewById(R.id.Name);
        age=findViewById(R.id.Age);
        dbHandler= new DBHandler(Report.this);
        Cursor c;
        c=dbHandler.RetrieveData(); //cursor is retrieving the data
        if(c.moveToFirst())
        {
            name.setText(c.getString(0));
            age.setText(c.getString(1));


        }

        c = dbHandler.getAllScores();
        ArrayList <Integer> results = new ArrayList<>();

        if (c.moveToFirst())
        {
            do {
//                Toast.makeText(this, c.getString(0), Toast.LENGTH_SHORT).show();
                results.add(Integer.valueOf(c.getString(0)));
            }while (c.moveToNext());
        }else Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show();
        total_quiz=findViewById(R.id.Quizes);
        total_quiz.setText(String.valueOf(results.size()));
        int total=0;
        int average=0;
        int min=10;
        int max=0;
        for(int i = 0; i < results.size(); i++)
        {  if(results.get(i)>max)
            max=results.get(i);
        else if (results.get(i)<min)
            min=results.get(i);
            total += results.get(i);
        }
        if (results.size()>0) {
            average = total / results.size(); // finding ther average value
//            Toast.makeText(this, "avg" + String.valueOf(average), Toast.LENGTH_SHORT).show();


            avg = findViewById(R.id.avg);
            avg.setText(String.valueOf(average));
        }
    }
}
