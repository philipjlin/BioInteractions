/**
 * MenuJPanel.java
 * Controller component for user interface
 * Contains components to change program interface and elements
 * 
 * @author Philip Lin
 */
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

@SuppressWarnings("serial")
public class MenuJPanel extends JPanel implements ActionListener, ItemListener, ChangeListener{
		
	// Communicates with:
	private View view;
	private Model model;
	private StepManager stepManager;
	
	// Menu JPanel components:
	private JButton setupButton, stepButton, saveButton, loadButton;
	private JToggleButton goButton;
	private ImageIcon setupIcon, stepIcon, saveIcon, loadIcon, goIcon, stopIcon;
	private JFileChooser fileChooser;
	private JSlider populationSlider, speedSlider;
	private JLabel populationLabel, speedLabel, actionLabel;
	
	/**
	 * Constructor
	 */
	public MenuJPanel(View view, Model model){
		
		this.view = view;
		this.model = model;
		
		// Set look and layout of menu panel
		setPreferredSize(new Dimension(600, 100));
		setLayout(new GridLayout(2, 5));
		setBackground(Color.WHITE);
		
		// Initialize menu panel components
		setupIcon = new ImageIcon("Icons/Setup_Icon.png");
		stepIcon = new ImageIcon("Icons/Step_Icon.png");
		saveIcon = new ImageIcon("Icons/Save_Icon.png");
		loadIcon = new ImageIcon("Icons/Load_Icon.png");
		goIcon = new ImageIcon("Icons/Go_Icon.png");
		stopIcon = new ImageIcon("Icons/Stop_Icon.png");
		setupButton = new JButton(setupIcon);
		stepButton = new JButton(stepIcon);
		saveButton = new JButton(saveIcon);
		loadButton = new JButton(loadIcon);
		goButton = new JToggleButton(goIcon);
		setupButton.setToolTipText("Setup Simulation");
		stepButton.setToolTipText("Perform One Step");
		saveButton.setToolTipText("Save State To File");
		loadButton.setToolTipText("Load State File");
		goButton.setToolTipText("Run Simulation");
		stepButton.setEnabled(false);
		goButton.setEnabled(false);
		populationSlider = new JSlider(1, 50, model.getPopulation());
		speedSlider = new JSlider(50, 1000, model.getSpeed());
		populationLabel = new JLabel("<html><b><font color=#696969>Slime Population</font></html>", JLabel.CENTER);
		speedLabel = new JLabel("<html><b><font color=#696969>Simulation Speed</font></html>", JLabel.CENTER);
		actionLabel = new JLabel("", JLabel.CENTER);
		fileChooser = new JFileChooser(new File("."));
				
		// Add listeners to components
		setupButton.addActionListener(this);		
		stepButton.addActionListener(this);
		saveButton.addActionListener(this);
		loadButton.addActionListener(this);
		goButton.addItemListener(this);
		populationSlider.addChangeListener(this);
		speedSlider.addChangeListener(this);
		
		// Add components to the menu panel grid
		add(saveButton);
		add(loadButton);
		add(setupButton);
		add(stepButton);
		add(goButton);
		add(populationLabel);
		add(populationSlider);
		add(speedLabel);
		add(speedSlider);
		add(actionLabel);
	}
	
	/**
	 * Handles the events that occur when Save, Load, Setup or Step button is pressed
	 * @param e event from clicked button
	 */
	public void actionPerformed(ActionEvent e){
		
		if( e.getSource() == setupButton ){
			view.setup();
			actionLabel.setText("Simulation Setup.");
			goButton.setEnabled(true);
			stepButton.setEnabled(true);
		}
		if( e.getSource() == stepButton){
			view.step();
			actionLabel.setText("Step Performed.");
		}
		// Buttons for save and load bring up file chooser to define file to be saved/loaded
		if( e.getSource() == saveButton ){
						
			if( fileChooser.showSaveDialog(MenuJPanel.this) == JFileChooser.APPROVE_OPTION ){
				
				File file = fileChooser.getSelectedFile();
				view.save(file, model);
				actionLabel.setText("State Saved.");
			}
		}
		if( e.getSource() == loadButton ){
			
			if( fileChooser.showOpenDialog(MenuJPanel.this) == JFileChooser.APPROVE_OPTION ){
				
                File file = fileChooser.getSelectedFile();
                view.load(file);
    				actionLabel.setText("State Loaded.");
    				goButton.setEnabled(true);
    				stepButton.setEnabled(true);
			}
		}
	}
	
	/**
	 * Handles the events that occur when the Go toggle button is pressed
	 * @param e event from clicked button
	 */
	public void itemStateChanged(ItemEvent e){
		
		// Creates a StepManager to update the model when go is pressed - cancels when depressed
		if( goButton.isSelected() ){
			goButton.setIcon(stopIcon);
			goButton.setToolTipText("Stop Simulation");
			actionLabel.setText("Running...");
			try{
				stepManager = new StepManager(view, model);
				stepManager.execute();
			}catch(Exception e1){ e1.printStackTrace(); }
		}
		else{	
			goButton.setIcon(goIcon);
			goButton.setToolTipText("Run Simulation");
			actionLabel.setText("Stopped.");
			stepManager.cancel(true);
			goButton.setSelected(false);
		}		
	}
	
	/**
	 * Handles the events that occur when a slider is changed
	 * @param e event from changed slider
	 */
	public void stateChanged(ChangeEvent e){
		
        if( !populationSlider.getValueIsAdjusting() )
            model.setPopulation( (int)populationSlider.getValue() );
        if( !speedSlider.getValueIsAdjusting() )
        		model.setSpeed( (int)speedSlider.getValue() );
	}

}