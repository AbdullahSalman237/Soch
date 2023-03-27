package com.example.soch;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class User extends Fragment {
    View view;
    private DBHandler dbHandler;
    ImageView updateData;
    private TextView NameEdt, AgeEdt, MedEdt;
    private Button btn,resume;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_user, container, false);
        getPatientData();
        return view;
    }


    public void getPatientData()
    {
        dbHandler = new DBHandler(getContext());
        Cursor c=null;
        NameEdt = (TextView) view.findViewById(R.id.patientname);
        AgeEdt = (TextView) view.findViewById(R.id.patientage);
        MedEdt = (TextView) view.findViewById(R.id.medicationTime);
        updateData=(ImageView) view.findViewById(R.id.updateData);
        btn=(Button)view.findViewById(R.id.button6);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),Report.class);
                startActivity(intent);
            }
        });

        updateData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                ((Dashboard) getActivity()).EditDetails();
            }
        });


        c=dbHandler.RetrieveData();
        // Retrieve array values from the database

        if(c.moveToFirst()) {

            String name = c.getString(0);
            String age = c.getString(1);

            NameEdt.setText(name);
            AgeEdt.setText(age);
            //MedEdt.setText(med);

        }
        MedicationHandler medHandler = new MedicationHandler(getContext()); // Create an instance of the DBHandler class
        Cursor arrayValues = medHandler.getArrayValues(); //
        if(arrayValues.moveToFirst()) {

            String med = arrayValues.getString(0);
            ArrayList<String> medication = new ArrayList<String>();
            medication.add(med);
            String editTextValue = TextUtils.join(",", medication);
            //Log.d("medication",medication);
            //System.out.print(med);
            Toast.makeText(getContext(),editTextValue,Toast.LENGTH_SHORT).show();
            if (MedEdt != null) {
                MedEdt.setText(editTextValue);
            }
        }
    }

}
