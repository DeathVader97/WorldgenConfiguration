package de.felixperko.worldgenconfig.GUI.Test;

public abstract class TestRunnable implements Runnable{
	
	TestManager manager;
	double waitTime;
	boolean wait = true;
	
	public TestRunnable(TestManager manager, double waitTime) {
		this.manager = manager;
		this.waitTime = waitTime;
	}
	
	public void execute(){
		run();
		if (wait)
			manager.nextActionTime = System.nanoTime()+(long)(waitTime*1000000000);
	}
}
