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

    //Controlling Y coordinate so that paddle won't go outside the screen
    private int maxY;
    private int minY;

    private int screenX;
    private int screenY;

    private Rect computerRect;

    public Computer(Context context, int screenX, int screenY) {
        x = screenX - 75;
        y = screenY/2;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.paddle2);

        //calculating maxY
        maxY = screenY - bitmap.getHeight();

        //top edge's y point is 0 so min y will always be zero
        minY = 0;

        //Assign initial score 0
        score = 0;

        this.screenX = screenX;
        this.screenY = screenY;

        computerRect = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update(Ball ball) {

        int paddleMiddle = bitmap.getHeight()/2 - ball.getBitmap().getHeight()/2;

        //If ball is on right side of screen, set speed as difference in ball and paddle * 0.16
        if(ball.getX() > screenX/2) {
            y += (ball.getY() - (y + paddleMiddle))*0.16;
        }

        //Else speed multiplier is 0.01 (ball on left side of screen)
        else {
            y += (ball.getY() - (y + paddleMiddle))*0.01;
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
