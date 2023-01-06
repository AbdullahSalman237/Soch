package com.example.soch;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    //ActivityMainBinding binding;
    private Button SubmitDetails,readCourseBtn;
    private DBHandler dbHandler;
    private EditText NameEdt, AgeEdt, MedEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        NameEdt = findViewById(R.id.patientname);
        AgeEdt = findViewById(R.id.patientage);
        MedEdt = findViewById(R.id.medicationTime);

        dbHandler = new DBHandler(MainActivity.this);
      //  dbHandler.OnUpgrade();
        SubmitDetails = findViewById(R.id.signup);
        Cursor c;
        c=dbHandler.RetrieveData();
        if(c.moveToFirst())
        {
            String name = c.getString(0);
            String age = c.getString(1);
            String med = c.getString(2);
            NameEdt.setText(name);
            AgeEdt.setText(age);
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

                dbHandler.OnUpgrade();

                dbHandler.addNewCourse(PersonName, PersonAge, PersonMed);
            }


    }

