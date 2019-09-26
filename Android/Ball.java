package com.example.pingpong;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Ball {
    private Bitmap bitmap;
    private int x;
    private int y;

    private int startX;
    private int startY;

    private int maxY;
    private int minY;
    private int maxX;
    private int minX;

    private int speedX;
    private int speedY;

    private int maxSpeed;

    private Rect ballRect;

    public Ball(Context context, int screenX, int screenY) {
        startX = x = screenX/2;
        startY = y = screenY/2;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ball);

        //calculating maxY
        maxY = screenY - bitmap.getHeight();

        //top edge's y point is 0 so min y will always be zero
        minY = 0;

        maxX = screenX - bitmap.getWidth();

        minX = 0;

        speedX = 10;
        speedY = 20;

        maxSpeed = 40;

        ballRect = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());
    }

    public void update() {
        x += speedX;
        y += speedY;

        if(x <= minX || x >= maxX)
        {
            reflectX();
        }

        if(y <= minY || y >= maxY)
        {
            reset();
        }

        ballRect.left = x;
        ballRect.top = y;
        ballRect.bottom = y + bitmap.getHeight();
        ballRect.right = x + bitmap.getWidth();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Rect getRect() {
        return ballRect;
    }

    public void reset() {
        x = startX;
        y = startY;

        speedY = 20;
        speedX = 10;
    }

    public void reflectX() {
        speedX = -speedX;
    }

    public void reflectY() {
        speedY = -speedY;
    }

    public void reflectPaddle(int paddleVelocity, int paddleDirection) {
        //Reflect the ball with same Y speed
        speedY = -speedY;

        speedX = (speedX + (speedX*paddleDirection >= 0 ? 1 : -1)*paddleVelocity) % maxSpeed;
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
