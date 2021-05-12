package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class MyAnimation extends Animation<TextureRegion> {
	public float stateTime;
	public int type;
	public boolean loop;
	public boolean activated;
	public int x;
	public int y;
	public int width;
	public int height;
	public static int screenWidth = 800;
	public static int screenHeight = 480;
	public static int play = 0;
	public static int trainAi = 1;

	MyAnimation(float frameDuration, TextureRegion[] frames, int type, boolean loop) {
		super(frameDuration, frames);
		this.stateTime = 0;
		this.type = type;
		this.loop = loop;
		this.activated = false;
	}

	public void setPosition(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public TextureRegion getKeyFrame() {
		return getKeyFrame(stateTime);
	}

	public boolean isAnimationFinished() {
		return isAnimationFinished(stateTime);
	}

	public void doneAnimation(MyGdxGame game, MainMenuScreen screen, String name) {
		// case work
		if (name == "play") { // play Button
			game.setScreen(new GameScreen(game));
            screen.dispose();
		} else if (name == "train") { // trainAi Button

		}
	}

	public static MyAnimation initializeAnimation(int type, int FRAME_COLS, int FRAME_ROWS) {
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
}