package com.example.soch.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.soch.OnItemClickListener;
import com.example.soch.R;

import java.util.ArrayList;

public class MedAdapter extends RecyclerView.Adapter<MedAdapter.ViewHolder> implements OnItemClickListener {
    // creating variables for our ArrayList and context
    private ArrayList<String> Medicationtime;
    private Context context;
    private final OnItemClickListener onItemClickListener;


    // creating constructor for our adapter class
    public MedAdapter(ArrayList<String> MEDArrayList, Context context, OnItemClickListener onItemClickListener) {
        this.Medicationtime = MEDArrayList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item

        ViewHolder view = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.medication, parent, false),onItemClickListener);
//        view.setOnClickListener(OnClickListener);
        return view;
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

    @Override
    public void onItemLongClick(int pos) {

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.

        private final TextView time;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            // initializing our text views.
            time = itemView.findViewById(R.id.time);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    if (onItemClickListener != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemLongClick(pos);
                        }
                    }
                    return false;
                }
            });

        }

    }
}



