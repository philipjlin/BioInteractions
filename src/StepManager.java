/**
 * StepManager.java
 * Manages threads that update the model
 * Runnables update position, direction, signal, and phermone arrays
 * 
 * @author Philip Lin
 */
import javax.swing.SwingWorker;
import java.util.List;
import java.util.concurrent.*;

public class StepManager extends SwingWorker<Void, Model> implements Runnable{
	
	// Communicates with:
	Model model;
	View view;
	
	// Executor manages thread execution
	ExecutorService threadExecutor;
	
	/**
	 * Constructor
	 */
	public StepManager(View view, Model model){
		
		this.view = view;
		this.model = model;		
		threadExecutor = Executors.newSingleThreadExecutor();
	}
	
	/**
	 * Pushes the threads that do the model updating to a background worker thread
	 */
	@Override
	protected Void doInBackground() throws Exception{
		
		while( !isCancelled() ){

			try{
				Thread.sleep(model.getSpeed());
				
				threadExecutor.execute(model.directionUpdater);
				threadExecutor.execute(model.phermoneUpdater);
				threadExecutor.execute(model.positionUpdater);
				threadExecutor.execute(model.signalUpdater);

				publish(model);
			}
			catch(InterruptedException ie){ ie.printStackTrace(); }	
		}
		
		return null;
	}
	
	/**
	 * Updates the GUI from the current thread (EDT)
	 */
	@Override
	protected void process(List<Model> chunks){
		
		view.updateGrid(model);
	}
	
}