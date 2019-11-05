package com.example.pingpong;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity{

    private Button menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get winner info from game activity
        String winner = "";

        //Bundle -- A mapping from String keys to various Parcelable values
        //Used for retrieving information passed between activities in the form of key - value pairs
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            winner = extras.getString("winner");
        }

        //Set view of activity as defined in the layout.xml file
        setContentView(R.layout.activity_end);

        //Display winner text
        ((TextView)findViewById(R.id.textView2)).setText("Game Over!\n " + winner);

        //Button to go back to main menu
        menuButton = findViewById(R.id.menubutton);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
