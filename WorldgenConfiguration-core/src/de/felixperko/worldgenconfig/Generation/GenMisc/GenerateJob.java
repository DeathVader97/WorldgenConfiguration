package de.felixperko.worldgenconfig.Generation.GenMisc;

import java.util.HashMap;

public class GenerateJob {
	
	JobThread thread;
	HashMap<Integer,Double> parameters;
	double progress;
	
	public GenerateJob(HashMap<Integer,Double> parameters){
		this.parameters = parameters;
	}
	
	public void start(){
		thread = new JobThread(parameters);
	}
	
	public void cancel(){
		if (thread != null)
			thread.cancel = true;
	}
	
	class JobThread extends Thread{
		boolean cancel = false;
		
		double[][] generatedData = new double[1][];
		HashMap<Integer, Double> parameters;
		
		public JobThread(HashMap<Integer, Double> parameters) {
			this.parameters = parameters;
		}

		@Override
		public void run() {
			int width = 
			
			
			
		}
	}
}
