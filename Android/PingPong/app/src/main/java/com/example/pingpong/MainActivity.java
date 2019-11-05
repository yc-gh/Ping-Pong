package com.example.pingpong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //image button
    private Button button1;
    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting the button
        button1 = findViewById(R.id.button1player);
        button2 = findViewById(R.id.button2player);

        //Adding a click listener
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        //Starting game activity
        Intent i = new Intent(this, GameActivity.class);

        switch(v.getId())
        {
            case R.id.button1player:
                i.putExtra("opponent", "computer");
                break;

            case R.id.button2player:
                i.putExtra("opponent", "player");
                break;
        }

        startActivity(i);

    }
}
