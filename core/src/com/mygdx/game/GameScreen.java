package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;

public class GameScreen implements Screen {
    final MyGdxGame game;
    OrthographicCamera camera;
    ShapeRenderer sp;
    Rectangle paddle;
    Paddle leftPaddle;
    Paddle rightPaddle;
    int screenWidth = 800;
    int screenHeight = 480;
    Ball ball;
    Sound ding;
    Genome genome;
    Generation gen;
    int[] layerConfig;
    static int repeat = 50;
    static boolean playWithBest = false;
    static boolean autoPlay = true;

    public GameScreen (final MyGdxGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        sp = new ShapeRenderer();
        ding = Gdx.audio.newSound(Gdx.files.internal("ping.mp3"));

        // AI stuff
        layerConfig = new int[]{2, 3, 3, 1};
        gen = new Generation(15, layerConfig, 0.2f);
    }

    @Override
    public void render (float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            autoPlay = !autoPlay;
        }

        nextGeneration();

        if (playWithBest) {
            play();
        } else {
            for (int i = 0; i < gen.size(); i++) {
                genome = gen.get(i);
                play();
            }
        }
    }

    public void nextGeneration() {
        boolean on = false;
        for (int i = 0; i < gen.size(); i++) {
            genome = gen.get(i);
            if (genome.isPlaying == true) {
                on = true;
            }
        }

        if (on) return;

        if (gen.generationNumber != repeat) { // create a new generation.
            gen.newGeneration(gen);
        } else { // play with the best genome from this generation.
            genome = gen.bestGenome();
            playWithBest = true;
        }
    }

    public void play() {
        leftPaddle = genome.leftPaddle;
        rightPaddle = genome.rightPaddle;
        ball = genome.ball;

        // update game (stop playing if condition verified)
        if (!checkScore()) return;


        // AI figuring out what to do...
        double[] input = new double[2];
        input[0] = ball.y;
        input[1] = rightPaddle.y;
        int action = genome.getAction(input);


        // move paddles.
        if (!autoPlay) {
            leftPaddle.updateLeft();
        } else {
            leftPaddle.aiUpdateLeft(leftPaddle.y + leftPaddle.height / 2, ball.y);
        }
        leftPaddle.checkBorders();
        //rightPaddle.updateRight();
        rightPaddle.aiUpdateRight(action);
        rightPaddle.checkBorders();

        // move ball
        ball.update();
        int borderValue = ball.checkBorders();
        updateScore(borderValue);
        ball.checkCollision(leftPaddle, ding);
        ball.checkCollision(rightPaddle, ding);

        // update game (stop playing if condition verified)
        if (!checkScore()) return;

        double[] output = genome.getOutput();
        // Scores
        game.batch.begin();
        game.font.draw(game.batch, ""+genome.score1, screenWidth/2-50, screenHeight-20);
        game.font.draw(game.batch, ":", screenWidth/2, screenHeight-20);
        game.font.draw(game.batch, ""+genome.score2, screenWidth/2+50, screenHeight-20);
        game.font.draw(game.batch, "current Generation: "+gen.generationNumber+", currentGenome: "+gen.currentGenome+", remaining: "+gen.remaining+", fitness: "+genome.ball.defense2, 20, screenHeight - 50);
        game.batch.end();

        sp.begin(ShapeRenderer.ShapeType.Filled);
        sp.setColor(1, 1, 1, 1);
        sp.rect(leftPaddle.x, leftPaddle.y, leftPaddle.width, leftPaddle.height);
        sp.rect(rightPaddle.x, rightPaddle.y, rightPaddle.width, rightPaddle.height);
        sp.circle(ball.x, ball.y, ball.radius);
        sp.end();
    }

    public void updateScore(int borderValue) {
        if (borderValue == 1) {
            genome.score2++;
        } else if (borderValue == -1) {
            genome.score1++;
        }
    }

    public boolean checkScore() {
        if (genome.score1 == 2) {
            // stop the game
            if (genome.isPlaying) gen.remaining--;
            genome.isPlaying = false;
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void dispose () {

    }

    @Override
    public void show() { // when the screen loads

    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}