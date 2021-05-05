package com.mygdx.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Generation {
	ArrayList<Genome> population;
	int[] layerSize;
	double mutationProbability;
	double minWeight = -1;
	double maxWeight = 1;
	int layers;
	int generationSize;
	int generationNumber;
	static int currentGenome;
	static int remaining;

	public Generation(int generationSize, int[] layerSize, double mutationProbability) {
	    this.generationSize = generationSize;
	    this.layerSize = layerSize;
	    this.mutationProbability = mutationProbability;
	    this.layers = layerSize.length;
	    population = generateRandomGenomes();
	    currentGenome = 0;
	    generationNumber = 1;
	    remaining = generationSize;
    }

    public int size() {
		return population.size();
	}

	public void add(Genome g) {
		population.add(g);
	}

	public Genome get(int idx) {
		return population.get(idx);
	}

    public void newGeneration(Generation previousGeneration) {
		// if this is the first generation.
		if (previousGeneration == null) {
			population = generateRandomGenomes();
			return;
		}

		population = evolve(previousGeneration);
		currentGenome = 0;
		generationNumber++;
		remaining = generationSize;
	}

	public ArrayList<Genome> generateRandomGenomes() {
		ArrayList<Genome> genomes = new ArrayList<>();
		for (int i = 0; i < generationSize; i++) {
			Genome newGenome = new Genome(layerSize);
			newGenome.initSynapsesRandomly();
			genomes.add(newGenome);
		}
		return genomes;
	}

	public ArrayList<Genome> evolve(Generation previousGeneration) {
		ArrayList<Genome> previousPopulation = previousGeneration.population;

		// select the best 5 genomes from the previous generation.
		ArrayList<Genome> newPopulation = selectN(5);

		// select two random genomes, cross over and mutate.
		for (int i = 1; i <= 5; i++) {
			Genome a = previousPopulation.get((int)(Math.random() * generationSize));
			Genome b = previousPopulation.get((int)(Math.random() * generationSize));

			Genome child = a.crossOver(b, layerSize);
			child.mutate(mutationProbability);
			newPopulation.add(child);
		}

		// Mutation only.
		for (int i = 1; i <= 5; i++) {
			Genome a = previousPopulation.get((int)(Math.random() * generationSize)).copie(layerSize);
			a.mutate(mutationProbability);
			newPopulation.add(a);
		}

		return newPopulation;
	}

	// select the best N genomes from the generation and remove the others.
	public ArrayList<Genome> selectN(int N) {
		Collections.sort(population);
		ArrayList<Genome> newPopulation = new ArrayList<>();

		for (int i = 0; i < N; i++) {
			newPopulation.add(population.get(i));
		}

		return newPopulation;
	}

	public Genome bestGenome() {
		Collections.sort(population);
		return population.get(0);
	}

	private double sigmoid(double x) {
		return 1 / (1 + Math.exp(-x));
	}

	private double randDouble(double min, double max) {
		Random r = new Random();
		return min + (max - min) * r.nextDouble();
	}
}