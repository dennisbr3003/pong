package com.db5443pr2454563denn.android.dennistennis;

import android.util.Log;

public class GameRunner extends Thread {

    private volatile boolean running = true; // volatile = to avoid caching
    private Game game;

    public GameRunner(Game game){
        this.game = game;
    }

    @Override
    public void run() {
        super.run();

        game.init();

        long lastTime = System.currentTimeMillis();

        // gameloop -->
        while(running){
            long now = System.currentTimeMillis();
            if((now - lastTime) < 100) { // <-- don't want the calculations to be distorted with exceptionally large intervals
                game.update(now - lastTime);
                game.draw();
            }
            lastTime = now;
        }

    }
    public void shut_down(){
        running = false; // now; we do not want this variable cached or the loop won't stop
    }
}
