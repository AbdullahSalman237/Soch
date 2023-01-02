package com.example.soch;

import android.app.Dialog;
import android.app.TaskInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Random;

public class QuizSimulation extends Fragment {
    View view;
    private DBHandler db;
    private int size=0;
    int score=0;
    private int i =0;
    private Button button1,button2,button3,button4,start_quiz,new_quiz;
    private int objects[]={-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
    private TextView textView1,textView2,textView3,textView4,textViewScore,result;
    private ImageView imageView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_quiz_simulation, container, false);
        db= new DBHandler(getContext());
        textViewScore=(TextView)view.findViewById(R.id.score);
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.start_quiz);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        start_quiz=dialog.findViewById(R.id.start_quiz);

        start_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        button1 = (Button)view.findViewById(R.id.option1);
        button2 = (Button)view.findViewById(R.id.option2);
        button3 = (Button)view.findViewById(R.id.option3);
        button4 = (Button)view.findViewById(R.id.option4);
        SetQuiz();
        GenerateQuiz();

                return view;
    }
    public void GenerateQuiz(){
        Image[] image = db.getImage();
        Random random = new Random();
        i=random.nextInt(20);



        DisplayQuiz(image[i]);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (button1.getText().toString() == image[i].getImageId())
                {       Toast.makeText(getContext(), "Correct", Toast.LENGTH_SHORT).show();
                    score++;
                }else Toast.makeText(getContext(),"Wrong",Toast.LENGTH_SHORT).show();

                Random random = new Random();
                int x=random.nextInt(2);
                i+=x+1;
                EvaluteQuiz();
                DisplayQuiz(image[i]);

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (button2.getText().toString() == image[i].getImageId()) {
                    Toast.makeText(getContext(), "Correct", Toast.LENGTH_SHORT).show();
                    score++;
                }
                else Toast.makeText(getContext(),"Wrong",Toast.LENGTH_SHORT).show();

                Random random = new Random();
                int x=random.nextInt(2);
                i+=x+1;
                EvaluteQuiz();
                DisplayQuiz(image[i]);

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (button3.getText().toString() == image[i].getImageId()) {
                    Toast.makeText(getContext(), "Correct", Toast.LENGTH_SHORT).show();
                    score++;
                }
                else Toast.makeText(getContext(),"Wrong",Toast.LENGTH_SHORT).show();

                Random random = new Random();
                int x=random.nextInt(2);
                i+=x+1;
                EvaluteQuiz();
                DisplayQuiz(image[i]);

            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (button4.getText().toString() == image[i].getImageId()) {
                    Toast.makeText(getContext(), "Correct", Toast.LENGTH_SHORT).show();
                    score++;
                }
                else Toast.makeText(getContext(),"Wrong",Toast.LENGTH_SHORT).show();

                Random random = new Random();
                int x=random.nextInt(2);
                i+=x+1;
                EvaluteQuiz();
                DisplayQuiz(image[i]);
            }
        });

    }
    public void SetQuiz()
    {
        if(db.checkImages())
            return;

        Drawable dbDrawable ;


        dbDrawable=getResources().getDrawable(R.drawable.newspaper);
        db.insetImage(dbDrawable, "NewsPaper","Document","op3","op4");//1
        dbDrawable=getResources().getDrawable(R.drawable.pen);
        db.insetImage(dbDrawable, "Pen","Color","Stick","wire");//2
        dbDrawable=getResources().getDrawable(R.drawable.phone);
        db.insetImage(dbDrawable, "Mobile Phone","box" ,"adapter","wire");//3
        dbDrawable=getResources().getDrawable(R.drawable.shoes);
        db.insetImage(dbDrawable, "Shoes","Trouser","Shirt","socks");//4
        dbDrawable=getResources().getDrawable(R.drawable.tissuse_box);
        db.insetImage(dbDrawable, "Tissuse Box","Box","o3","wire");//5
        dbDrawable=getResources().getDrawable(R.drawable.walking_stick);
        db.insetImage(dbDrawable, "Walking Stick","wood","Stick","wire");//6
        dbDrawable=getResources().getDrawable(R.drawable.dustbin);
        db.insetImage(dbDrawable, "Dustbin","Box","Stick","wire");//7
        dbDrawable=getResources().getDrawable(R.drawable.electric_heater);
        db.insetImage(dbDrawable, "Electric Heater","Color","Stick","wire");//8
        dbDrawable=getResources().getDrawable(R.drawable.glasses);
        db.insetImage(dbDrawable, "Glasses","Color","Stick","wire");//9
        dbDrawable=getResources().getDrawable(R.drawable.hair_brush);
        db.insetImage(dbDrawable, "hairbrush","comb","Stick","wire");//10
        dbDrawable=getResources().getDrawable(R.drawable.hearing_aids);
        db.insetImage(dbDrawable, "Hearing Aids","Color","Stick","wire");//11
        dbDrawable=getResources().getDrawable(R.drawable.keys);
        db.insetImage(dbDrawable, "Keys","Color","Stick","wire");//12
        dbDrawable=getResources().getDrawable(R.drawable.chair);
        db.insetImage(dbDrawable, "chair","bench","Sofa","wire");//13
        dbDrawable=getResources().getDrawable(R.drawable.table);
        db.insetImage(dbDrawable, "table","couch","shelf","cupboard");//14
        dbDrawable=getResources().getDrawable(R.drawable.remote);
        db.insetImage(dbDrawable, "Remote","Charger","Stick","wire");//15
        dbDrawable=getResources().getDrawable(R.drawable.fork);
        db.insetImage(dbDrawable, "fork","Spoon","Stick","wire");//16
        dbDrawable=getResources().getDrawable(R.drawable.plate);
        db.insetImage(dbDrawable, "Plate","Tray","Dish","wire");//17
        dbDrawable=getResources().getDrawable(R.drawable.spoon);
        db.insetImage(dbDrawable, "Spoon","Color","Stick","wire");//18
        dbDrawable=getResources().getDrawable(R.drawable.medicine);
        db.insetImage(dbDrawable, "Medicine","Color","Stick","wire");//19
        dbDrawable=getResources().getDrawable(R.drawable.glass);
        db.insetImage(dbDrawable, "glass","Color","Stick","wire");//20
    }
    public boolean EvaluteQuiz()
    {
        textViewScore.setText(String.valueOf(score));
        size++;
        if(i>=20)
            i=0;
        if (size>=10)
        {
            Dialog dialog2 = new Dialog(getContext());
            dialog2.setContentView(R.layout.result);
            dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog2.setCancelable(false);
            result=dialog2.findViewById(R.id.score_of);
            new_quiz=dialog2.findViewById(R.id.new_quiz);
            result.setText(String.valueOf(score));

            new_quiz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    i=0;
                    size=0;
                    score=0;
                    textViewScore.setText(String.valueOf(score));
                    dialog2.dismiss();
                    GenerateQuiz();
                }
            });
            dialog2.show();
            return true;
        }
        return false;
    }

    private  void DisplayQuiz(Image image)
    {
        textView1=(TextView) view.findViewById(R.id.option1);
        textView2=(TextView) view.findViewById(R.id.option2);
        textView3=(TextView) view.findViewById(R.id.option3);
        textView4=(TextView) view.findViewById(R.id.option4);

        String option[]={image.getImageId(),image.getOption2(),image.getOption3(),image.getOption4()};
        Random random= new Random();
        int k= random.nextInt(4);
        textView1.setText(option[k]);
        k++;
        if (k>=4)
            k=0;
        textView2.setText(option[k]);
        k++;
        if (k>=4)
            k=0;
        textView3.setText(option[k]);
        k++;
        if (k>=4)
            k=0;
        byte[] bytes=image.getImageByteArray();
        imageView=(ImageView)view.findViewById(R.id.imageView3);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView.setImageBitmap(bitmap);
        textView4.setText(option[k]);

    }


}
