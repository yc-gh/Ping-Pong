package com.example.pingpong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;

public class GameView extends SurfaceView implements Runnable {
    volatile boolean playing;
    private Thread gameThread = null;
    private Player player;
    private Opponent opponent;
    private Ball ball;

    //Drawing surface classes
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;

    //Paddle velocity calculations
    VelocityTracker velocity = VelocityTracker.obtain();
    float playerVelocity;
    float player2Velocity;

    private int screenX, screenY;
    private float newY;

    //It allows access to application-specific resources and classes,
    //as well as up-calls for application-level operations
    //such as launching activities, broadcasting and receiving intents etc.
    //Used here to start end activity and end current activity when game is over
    Context context;

    //Track whether opponent is computer or player
    private boolean singlePlayer;

    //For printing countdown timer upon ball reset
    final String[] timer = new String[]{""};

    public GameView(Context context, int screenX, int screenY, boolean isSinglePlayer) {
        super(context);

        //Keep a boolean variable to track computer or player opponent
        //Used for choosing opponent and notifying touch event handler to handle multiple touches
        singlePlayer = isSinglePlayer;

        //Create player object
        player = new Player(context, screenX, screenY);

        //Create computer or player opponent object
        if(singlePlayer) opponent = new Computer(context, screenX, screenY);
        else opponent = new Player2(context, screenX, screenY);

        //Create ball object
        ball = new Ball(context, screenX, screenY);

        //Get surface and initialize paint for drawing
        surfaceHolder = getHolder();
        paint = new Paint();

        //Set paint attributes
        paint.setColor(Color.WHITE);
        paint.setTextSize(100);
        paint.setTypeface(Typeface.create("Unispace",Typeface.NORMAL));

        //Get screen dimensions
        this.screenX = screenX;
        this.screenY = screenY;

        this.context = context;
    }

    @Override
    public void run() {
        //Reset ball on first run -- required to avoid score issues
        ball.reset(this, timer);

        //Update and draw repeatedly while playing
        while (playing) {
            update();
            draw();
            //SurfaceView automatically refreshes at VSYNC rate
        }
    }

    private void update() {

        //End game when score reaches 10
        if(player.getScore() >= 10 || opponent.getScore() >= 10) {
            endGame();
        }

        //Detect collisions with player paddle
        if(Rect.intersects(player.getRect(), ball.getRect())) {
            ball.reflectPaddle(playerVelocity,1);
        }

        //Detect collisions with opponent paddle
        else if(Rect.intersects(opponent.getRect(), ball.getRect())) {
            ball.reflectPaddle(player2Velocity,2);
        }

        //Update all entities
        ball.update(this, timer, player, opponent);
        player.update();
        opponent.update(ball);
    }

    private void draw() {
        //Get drawing surface and lock it for drawing
        if (surfaceHolder.getSurface().isValid()) {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.rgb(0,0,0));

            //Draw player bitmap
            canvas.drawBitmap(player.getBitmap(),player.getX(),player.getY(),paint);

            //Draw opponent bitmap
            canvas.drawBitmap(opponent.getBitmap(),opponent.getX(),opponent.getY(),paint);

            //Draw ball bitmap
            canvas.drawBitmap(ball.getBitmap(),ball.getX(),ball.getY(),paint);

            //Draw score
            canvas.drawText(timer[0], screenX/2 + 30, 80, paint);
            canvas.drawText(String.valueOf(player.getScore()), screenX/4, 80, paint);
            canvas.drawText(String.valueOf(opponent.getScore()), screenX*3/4, 80, paint);

            //Unlock surface
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    //Called when application minimized
    public void pause() {
        //Stop thread when paused
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }
    }

    //Called when application resumes
    public void resume() {
        //Restart thread when resumed
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void endGame() {
        //Start new activity for end screen
        Intent i=new Intent(context, EndActivity.class);

        //Pass winner information
        String winner = " wins the game with a score of ";
        i.putExtra("winner", player.getScore() > opponent.getScore() ? "Player 1" + winner + player.getScore() : "Player 2" + winner + opponent.getScore());

        //Start end game activity
        context.startActivity(i);

        //Finish current activity
        ((Activity) context).finish();
    }


    //Touch events handled here
    //Only movement motions need to be handled -- no need to handle ACTION_DOWN or ACTION_UP
    //VelocityTracker is used here
    //The motion event is added to VelocityTracker object and velocity for corresponding pointer id is calculated
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            //Handle motion movement actions
            case MotionEvent.ACTION_MOVE:{

                //If playing single player
                //Handle one touch
                if(singlePlayer) {

                    //Get new Y from motion event
                    newY = motionEvent.getY();

                    //Set new Y for paddle
                    player.setY((int) (newY - 100));

                    //Add motion event to VelocityTracker object for velocity calculation
                    velocity.addMovement(motionEvent);

                    //Compute velocity -- no. of pixels in (units) microseconds
                    //Here -> no. of pixels in 7 microseconds
                    velocity.computeCurrentVelocity(7);

                    //Set player velocity for ball speed change on paddle reflection
                    playerVelocity = velocity.getYVelocity(0);

                }

                //Else if playing 2 player
                //Handle multiple touches by their corresponding pointer id
                //First touch on screen is pointer id 0, second is 1 .. so on
                else {
                    int pointerCount = motionEvent.getPointerCount();
                    for(int i = 0; i < pointerCount; ++i)
                    {
                        int pointerIndex = i;
                        int pointerId = motionEvent.getPointerId(pointerIndex);
                        if(pointerId == 0)
                        {
                            newY = motionEvent.getY(pointerIndex);

                            if(motionEvent.getX(pointerIndex) < screenX/2)
                            {
                                player.setY((int) (newY - 100));
                                velocity.addMovement(motionEvent);
                                velocity.computeCurrentVelocity(7);
                                playerVelocity = velocity.getYVelocity(pointerId);
                            }
                            else
                            {
                                opponent.setY((int) (newY - 100));
                                velocity.addMovement(motionEvent);
                                velocity.computeCurrentVelocity(7);
                                player2Velocity = velocity.getYVelocity(pointerId);
                            }

                        }
                        else if(pointerId == 1)
                        {
                            newY = motionEvent.getY(pointerIndex);

                            if(motionEvent.getX(pointerIndex) < screenX/2)
                            {
                                player.setY((int) (newY - 100));
                                velocity.addMovement(motionEvent);
                                velocity.computeCurrentVelocity(7);
                                playerVelocity = velocity.getYVelocity(pointerId);
                            }
                            else
                            {
                                opponent.setY((int) (newY - 100));
                                velocity.addMovement(motionEvent);
                                velocity.computeCurrentVelocity(7);
                                player2Velocity = velocity.getYVelocity(pointerId);
                            }
                        }
                    }
                }

                break;
            }
        }
        return true;
    }
}
