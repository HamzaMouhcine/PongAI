package com.mygdx.game.animation;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import com.mygdx.game.ai.*;

public class myTextButton extends TextButton {
    public Genome genome;

    public myTextButton(String text, Skin skin) {
        super(text, skin);
    }

    public myTextButton(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

    public myTextButton(String text, TextButtonStyle style) {
        super(text, style);
    }

    public void save(String genomeName) {
        JSONParser parser = new JSONParser();
        JSONArray genomes = new JSONArray();

        // open the file as an array of genomes:
        try {
            genomes = (JSONArray) parser.parse(new FileReader("savedGenomes/genomes.json"));
        } catch (Exception e) {
            try {
                File file = new File("savedGenomes/genomes.json");
                file.createNewFile();
            } catch (Exception e2) {
                System.out.println("file error: "+e2);
            }
            System.out.println("ignore this: "+e);
        }

        // add the genome to the array:
        JSONObject genomeJSON = genome.toJSON();
        genomeJSON.put("name", genomeName);
        genomes.add(genomeJSON);

        // save the array back in genomes.json
        try {
            FileWriter myWriter = new FileWriter("savedGenomes/genomes.json");
            myWriter.write(genomes.toString());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
