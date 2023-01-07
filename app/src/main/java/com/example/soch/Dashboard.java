package com.example.soch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dashboard extends AppCompatActivity {
    public Boolean QuizInProgress=false;
    FragmentCommunicator fragmentCommunicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dashboard);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(navListner);

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, new User()).commit();
    }

    Fragment selectedFragment = null;
    private BottomNavigationView.OnNavigationItemSelectedListener navListner = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (QuizInProgress == true) //check if quiz chal raha ya nahin quiz chal raha toh true hogaya
            {

                String x="";
                switch (item.getItemId()) {
                    case R.id.home:
                        x="home";
                        break;
                    case R.id.quiz:
                        x="quiz";
                        break;
                    case R.id.godseye:
                        x="godseye";
                        break;


                }
                fragmentCommunicator.passData(x);
            }

            if(QuizInProgress==false)
            {
            switch (item.getItemId()) {
                case R.id.home:
                    selectedFragment = new User();
                    QuizInProgress=false;
                    break;
                case R.id.quiz:
                    selectedFragment = new QuizSimulation();
                    QuizInProgress=true;
                    break;
                case R.id.godseye:
                    selectedFragment = new ObjectRecognizer();
                    QuizInProgress=false;
                    break;
            }
                changeInInterface(selectedFragment);
            }

            return true;

        }
    };
    public void changeInInterface(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout, fragment).commit();

    }
    public interface FragmentCommunicator {

        public void passData(String name);
    }

    public void passVal(FragmentCommunicator fragmentCommunicator) {
        this.fragmentCommunicator = fragmentCommunicator;
    }

    public void EditDetails()////Moving back to Main Activity (Sign Up) for updation
    {
        Intent intent = new Intent(this,MainActivity.class);

        startActivity(intent);

    }
}



