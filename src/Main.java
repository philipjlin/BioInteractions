/**
 * Main.java
 * Main class
 * Sets up the simulation using the View and Model
 * 
 * @author Philip Lin
 */

public class Main{
	
	public static void main(String[] args){
		
		Model model = new Model();
		View view = new View(model);
		
		view.initialize();
	}
	
}