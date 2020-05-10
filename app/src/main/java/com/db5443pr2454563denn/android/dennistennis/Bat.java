package com.db5443pr2454563denn.android.dennistennis;

import android.graphics.Bitmap;
import android.graphics.Rect;

import java.util.Random;

public class Bat extends Sprite{

    private Random random = new Random();
    private int direction;
    private float speed = 0.4f;

    public void setPosition(float y) {
        setY(y - getRect().centerY()); // move bat from the center (not top left corner)
    }

    public void update(long elapsed, Ball ball) {

        // if the ball is going towards the opponent then this side does not have to move
        if (ball.getDirectionX() < 0){
            return;
        }

        int decision = random.nextInt(20); // <-- generate a random number: 20 possibilities 0 .. 19
        if(decision == 0){ // one in 20 times theoretically
            direction = 1;
        } else if(decision == 1){ // also one in 20 times theoretically
            direction = random.nextInt(2)*2 - 1;;
        }
        else if(decision < 4){ // 2 out of 20 (0 and 1 are already handled) times it should move towards the ball
            if(ball.getScreenRect().centerY() < getScreenRect().centerY()) {  // <-- (ball.getScreenRect()) this gets the rectangle directly surrounding the ball
               direction = -1;                                                // <-- (getScreenRect()) get the rectangle representing the bat (this class). centreY get the the center Y coordinate
            } else {
                direction = 1;
            }
        }

        if(getScreenRect().top <= 0){ // <-- top of the bast will go off the screen; switch direction
            direction = 1;
        } else if(getScreenRect().bottom >= getScreenHeight()){ // <-- it's going of the bottom end.
            direction = -1;
        }

        float y = getY();
        // get y coordinate, do something do it en give it back (setY)
        y += direction * speed * elapsed; // only update y (up and down)
        setY(y);
    }

    public enum Position{
        LEFT, RIGHT
    }

    private Position position;
    private static final int margin = 40;


    public Bat(int screenWidth, int screenHeight, Position position) {
        super(screenWidth, screenHeight);
        this.position = position;
    }

    @Override
    public void init(Bitmap image, Bitmap shadow) {
        super.init(image, shadow);
        initPosition();
        direction = random.nextInt(2)*2 - 1;
                /*
                    random.nextInt(2) results in 0 or 1
                    *2 results in 0 or 2
                    -1 results in 0 - 1 = -1 or 2 - 1 = 1
                */
        if(position == Position.LEFT){
            setX(margin);
        }else if(position == Position.RIGHT){
            setX(getScreenWidth() - (margin + getRect().width()));
        }
    }

    public void initPosition(){
        setY(getScreenHeight()/2 - getRect().centerY()); // get center from 'bitmap' getRect image boundaries
    }

}
