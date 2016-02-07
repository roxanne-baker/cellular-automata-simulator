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
![Design Image](https://github.com/duke-compsci308-spring2016/cellsociety_team11/blob/master/ClassDiagram.png)

###User Interface

The program interface will be represented by a rectangular frame showing the name of the simulation and the time passed on top, the square grid pane below it and 8 buttons in 2 rows on the very bottom giving the options to the user to load file, start, stop, pause, resume, slow down, speed up and fast forward through the animation. Erroneous messages will be displayed in the space for the grid.
![Interface Image](https://github.com/duke-compsci308-spring2016/cellsociety_team11/blob/master/Screen.jpg)

###Design Details

**CellSocietyMain** - Only contains a “main” function to create the UserInterface.  Is not intended to be extended.

**UserInterface** - Creates the main window for the animation.  At the top of the window is basic information about the current animation, such as the name of the animation and the number of generations it has been running for.  The bottom of the window contains various buttons that the user can press (see the User Interface section).  In between these will be the actual grid.

The text for the buttons and at the top of the window will be contained in a separate resource file.

One of the buttons is responsible for loading in an XML file.  The user will select an XML file to load in, which will be parsed with the XMLConfiguration class and passed to a Simulation object.

While the simulation is running (once it has been started, but not paused or stopped), the UserInterface will also update the appearance of the grid based on the animation rate.  This is intended to be a property of Simulation that can be changed when the user pressed the buttons for Speed Up, Slow Down, or Step Forward.

Since the UserInterface is closed, this is not intended to be extended to accommodate additional requirements (instead, these will be handled by extensions to other classes such as Simulation and Grid).

The UserInterface class should also have a single function, to be called in CellSocietyMain, that is responsible for running/updating the User Interface.

**Simulation** - Abstract Superclass.  Takes in information about the Grid in its constructor.  The superclass contains methods for changing the rate of the animation, and creates the Grid.  The subclasses of Simulation (one subclass for each different type of simulation) will contain the “rules” for the animation and contain methods for updating the state of each Cell in Grid.  It will also contain all of the possible states as static final variables.  This can be extended to add new subclasses representing different types of Simulations.

**XMLConfiguration** - Abstract final class. Reads an XML file and returns the specifications found in the file. Specific configurations could be accessed using the getParameter method of this class, which will accept as its argument a string. This string will have the same name as the configuration’s tag in the XML file. The services of this class will be called from the UserInterface class. This class could be extended in the future to give feedback with regards to misformatted files. 

**Grid** - Superclass.  Takes in information about the size of the grid and type of simulation, and creates an array-of-an-array of Cell(s) that represent the Grid.  This is created by the Simulation class.  Subclasses will create Grids made up of different shapes (e.g., SquareGrid, TriangleGrid, etc.), and methods to set the neighbors for the individual Cell.

**Cell** - Takes in the type of Simulation.  Contains information about the neighbors of the Cell and the current state of the Cell.  The state will be represented as a String.

**Use Cases:**

**Case 1:** The updateState(Cell cell) method of the GameOfLifeSimulation subclass will count the cells around the middle cell and, depending on the number of cells around it and its own state, set its state to either dead or alive. 

**Case 2:** Identical to Case 1 .

**Case 3:** The updateState method of the simulation will update all the cells in the screen by calling all the necessary cell methods concurrently and refreshing the screen. 

**Case 4:** The XMLConfiguration class will read the type of the simulation to be created. It will then return this to the UserInterface class. The UserInterface class, as part of its method to create a new simulation, will call and launch the necessary simulation. The simulation will then call the getParameter method of the XMLConfiguration class with the argument “probCatch” to set the parameter of the simulation. 

**Case 5:** While a simulation is underway, the user can change to a different simulation by clicking the button responsible for loading the XML file. This will automatically create a new simulation object and show it on the screen. 

###Design Considerations

Design decisions that were discussed at length:

Buttons for the user to click on vs keys for the user to press for user interface:

The buttons are somewhat more difficult to extend (in that it necessitates changing the appearance of the window), but considerably easier for the user, while the opposite is true for using key presses.  Ultimately, it was decided that making the program easier on the user was more important.

How to represent the state:

Initially, we planned on having Cell contain an “updateState” function that would change the state based on the Simulation being ran, because this is what seemed intuitive.  However, it was instead decided to include this in the Simulation subclass.  This would allow for Grid and Cell to not have a dependency on the type of Simulation, and for each Simulation class to have constants for the possible states to pass to the Cell when updating it.

Separating UserInterface from Main:

The Main class is primarily responsible for creating an instance of UserInterface and calling a method from it to run the program.  Initially, we were unsure of the role of the main class because of it essentially just relying on UserInterface and its methods.  However, we felt that it would still be beneficial to have a separate main class because this is easier on the user--instead of having to run the program from the much-larger UserInterface class, they can run it from a small and simple class.

Inheritance Hierarchy:

We tried to separate out each main component of the animation into its own set of classes.  Since the UserInterface needs to create/display the Simulation and it has a very specific function (showing the user interface on the screen and providing the necessary functionality to it), it seemed natural for it to have a class of its own.

###Team Responsibilities

- Roxanne Baker - Simulation
- Zdravko Paskalev - Visualization
- Sarp Uner - Configuration


All the team members have agreed to collaborate on others’ components upon request.  We have arranged multiple meetings in the upcoming week to work on the project together, in addition to working on the individual components on our own time.  We plan to start by ensuring that we have a basic Cell and Grid classes working, as these are vital to the Visualization and Simulation components, before focusing primarily on our own individual components.



