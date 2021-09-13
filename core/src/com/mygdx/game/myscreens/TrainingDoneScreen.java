package com.mygdx.game.myscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import com.mygdx.game.*;
import com.mygdx.game.animation.*;
import com.mygdx.game.ai.*;

public class TrainingDoneScreen implements Screen {
    final MyGdxGame game;
    public Stage stage;
    public ArrayList<Genome> population;
    public Skin skin;
    public myTextButton selected;
    static int done = 7;


    public TrainingDoneScreen(final MyGdxGame game, final ArrayList<Genome> population) {
        this.game = game;
        this.population = population;
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Table outerTable = new Table();
        stage.addActor(outerTable);
        outerTable.setFillParent(true);


        // Title row:
        Image result = new Image(new Texture("result.png"));
        result.setPosition(0, 0);
        outerTable.add(result).colspan(4).height(100).expandX().row();

        // Last generation genomes panel:
        Table genomesPanel = new Table();
        TextureRegion upRegion = new TextureRegion(new Texture(Gdx.files.internal("genome_result.png")));
        TextureRegion checkedRegion = new TextureRegion(new Texture(Gdx.files.internal("genome_result_selected.png")));
        BitmapFont buttonFont = new BitmapFont();
        TextureRegionDrawable up = new TextureRegionDrawable(upRegion);
        TextureRegionDrawable checked = new TextureRegionDrawable(checkedRegion);
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(up, null, checked, buttonFont);

        // filling the panel with the given generation of genomes.
        ButtonGroup genomesGroup = new ButtonGroup();
        for (int i = 0; i < population.size(); i++) {
            Genome genome = population.get(i);
            myTextButton genomeButton = new myTextButton("Genome "+(i+1)+"\n score : "+genome.score1+" - "+genome.score2+"\n fitness : "+genome.fitness, style);
            genomeButton.genome = genome;
            genomeButton.addListener(new ChangeListener() {
                public void changed (ChangeEvent event, Actor actor) {
                    // save genome here!
                    if (((myTextButton)actor).isChecked()) {
                        System.out.println("genome changed: " + ((myTextButton) actor).getText());
                        selected = ((myTextButton) actor);
                    }
                }
            });
            genomesGroup.add(genomeButton);
            if ((i+1)%4 == 0) genomesPanel.add(genomeButton).row();
            else genomesPanel.add(genomeButton);
        }

        ScrollPane genomesPanelScroll = new ScrollPane(genomesPanel);
        outerTable.add(genomesPanelScroll).colspan(4).height(300).width(800).row();

        // last row: Test, Save, and Done buttons:
        //save
        TextureRegion saveUpRegion = new TextureRegion(new Texture(Gdx.files.internal("save.png")));
        TextureRegion saveHoverRegion = new TextureRegion(new Texture(Gdx.files.internal("save_hover.png")));
        TextureRegionDrawable saveUp = new TextureRegionDrawable(saveUpRegion);
        TextureRegionDrawable saveHover = new TextureRegionDrawable(saveHoverRegion);
        Button.ButtonStyle saveStyle = new Button.ButtonStyle(saveUp, null, null);
        saveStyle.over = saveHover;
        Button save = new Button(saveStyle);

        save.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                final TextField genomeName = new TextField("name", skin);
                Dialog dialog = new Dialog("genome name", skin) {
                    public void result(Object obj) {
                        selected.save(genomeName.getText());
                    }
                };

                dialog.getContentTable().add(genomeName);
                dialog.button("save");
                dialog.key(Input.Keys.ENTER, true);
                dialog.show(stage);
            }
        });
        outerTable.add(save).spaceTop(5).width(180).height(70).left();

        //test
        TextureRegion testUpRegion = new TextureRegion(new Texture(Gdx.files.internal("test.png")));
        TextureRegion testHoverRegion = new TextureRegion(new Texture(Gdx.files.internal("test_hover.png")));
        TextureRegionDrawable testUp = new TextureRegionDrawable(testUpRegion);
        TextureRegionDrawable testHover = new TextureRegionDrawable(testHoverRegion);
        Button.ButtonStyle testStyle = new Button.ButtonStyle(testUp, null, null);
        testStyle.over = testHover;
        Button test = new Button(testStyle);
        test.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selected.genome.reset();
                GameScreen gameScreen = new GameScreen(game, selected.genome, false, true);
                gameScreen.trainGenomes = population;
                game.setScreen(gameScreen);
                dispose();
            }
        });
        outerTable.add(test).spaceBottom(5).spaceTop(5).width(180).height(70).left();

        // filling space
        Image empty = new Image(new Texture("empty.png"));
        outerTable.add(empty).spaceTop(5).width(180).height(70);

        //done
        TextureRegion doneUpRegion = new TextureRegion(new Texture(Gdx.files.internal("done.png")));
        TextureRegion doneHoverRegion = new TextureRegion(new Texture(Gdx.files.internal("done_hover.png")));
        TextureRegionDrawable doneUp = new TextureRegionDrawable(doneUpRegion);
        TextureRegionDrawable doneHover = new TextureRegionDrawable(doneHoverRegion);
        Button.ButtonStyle doneStyle = new Button.ButtonStyle(doneUp, null, null);
        doneStyle.over = doneHover;
        Button done = new Button(doneStyle);
        done.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
            }
        });
        outerTable.add(done).spaceTop(5).width(180).height(70).right();

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
