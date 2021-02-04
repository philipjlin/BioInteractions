/**
 * GridJPanel.java
 * Creates the environment JPanel of the program added to the content pane
 * A NxN grid to be filled in with individual JPanels
 * 
 * @author Philip Lin
 */
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class GridJPanel extends JPanel{
	
	/**
	 * Constructor
	 */
	public GridJPanel(){
		
		setSize(new Dimension(600, 600));
		setLayout(new GridLayout(Model.ROWS, Model.COLS));
		setBackground(Color.WHITE);
	}
	
}