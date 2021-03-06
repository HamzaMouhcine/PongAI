package com.mygdx.game.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Generation {
	public ArrayList<Genome> population;
	int[] layerSize;
	double mutationProbability;
	double minWeight = -1;
	double maxWeight = 1;
	int layers;
	int generationSize;
	public int generationNumber;
	public static int currentGenome;
	public static int remaining;

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

		// select the best N genomes from the previous generation.
		int N = Math.min((int) (size() * 0.2), generationSize);
		ArrayList<Genome> newPopulation = selectN(N);

		// select two random genomes, cross over and mutate.
		N = Math.min((int) (size() * 0.4), generationSize - newPopulation.size());
		for (int i = 1; i <= 5; i++) {
			Genome a = previousPopulation.get((int)(Math.random() * generationSize));
			Genome b = previousPopulation.get((int)(Math.random() * generationSize));

			Genome child = a.crossOver(b, layerSize);
			child.mutate(mutationProbability);
			newPopulation.add(child);
		}

		// Mutation only.
		N = Math.min((int) (size() * 0.2), generationSize - newPopulation.size());
		for (int i = 1; i <= N; i++) {
			Genome a = previousPopulation.get((int)(Math.random() * generationSize)).copie(layerSize);
			a.mutate(mutationProbability);
			newPopulation.add(a);
		}

		System.out.print("mysize "+newPopulation.size()+" ");
		// generate the remaining genomes randomly.
		int left = generationSize - newPopulation.size();
		for (int i = 1; i <= left; i++) {
			Genome a = new Genome(layerSize);
			a.initSynapsesRandomly();
			newPopulation.add(a);
		}
		System.out.println(newPopulation.size());

		return newPopulation;
	}

	// select the best N genomes from the generation and remove the others.
	public ArrayList<Genome> selectN(int N) {
		Collections.sort(population);
		ArrayList<Genome> newPopulation = new ArrayList<>();

		for (int i = 0; i < N; i++) {
			Genome tmp = population.get(i);
			tmp.reset();
			newPopulation.add(tmp);
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