/**
 * SlimeMoldPanel.java
 * Creates a JPanel with a slime mold 2D graphic
 * 
 * @author Philip Lin
 */
import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class SlimeMoldJPanel extends JPanel{
	
	private Graphics2D slimeG2D;
	private int degrees;
	
	/**
	 * Constructor
	 * @param degrees rotation of the slime mold in the panel
	 */
	public SlimeMoldJPanel(int degrees){
		
		this.degrees = degrees;
	}
	
	/**
	 * Positions, rotates, and fills the slime mold graphic
	 */
	@Override
	public void paintComponent(Graphics g){
		
		super.paintComponent(g);
		
		slimeG2D = (Graphics2D)g;
		int x = (int)getBounds().getWidth();
		int y = (int)getBounds().getHeight();
		slimeG2D.translate(x/2, y/2);
		slimeG2D.rotate(Math.toRadians(degrees));
		int[] xcoords = {0, 0, x/3};
		int[] ycoords = {-y/6, y/6, 0};
		setBackground(Color.BLACK);
		slimeG2D.setColor(Color.GREEN);	
		slimeG2D.fillPolygon(xcoords, ycoords, 3);
	}

}