package com.example.pingpong;

import android.graphics.Bitmap;
import android.graphics.Rect;

public interface Opponent {

    public void update(Ball ball);

    public Bitmap getBitmap();

    public Rect getRect();

    public int getX();

    public int getY();

    public void setY(int y);

    public int getScore();

    public void incScore();
}
