package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

	public void clicked(float inputX, float inputY, OrthographicCamera camera) {
		Vector3 vec=new Vector3(inputX, inputY,0);
		camera.unproject(vec);
		inputX = vec.x;
		inputY = vec.y;
		if (x <= inputX && inputX <= x + width && y <= inputY && inputY <= y + height) {
			activated = true;
			stateTime = 0;
		}
	}

	public TextureRegion getKeyFrame() {
		return getKeyFrame(stateTime);
	}

	public boolean isAnimationFinished() {
		return isAnimationFinished(stateTime);
	}

	public void doneAnimation(MyGdxGame game, MainMenuScreen screen) {
		// case work
		if (type == 0) { // play Button
			game.setScreen(new GameScreen(game));
            screen.dispose();
		} else if (type == 1) { // trainAi Button

		}
	}
}