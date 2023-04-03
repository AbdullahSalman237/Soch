package com.example.soch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    DBHandler db = new DBHandler(SplashScreen.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide(); // hiding the top bar

        Thread thread = new Thread() {
            public void run() {
                try {
                    if(db.checkImages()) {

                    }
                    else{
                        Drawable dbDrawable ;
                        db.insertObjName("clock","گھڑی");
                        db.insertObjName("glasses","عینک");

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
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {

                    if(db.checkData()) //if database already as data
                    {                           // then open dashboard
                        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                        startActivity(intent);
                    }
                    else  //otherwise open sign up to sign up the user
                    {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                    }
                }
            }
        };
        thread.start();
    }
    public void SetQuiz()
    {

    }
}