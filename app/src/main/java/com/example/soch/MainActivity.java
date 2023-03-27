package com.example.soch;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //ActivityMainBinding binding;
    private Button SubmitDetails, resume, btn, timepicker;
    private DBHandler dbHandler;
    private MedicationHandler medicationHandler;
    private EditText NameEdt;
    private EditText AgeEdt;
    private TextView MedEdt;
    private int hour, minute;
    private LinearLayout parentLayout;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);// when the intent is called
        setContentView(R.layout.signup);  //  signup fragment is shown to user
        Dialog dialog = new Dialog(this);
        //user is shown a cancellation dialogbox
        dialog.setContentView(R.layout.user_instructions);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        resume = dialog.findViewById(R.id.button3);

        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

        //instructions khatam now give details


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();  //hiding the top action bar
        }
        NameEdt = findViewById(R.id.patientname);// saving id in a variable
        AgeEdt = findViewById(R.id.patientage); //  for to manipulate the EditText placeholder
        MedEdt = findViewById(R.id.medicationTime);
        btn = findViewById(R.id.button2);
        parentLayout = findViewById(R.id.parent_layout);
        timepicker = findViewById(R.id.button4);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new EditText field
                EditText editText = new EditText(MainActivity.this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                editText.setLayoutParams(layoutParams);

                // Add the EditText field to the parent layout
                parentLayout.addView(editText);
            }

        });

        timepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current time as the default time for the TimePickerDialog
                Calendar calendar = Calendar.getInstance();
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);

                // Create a new TimePickerDialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        MainActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                // Update the TextView with the selected time
                                MedEdt.setText(hourOfDay + ":" + minute);
                            }
                        },
                        hourOfDay,
                        minute,
                        false
                );

                // Display the TimePickerDialog
                timePickerDialog.show();
            }
        });

        // Create a Handler to update the time ticker every second
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                // Get the current time
                Calendar calendar = Calendar.getInstance();
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                // Update the TextView with the current time
                MedEdt.setText(hourOfDay + ":" + minute);

                // Post the Runnable to run again after 1 second
                handler.postDelayed(this, 1000);
            }
        });

/////////////////////////////////////////////

        /////////////////////////////////////


        medicationHandler = new MedicationHandler(MainActivity.this);
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
            AgeEdt.setText(age); //setting data at EditText placeholder
            MedEdt.setText(med); //from the database
        }
        // when the submit button is clicked a function is called
        SubmitDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fun();

            }

        });
    }
    public void signup() {
        Intent intent = new Intent(this,Dashboard.class);
        //intent.putExtra(data jaiga ismein to second page)
        startActivity(intent);


    }
    public void fun() {
        // saving string value of EditText placholder in variables
        String PersonName = NameEdt.getText().toString();
        String PersonAge = AgeEdt.getText().toString();
        String PersonMed = MedEdt.getText().toString();
        // only continue if the information is fulfilled



        if (PersonName.matches("") || PersonAge.matches("")
                || PersonMed.matches("")) {
            Toast.makeText(this, "براۓ مہربانی خالی جگہ پر کریں", Toast.LENGTH_SHORT).show();
            return;
        }
        if (PersonName.matches("(?s).*[\\u0626-\\u06D2].*")||PersonName.matches("(?s).*[A-Za-z].*"))
        {;
        }else {
            Toast.makeText(MainActivity.this,"براۓ مہربانی نام اردو یا انگریزی میں درج کریں",Toast.LENGTH_SHORT).show();
        return;
        }

        if (Integer.valueOf(PersonAge)<30 || Integer.valueOf(PersonAge)>100 ) {
            Toast.makeText(this, "براۓ مہربانی درست عمر کا اندراج کریں", Toast.LENGTH_SHORT).show();
            return;
        }
        // signup function opens dashboard
        signup();
        dbHandler.OnUpgradeUser(); // delete any previous data
        dbHandler.addUser(PersonName, PersonAge, PersonMed);// insert in db with string varibles
        medicationHandler.addArrayValue(PersonMed); //added in Medication Table too.
        //yahan database mein write ho raha hai
    }

}
