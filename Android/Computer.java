package com.example.pingpong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Computer {
    private Bitmap bitmap;
    private int x;
    private int y;


    //Controlling Y coordinate so that paddle won't go outside the screen
    private int maxY;
    private int minY;

    private Rect computerRect;

    public Computer(Context context, int screenX, int screenY) {
        x = 75;
        y = screenY - 110;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.paddle2);

        //calculating maxY
        maxY = screenY - bitmap.getHeight();

        //top edge's y point is 0 so min y will always be zero
        minY = 0;

        computerRect = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update(Ball ball) {
        x = ball.getX() + ball.getBitmap().getWidth()/2 - bitmap.getWidth()/2;

        computerRect.left = x;
        computerRect.top = y;
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

}
