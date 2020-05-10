package com.db5443pr2454563denn.android.dennistennis;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameRunner runner;
    private Game game;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //SurfaceHolder holder = getHolder();
        game.onTouchEvent(event);
        return true; // to get all events
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // created
        Log.d("DB", "created");
        game = new Game(getHeight(), getWidth(), holder, getResources(), getContext());
        runner = new GameRunner(game);
        runner.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // resize
        Log.d("DB", "changed");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // app is on pause
        Log.d("DB", "destroyed");
        if(runner != null){
            runner.shut_down();
            // now wait a while to close all actions properly
            while (runner != null){
                try {
                    runner.join(); // this will terminate the thread eventually
                    runner = null;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
