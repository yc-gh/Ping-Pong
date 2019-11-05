package com.example.pingpong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.Log;

public class Ball {
    //Bitmap image of the ball
    private Bitmap bitmap;

    //Coordinate trackers
    private volatile int x;
    private volatile int y;

    //Starting coordinates
    private int startX;
    private int startY;

    //Max bounds for ball positions
    private int maxY;
    private int minY;
    private int maxX;
    private int minX;

    //Default speeds
    private int defSpeedX;
    private int defSpeedY;

    //Current speeds
    private volatile int speedX;
    private volatile int speedY;

    //Max speed
    private int maxSpeed;

    //Track which paddle ball collided with -- to avoid repeated collisions in small time frame
    private volatile int collideSide;

    //Rectangle around the ball -- for collision detection
    private Rect ballRect;

    public Ball(Context context, int screenX, int screenY) {
        startX = x = screenX/2;
        startY = y = screenY/2;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball);

        //Calculating maxY
        maxY = screenY - bitmap.getHeight();

        //Top edge's y point is 0 so min y will always be zero
        minY = 0;

        maxX = screenX - bitmap.getWidth();

        minX = 0;

        defSpeedX = speedX = 20;
        defSpeedY = speedY = 10;

        maxSpeed = 30;

        //Set the rectangle around the ball
        ballRect = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update(GameView obj, String[] timer, Player player1, Opponent player2) {
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
            player2.incScore();
            reset(obj, timer);
        }

        else if(x > maxX) {
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
        obj.post(new Runnable() {

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
