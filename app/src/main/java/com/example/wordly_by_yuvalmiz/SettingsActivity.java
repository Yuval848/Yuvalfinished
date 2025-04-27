package com.example.wordly_by_yuvalmiz;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.widget.Switch;
import android.widget.Toast;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;

public class  SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Spinner spinner;
    private String[] arrcolor = {"","Blue","Pink","Cyan","Purple","Orange","Purple_light"};
    private boolean isFirstime = true;

    private Switch aSwitch;

    Intent serviceIntent;


    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        Intent i = getIntent();
        String color = i.getStringExtra("color");
        linearLayout = findViewById(R.id.settings_activity);
        setBackgroundColor(color);

        serviceIntent = new Intent(this, PlayService.class);
        aSwitch = findViewById(R.id.music_switch);
        aSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Switch is ON
                Toast.makeText(SettingsActivity.this, "Switch is ON", Toast.LENGTH_SHORT).show();
                playAudio();
            } else {
                // Switch is OFF
                Toast.makeText(SettingsActivity.this, "Switch is OFF", Toast.LENGTH_SHORT).show();
                stopAudio();
            }
        });


        ArrayAdapter aa =
                new ArrayAdapter(this, android.R.layout.simple_spinner_item,arrcolor);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(aa);





    }

    private void stopAudio() {
        stopService(serviceIntent);

    }

    private void playAudio() {
        startService(serviceIntent);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view,int position, long l) {
        //Toast.makeText(this,"onItemSelected", Toast.LENGTH_SHORT).show();
        if(isFirstime == false)
        {
            Intent intent = new Intent();

            intent.putExtra("color",arrcolor[position]);
            setResult(RESULT_OK,intent);

            setBackgroundColor(arrcolor[position]);
            finish();

        }
        isFirstime= false;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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