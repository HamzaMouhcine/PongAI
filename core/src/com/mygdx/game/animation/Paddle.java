package com.mygdx.game.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;

public class Paddle {
    public float x;
    public float y;
    public float width = 20;
    public float height = 100;
    float screenWidth = 800;
    float screenHeight = 480;

    public Paddle(float x) {
        this.x = x;
        this.y = (screenHeight / 2) - (height / 2);
    }

    void initialize() {
        this.y = (screenHeight / 2) - (height / 2);
    }

    public void updateLeft() {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) this.y -= 300 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.A)) this.y += 300 * Gdx.graphics.getDeltaTime();
    }

    void updateRight() {
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) this.y -= 300 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) this.y += 300 * Gdx.graphics.getDeltaTime();
    }

    public void aiUpdateLeft(double paddlePosition, double ballPosition) {
        if (paddlePosition < ballPosition) this.y += 300 * Gdx.graphics.getDeltaTime();
        else if (paddlePosition > ballPosition) this.y -= 300 * Gdx.graphics.getDeltaTime();
    }

    public void aiUpdateRight(int action) {
        if (action == 1) {          // move the paddle up.
            this.y += 300 * Gdx.graphics.getDeltaTime();
        } else if (action == -1) {  // move the paddle down.
            this.y -= 300 * Gdx.graphics.getDeltaTime();
        }
    }

    public void checkBorders() {
        if (y < 0) {
            y = 0;
        }
        if (y+height > screenHeight) {
            float delta = y+height-screenHeight;
            y -= delta;
        }
    }
}
