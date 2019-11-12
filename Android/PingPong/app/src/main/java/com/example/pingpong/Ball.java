package com.example.pingpong;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.concurrent.ThreadLocalRandom;

public class Ball {

    Context context;

    //Bitmap image of the ball
    private Bitmap bitmap;

    //Volatile fields used so that UI thread and game thread
    // fetch updated values from main memory instead of cached

    //Coordinate trackers
    private volatile int x;
    private volatile int y;

    //Starting coordinates
    private volatile int startX;
    private volatile int startY;

    //Max bounds for ball positions
    private int maxY;
    private int minY;
    private int maxX;
    private int minX;

    //Default speeds
    private volatile int defSpeedX;
    private volatile int defSpeedY;

    //Current speeds
    private volatile int speedX;
    private volatile int speedY;

    //Max speed
    private int maxSpeed;

    //Track which paddle ball collided with -- to avoid repeated collisions in small time frame
    private volatile int collideSide;

    //Track total score for game levels
    private int totalScore;

    //Rectangle around the ball -- for collision detection
    private Rect ballRect;

    public Ball(Context context, int screenX, int screenY) {
        this.context = context;
        startX = x = screenX/2;
        startY = y = screenY/2;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball);

        //Calculating maxY
        maxY = screenY - bitmap.getHeight();

        //Top edge's y point is 0 so min y will always be zero
        minY = 0;

        maxX = screenX - bitmap.getWidth();

        minX = 0;

        defSpeedX = speedX = 15;
        defSpeedY = speedY = 10;

        maxSpeed = 30;

        //Initialize total score
        totalScore = 0;

        //Set the rectangle around the ball
        ballRect = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public synchronized void update(GameView obj, String[] timer, Player player1, Opponent player2) {

        if(totalScore >=5 && totalScore < 10) {
            defSpeedX = 20;
        }
        else if(totalScore >=10) {
            defSpeedX = 25;
        }

        x += speedX;
        y += speedY;

        //Update ball rectangle to match position
        ballRect.left = x;
        ballRect.top = y;
        ballRect.bottom = y + bitmap.getHeight();
        ballRect.right = x + bitmap.getWidth();

        //Reflect ball when it touches upper or lower edges
        if(y <= minY || y >= maxY) {
            reflectY();
        }

        //Else if ball touches left or right edge
        //Increase score for opposite side
        else if(x < minX) {
            //Increment score when either side scores (reset is called)
            totalScore++;
            player2.incScore();
            reset(obj, timer);
        }

        else if(x > maxX) {
            totalScore++;
            player1.incScore();
            reset(obj, timer);
        }
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Rect getRect() {
        return ballRect;
    }

    public void reset(final GameView obj, final String[] timer) {

        //Reset position of ball
        x = startX;
        y = startY;

        //Set speed 0 while countdown is shown
        speedY = 0;
        speedX = 0;

        //3 second reset timer after either side scores
        //And display text with time remaining
        //All drawing in the view must be done from within the UI thread, therefore actual drawing cannot be done here
        //This just modifies the text that is drawn in the draw method
        //Does not freeze UI thread

        ((Activity)context).runOnUiThread(new Runnable() {

            @Override
            public void run() {
                new CountDownTimer(3200, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        timer[0] = String.valueOf((int)millisUntilFinished/1000);
                    }

                    @Override
                    public void onFinish() {
                        timer[0] = "";

                        //Reset speed of ball
                        //And randomly set direction to left or right
                        speedX = defSpeedX;
                        speedY = defSpeedY;

                        //Reset paddle collision detection side
                        collideSide = 0;
                    }
                }.start();
            }
        });
    }

    //Reflect from upper and lower edges
    public void reflectY() {
        speedY = -speedY;
    }

    //If ball collides with paddle, reflect with new speed
    public void reflectPaddle(float paddleVelocity, int side) {

        //Prevent multiple paddle collisions from being detected in a small time frame
        if(side == collideSide)
        {
            return;
        }

        //Reflect the ball with same Y speed
        speedX = -speedX;

        speedY = (int) (speedY + paddleVelocity);

        if(Math.abs(speedY) > maxSpeed)
        {
            speedY = (speedY > 0 ? 1 : -1)*maxSpeed;
        }

        collideSide = side;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
