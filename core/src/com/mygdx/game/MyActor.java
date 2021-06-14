package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class MyActor extends Actor {
	MyAnimation animation;
	MyGdxGame game;
	Screen screen;
	public Genome genome;

	public MyActor(MyAnimation a, String name, MyGdxGame game, Screen screen) {
		this.animation = a;
		this.game = game;
		this.screen = screen;
		setBounds(a.x, a.y, a.width, a.height);
		setName(name);

		addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				animation.activated = true;
				animation.stateTime = 0;
				return true;
			}
		});
	}

	public void draw(Batch batch, float parentAlpha) {
		Color color = getColor();
		batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
		batch.draw(animation.getKeyFrame(), animation.x, animation.y);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (animation.isAnimationFinished()) {
			if (getName() == "select") animation.startPlayScreen(game, screen, genome);
			else animation.doneAnimation(game, screen, getName());
		}
		if (animation.activated) {
			animation.stateTime += delta;
		}
	}
}