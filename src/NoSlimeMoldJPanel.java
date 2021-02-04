/**
 * NoSlimeMoldPanel.java
 * Creates a JPanel with no slime mold with colored background based on phermone level
 * 
 * @author Philip Lin
 */
import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class NoSlimeMoldJPanel extends JPanel{
		
	/**
	 * Constructor
	 * @param phermoneLevel phermone level from array
	 */
	public NoSlimeMoldJPanel(int phermoneLevel){
		
		setBackgroundColor(phermoneLevel);
	}
	
	/**
	 * Sets the color of the background based on phermone level
	 * @param phermoneLevel
	 */
	public void setBackgroundColor(int phermoneLevel){
		
		switch( phermoneLevel ){
			case 0:					setBackground(Color.BLACK);
									break;
			case 1: 					setBackground(Color.WHITE);
									break;
			case 2: 					setBackground(Color.YELLOW);
									break;
			case 3:					setBackground(Color.ORANGE);
									break;
			case Model.phermoneMax:	setBackground(Color.RED);
									break;
		}
	}

}