package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Iterator;

public class SelectGenomeScreen implements Screen {
    final MyGdxGame game;
    public Stage stage;
    public Table table;

    // Actors
    MyActor selectActor;
    MyActor backActor;

    // Animation
    MyAnimation[] animations;
    int select = 2, back = 3;

    public SelectGenomeScreen(final MyGdxGame game) {
        this.game = game;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        //table.setDebug(true); // This is optional, but enables debug lines for tables.

        // Add widgets to the table here.
        TextureRegion upRegion = new TextureRegion(new Texture(Gdx.files.internal("genome_button.png")));
        TextureRegion checkedRegion = new TextureRegion(new Texture(Gdx.files.internal("genome_button_checked.png")));
        TextureRegion descriptionRegion = new TextureRegion(new Texture(Gdx.files.internal("genome_description.png")));
        BitmapFont buttonFont = new BitmapFont();

        TextureRegionDrawable up = new TextureRegionDrawable(upRegion);
        TextureRegionDrawable checked = new TextureRegionDrawable(checkedRegion);
        TextureRegionDrawable description = new TextureRegionDrawable(descriptionRegion);
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(up, null, checked, buttonFont);
        TextButton.TextButtonStyle descriptionStyle = new TextButton.TextButtonStyle(description, null, null, buttonFont);

        // listens to button clicks for all the buttons in the list.
        ChangeListener listener = new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                if (!((TextButton)actor).isChecked()) return;
                System.out.println("hello "+actor.getName()+" "+((TextButton)actor).isChecked());
                Array<Actor> buttons = table.getChildren();
                for (int i = 0; i < buttons.size; i++) {
                    if (((TextButton)actor).getText() == ((TextButton)buttons.get(i)).getText()) continue;
                    TextButton tb = (TextButton)buttons.get(i);
                    System.out.println("hello "+tb.getText());
                    tb.setChecked(false);
                }
            }
        };


        table = new Table();

        TextButton button4 = new TextButton("Button 4", style);
        TextButton button5 = new TextButton("Button 5", style);
        TextButton descriptionButton = new TextButton("Great genome", descriptionStyle);

        TextButton button1 = new TextButton("Button 1", style);
        button1.setText("Button 1");
        button1.addListener(listener);
        table.add(button1).spaceBottom(20).row();
        TextButton button2 = new TextButton("Button 2", style);
        button2.addListener(listener);
        table.add(button2).spaceBottom(20).row();
        table.add(button4).spaceBottom(20).row();
        table.add(button5);

        TextButton button3 = new TextButton("button 3", style);
        TextButton button10 = new TextButton("button 10", style);
        TextButton button11 = new TextButton("button 11", style);
        Image selectGenome = new Image(new Texture("select_genome.png"));
        Image empty = new Image(new Texture("empty.png"));

        Table outerTable = new Table();
        stage.addActor(outerTable);
        outerTable.setFillParent(true);
        ScrollPane scroll = new ScrollPane(table);

        // Animations
        animations = new MyAnimation[2];
        animations[select-2] = MyAnimation.initializeAnimation(select, 9, 1);
        animations[back-2] = MyAnimation.initializeAnimation(back, 9, 1);

        // actors
        selectActor = new MyActor(animations[select-2], "select", game, this);
        backActor = new MyActor(animations[back-2], "back", game, this);
        //stage.addActor(selectActor);
        //stage.addActor(backActor);

        //first row
        outerTable.add(selectGenome).height(90).colspan(3).spaceBottom(10).expandX().row();

        // second row
        outerTable.add(scroll).width(300).height(300).spaceRight(10).top();
        outerTable.add(descriptionButton).width(490).height(300).colspan(2).top();

        // last row
        outerTable.row();
        outerTable.add(empty).width(300).spaceRight(10);
        outerTable.add(backActor).width(235).spaceRight(5);
        //outerTable.add(button11).width(235).spaceLeft(5);
        outerTable.add(selectActor).width(235).spaceLeft(5);

        //outerTable.debug();
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
