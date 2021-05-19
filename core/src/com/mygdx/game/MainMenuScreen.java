package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuScreen implements Screen {
    final MyGdxGame game;
    OrthographicCamera camera;
    Texture pongAi;
    Sprite pongAiSprite;
    int screenWidth = 800;
    int screenHeight = 480;
    private Stage stage;

    // Actors
    MyActor playActor;
    MyActor trainActor;

    // Animation
    MyAnimation[] animations;
    int play = 0, trainAi = 1;

    public MainMenuScreen(final MyGdxGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // Scene2d stage
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // PongAI logo
        pongAi = new Texture(Gdx.files.internal("PongAI.png"));
        pongAiSprite = new Sprite(pongAi);
        pongAiSprite.setPosition(screenWidth / 2 - 400 / 2, screenHeight - 140 - 10);

        // animations
        animations = new MyAnimation[2];
        animations[play] = MyAnimation.initializeAnimation(play, 9, 1);
        animations[trainAi] = MyAnimation.initializeAnimation(trainAi, 9, 1);

        // actors
        playActor = new MyActor(animations[play], "play", game, this);
        trainActor = new MyActor(animations[trainAi], "train", game, this);
        stage.addActor(playActor);
        stage.addActor(trainActor);
    }

    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        //ScreenUtils.clear(0.4f, 0.5f, 0.6f, 1); // different color

        // stage
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        pongAiSprite.draw(game.batch);
        game.batch.end();

    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // test without this line!
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

    @Override
    public void dispose() {
        stage.dispose();
    }
}
