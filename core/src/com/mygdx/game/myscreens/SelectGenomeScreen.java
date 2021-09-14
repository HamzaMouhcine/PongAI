package com.mygdx.game.myscreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.*;
import com.mygdx.game.animation.*;
import com.mygdx.game.ai.*;
import com.mygdx.game.InputUtility.*;
import java.util.ArrayList;



public class SelectGenomeScreen implements Screen {
    final MyGdxGame game;
    public Stage stage;
    public Table table;
    public Genome selectedGenome;
    public TextButton descriptionButton;

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
        TextureRegion descriptionRegion = new TextureRegion(new Texture(Gdx.files.internal("genome_description.png")));
        BitmapFont buttonFont = new BitmapFont();

        TextureRegionDrawable description = new TextureRegionDrawable(descriptionRegion);
        TextButton.TextButtonStyle descriptionStyle = new TextButton.TextButtonStyle(description, null, null, buttonFont);

        // listens to button clicks for all the buttons in the list.
        ChangeListener listener = new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                // get selected genome to update genome description component
                if (((myTextButton)actor).isChecked()) {
                    System.out.println("genome clicked: " + ((myTextButton) actor).genome.name);
                    selectedGenome = ((myTextButton) actor).genome;
                }
            }
        };

        ChangeListener deleteListener = new ChangeListener() {
            public void changed (ChangeEvent event, Actor actor) {
                // get selected genome to update genome description component
                if (((myTextButton)actor).isChecked()) {
                    System.out.println("genome deleted: " + ((myTextButton) actor).genome.name);
                    InputUtility.deleteGenome(((myTextButton) actor).genome.name);
                    ((myTextButton) actor).genomeActor.remove();
                    actor.remove();
                }
            }
        };


        table = new Table();
        descriptionButton = new TextButton("Great genome", descriptionStyle);

        // Last generation genomes panel:
        Table genomesPanel = new Table();
        TextureRegion upRegion = new TextureRegion(new Texture(Gdx.files.internal("genome_result.png")));
        TextureRegion upRegion2 = new TextureRegion(new Texture(Gdx.files.internal("delete_button.png")));
        TextureRegion checkedRegion = new TextureRegion(new Texture(Gdx.files.internal("genome_result_selected.png")));
        TextureRegionDrawable up = new TextureRegionDrawable(upRegion);
        TextureRegionDrawable up2 = new TextureRegionDrawable(upRegion2);
        up.setMinSize(200, 100);
        up2.setMinSize(100, 90);
        TextureRegionDrawable checked = new TextureRegionDrawable(checkedRegion);
        TextureRegionDrawable checked2 = new TextureRegionDrawable(checkedRegion);
        checked.setMinSize(200, 100);
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(up, null, checked, buttonFont);
        TextButton.TextButtonStyle style2 = new TextButton.TextButtonStyle(up2, null, null, buttonFont);

        // filling the panel with the given generation of genomes.
        ButtonGroup genomesGroup = new ButtonGroup();

        ArrayList<Genome> savedGenomes = InputUtility.getGenomes();
        for (int i = 0; i < savedGenomes.size(); i++) {
            Genome genome = savedGenomes.get(i);
            myTextButton genomeButton = new myTextButton(genome.name, style);
            myTextButton deleteButton = new myTextButton("", style2);
            genomeButton.setSize(200, 100);

            genomeButton.genome = genome;
            deleteButton.genome = genome;
            deleteButton.genomeActor = genomeButton;
            genomeButton.addListener(listener);
            deleteButton.addListener(deleteListener);
            genomesGroup.add(genomeButton);

            genomesPanel.add(genomeButton);
            genomesPanel.add(deleteButton).row();
        }

        Image selectGenome = new Image(new Texture("select_genome.png"));
        Image empty = new Image(new Texture("empty.png"));

        Table outerTable = new Table();
        stage.addActor(outerTable);
        outerTable.setFillParent(true);
        ScrollPane genomesPanelScroll = new ScrollPane(genomesPanel);


        // skldfdmfksmfjdmfjsdkljslmfjd
        //first row  check here if only 2 columns are needed!!!!!!!!!!!!!!!!!!!!
        outerTable.add(selectGenome).height(90).colspan(3).spaceBottom(10).expandX().row();

        // second row
        outerTable.add(genomesPanelScroll).width(300).height(300).spaceRight(10).top();
        outerTable.add(descriptionButton).width(490).height(300).colspan(2).top();

        // last row
        // Animations
        animations = new MyAnimation[2];
        animations[select-2] = MyAnimation.initializeAnimation(select, 9, 1);
        animations[back-2] = MyAnimation.initializeAnimation(back, 9, 1);

        // actors
        selectActor = new MyActor(animations[select-2], "select", game, this);
        backActor = new MyActor(animations[back-2], "back", game, this);

        outerTable.row();
        outerTable.add(empty).width(300).spaceRight(10);
        outerTable.add(backActor).width(235).spaceRight(5);
        outerTable.add(selectActor).width(235).spaceLeft(5);

        //outerTable.debug();
    }

    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // update genome description:
        if (selectedGenome != null) {
            descriptionButton.setText(selectedGenome.name+"\n"
                    + "fitness : "+selectedGenome.fitness);
            selectActor.genome = selectedGenome;
        }

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
