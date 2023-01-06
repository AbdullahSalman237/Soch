package com.example.soch;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class User extends Fragment {
    View view;
    private DBHandler dbHandler;
    ImageView updateData;
    private TextView NameEdt, AgeEdt, MedEdt;
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

        updateData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                ((Dashboard) getActivity()).EditDetails();
            }
        });

        c=dbHandler.RetrieveData();
        if(c.moveToFirst())
        {

            String name = c.getString(0);
            String age = c.getString(1);
            String med = c.getString(2);
            Toast.makeText(getContext(),name+age+med,Toast.LENGTH_SHORT).show();
            NameEdt.setText(name);
            AgeEdt.setText(age);
            MedEdt.setText(med);
        }


    }

}
