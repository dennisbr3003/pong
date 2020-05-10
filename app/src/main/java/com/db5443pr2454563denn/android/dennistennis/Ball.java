package com.db5443pr2454563denn.android.dennistennis;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.Random;

public class Ball extends Sprite{

    // these two should not be cached, hence volatile
    private volatile float speedX = 0.1f;
    private volatile float speedY = 0.1f;

    private int directionX; // right = 1 left -1
    private int directionY;
    private Context context;

    private int hit_border_sound_id;

    // not sure if this should be here but the only event that uses this sound is here
    private SoundPool ball_sp;

    public Ball(int screenWidth, int screenHeight, Context context) {
        super(screenWidth, screenHeight);
        this.context = context;
        ball_sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    }

    @Override
    public void init(Bitmap image, Bitmap shadow) {
        super.init(image, shadow);
        initBallPosition();
        Random random = new Random();
        directionX = random.nextInt(2) * 2 -1;
        directionY = random.nextInt(2) * 2 -1; // bound = 2, so number can be 0, 1

        // load the sound that needs to be played when the ball hits the screen's edge
        hit_border_sound_id = ball_sp.load(context, R.raw.hit_border, 0);

    }

    public void update(long elapsed){

        float x = getX();
        float y = getY();

        // check if the ball (this object) hits the edges of the screen
        Rect screenRect = getScreenRect(); // invisible rectangle around the ball
        if(screenRect.left <= 0){
            ball_sp.play(hit_border_sound_id, 1, 1, 1, 0, 1);
            directionX = 1;  // +1 --> right, -1 --> left
        } else if(screenRect.right >= getScreenWidth()){
            ball_sp.play(hit_border_sound_id, 1, 1, 1, 0, 1);
            directionX = -1;
        }

        if(screenRect.top <= 0){
            ball_sp.play(hit_border_sound_id, 1, 1, 1, 0, 1);
            directionY = 1;  // +1 --> right, -1 --> left
        } else if(screenRect.bottom >= getScreenHeight()){
            ball_sp.play(hit_border_sound_id, 1, 1, 1, 0, 1);
            directionY = -1;
        }


        // this is the actual move --> The gameRunner makes sure it's being drawn
        x += directionX * speedX * elapsed;
        y += directionY * speedY * elapsed;

        setX(x);
        setY(y);

    }

    public int getDirectionX() {
        return directionX;
    }

    public void moveRight() {
        directionX = 1; // <-- right
    }

    public void moveLeft() {
        directionX = -1; // <-- left
    }

    public void initBallPosition(){
        setX(getScreenWidth()/2 - getRect().centerX());
        setY(getScreenHeight()/2 - getRect().centerY());
    }

    // usage: after x bounces increase the speed
    public void setSpeed(float speedX, float speedY){
        this.speedX += speedX;
        this.speedY += speedY;
    }

}
