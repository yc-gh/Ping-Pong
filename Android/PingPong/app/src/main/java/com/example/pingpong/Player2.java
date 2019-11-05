package com.example.pingpong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Player2 implements Opponent {
    private Bitmap bitmap;

    //Control location
    private int x;
    private int y;

    //Control score
    private int score;

    //Controlling Y coordinate so that paddle won't go outside the screen
    private int maxY;
    private int minY;

    private Rect playerRect;

    public Player2(Context context, int screenX, int screenY) {
        x = screenX - 75;
        y = screenY/2;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.paddle2);

        //calculating maxY
        maxY = screenY - bitmap.getHeight();

        //top edge's y point is 0 so min y will always be zero
        minY = 0;

        //Assign initial score -1
        //The ball is constructed at middle of the screen
        //But the screen orientation changes which triggers reset() method and increments score
        score = 0;

        playerRect = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update(Ball ball) {

        playerRect.left = x;
        playerRect.top = y + 15;
        playerRect.bottom = y + bitmap.getHeight();
        playerRect.right = x + bitmap.getWidth();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Rect getRect() {
        return playerRect;
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
