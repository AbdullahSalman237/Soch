package com.example.soch;

import static android.os.SystemClock.sleep;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TaskInfo;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
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

import java.util.Locale;
import java.util.Random;

public class QuizSimulation extends Fragment {
    View view;
    private TextToSpeech mTTS;
    private DBHandler db;
    private int size=0;
    private int totalImages=0;
    int score=0;
    private Boolean quizStarted=false;
    private int i =0;
    private TextView button1,button2,button3,button4;
    private Button start_quiz,new_quiz,cancel_quiz,resume_quiz,resultToHome,resultToGD,startToHome,startToGd;
    public String allOptions="";
    private TextView textView1,textView2,textView3,textView4,textViewScore,result;
    private ImageView imageView;

    String x=null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_quiz_simulation, container, false);
        ///////////////////////////////
        mTTS = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(new Locale("ur"));
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {

                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });



////////////////////////////////////////////////////////////////////////
        ((Dashboard) getActivity()).passVal(new Dashboard.FragmentCommunicator() {
            @Override
            public void passData(String name) {
                x=name;
                if (quizStarted)
                {
                    Dialog dialog = new Dialog(getContext());
                    //user is shown a cancellation dialogbox
                    dialog.setContentView(R.layout.cancel_quiz);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.setCancelable(false);
                    //2 buttons in cancelquiz.xml
                    cancel_quiz=dialog.findViewById(R.id.cancel_quiz);
                    resume_quiz=dialog.findViewById(R.id.resume_quiz);
                    // if user does not want to cancel the quiz continues
                    resume_quiz.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    //if user decides to cancel
                    cancel_quiz.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            // user is asked fragment he wants to go on
                            if (name=="home") {
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        ((Dashboard) getActivity()).QuizInProgress=false;
                                        ((Dashboard) getActivity()).changeInInterface(new User());
                                    }
                                }, 800L); //2 seconds delay

                            }
                            else if (name=="godseye"){
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        ((Dashboard) getActivity()).QuizInProgress=false;
                                        ((Dashboard) getActivity()).changeInInterface(new ObjectRecognizer());
                                    }
                                }, 800L); //2 seconds delay

                        }}
                    });
                    dialog.show();
                }
