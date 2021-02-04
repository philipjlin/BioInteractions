/**
 * View.java
 * View component
 * Sets up the user interface display
 * Includes methods to serialize model
 * 
 * @author Philip Lin
 */
import java.awt.*;
import javax.swing.*;
import java.io.*;

@SuppressWarnings("serial")
public class View extends JFrame{
	
	// Communicates with:
	private Model model;
	
	// View components:
	private MenuJPanel menu;
	private GridJPanel grid;
	
	/**
	 * Constructor
	 */
	public View(Model model){
		
		this.model = model;
		
		setTitle("Slime Mold Simulator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	/**
	 * Initializes the Slime Mold Simulator
	 * Adds menu and environment JPanels to the JFrame
	 */
	public void initialize(){
		
		menu = new MenuJPanel(this, model);
		grid = new GridJPanel();
		
		getContentPane().setPreferredSize(new Dimension(600, 700));
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(menu, BorderLayout.NORTH);
		getContentPane().add(grid, BorderLayout.CENTER);
		pack();
	}
	
	/**
	 * Randomly sets up the Slime Molds when JButton is pressed
	 * Adds individual SlimeMoldJPanels or normal JPanels to Environment JPanel Grid
	 */
	public void setup(){
		
		model.initializePositions();
		model.initializeDirectionsSignalsPhermones();
				
		updateGrid(model);
	}
	
	/**
	 * Updates the environment and slime molds for 1 step of molds
	 * Adds individual SlimeMoldJPanelsor NonSlimeMoldJPanels to Environment JPanel Grid
	 */
	public void step(){
				
		model.updateDirections();		
		model.updatePhermones();
		model.updatePositions();
		model.updateSignals();
		
		updateGrid(model);
	}
	
	/**
	 * Updates the environment grid with new slime mold and non slime mold panels
	 */
	void updateGrid(Model model){
		
		grid.removeAll();

		for( int i = 0; i < Model.ROWS; i++ ){
			for( int j = 0; j < Model.COLS; j++ ){
				
				if( (model.getPositions())[i][j] == true )
					grid.add(new SlimeMoldJPanel( model.getDirections()[i][j] ));
				else
					grid.add(new NoSlimeMoldJPanel( model.getPhermones()[i][j] ));
				
				grid.validate();
			}
		}
	}
	
	/**
	 * Saves the current model as a serialized object file
	 * FileOutputStream used to write file represented by file object
	 * ObjectOutputStream used to write model object to the file in the output stream
	 * @param file file object where model object is to be saved 
	 * @param model model object to be saved
	 */
	void save(File file, Model model){
		
		try{
			FileOutputStream fileOS = new FileOutputStream(file);
	        ObjectOutputStream objectOS = new ObjectOutputStream(fileOS);
	        
	        objectOS.writeObject(model);
	        
	        objectOS.close();
	        fileOS.close();	        
	    }
		catch(IOException ioe){ ioe.printStackTrace(); }
	}
	
	/**
	 * Loads a specific serialized object file containing a model previously saved 
	 * FileInputStream used to obtain file represented by a file object
	 * ObjectInputStream used to obtain model object from the representative file object
	 * Model obtained from the input stream is a previously saved model object and is used to update the view upon load
	 * @param file file object that represents model object
	 */
	void load(File file){
				
		try{
			
			FileInputStream fileIS = new FileInputStream(file);
			ObjectInputStream objectIS = new ObjectInputStream(fileIS);
			
			Model saveState = (Model)objectIS.readObject();
			
			fileIS.close();
			objectIS.close();
			
			model.setPositions(saveState.getPositions());
			model.setSignals(saveState.getSignals());
			model.setPhermones(saveState.getPhermones());
			model.setDirections(saveState.getDirections());
			updateGrid(saveState);			
		}
		catch(IOException ioe){ ioe.printStackTrace(); }
		catch(ClassNotFoundException cnfe){ cnfe.printStackTrace(); }
	}
	
}