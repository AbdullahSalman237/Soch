package com.example.soch;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //ActivityMainBinding binding;
    private Button SubmitDetails;
    private DBHandler dbHandler;
    private EditText NameEdt, AgeEdt;
    private TextView MedEdt;
    private int hour,minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);// when the intent is called
        setContentView(R.layout.signup);  //  signup fragment is shown to user
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();  //hiding the top action bar
        }
        NameEdt = findViewById(R.id.patientname);// saving id in a variable
        AgeEdt = findViewById(R.id.patientage); //  for to manipulate the EditText placeholder
        MedEdt = findViewById(R.id.medicationTime);
        //////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////     Medicine Time
        /////////////////////////////////////////////////////////////////////////////
        MedEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        hour = i;
                        minute=i1;
                        MedEdt.setText(String.format(Locale.getDefault(),"%02d:%02d",hour,minute));

                    }
                };
                TimePickerDialog timePickerDialog=new TimePickerDialog(MainActivity.this,onTimeSetListener,hour,minute,true);
                timePickerDialog.show();


            }

        });
/////////////////////////////////////////////

        /////////////////////////////////////


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
        if (Integer.valueOf(PersonAge)<30 || Integer.valueOf(PersonAge)>100 ) {
            Toast.makeText(this, "براۓ مہربانی درست عمر کا اندراج کریں", Toast.LENGTH_SHORT).show();
            return;
        }
        // signup function opens dashboard
        signup();
        dbHandler.OnUpgradeUser(); // delete any previous data
        dbHandler.addUser(PersonName, PersonAge, PersonMed);// insert in db with string varibles
        //yahan database mein write ho raha hai
    }

}
