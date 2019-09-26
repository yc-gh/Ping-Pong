package com.example.pingpong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Player {
    private Bitmap bitmap;
    private int x;
    private int y;


    //Controlling Y coordinate so that paddle won't go outside the screen
    private int maxY;
    private int minY;

    private Rect playerRect;

    public Player(Context context, int screenX, int screenY) {
        x = 75;
        y = 50;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.paddle1);

        //calculating maxY
        maxY = screenY - bitmap.getHeight();

        //top edge's y point is 0 so min y will always be zero
        minY = 0;

        playerRect = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update() {

        playerRect.left = x;
        playerRect.top = y;
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

}
