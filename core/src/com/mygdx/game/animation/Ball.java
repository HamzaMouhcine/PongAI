package com.mygdx.game.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.ai.*;

public class Ball {
    public float x;
    public float y;
    public float radius;
    float xSpeed;
    float ySpeed;
    float screenWidth = 800;
    float screenHeight = 480;
    int defense1;
    public int defense2;

    public Ball() {
        radius = 7;
        initialize(true);
    }

    public void update() {
        this.x += xSpeed * Gdx.graphics.getDeltaTime();
        this.y += ySpeed * Gdx.graphics.getDeltaTime();
    }

    public int checkBorders() {
        int borderValue = horizontalBorder();
        verticalBorder();
        return borderValue;
    }

    public void verticalBorder() {
        if (y-radius <= 0) {
            ySpeed *= -1;
            y = radius;
        }

        if (y+radius >= screenHeight) {
            ySpeed *= -1;
            y = screenHeight - radius;
        }
    }

    public int horizontalBorder() {
        int res = 0;
        if (x-radius <= 0 || x+radius >= screenWidth) {
            if (xSpeed > 0) { //  left border collision
                res = -1;
            } else {   // right border collision
                res = 1;
            }
            initialize(false);
        }

        return res;
    }

    public void initialize(boolean reset) {
        x = screenWidth / 2;
        y = screenHeight / 2;
        float angle = MathUtils.random(-MathUtils.PI/4, MathUtils.PI/4);
        xSpeed = 300 * MathUtils.cos(angle);
        ySpeed = 300 * MathUtils.sin(angle);

        if (MathUtils.random(1) <= 0.5) xSpeed *= -1;
        if (reset) {
            defense1 = 0;
            defense2 = 0;
        }
    }

    public void checkCollision(Paddle p, Sound ding, Genome genome) {
        float impactX = x;
        float impactY = y;
        if (this.xSpeed > 0) {  //  right paddle collision
            impactX += radius;
            if (p.y <= impactY && impactY <= p.y+p.height
                    &&  p.x <= impactX && impactX <= p.x+p.width) {
                ding.play();
                float angle = 45;
                xSpeed = -300 * MathUtils.cosDeg(MathUtils.map(p.y, p.y+p.height, -angle, angle, y));
                ySpeed =  300 * MathUtils.sinDeg(MathUtils.map(p.y, p.y+p.height, -angle, angle, y));
                x = p.x - radius;
                defense2++;
                genome.fitness++;
            }
        } else {    // left paddle collision
            impactX -= radius;
            if (p.y <= impactY && impactY <= p.y+p.height
                    &&  p.x <= impactX && impactX <= p.x+p.width) {
                ding.play();
                float angle = 45;
                xSpeed = 300 * MathUtils.cosDeg(MathUtils.map(p.y, p.y+p.height, -angle, angle, y));
                ySpeed = 300 * MathUtils.sinDeg(MathUtils.map(p.y, p.y+p.height, -angle, angle, y));
                x = p.x + p.width + radius;
                defense1++;
            }
        }
    }
}
