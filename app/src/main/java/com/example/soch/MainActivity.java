package com.example.soch;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soch.adapter.MedAdapter;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //ActivityMainBinding binding;
    private Button SubmitDetails,resume,addMed;
    private DBHandler dbHandler;
    private EditText NameEdt;
    private EditText AgeEdt;
    private TextView MedEdt;
    private RecyclerView time;
    private MedAdapter medAdapter;
    private int hour,minute;
    public ArrayList<String> MedicationTime = new ArrayList<>();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);// when the intent is called
        setContentView(R.layout.signup);  //  signup fragment is shown to user
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();  //hiding the top action bar
        }
        time=findViewById(R.id.medication);
        time.setHasFixedSize(true);
        time.setLayoutManager(new LinearLayoutManager(this));
        medAdapter=new MedAdapter(MedicationTime,MainActivity.this);
        time.setAdapter(medAdapter);

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

        NameEdt = findViewById(R.id.patientname);// saving id in a variable
        AgeEdt = findViewById(R.id.patientage); //  for to manipulate the EditText placeholder
//        MedEdt = findViewById(R.id.medicationTime);

        //////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////     Age
        /////////////////////////////////////////////////////////////////////////////
//        String[] data = new String[]{"Berlin", "Moscow", "Tokyo", "Paris"};
//        AgeEdt.setDisplayedValues(data);


        //////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////     Medicine Time
        /////////////////////////////////////////////////////////////////////////////
        addMed=findViewById(R.id.addMed);
        addMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        hour = i;
                        minute=i1;
//                        MedEdt.setText(String.format(Locale.getDefault(),"%02d:%02d",hour,minute));
                        MedicationTime.add(String.format(Locale.getDefault(),"%02d:%02d",hour,minute));


                        medAdapter.notifyDataSetChanged();
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

            NameEdt.setText(name);
            AgeEdt.setText(age); //setting data at EditText placeholder
//            MedEdt.setText(med); //from the database
        }

        c=dbHandler.getMedTime();
        if (c.moveToFirst())
        {
            do{
                MedicationTime.add(c.getString(0));
            }while (c.moveToNext());

        }

        medAdapter.notifyDataSetChanged();

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

        // only continue if the information is fulfilled



        if (PersonName.matches("") || PersonAge.matches("")){
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
        dbHandler.addUser(PersonName, PersonAge);// insert in db with string varibles
        for (int i=0;i<MedicationTime.size();i++)
        {
            dbHandler.insertMedTIme(MedicationTime.get(i));
        }


        //yahan database mein write ho raha hai
    }

}
