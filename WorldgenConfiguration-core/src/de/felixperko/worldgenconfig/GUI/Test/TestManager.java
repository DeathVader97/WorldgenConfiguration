package de.felixperko.worldgenconfig.GUI.Test;

import java.util.ArrayList;

public class TestManager{
	
	public TestWidget widget;
	
	public TestManager(TestWidget testWidget){
		this.widget = testWidget;
		widget.testManager = this;
	}
	
	public ArrayList<TestRunnable> pendingActions = new ArrayList<>();
	
	public long nextActionTime;

	public boolean jump = false;
	
	public void tick() {
		long t = System.nanoTime();
		while (t >= nextActionTime){
			int size = pendingActions.size();
			if (size == 0){
				widget.tick();
				return;
			} else {
				pendingActions.get(size-1).execute();
				pendingActions.remove(size-1);
			}
		}
	}
}
