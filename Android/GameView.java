package com.example.pingpong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;

public class GameView extends SurfaceView implements Runnable {
    volatile boolean playing;
    private Thread gameThread = null;
    private Player player;
    private Computer computer;
    private Ball ball;

    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    VelocityTracker velocity = VelocityTracker.obtain();
    int playerVelocity;
    int computerVelocity;

    int playerDirection;
    int computerDirection;

    private int screenX, screenY;
    private int newX;

    public GameView(Context context, int screenX, int screenY) {
        super(context);
        player = new Player(context, screenX, screenY);
        computer = new Computer(context, screenX, screenY);
        ball = new Ball(context, screenX, screenY);

        surfaceHolder = getHolder();
        paint = new Paint();

        this.screenX = screenX;
        this.screenY = screenY;
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();
        }
    }

    private void update() {
        ball.update();
        player.update();
        computer.update(ball);

        if(Rect.intersects(player.getRect(), ball.getRect())) {
            ball.reflectPaddle(playerVelocity, playerDirection);
        }

        else if(Rect.intersects(computer.getRect(), ball.getRect())) {
//            ball.reflectPaddle(computerVelocity, computerDirection);
            ball.reflectY();
        }
    }

    private void draw() {
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.rgb(50,150,50));

            canvas.drawBitmap(
                    player.getBitmap(),
                    player.getX(),
                    player.getY(),
                    paint);

            canvas.drawBitmap(
                    computer.getBitmap(),
                    computer.getX(),
                    computer.getY(),
                    paint);

            canvas.drawBitmap(
                    ball.getBitmap(),
                    ball.getX(),
                    ball.getY(),
                    paint);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    private void control() {
        try {
            gameThread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:{
                player.setX((int)motionEvent.getX()-100);
                invalidate();
                break;
            }
            case MotionEvent.ACTION_DOWN:{
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                int size = motionEvent.getHistorySize();
                int oldX = 0;

                if(size>0) {
                    oldX = (int) motionEvent.getHistoricalX(size - 1);
                }

                newX = (int)motionEvent.getX();
                playerDirection = newX - oldX;
                player.setX(newX - 100);

                velocity.addMovement(motionEvent);
                velocity.computeCurrentVelocity(20);
                playerVelocity = (int)velocity.getXVelocity();

                invalidate();
                break;
            }
        }
        return true;
    }
}
