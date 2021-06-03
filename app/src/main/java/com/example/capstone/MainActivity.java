package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    thtest thtest;
    public static Context context;
    private Button button1;
    Handler handler = new Handler(Looper.getMainLooper());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        thtest = new thtest(handler);
        thtest.start();

        button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Calendar.class);
                startActivity(intent);
            }
        });

        Switch sw = (Switch)findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ((MainActivity)MainActivity.context).thtest.send("ON");

                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ((MainActivity)MainActivity.context).thtest.send("OFF");
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }
}