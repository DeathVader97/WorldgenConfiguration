package de.felixperko.worldgenconfig.MainMisc;

import de.felixperko.worldgenconfig.GUI.Test.TestManager;

public class TickThread extends Thread{
	
	TestManager testManager = null;
	
	int waitMs = 1;
	
	private void onInit() {
		
	}

	private void loop() {
		if (testManager != null)
			testManager.tick();
	}
	
	@Override
	public void run() {
		onInit();
		while (!Thread.interrupted()){
			
			loop();
			
			if (waitMs > 0){
				try {
					Thread.sleep(waitMs);
				} catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}

	public void setTestManager(TestManager testManager) {
		this.testManager = testManager;
	}

	public TestManager getTestManager() {
		return testManager;
	}
}
