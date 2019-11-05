package com.example.pingpong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Player {
    private Bitmap bitmap;

    //Control location
    private int x;
    private int y;

    //Control Score
    private int score;

    //Controlling Y coordinate so that paddle won't go outside the screen
    private int maxY;
    private int minY;

    //Rectangle around player bitmap -- for collision detection
    private Rect playerRect;

    public Player(Context context, int screenX, int screenY) {
        x = 50;
        y = screenY/2;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.paddle1);

        //calculating maxY
        maxY = screenY - bitmap.getHeight();

        //top edge's y point is 0 so min y will always be zero
        minY = 0;

        //Assign initial score -1
        //The ball is constructed at middle of the screen
        //But the screen orientation changes which triggers reset() method and increments score
        score = 0;

        //Set the rectangle for player
        playerRect = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update() {

        playerRect.left = x;
        playerRect.top = y;
        playerRect.bottom = y + bitmap.getHeight();
        playerRect.right = x + bitmap.getWidth() - 10;
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

    public float getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getScore() { return this.score; }

    public void incScore() { ++this.score; }

}
