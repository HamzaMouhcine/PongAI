package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen implements Screen {
    final MyGdxGame game;
    OrthographicCamera camera;
    Texture pongAi;
    Sprite pongAiSprite;
    int screenWidth = 800;
    int screenHeight = 480;

    // Animation
    MyAnimation[] animations;
    int play = 0;
    int trainAi = 1;

    public MainMenuScreen(final MyGdxGame game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        // PongAI logo
        pongAi = new Texture(Gdx.files.internal("PongAI.png"));
        pongAiSprite = new Sprite(pongAi);
        pongAiSprite.setPosition(screenWidth / 2 - 400 / 2, screenHeight - 140 - 10);

        // animations
        animations = new MyAnimation[2];
        animations[play] = initializeAnimation(play, 9, 1);
        animations[trainAi] = initializeAnimation(trainAi, 9, 1);
    }

    public MyAnimation initializeAnimation(int type, int FRAME_COLS, int FRAME_ROWS) {
        Texture sheet = null;
        if (type == play) {
            sheet = new Texture(Gdx.files.internal("play_sheet.png"));
        } else if (type == trainAi) {
            sheet = new Texture(Gdx.files.internal("train_sheet.png"));
        }
        TextureRegion[][] tmp = TextureRegion.split(sheet,
                sheet.getWidth() / FRAME_COLS,
                sheet.getHeight() / FRAME_ROWS);
        TextureRegion[] frames = new TextureRegion[FRAME_ROWS * FRAME_COLS];
        for (int i = 0; i < FRAME_COLS; i++) {
            frames[i] = tmp[0][i];
        }

        MyAnimation animation = null;
        if (type == play) {
            animation = new MyAnimation(0.025f, frames, 0, false);
            animation.setPosition(screenWidth / 2 - 150 / 2, 230, 150, 60);
        } else if (type == trainAi) {
            animation = new MyAnimation(0.025f, frames, 1, false);
            animation.setPosition(screenWidth / 2 - 150 / 2, 140, 150, 60);
        }
        return animation;
    }

    @Override
    public void show() {

    }

    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        //ScreenUtils.clear(0.4f, 0.5f, 0.6f, 1);

        // update state time of each animation
        updateAnimations();

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        pongAiSprite.draw(game.batch);
        for (MyAnimation a : animations) {
            game.batch.draw(a.getKeyFrame(), a.x, a.y);
        }
        game.batch.end();

        checkClick();
    }

    public void checkClick() {
        if (Gdx.input.isTouched()) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.input.getY();
            for (MyAnimation a : animations) {
                a.clicked(mouseX, mouseY, camera);
            }
        }
    }

    public void updateAnimations() {
        for (int i = 0; i < animations.length; i++) {
            MyAnimation a = animations[i];
            if (a.isAnimationFinished()) {
                a.doneAnimation(game, this);
            }
            if (a.activated) {
                a.stateTime += Gdx.graphics.getDeltaTime();
            }
        }
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

    @Override
    public void dispose() {

    }
}
