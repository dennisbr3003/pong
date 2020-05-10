package com.db5443pr2454563denn.android.dennistennis;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {

    private float x;
    private float y;
    private int screenHeight;
    private int screenWidth;
    private Bitmap image;
    private Bitmap shadow;
    private Rect boundaries;


    public Sprite(int screenWidth, int screenHeight){
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

    public void init(Bitmap image, Bitmap shadow){
        this.image = image;
        this.shadow = shadow;
        boundaries = new Rect(0, 0, image.getWidth(), image.getHeight());
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public Rect getRect(){
        return boundaries;
    }

    public Rect getScreenRect(){
        return new Rect((int)x, (int)y, (int) (int)(x + getRect().width()), (int)(y + getRect().height()));
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(shadow, x, y, null);
        canvas.drawBitmap(image, x, y, null);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
