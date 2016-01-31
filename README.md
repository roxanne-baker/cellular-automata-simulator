# cellsociety
Duke CompSci 308 Cell Society Project
###Introduction

The goal of this project is to create a program that is capable of simulating a variety of Cellular Automata (CA) on a grid of cells.  This will be done by reading in an XML file stating which simulation to run.  At any point, the user may load in a new XML file to start a new simulation.

The design goals of this project are to make it easy to use and allow for the addition of new simulations or changes with the structure of the grid (e.g., shape of the cells in the grid).  

These aspects of the project--the structure of the grid and simulations--will be left open.  The User Interface will be closed.

###Overview

Classes:

Grid - Superclass; represents grid based on parameters read from the XML file.  Represented as an array-of-arrays of Cells.

    SquareGrid - Cells are made up of squares

Cell - the individual cells of the grid.  Stores the current type of simulation, the current state of the Cell, and the neighbors of the Cell.  Determines next state of cell based on these parameters.

Simulation - Superclass; represents a certain “type” of simulation and the rules for it and the animation.

    SegregationSimulation
    PredatorPreySimulation
    FireSimulation
    GameOfLifeSimulation

UserInterface - creates the UserInterface for the game, where the user can press buttons in the window to start, pause, stop, speed up, slow down the animation and load in a new XML file to start a new simulation.  Is also responsible for creating the animation and displaying it.

CellSocietyMain - This is the class that is used to start the program by instantiating the UserInterface.

XMLConfiguration - Reads and processes the XML file for the configuration of the simulation.

###User Interface

The program interface will be represented by a rectangular frame showing the name of the simulation and the time passed on top, the square grid pane below it and 8 buttons in 2 rows on the very bottom giving the options to the user to load file, start, stop, pause, resume, slow down, speed up and fast forward through the animation. Erroneous messages will be displayed in the space for the grid.
![Design Image](https://github.com/duke-compsci308-spring2016/cellsociety_team11/Screen.jpg)

###Design Details

