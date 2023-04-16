# Biological Interaction Simulator


## Repository
<https://github.com/philipjlin/BioInteractions>


## Description
Simulation of the behavior of a group of biological specimens in a closed environment. In this example, slime molds are the organisms modeled, and their behavior is influenced by the organisms they come into proximity with, as they will aggregate toward areas with high concentration of chemicals that each individual releases.

The simulation is implemented using an MVC pattern, and the system operates using a pool of concurrent threads to execute the movement patterns of each individual specimen in the sample.


## Technologies
Developed in Java.


## High Level Components
    * Environment model that contains individuals
    * Thread manager to keep track of individuals and their attributes
    * View to set up, display, and run simulation


## Class Overview
    Domain Objects
        - Model - model for the environment containing individual slime molds with locations, directions they face, and location and level of phermones they produce. Position, signal, direction, and phermone level are all kept track in 2D arrays, and updated as time passes in the simulation. The simulation speed, size of the environment, the number of slime molds, the amount of phermone produced, and how the phermones propagate are all field that can be modified in the model. Transient runnable objects are used to update all the fields for as long as the simulation is running. The model includes methods to set up the simulation's initial conditions, and update the model for each step taken. 
        - StepManager - manages threads that update the model during each step of the simulation. This is achieved using a ThreadExecutor object, which concurrently executes model update methods while the simulation is running, resulting in updates to the GUI. 
        - Main - initializes the simulation.


## View
    View - Contains methods to initialize the view grid, as well as setup and update the view grid based on changes to the model. Also has methods to load a saved state or to save the current state.
    GridJPanel - Swing user interface that renders the environment grid the simulation takes place on.
    MenuJPanel - Swing user interface that renders the menu for the simulation.
    NoSlimeMoldJPanel - Swing user interface that defines a cell in the environment grid without an individual slime mold, that has no or any level of phermone.
    SlimeMoldJPanel - Swing user interface that defines a cell in the environment where an individual slime mold currently is.


## Persistence
The current state of the system can be saved in a serializable file save_state.ser.
