package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
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
	public static int select = 2;
	public static int back = 3;
	public static int start = 4;

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

	public void doneAnimation(MyGdxGame game, Screen screen, String name) {
		if (name == "play") { // play Button
			game.setScreen(new SelectGenomeScreen(game));
			screen.dispose();
		} else if (name == "train") { // trainAi Button
			game.setScreen(new TrainAiScreen(game));
			screen.dispose();
		} else if (name == "select") {

		} else if (name == "back") {
			game.setScreen(new MainMenuScreen(game));
			screen.dispose();
		} else if (name == "start") {

		}
	}

	public static MyAnimation initializeAnimation(int type, int FRAME_COLS, int FRAME_ROWS, FileHandle file, int x, int y, int width, int height) {
		Texture sheet = new Texture(file);

		TextureRegion[][] tmp = TextureRegion.split(sheet,
				sheet.getWidth() / FRAME_COLS,
				sheet.getHeight() / FRAME_ROWS);
		TextureRegion[] frames = new TextureRegion[FRAME_ROWS * FRAME_COLS];
		for (int i = 0; i < FRAME_COLS; i++) {
			frames[i] = tmp[0][i];
		}

		MyAnimation animation = null;
		animation = new MyAnimation(0.025f, frames, 0, false);
		animation.setPosition(x, y, width, height);

		return animation;
	}

	public static MyAnimation initializeAnimation(int type, int FRAME_COLS, int FRAME_ROWS) {
		Texture sheet = null;
		if (type == play) {
			sheet = new Texture(Gdx.files.internal("play_sheet.png"));
		} else if (type == trainAi) {
			sheet = new Texture(Gdx.files.internal("train_sheet.png"));
		} else if (type == select) {
			sheet = new Texture(Gdx.files.internal("select_sheet.png"));
		} else if (type == back) {
			sheet = new Texture(Gdx.files.internal("back_sheet.png"));
		} else if (type == start) {
			sheet = new Texture(Gdx.files.internal("start_sheet.png"));
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
		} else if (type == select) {
			animation = new MyAnimation(0.025f, frames, 2, false);
			animation.setPosition(555, 0, 235, 80);
		} else if (type == back) {
			animation = new MyAnimation(0.025f, frames, 3, false);
			animation.setPosition(310, 0, 235, 80);
		}

		return animation;
	}
}