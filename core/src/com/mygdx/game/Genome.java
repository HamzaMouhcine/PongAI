package com.mygdx.game;

import java.util.Random;

public class Genome implements Comparable<Genome>{
	private double[][] neurons; // [layer index][neuron index]
	public double[][][] synapses;  //  [layer index][neuron index][synapse index]
	private int[] layerSize;
	public int fitness;
	private double minWeight = -1;
	private double maxWeight = 1;
	private int layers;
	public boolean isPlaying;
	public Ball ball;
	public Paddle leftPaddle;
	public Paddle rightPaddle;
	static int screenWidth = 800;
	static int screenHeight = 480;
	public int score1, score2;

	public Genome(int[] layerSize) {
		layers = layerSize.length;
		this.layerSize = new int[layers];

		// initialize the game set.
		leftPaddle = new Paddle(5);
		rightPaddle = new Paddle(screenWidth-5-20);
		ball = new Ball();
		score1 = 0;
		score2 = 0;

		isPlaying = true;
		// Generate neurons
		neurons = new double[layers][];
		for (int i = 0; i < layers; i++) {
			this.layerSize[i] = layerSize[i];
			if (i != layers - 1) {
				this.layerSize[i]++;  // The last neuron is the bias
			}
			neurons[i] = new double[this.layerSize[i]];
		}

		// Set biases to 1
		for (int i = 0; i < layers - 1; i++) {
			neurons[i][this.layerSize[i] - 1] = 1;
		}

		// Initialize the synapses
		synapses = new double[layers - 1][][];    // The last layer is the output, thus it doesn't have any outgoing synapses
		for (int i = 0; i < layers - 1; i++) {
			synapses[i] = new double[this.layerSize[i]][];
			for (int j = 0; j < this.layerSize[i]; j++) {
				if (i + 1 != layers - 1) {
					synapses[i][j] = new double[this.layerSize[i + 1] - 1];  //  excluding the bias
				} else {
					synapses[i][j] = new double[this.layerSize[i + 1]];
				}
			}
		}
	}

	public void initSynapsesRandomly() {
		for (int i = 0; i < layers - 1; i++) {
			for (int j = 0; j < layerSize[i]; j++) {
				for (int k = 0; k < synapses[i][j].length; k++) {
					synapses[i][j][k] = randDouble(minWeight, maxWeight);
				}
			}
		}
	}

	public int getAction(double input[]) {
		setInput(input);
		setNeuronsValues();

		int action = output(neurons[layers-1]);
		return action;
	}

	private void setInput(double[] input) {
		for (int i = 0; i < layerSize[0]-1; i++) {
			neurons[0][i] = input[i];
		}
	}

	public double[] getInput() {
		return neurons[0];
	}

	public double[] getOutput() {
		return neurons[layers-1];
	}

	private int output(double[] outputLayer) {
		if (outputLayer[0] >= 0.50) {
			return 1;  //  move the paddle up.
		} else return -1;  // move the paddle down.
	}

	public Genome copie(int[] layerSize) {
		Genome cp = new Genome(layerSize);
		for (int l = 0; l < layers - 1; l++) {
			for (int n = 0; n < layerSize[l]; n++) {
				for (int m = 0; m < synapses[l][n].length; m++) {
					cp.synapses[l][n][m] = synapses[l][n][m];
				}
			}
		}
		return cp;
	}

	public void mutate(double mutationProbability) {
		for (int l = 0; l < layers-1; l++) {
			for (int n = 0; n < layerSize[l]; n++) {
				for (int m = 0; m < synapses[l][n].length; m++) {
					if (Math.random() < mutationProbability) {
						synapses[l][n][m] += synapses[l][n][m] * (Math.random() - 0.5) * 3 + (Math.random() - 0.5);
					}
				}
			}
		}
	}

	// create a new child by mixing the synapses of the parents this and mate.
	public Genome crossOver(Genome mate, int[] layerSize) {
		Genome child = new Genome(layerSize);

		int layerCut = (int)(Math.random() * (layers - 1));
		int neuronCut = (int)(Math.random() * (layerSize[layerCut]));

		for (int l = 0; l < layers - 1; l++) {
			for (int n = 0; n < layerSize[l]; n++) {
				for (int m = 0; m < synapses[l][n].length; m++) {
					if (l < layerCut) {
						child.synapses[l][n][m] = this.synapses[l][n][m];
					} else if (l == layerCut && n <= neuronCut) {
						child.synapses[l][n][m] = this.synapses[l][n][m];
					} else {
						child.synapses[l][n][m] = mate.synapses[l][n][m];
					}
				}
			}
		}

		return child;
	}

	private void setNeuronsValues() {
		// Initalize neurons with the value 0.
		for (int i = 1; i < layers; i++) {
			int size;
			if (i + 1 != layers) {
				size = layerSize[i] - 1;
			} else {
				size = layerSize[i];
			}

			for (int j = 0; j < size; j++) {
				neurons[i][j] = 0;
			}
		}

		// Multiply neurons and synapses and sum the results (calculate the next neurons values)
		for (int i = 1; i < layers; i++) {
			int size;
			if (i != layers - 1) {
				size = layerSize[i] - 1;
			} else {
				size = layerSize[i];
			}
			for (int j = 0; j < size; j++) {
				for (int k = 0; k < layerSize[i - 1]; k++) {
					neurons[i][j] += neurons[i-1][k] * synapses[i - 1][k][j];
				}

				// Activation function
				neurons[i][j] = sigmoid(neurons[i][j]);
			}
		}
	}

	private double sigmoid(double x) {
		return 1 / (1 + Math.exp(-x));
	}

	private double randDouble(double min, double max) {
		Random r = new Random();
        return min + (max - min) * r.nextDouble();
	}

	// Sort genomes by fitness.
	public int compareTo(Genome other) {
		return other.fitness - this.fitness;
	}
}