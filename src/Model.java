/**
 * Model.java
 * Model component
 * Models the locations, direction of the slime molds
 * Models the location of phermone signals left by the molds and the location and level of phermones produced
 * 
 * @author Philip Lin
 */
import java.io.*;
import java.util.Random;

public class Model implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	// Threads: 
	final transient Runnable directionUpdater;
	final transient Runnable phermoneUpdater;
	final transient Runnable positionUpdater;
	final transient Runnable signalUpdater;
	
	// Model arrays: 
	private boolean[][] positions;
	private int[][] signals;
	private int[][] phermones;
	private int[][] directions;
	
	// Variables:
	final static Random generator = new Random();
	final static int ROWS = 50;
	final static int COLS = 50;
	final static int phermoneMax = 4;
	final static int phermone1Step = 3;
	private int population;
	private int speed;
	
	/**
	 * Constructor
	 * Defines the threads as runnables
	 */
	public Model(){
		
		directionUpdater = new Runnable(){
            public void run(){
                Model.this.updateDirections();
            }
        };
        
        phermoneUpdater = new Runnable(){
        		public void run(){
        			Model.this.updatePhermones();
        		}
        };
        
        positionUpdater = new Runnable(){
    			public void run(){
    				Model.this.updatePositions();
    			}
        };
        
        signalUpdater = new Runnable(){
        		public void run(){
        			Model.this.updateSignals();
        		}
        };
		
		population = 10;
		speed = 500;
	}
	
	/**
	 * Randomizes the positions of the slime molds at the start
	 * Used when simulation is initialized in the view
	 */
	public void initializePositions(){
		
		positions = new boolean[ROWS][COLS];
		
		for( int i = 0; i < population; i++){
			
			int xpos = generator.nextInt(ROWS);
			int ypos = generator.nextInt(COLS);
			
			if( positions[xpos][ypos] == false )
				positions[xpos][ypos] = true;
			else
				--i;
		}
	}
	
	/**
	 * Initializes the model arrays directions, signals, and phermones to initial values
	 * Signals start where molds are initially positioned, and are set to level of 2 (level reduces each step)
	 * Directions randonly initialized for each mold
	 * Phermones initialized to the defined max phermone level
	 * Used when simulation is initialized in the view
	 */
	public void initializeDirectionsSignalsPhermones(){
		
		directions = new int[ROWS][COLS];
		signals = new int[ROWS][COLS];
		phermones = new int[ROWS][COLS];
		
		for( int i = 0; i < ROWS; i++ ){
			for( int j = 0; j < COLS; j++ ){
				
				if( positions[i][j] == true ){
					
					directions[i][j] = generator.nextInt(8) * 45;
					signals[i][j] = 3;
					phermones[i][j] = phermoneMax;
				}
			}
		}
	}
		
	/**
	 * Moves every cell 1 position in the position array
	 * Movement based on direction of the cell from the direction array
	 * Also transfers pre-determined direction from old position to new position
	 */
	public void updatePositions(){
		
		int newXPosition = 0, newYPosition = 0;
		boolean[][] newPositions = new boolean[ROWS][COLS];
		int[][] newDirections = new int[ROWS][COLS];
				
		for( int i = 0; i < ROWS; i++ ){
			for( int j = 0; j < COLS; j++ ){
				
				if( positions[i][j] == true ){
					
					// Determine new position based on the direction
					switch( directions[i][j] ){
						case 0:		newXPosition = i;
									newYPosition = j + 1;
									break;
						case 45:	newXPosition = i + 1;
									newYPosition = j + 1;
									break;
						case 90:	newXPosition = i + 1;
									newYPosition = j;
									break;
						case 135:	newXPosition = i + 1;
									newYPosition = j - 1;
									break;
						case 180:	newXPosition = i;
									newYPosition = j - 1;
									break;
						case 225:	newXPosition = i - 1;
									newYPosition = j - 1;
									break;
						case 270:	newXPosition = i - 1;
									newYPosition = j;
									break;
						case 315:	newXPosition = i - 1;
									newYPosition = j + 1;
									break;
					}
					
					// Create new position and direction arrays
					// Cells only move if the location to move to is unoccupied
					if( newPositions[checkBounds(newXPosition)][checkBounds(newYPosition)] != true )
						newPositions[checkBounds(newXPosition)][checkBounds(newYPosition)] = true;
					else
						newPositions[i][j]  = true;
					newDirections[checkBounds(newXPosition)][checkBounds(newYPosition)] = directions[i][j];
				}
			}
		}
		
		// Set the position and direction arrays to the references of the new arrays
		positions = newPositions;
		directions = newDirections;
	}
	
	/**
	 * Sets new directions for current cells into the direction array
	 * New direction based on current cell direction and phermone level of surrounding patches
	 */
	public void updateDirections(){
		
		int choice = -1;
		
		for( int i = 0; i < ROWS; i++ ){
			for( int j = 0; j < COLS; j++ ){
				
				if( positions[i][j] == true ){
					
					// Determine which direction has the highest level of phermone
					switch( directions[i][j] ){
					
						case 0:		choice = findDirection(phermones[checkBounds(i-1)][checkBounds(j+1)], phermones[checkBounds(i)][checkBounds(j+1)], phermones[checkBounds(i+1)][checkBounds(j+1)]);
									break;
						case 45:	choice = findDirection(phermones[checkBounds(i)][checkBounds(j+1)], phermones[checkBounds(i+1)][checkBounds(j+1)], phermones[checkBounds(i+1)][checkBounds(j)]);
									break;
						case 90:	choice = findDirection(phermones[checkBounds(i+1)][checkBounds(j+1)], phermones[checkBounds(i+1)][checkBounds(j-1)], phermones[checkBounds(i)][checkBounds(j-1)]);
									break;
						case 135:	choice = findDirection(phermones[checkBounds(i+1)][checkBounds(j)], phermones[checkBounds(i+1)][checkBounds(j-1)], phermones[checkBounds(i)][checkBounds(j-1)]);
									break;
						case 180:	choice = findDirection(phermones[checkBounds(i+1)][checkBounds(j-1)], phermones[checkBounds(i)][checkBounds(j-1)], phermones[checkBounds(i-1)][checkBounds(j-1)]);
									break;
						case 225:	choice = findDirection(phermones[checkBounds(i)][checkBounds(j-1)], phermones[checkBounds(i-1)][checkBounds(j-1)], phermones[checkBounds(i-1)][checkBounds(j)]);
									break;
						case 270:	choice = findDirection(phermones[checkBounds(i-1)][checkBounds(j-1)], phermones[checkBounds(i-1)][checkBounds(j)], phermones[checkBounds(i-1)][checkBounds(j+1)]);
									break;
						case 315:	choice = findDirection(phermones[checkBounds(i-1)][checkBounds(j)], phermones[checkBounds(i-1)][checkBounds(j+1)], phermones[checkBounds(i)][checkBounds(j+1)]);
									break;
					}
					// Set the new direction
					switch( choice ){
						case 0:		directions[i][j] = checkDegrees(directions[i][j] - 45);
									break;
						case 1: 	directions[i][j] = checkDegrees(directions[i][j]);
									break;
						case 2: 	directions[i][j] = checkDegrees(directions[i][j] + 45);
									break;
					}
				}
			}
		}
	}
	
	/**
	 * Reduces current signal intensities of all phermone signals in signals array
	 * Adds new signals according to positions
	 */
	public void updateSignals(){
		
		for( int i = 0; i < ROWS; i++ ){
			for( int j = 0; j < COLS; j++ ){
				
				// Reduce current signals
				if( signals[i][j] > 0 )
					signals[i][j] -= 1;
				// Set new signals
				if(positions[i][j] == true)
					signals[i][j] = 2;
			}
		}
	}
	
	/**
	 * Add phermones to the phermone array
	 * Using the signals array to add based on position of signals and intensity level
	 */
	public void updatePhermones(){
		
		// Diffuse current phermone levels
		for( int i = 0; i < ROWS; i++ ){
			for( int j = 0; j < COLS; j++ ){
				
				if( phermones[i][j] > phermoneMax )
					phermones[i][j] = phermoneMax;
				if( phermones[i][j] > 0 )
					phermones[i][j] -= 1;	
			}
		}
		
		// Add new phermones to patches based on signal locations and levels
		for( int i = 0; i < ROWS; i++ ){
			for( int j = 0; j < COLS; j++ ){
				
				if( signals[i][j] == 1 ){
					phermones[checkBounds(i-1)][checkBounds(j-1)] += phermone1Step;
					phermones[checkBounds(i-1)][checkBounds(j)] += phermone1Step;
					phermones[checkBounds(i-1)][checkBounds(j+1)] += phermone1Step;
					phermones[checkBounds(i)][checkBounds(j-1)] += phermone1Step;
					phermones[checkBounds(i)][checkBounds(j+1)] += phermone1Step;
					phermones[checkBounds(i+1)][checkBounds(j-1)] += phermone1Step;
					phermones[checkBounds(i+1)][checkBounds(j)] += phermone1Step;
					phermones[checkBounds(i+1)][checkBounds(j+1)] += phermone1Step;
				}
				if( signals[i][j] == 2 ){
					phermones[checkBounds(i)][checkBounds(j)] = phermoneMax;
				}
			}
		}
	}

	/*
	 * Getters, Setters, Utility Methods
	 */
	public boolean[][] getPositions(){ return positions; }
	public int[][] getSignals(){ return signals; }
	public int[][] getPhermones(){ return phermones; }
	public int[][] getDirections(){ return directions; }
	public int getPopulation(){ return population; }
	public int getSpeed(){ return speed; }
	public void setPositions(boolean[][] positions){ this.positions = positions; }
	public void setSignals(int[][] signals){ this.signals = signals; }
	public void setPhermones(int[][] phermones ){ this.phermones = phermones; }
	public void setDirections(int[][] directions){ this.directions = directions; }
	public void setPopulation(int population){ this.population = population; }
	public void setSpeed(int speed){ this.speed = speed; }
	 
	static int findDirection(int a, int b, int c){
				
		if( a == b && a == c )
			return generator.nextInt(3);
		else if( a == b && a > c )
			return generator.nextInt(2);
		else if( a == c && a > b )
			return 2 * generator.nextInt(2);
		else if( b == c && b > a )
			return 1 + generator.nextInt(2);
		else{
			if( Math.max(a, Math.max(b, c)) == a )
				return 0;
			else if( Math.max(a, Math.max(b, c)) == b )
				return 1;
			else
				return 2;
		}
	}
	
	static int checkBounds(int coordinate){
		if( coordinate >= ROWS )
			coordinate -= ROWS;
		else if( coordinate < 0 )
			coordinate += ROWS;
		return coordinate;
	}
	
	static int checkDegrees(int degrees){
		if( degrees > 315 )
			degrees = 0;
		else if( degrees < 0 )
			degrees = 315;
		return degrees;
	}
	
}