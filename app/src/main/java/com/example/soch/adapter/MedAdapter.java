package com.example.soch.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.soch.R;

import java.util.ArrayList;

public class MedAdapter extends RecyclerView.Adapter<MedAdapter.ViewHolder> {
    // creating variables for our ArrayList and context
    private ArrayList<String> Medicationtime;
    private Context context;

    // creating constructor for our adapter class
    public MedAdapter(ArrayList<String> MEDArrayList, Context context) {
        this.Medicationtime = MEDArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.medication, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MedAdapter.ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        String time = Medicationtime.get(position);

        holder.time.setText(time);
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return Medicationtime.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.

        private final TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            time=itemView.findViewById(R.id.time);
        }
    }
}

