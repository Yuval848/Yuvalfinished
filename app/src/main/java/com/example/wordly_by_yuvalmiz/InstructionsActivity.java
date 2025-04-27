package com.example.wordly_by_yuvalmiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class InstructionsActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnok;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        btnok=findViewById(R.id.okbtn);
        btnok.setOnClickListener(this);
        linearLayout = findViewById(R.id.activityInstruction);

        Intent i = getIntent();
        String color = i.getStringExtra("color");
        setBackgroundColor(color);
    }

    @Override
    public void onClick(View v) {
        if(v==btnok)
        {
            finish();
        }
    }
    public void setBackgroundColor(String str) {
        switch (str)
        {
            case"Blue":
            {
                linearLayout.setBackgroundColor(Color.BLUE);
                break;
            }
            case"Purple":
            {
                linearLayout.setBackgroundColor(Color.argb(255,167,29,216));
                break;

            }
            case"Orange":
            {
                linearLayout.setBackgroundColor(Color.argb(255,217,105,28));
                break;

            }


            case "Pink":
            {
                linearLayout.setBackgroundColor(Color.argb(255,255,192,203));
                break;
            }
            case "Cyan":
            {
                linearLayout.setBackgroundColor(Color.argb(255, 2,227,245));
                break;
            }
            case "Purple_light":
            {
                linearLayout.setBackgroundColor(Color.argb(255, 125,0 , 255));
                break;
            }

            default:
                break;


        }
    }
}