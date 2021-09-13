package com.mygdx.game.myscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.*;
import com.mygdx.game.animation.*;
import com.mygdx.game.ai.*;
import java.util.ArrayList;

public class GameScreen implements Screen {
    final MyGdxGame game;
    OrthographicCamera camera;
    ShapeRenderer sp;
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
    public boolean playWithBest = false, complete = false;
    public boolean autoPlay = true;
    public boolean allAtOnce;
    public boolean playOnce = false, test = false;
    public ArrayList<Genome> trainGenomes;

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

    public GameScreen (final MyGdxGame game, Genome genome, boolean playOnce, boolean test) {
        this(game);
        this.playOnce = playOnce;
        this.genome = genome;
        this.test = test;
        if (test) genome.reset();
    }

    // to create a game to train genomes (created after clicking on start on the TrainingAiScreen)
    public GameScreen (final MyGdxGame game, int generationSize, int numberOfGenerations, boolean allAtOnce) {
        this.game = game;
        this.allAtOnce = allAtOnce;
        this.playOnce = false;
        repeat = numberOfGenerations;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        sp = new ShapeRenderer();
        ding = Gdx.audio.newSound(Gdx.files.internal("ping.mp3"));

        // AI stuff
        layerConfig = new int[]{2, 3, 3, 1};
        gen = new Generation(generationSize, layerConfig, 0.2f);
    }

    @Override
    public void render (float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            autoPlay = !autoPlay;
        }

        // if we came from selectedButton screen (we only play one time, best to reach 5).
        if (playOnce) {
            // return back to selectedGenome screen if a player reached 5 points:
            if (genome.score1 == 5 || genome.score2 == 5 || !genome.isPlaying) {
                game.setScreen(new SelectGenomeScreen(game));
                this.dispose();
            } else {
                play();
            }
            return;
        } else if (test) {
            // return back to trainDoneScreen
            if (genome.score1 == 3 || genome.score2 == 3 || !genome.isPlaying) {
                game.setScreen(new TrainingDoneScreen(game, trainGenomes));
                this.dispose();
            } else {
                play();
            }
            return;
        }

        if (complete) {
            //play();
            game.setScreen(new TrainingDoneScreen(game, gen.population));
            this.dispose();
        } else if (allAtOnce) { // run all the genomes of the current generation at once
            nextGeneration();
            for (int i = 0; i < gen.size(); i++) {
                genome = gen.get(i);
                play();
            }
        } else { // run the genomes of the current generation one by one.
            nextGenome();
            play();
        }
    }

    // moves to the next genome if the evaluation of the current genome is complete.
    public void nextGenome() {
        if (genome == null) {
            genome = gen.get(0);
        }

        if (genome.isPlaying) {
            return;
        }

        if (gen.currentGenome == gen.size() - 1) {
            nextGeneration();
        } else {
            gen.currentGenome++;
            genome = gen.get(gen.currentGenome);
        }
    }

    // creates a new generation if the evaluation of the current generation is complete.
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
            genome = gen.get(0);
        } else { // play with the best genome from this generation.
            genome = gen.bestGenome();
            playWithBest = true;
            complete = true;
        }
    }

    // updates + renders the ball and paddles of the selected genome.
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
        ball.checkCollision(leftPaddle, ding, genome);
        ball.checkCollision(rightPaddle, ding, genome);

        // update game (stop playing if condition verified)
        if (!checkScore()) return;

        double[] output = genome.getOutput();
        // Scores
        game.batch.begin();
        game.font.draw(game.batch, ""+genome.score1, screenWidth/2-50, screenHeight-20);
        game.font.draw(game.batch, ":", screenWidth/2, screenHeight-20);
        game.font.draw(game.batch, ""+genome.score2, screenWidth/2+50, screenHeight-20);

        if (playOnce || test) {}
        else if (allAtOnce) {
            game.font.draw(game.batch, "current Generation: " + gen.generationNumber + ", remaining: " + gen.remaining, 20, screenHeight - 50);
        } else {
            game.font.draw(game.batch, "current Generation: " + gen.generationNumber + ", currentGenome: " + gen.currentGenome + ", remaining: " + gen.remaining + ", fitness: " + genome.ball.defense2, 20, screenHeight - 50);
        }
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

    // returns true if the genome is still playing,
    // else it returns false and reduces the number of remaining genomes (only once)
    public boolean checkScore() {
        int limitScore = 2;
        if (playOnce) limitScore = 5;
        if (test) limitScore = 3;

        if (genome.score1 == limitScore || genome.fitness == 5) {
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
