package com.example.soch;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ObjectRecognizer extends Fragment {
    View view;
    Button r;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_object_recognizer, container, false);
        // By ID we can get each component which id is assigned in XML file get Buttons and imageview.

        Dialog dialog_instructions = new Dialog(getContext());
        //user is shown a cancellation dialogbox
        dialog_instructions.setContentView(R.layout.object_instructions);
        dialog_instructions.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_instructions.setCancelable(false);

        r=dialog_instructions.findViewById(R.id.doit);
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_instructions.dismiss();
            }
        });
        dialog_instructions.show();

        return view;
    }
}