///////////////////////////////////////////////////////////////////////////////////////////
            }
        });
        quizStarted=true;
        db= new DBHandler(getContext());
        textViewScore=(TextView)view.findViewById(R.id.score);

        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.start_quiz);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        startToHome=dialog.findViewById(R.id.home);
        startToGd=dialog.findViewById(R.id.godseye);
        start_quiz=dialog.findViewById(R.id.start_quiz);

        startToGd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ((Dashboard) getActivity()).QuizInProgress=false;
                ((Dashboard) getActivity()).changeInInterface(new ObjectRecognizer());
                dialog.dismiss();
            }
        });
        startToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ((Dashboard) getActivity()).QuizInProgress=false;
                ((Dashboard) getActivity()).changeInInterface(new User());
                dialog.dismiss();
            }
        });

        start_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenerateQuiz();
                dialog.dismiss();


            }
        });
        dialog.show();

        button1 = (TextView) view.findViewById(R.id.option1);
        button2 = (TextView) view.findViewById(R.id.option2);
        button3 = (TextView) view.findViewById(R.id.option3);
        button4 = (TextView) view.findViewById(R.id.option4);
        SetQuiz();


                return view;
    }
    public void GenerateQuiz(){
        Image[] image = db.getImage();
        Random random = new Random();
        totalImages= image.length;
        int iterator= totalImages/10;

        i=random.nextInt(totalImages);



        DisplayQuiz(image[i]);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (button1.getText().toString() == image[i].getImageId())
                {
                score++;
                button1.setBackgroundResource(R.drawable.correct_button);
            }
                else {

                button1.setBackgroundResource(R.drawable.buttoncolours);
            }
                Random random = new Random();
                int x=random.nextInt(iterator);
                i+=x+1;
                if(!EvaluteQuiz())
                {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            DisplayQuiz(image[i]);
                        }
                    }, 200L); //2 seconds delay
                }

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (button2.getText().toString() == image[i].getImageId()) {

                    score++;
                    button2.setBackgroundResource(R.drawable.correct_button);
                }
                else {

                    button2.setBackgroundResource(R.drawable.buttoncolours);
                }
                Random random = new Random();
                int x=random.nextInt(iterator);
                i+=x+1;
                if(!EvaluteQuiz())
                {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            DisplayQuiz(image[i]);
                        }
                    }, 200L); //2 seconds delay
                }


            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                if (button3.getText().toString() == image[i].getImageId()) {

                    score++;
                    button3.setBackgroundResource(R.drawable.correct_button);

                }
                else {

                    button3.setBackgroundResource(R.drawable.buttoncolours);

                }
                Random random = new Random();
                int x=random.nextInt(iterator);
                i+=x+1;

                if(!EvaluteQuiz())
                {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            DisplayQuiz(image[i]);
                        }
                    }, 200L); //2 seconds delay
                }

            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (button4.getText().toString() == image[i].getImageId()) {
                    score++;
                    button4.setBackgroundResource(R.drawable.correct_button);

                }
                else {
                    button4.setBackgroundResource(R.drawable.buttoncolours);
                }
                Random random = new Random();
                int x=random.nextInt(iterator);
                i+=x+1;
                if(!EvaluteQuiz())
                {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            DisplayQuiz(image[i]);
                        }
                    }, 200L); //2 seconds delay
                }

            }
        });

    }
    public void SetQuiz()
    {
        if(db.checkImages()) {

            return;
        }
        Drawable dbDrawable ;


        dbDrawable=getResources().getDrawable(R.drawable.newspaper);
        db.insetImage(dbDrawable, "اخبار","کاغزات","کتاب","رسالہ");//1
        dbDrawable=getResources().getDrawable(R.drawable.pen);
        db.insetImage(dbDrawable, "قلم","کاغز","چھڑی","تنکا");//2
        dbDrawable=getResources().getDrawable(R.drawable.phone);
        db.insetImage(dbDrawable, "موبائل فون","ڈبا" ,"بکسا","ٹیلی فون");//3
        dbDrawable=getResources().getDrawable(R.drawable.shoes);
        db.insetImage(dbDrawable, "جوتا","پجاما","کمیز","جوراب");//4
        dbDrawable=getResources().getDrawable(R.drawable.tissuse_box);
        db.insetImage(dbDrawable, "ٹیشو کا ڈبا","کوڑا دان","ڈبا" ,"بکسا");//5
        dbDrawable=getResources().getDrawable(R.drawable.walking_stick);
        db.insetImage(dbDrawable, "چھڑی","تنکا","قلم","تار");//6
        dbDrawable=getResources().getDrawable(R.drawable.dustbin);
        db.insetImage(dbDrawable, "کوڑا دان","ڈبا" ,"بکسا","ٹیشو کا ڈبا");//7
        dbDrawable=getResources().getDrawable(R.drawable.electric_heater);
        db.insetImage(dbDrawable, "ہیٹر","ڈبا" ,"بکسا","ٹیشو کا ڈبا");//8
        dbDrawable=getResources().getDrawable(R.drawable.glasses);
        db.insetImage(dbDrawable, "عینک","آلات سماعت","چھڑی","تار");//9
        dbDrawable=getResources().getDrawable(R.drawable.hair_brush);
        db.insetImage(dbDrawable, "بالوں کا برش","تار","عینک","چابی");//10
        dbDrawable=getResources().getDrawable(R.drawable.hearing_aids);
        db.insetImage(dbDrawable, "آلات سماعت","چھڑی","عینک","چابی");//11
        dbDrawable=getResources().getDrawable(R.drawable.keys);
        db.insetImage(dbDrawable, "چابی","آلات سماعت","چھڑی","تار");//12
        dbDrawable=getResources().getDrawable(R.drawable.chair);
        db.insetImage(dbDrawable, "کرسی","میز","صوفہ","دستر خواں");//13
        dbDrawable=getResources().getDrawable(R.drawable.table);
        db.insetImage(dbDrawable,"میز", "کرسی","صوفہ","دستر خواں");//14
        dbDrawable=getResources().getDrawable(R.drawable.remote);
        db.insetImage(dbDrawable, "ریموٹ", "موبائل فون","ڈبا" ,"بکسا");//15
        dbDrawable=getResources().getDrawable(R.drawable.fork);
        db.insetImage(dbDrawable, "کانٹا","چمچ","چھری","پلیٹ");//16
        dbDrawable=getResources().getDrawable(R.drawable.plate);
        db.insetImage(dbDrawable, "پلیٹ","کانٹا","چمچ","چھری");//17
        dbDrawable=getResources().getDrawable(R.drawable.spoon);
        db.insetImage(dbDrawable, "چمچ","چھری","پلیٹ","کانٹا");//18
        dbDrawable=getResources().getDrawable(R.drawable.medicine);
        db.insetImage(dbDrawable, "ادویات","چمچ","پلیٹ","کانٹا");//19
        dbDrawable=getResources().getDrawable(R.drawable.glass);
        db.insetImage(dbDrawable, "گلاس","چمچ","پلیٹ","کانٹا");//20
    }
    public boolean EvaluteQuiz()
    {
        textViewScore.setText(String.valueOf(score));
        size++;
        if(i>=totalImages)
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
            resultToGD=dialog2.findViewById(R.id.godseye);
            resultToHome=dialog2.findViewById(R.id.home);
            resultToGD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ((Dashboard) getActivity()).QuizInProgress=false;
                ((Dashboard) getActivity()).changeInInterface(new ObjectRecognizer());
                dialog2.dismiss();
            }
        });
            resultToHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    ((Dashboard) getActivity()).QuizInProgress=false;
                    ((Dashboard) getActivity()).changeInInterface(new User());
                    dialog2.dismiss();
                }
            });




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
        }else {

        }
        return false;
    }

    private  void DisplayQuiz(Image image)
    {


        textView1=(TextView) view.findViewById(R.id.option1);
        textView2=(TextView) view.findViewById(R.id.option2);
        textView3=(TextView) view.findViewById(R.id.option3);
        textView4=(TextView) view.findViewById(R.id.option4);
        textView1.setBackgroundResource(R.drawable.colouring);
        textView2.setBackgroundResource(R.drawable.colouring);
        textView3.setBackgroundResource(R.drawable.colouring);
        textView4.setBackgroundResource(R.drawable.colouring);

        allOptions="";
        byte[] bytes=image.getImageByteArray();
        imageView=(ImageView)view.findViewById(R.id.imageView3);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView.setImageBitmap(bitmap);
        String text = "یہ کیا ہے";
        allOptions +=text+"  ";
        String option[]={image.getImageId(),image.getOption2(),image.getOption3(),image.getOption4()};
        Random random= new Random();
        int k= random.nextInt(4);
    //    Toast.makeText(getContext(),"option1",Toast.LENGTH_SHORT).show();
//        speak(option[k]);
        allOptions+=option[k];
        allOptions+=" ";
        textView1.setText(option[k]);
        k++;
        if (k>=4)
            k=0;
      //  Toast.makeText(getContext(),"option2",Toast.LENGTH_SHORT).show();
  //      speak(option[k]);
        allOptions+=option[k];
        allOptions+=" ";
        textView2.setText(option[k]);
        k++;
        if (k>=4)
            k=0;
//        Toast.makeText(getContext(),"option3",Toast.LENGTH_SHORT).show();
//        speak(option[k]);
        allOptions+=option[k];
        allOptions+=" ";
        textView3.setText(option[k]);
        k++;
        if (k>=4)
            k=0;
//        Toast.makeText(getContext(),"option4",Toast.LENGTH_SHORT).show();
//        speak(option[k]);
        allOptions+=option[k];
        allOptions+=" ";
        textView4.setText(option[k]);
        if (size<10) {
            speak(1F, allOptions);
        }
    }

    private void speak(float speed,String text) {


        float pitch = 1.11F;

        //float speed = 1.5F;

        mTTS.setPitch(pitch);
        mTTS.setSpeechRate(speed);

        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

}
