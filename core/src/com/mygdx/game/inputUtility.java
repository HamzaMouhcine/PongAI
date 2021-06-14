package com.mygdx.game;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class inputUtility {
    static ArrayList<Genome> getGenomes() {
        ArrayList<Genome> inputGenomes = new ArrayList<>();

        JSONParser parser = new JSONParser();
        JSONArray genomes = new JSONArray();

        // open the file
        try {
            genomes = (JSONArray) parser.parse(new FileReader("savedGenomes/genomes.json"));
        } catch (Exception e) {
            try {
                File file = new File("savedGenomes/genomes.json");
                file.createNewFile();
            } catch (Exception e2) {
                System.out.println(e2);
            }
            System.out.println(e);
        }

        // loop over the genomes from the file
        for (Object o : genomes)
        {
            JSONObject genomeJSON = (JSONObject) o;
            Genome newGenome = JsonToGenome(genomeJSON);
            inputGenomes.add(newGenome);
        }

        return inputGenomes;
    }

    static Genome JsonToGenome(JSONObject genomeJSON) {
        Genome genome = new Genome(true);

        Number fitness = (Number)genomeJSON.get("fitness");
        genome.fitness = Integer.parseInt(fitness.toString());

        String name = (String) genomeJSON.get("name");
        genome.name = name;

        JSONArray cars = (JSONArray) genomeJSON.get("cars");
        JSONArray synapsesJSON = (JSONArray) genomeJSON.get("synapses");
        JSONArray neuronsJSON  = (JSONArray) genomeJSON.get("neurons");
        JSONArray layerSizeJSON = (JSONArray) genomeJSON.get("layerSize");

        Number layers = (Number)genomeJSON.get("layers");
        genome.layers = Integer.parseInt(layers.toString());
        int[] layerSize = new int[layerSizeJSON.size()];
        for (int i = 0; i < layerSizeJSON.size(); i++)
            layerSize[i] = Integer.parseInt(layerSizeJSON.get(i).toString());
        genome.layerSize = layerSize;

        double[][][] synapses = new double[Integer.parseInt(layers.toString())][][];
        for (int i = 0; i < ((JSONArray)genomeJSON.get("synapses")).size(); i++) {
            JSONArray firstLayer = (JSONArray) synapsesJSON.get(i);
            synapses[i] = new double[firstLayer.size()][];
            for (int j = 0; j < firstLayer.size(); j++) {
                JSONArray secondLayer = (JSONArray) firstLayer.get(j);
                synapses[i][j] = new double[secondLayer.size()];
                for (int w = 0; w < secondLayer.size(); w++) {
                    synapses[i][j][w] = (Double)secondLayer.get(w);
                }
            }
        }
        genome.synapses = synapses;

        double[][] neurons = new double[Integer.parseInt(layers.toString())][];
        for (int i = 0; i < ((JSONArray)genomeJSON.get("neurons")).size(); i++) {
            JSONArray firstLayer = (JSONArray) neuronsJSON.get(i);
            neurons[i] = new double[firstLayer.size()];
            for (int j = 0; j < firstLayer.size(); j++) {
                neurons[i][j] = (Double)firstLayer.get(j);
            }
        }
        genome.neurons = neurons;

        return genome;
    }
}
