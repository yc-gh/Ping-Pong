package com.example.pingpong;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
        //This is used to insert image buttons over the game drawing area
        FrameLayout game = new FrameLayout(this);

        //Layout used for setting location and size of image button
        RelativeLayout gameWidgets = new RelativeLayout(this);

        //Image buttons to pause and quit the game
        ImageButton endGameButton = new ImageButton(this);
        ImageButton pauseGameButton = new ImageButton(this);

        //Set image bitmaps used for buttons
        Bitmap closeButton = BitmapFactory.decodeResource(this.getResources(), R.drawable.closebtn);
        Bitmap pauseButton = BitmapFactory.decodeResource(this.getResources(), R.drawable.pausebtn);
        endGameButton.setImageBitmap(closeButton);
        pauseGameButton.setImageBitmap(pauseButton);

        //Set layout parameters for buttons
        RelativeLayout.LayoutParams endButtonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,  RelativeLayout.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams pauseButtonParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,  RelativeLayout.LayoutParams.WRAP_CONTENT);

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
        endButtonParams.leftMargin = size.x/2 - 50;
        endButtonParams.topMargin = size.y - 70;
        endButtonParams.width = closeButton.getWidth();
        endButtonParams.height = closeButton.getHeight();

        //End the game when close button is pressed
        endGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Add button to relative layout
        gameWidgets.addView(endGameButton, endButtonParams);

        //Set pause button parameters
        pauseButtonParams.leftMargin = size.x/2 + 50;
        pauseButtonParams.topMargin = size.y - 70;
        pauseButtonParams.width = pauseButton.getWidth();
        pauseButtonParams.height = pauseButton.getHeight();

        //Pause/Resume the game when pause button is pressed
        pauseGameButton.setOnClickListener(new View.OnClickListener() {
            boolean playing = true;
            @Override
            public void onClick(View view) {
                if(playing) {
                    gameView.pause();
                    playing = false;
                }
                else {
                    gameView.resume();
                    playing = true;
                }
            }
        });

        //Add button to relative layout
        gameWidgets.addView(pauseGameButton, pauseButtonParams);

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
