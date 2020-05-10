package com.db5443pr2454563denn.android.dennistennis;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class Game {

    private enum State{
        PAUSED,WON,LOST,RUNNING
    }

    private SurfaceHolder holder;
    private Resources resources;

    private Ball ball;
    private Bat playerbat;
    private Bat opponentbat;
    private Paint textPaint;
    private Context context;

    private SoundPool sp;
    private int[] sounds = new int[5];
    private State state = State.PAUSED;
    private int bounce_counter = 0;
    private GameListener gameListener;

    public GameListener getGameListener() {
        return gameListener;
    }

    public void setWebEventListener(GameListener gameListener) {
        this.gameListener = gameListener;
    }


    public Game(int screenHeight, int screenWidth, SurfaceHolder holder, Resources resources, Context context){

        // local vars
        this.holder = holder;
        this.resources = resources;
        this.context = context;

        // sprites
        ball = new Ball(screenWidth,screenHeight, context);
        playerbat = new Bat(screenWidth,screenHeight, Bat.Position.LEFT);
        opponentbat = new Bat(screenWidth,screenHeight, Bat.Position.RIGHT);

        // info
        textPaint = new Paint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(40);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);

        // sounds
        sp = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        // setup listener

    }

    public void init(){

        // load bitmaps for sprites
        Bitmap ballImage = BitmapFactory.decodeResource(resources, R.drawable.ball30x30);
        Bitmap ballShadow = BitmapFactory.decodeResource(resources, R.drawable.ball30x30);
        Bitmap playerbatimage = BitmapFactory.decodeResource(resources, R.drawable.player_pad);
        Bitmap opponentbatimage = BitmapFactory.decodeResource(resources, R.drawable.system_pad);

        // init sprites
        ball.init(ballImage, ballShadow);
        playerbat.init(playerbatimage, playerbatimage);
        opponentbat.init(opponentbatimage, opponentbatimage);

        // init sound array for easy (and quick) access
        sounds[Sounds.START] = sp.load(context, R.raw.start,1); // sounds must be loaded before they can be played
        sounds[Sounds.WIN] = sp.load(context, R.raw.win,1);
        sounds[Sounds.LOSE] = sp.load(context, R.raw.lose,1);
        sounds[Sounds.BOUNCE1] = sp.load(context, R.raw.bat_hit,1);
        sounds[Sounds.SPEEDUP] = sp.load(context, R.raw.speedup, 1);

        // if the load is completed then play the start sound. The listener will detect that
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                if(sounds[Sounds.START] == sampleId){  // start sound
                    sp.play(sampleId, 1, 1, 1, 0, 1);
                }
            }
        });

    }

    public void update(long elapsed){

        if (state == State.RUNNING){
            updateGame(elapsed);
        }


    }

    private void iniObjectPositions(){
        ball.initBallPosition();
        opponentbat.initPosition();
        playerbat.initPosition();
    }

    // speed can (not yet) be set in the ball object ball
    public void assessBounceCounter(int counter){
        if(counter == 10){
            bounce_counter =0; // reset
            //play sound that indicates speed increase
            sp.play(sounds[Sounds.SPEEDUP], 1, 1, 1, 0, 1);
            ball.setSpeed(0.1f, 0.1f); // this will be added to the speed
        }
    }
    private void updateGame(long elapsed){
        // the ball has a rectangle around it (ball.getScreenRect()). It has a left coordinate.If it touches
        // the right side of the rect of the playerbat the ball must change direction. This can be checked
        // with the contains method of the rectangle class (see below)
        if(playerbat.getScreenRect().contains(ball.getScreenRect().left, ball.getScreenRect().centerY())){
            sp.play(sounds[Sounds.BOUNCE1], 1, 1, 1, 0, 1);
            bounce_counter++;
            assessBounceCounter(bounce_counter);
            ball.moveRight();
        } else if(opponentbat.getScreenRect().contains(ball.getScreenRect().right, ball.getScreenRect().centerY())){
            sp.play(sounds[Sounds.BOUNCE1], 1, 1, 1, 0, 1);
            bounce_counter++;
            assessBounceCounter(bounce_counter);
            ball.moveLeft();
        } else if(ball.getScreenRect().left < playerbat.getScreenRect().right){
            // player missed the ball, system won
            state = State.LOST;
            sp.play(sounds[Sounds.LOSE], 1, 1, 1, 0, 1);
            iniObjectPositions();
        }else if(ball.getScreenRect().right > opponentbat.getScreenRect().left){
            // system missed the ball, player won
            state = State.WON;
            sp.play(sounds[Sounds.WIN], 1, 1, 1, 0, 1);
            iniObjectPositions();
        }

        ball.update(elapsed);
        opponentbat.update(elapsed, ball); // <-- the player controls movement by itself so no update for player bat
    }

    private void drawText(Canvas canvas, String text){
        canvas.drawColor(Color.DKGRAY);
        canvas.drawText(text, canvas.getWidth()/2, canvas.getHeight()/2, textPaint);

    }

    public void draw(){
        Canvas canvas = holder.lockCanvas();
        if(canvas != null){
            // canvas is now mine
            switch(state){
                case PAUSED:
                    drawText(canvas, "Tap screen to start...");
                    break;
                case WON:
                    drawText(canvas, "You win");
                    break;
                case LOST:
                    drawText(canvas, "You lose");
                    break;
                case RUNNING:
                    drawGame(canvas);
                    break;
                default:
                    break;
            }

            holder.unlockCanvasAndPost(canvas);
        }
    };

    private void drawGame(Canvas canvas){

        canvas.drawColor(Color.WHITE);
        ball.draw(canvas);
        playerbat.draw(canvas);
        opponentbat.draw(canvas);

    }

    public void onTouchEvent(MotionEvent event) {
        if(state == State.RUNNING) {
            playerbat.setPosition(event.getY()); // bat is going to be moved up and down, not left to right so no X
        } else { // must be paused
            state = State.RUNNING;
        }
    }
}
