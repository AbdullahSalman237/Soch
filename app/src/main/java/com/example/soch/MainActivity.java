package com.example.soch;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //ActivityMainBinding binding;
    private Button SubmitDetails,readCourseBtn;
    private DBHandler dbHandler;
    int hour,minute;
    private EditText NameEdt, AgeEdt, MedEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        NameEdt = findViewById(R.id.patientname);//edittext
        AgeEdt = findViewById(R.id.patientage);
        MedEdt = findViewById(R.id.medicationTime);

        MedEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeset();
            }

        });


        dbHandler = new DBHandler(MainActivity.this);
      //  dbHandler.OnUpgrade();
        SubmitDetails = findViewById(R.id.signup);//button
        Cursor c;
        c=dbHandler.RetrieveData(); //cursor is retrieving the data
        if(c.moveToFirst())
        {
            String name = c.getString(0);
            String age = c.getString(1);
            String med = c.getString(2);
            NameEdt.setText(name);
            AgeEdt.setText(age); //yahan data set ho raha hai
            MedEdt.setText(med);
        }

        SubmitDetails.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                fun();
                signup(v);

            }

        });
    }

    private void timeset() {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                hour = i;
                minute=i1;
                MedEdt.setText(String.format(Locale.getDefault(),"%02d:%02d",hour,minute));

            }
        };
        TimePickerDialog timePickerDialog=new TimePickerDialog(this,onTimeSetListener,hour,minute,true);
        timePickerDialog.show();
    }

    public void signup(View view) {
        Intent intent = new Intent(this,Dashboard.class);
        //intent.putExtra(data jaiga ismein to second page)
        startActivity(intent);


    }
    public void fun()
    {


                String PersonName = NameEdt.getText().toString();
                String PersonAge = AgeEdt.getText().toString();
                String PersonMed = MedEdt.getText().toString();

                dbHandler.OnUpgradeUser();

                dbHandler.addUser(PersonName, PersonAge, PersonMed);
                //yahan database mein write ho raha hai
    }


    }

