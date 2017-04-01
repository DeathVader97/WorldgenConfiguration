package de.felixperko.worldgenconfig;

public class TickThread extends Thread{
	
	int waitMs = 1;
	
	private void onInit() {
		
	}

	private void loop() {
		
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
}
