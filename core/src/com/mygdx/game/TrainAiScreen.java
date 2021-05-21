package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Iterator;

public class TrainAiScreen implements Screen {
    final MyGdxGame game;
    public Stage stage;
    public MyAnimation[] animations;
    static int start = 4;
    static int back = 3;
    static MyActor startActor;
    static MyActor backActor;

    public TrainAiScreen(final MyGdxGame game) {
        this.game = game;
        stage = new Stage();
        Table table = new Table();

        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        Image settings = new Image(new Texture("settings.png"));
        table.add(settings).width(800).height(100).spaceBottom(10).colspan(2).row();

        Label checkLabel = new Label("Run Genomes of a generation", skin);
        checkLabel.setAlignment(Align.center);
        table.add(checkLabel).colspan(2).expandX().fillX();

        CheckBox checkBoxAll = new CheckBox("All at once", skin);
        CheckBox checkBoxOne = new CheckBox("One by one", skin);

        table.row();
        ButtonGroup runBox = new ButtonGroup();
        runBox.add(checkBoxAll);
        runBox.add(checkBoxOne);
        table.add(checkBoxAll).expandX().fillX();
        table.add(checkBoxOne).expandX().fillX();

        table.row().spaceTop(10);
        Label sizeLabel = new Label("Generation Size", skin);
        sizeLabel.setAlignment(Align.center);
        table.add(sizeLabel).colspan(2).expandX().fillX();

        table.row();
        TextField generationSize = new TextField("15", skin);
        table.add(generationSize).colspan(2);
        String test = generationSize.getText(); // how to read the input

        table.row().spaceTop(10);
        Label generationLabel = new Label("Number of generations", skin);
        generationLabel.setAlignment(Align.center);
        table.add(generationLabel).colspan(2).expandX().fillX();

        table.row();
        TextField numberOfGenerations = new TextField("20", skin);
        table.add(numberOfGenerations).colspan(2);

        stage.addActor(table);
        table.setFillParent(true);
        table.top();
        //table.debug();

        // Animations
        animations = new MyAnimation[5];
        animations[start] = MyAnimation.initializeAnimation(start, 9, 1, Gdx.files.internal("start_sheet.png"), 565, 0, 235, 80);
        animations[back] = MyAnimation.initializeAnimation(back, 9, 1, Gdx.files.internal("back_sheet.png"), 0, 0, 235, 80);

        // actors
        startActor = new MyActor(animations[start], "start", game, this);
        backActor = new MyActor(animations[back], "back", game, this);

        Image empty = new Image(new Texture(Gdx.files.internal("empty.png")));

        Table foot = new Table();
        foot.add(backActor);
        foot.add(empty).expandX().fillX();
        foot.add(startActor);

        stage.addActor(foot);
        foot.setFillParent(true);
        foot.bottom();
        //foot.debug();
    }

    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ScreenUtils.clear(0, 0, 0.2f, 1);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // test without this line!
        //table.setTransform(true);
        //table.setSize(width, height);
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
