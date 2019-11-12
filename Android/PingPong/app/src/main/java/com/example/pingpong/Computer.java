package com.example.pingpong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Computer implements Opponent{
    private Bitmap bitmap;

    //Control location
    private int x;
    private int y;

    //Control score
    private int score;

    //Control speed for paddle
    private float fastSpeed;
    private float slowSpeed;

    private int screenX;
    private int screenY;

    private Rect computerRect;


    public Computer(Context context, int screenX, int screenY) {
        x = screenX - 75;
        y = screenY/2;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.paddle2);

        //Assign initial score 0
        score = 0;

        //Set fast and slow speeds
        fastSpeed = 0.16f;
        slowSpeed = 0.01f;

        this.screenX = screenX;
        this.screenY = screenY;

        computerRect = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update(Ball ball) {

        int paddleMiddle = bitmap.getHeight()/2 - ball.getBitmap().getHeight()/2;

        //If ball is on right side of screen, set speed as difference in ball and paddle * fast speed
        if(ball.getX() > screenX/2) {
            y += (ball.getY() - (y + paddleMiddle))*fastSpeed;
        }

        //Else speed multiplier is slow speed (ball on left side of screen)
        else {
            y += (ball.getY() - (y + paddleMiddle))*slowSpeed;
        }

        computerRect.left = x;
        computerRect.top = y + 15;
        computerRect.bottom = y + bitmap.getHeight();
        computerRect.right = x + bitmap.getWidth();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Rect getRect() {
        return computerRect;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getScore() { return this.score; }

    public void incScore() { ++this.score; }

}
