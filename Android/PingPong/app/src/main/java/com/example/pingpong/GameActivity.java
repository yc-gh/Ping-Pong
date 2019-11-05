package com.example.pingpong;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {


    //Declaring game view
    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //FrameLayout to allow multiple child components to stack on each other
        //This is used to insert image button over the game draw area
        FrameLayout game = new FrameLayout(this);

        //Layout used for setting location and size of image button
        RelativeLayout gameWidgets = new RelativeLayout(this);

        ImageButton endGameButton = new ImageButton(this);

        //Set image used for button
        endGameButton.setImageResource(R.drawable.closebtn);

        RelativeLayout.LayoutParams buttonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,  RelativeLayout.LayoutParams.WRAP_CONTENT);

        //Getting display object
        Display display = getWindowManager().getDefaultDisplay();

        //Getting the screen resolution into point object
        Point size = new Point();
        display.getSize(size);

        //Get opponent info from main activity
        String opponent = getIntent().getExtras().getString("opponent");

        //Initializing game view object
        //this time we are also passing the screen size to the GameView constructor
        gameView = new GameView(this, size.x, size.y, opponent.equals("computer"));

        //Set image button parameters
        buttonParams.leftMargin = size.x/2;
        buttonParams.topMargin = size.y - 70;
        buttonParams.width = 70;
        buttonParams.height = 70;

        //End the game when close button is pressed
        endGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Add button to relative layout
        gameWidgets.addView(endGameButton, buttonParams);

        //Add game and game widget to frame layout
        game.addView(gameView);
        game.addView(gameWidgets);

        //Set the game view as content to be viewed
        setContentView(game);
    }

    //pausing the game when activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    //running the game when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}
