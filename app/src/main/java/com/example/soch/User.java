package com.example.soch;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.soch.adapter.MedAdapter;

import java.util.ArrayList;

public class User extends Fragment {
    View view;
    private DBHandler dbHandler;
    ImageView updateData;
    private TextView NameEdt, AgeEdt;
    RecyclerView MedEdt;
    private MedAdapter medAdapter;
    public ArrayList<String> MedicationTime;
    private Button btn,resume;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_user, container, false);
        MedicationTime = new ArrayList<>();
        getPatientData();
        return view;
    }
    public void getPatientData()
    {
        MedEdt = (RecyclerView) view.findViewById(R.id.medicationTime);
        MedEdt.setHasFixedSize(true);
        MedEdt.setLayoutManager(new LinearLayoutManager(getContext()));
        medAdapter=new MedAdapter(MedicationTime,getContext());
        MedEdt.setAdapter(medAdapter);


        dbHandler = new DBHandler(getContext());
        Cursor c=null;
        NameEdt = (TextView) view.findViewById(R.id.patientname);
        AgeEdt = (TextView) view.findViewById(R.id.patientage);

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
        if(c.moveToFirst())
        {

            String name = c.getString(0);
            String age = c.getString(1);
            NameEdt.setText(name);
            AgeEdt.setText(age);
        }
        c=dbHandler.getMedTime();
        if (c.moveToFirst())
        {
            do{
                MedicationTime.add(c.getString(0));
            }while (c.moveToNext());

        }

        medAdapter.notifyDataSetChanged();



    }

}