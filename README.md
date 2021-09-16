# PongAI

## Description
I created an AI that learns to play Pong, with the help of neural networks and a genetic algorithm.


## Play the game

### Overview

The neural network controls the right paddle. You can control the left paddle or let the computer play for you (it will never fail; this is useful to train the Neural Network while you do something else).

### Controls

Spacebar: enables/disables auto play for the left paddle.
a/w : If auto play is disabled, use these buttons to control the left paddle.
synapses.txt

## Source Code

### Compile the source code

I have used Intellij to compile the whole project. Download Intellij, and use it to run the game.

### Files

#### core/src/com/mygdx/game/ai

Contains two files:
* Generation: it defines a population of genomes, and defines the genetic algorithm used to evolve this generation.
* Genome: it defines a genome and the game state, also the mutation, crossover and selection operations.

#### core/src/com/mygdx/game/InputUtility

Helper function to get saved genomes, and delete/save a genome.

#### core/src/com/mygdx/animation & myscreens

This folder defines the UI(buttons, paddles, ball, animations).

## How the Neural Network works

### Genetic algorithm

The Neural Network will start with completely random synapses. This will inevitably lead to not score any points. When all the genomes of the current generation are tested, a new generation will be created(based on the previous generation):

* 20% => the best genomes of the last generation(using the fitness function), the fitness of a genome is represented by the number of times the ai was able to block the ball before the game ends(the game ends when the left paddle scores two points or the ai paddle blocks the ball 5 times)
* 40% => mixing genomes from the last generation(cross-over).
* 20% => mutation of genomes from the last generation.
* 20% => new random genomes.
If one genome scored a point

### Inputs and Outputs

There are two inputs:

* The y coordinate of the ball.
* The y coordinate of the right paddle.

There is only one output. The output value is a number between zero and one.

* If output <= 0.5, the paddle will go down.
* If output > 0.5, the paddle will go up.

## UI

![PongAI startScreen](https://raw.githubusercontent.com/HamzaMouhcine/PongAI/master/assets/start_screen.png)

You can customize the size of the generation, and the number of generations. you also have the choice of testing the whole generation at once, or testing each genome of a generation one by one before moving on to the next generation.

![PongAI Settings](https://raw.githubusercontent.com/HamzaMouhcine/PongAI/master/assets/settings_screen.png)

After all the generations are tested, you can save/test any genome of the last generation.
Genoms are saved in a json file: assets/savedGenomes/genomes.json

![PongAI Result](https://raw.githubusercontent.com/HamzaMouhcine/PongAI/master/assets/result_screen.png)

You can play against any saved genome, if you select the play button in the start screen.

![PongAI Settings](https://raw.githubusercontent.com/HamzaMouhcine/PongAI/master/assets/select_screen.png)
