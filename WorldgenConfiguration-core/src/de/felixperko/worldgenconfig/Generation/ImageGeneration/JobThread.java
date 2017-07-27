package de.felixperko.worldgenconfig.Generation.ImageGeneration;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;

import de.felixperko.worldgenconfig.Generation.GenMisc.Parameters;

public class JobThread extends Thread{
	
	GenerationManager manager;
	
	Condition condition;
	Lock lock;
	Parameters parameters;
	
	int size = 50;
	
	double scale = 1;
	
	Double[][] values = new Double[size][size];
	
	static int id_counter = 0;
	int id;
	
	public boolean interrupted = false;
	
	public JobThread(GenerationManager manager, Condition condition, Lock lock, Parameters parameters) {
		this.manager = manager;
		this.condition = condition;
		this.lock = lock;
		this.parameters = parameters;
		id = id_counter;
		id_counter++;
	}
	
	public void setParameters(Parameters parameters, boolean regenerateData){
		this.parameters = parameters;
		if (regenerateData)
			values = new Double[size][size];
	}
	
	public void changeSize(int newSize){
		this.scale = size/(float)newSize;
		this.size = newSize;
//		values = new Double[size][size];
		interrupted = true;
	}

	@Override
	public void run() {
		
		while (!Thread.interrupted()){
			
			
			Job job = manager.grabJob();
			if (job == null){
				lock.lock();
				try {
					condition.awaitNanos(1000000000);
					continue;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					lock.unlock();
				}
			}
			doJob(job);
		}
	}

	private void doJob(Job job) {
		interrupted = false;
		int size = (int)Math.round(this.size*job.scaling);
		job.size = size;
		if (values.length < size)
			values = new Double[size][size];
		Pixmap pm = new Pixmap(size, size, Format.RGBA8888);
		try {
			for (int x = 0 ; x < size ; x++){
				for (int y = 0 ; y < size ; y++){
					if (interrupted)
						return;
					if (parameters == null)
						pm.setColor(0);
					else{
						int colorValue = parameters.selectionRuleset.getValue(x*scale/job.scaling+job.shiftX*scale+0.5/job.scaling, y*scale/job.scaling+job.shiftY*scale+0.5/job.scaling);
						//((r&0x0ff)<<16)|((g&0x0ff)<<8)|(b&0x0ff);
						pm.setColor(new Color(((colorValue>>16)&0x0ff)/256f, ((colorValue>>8)&0x0ff)/256f, (colorValue&0x0ff)/256f, 1f));
					}
					pm.drawPixel(x, y);
				}
			}
			job.finish(pm);
		} catch (NullPointerException e) {
			System.err.println("NPE at job");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	public double getValue(Job job, int indexX, int indexY) throws GenerationPathIncompleteException{
//		Double value = values[indexX][indexY];
//		if (value != null)
//			return value;
//		supply.setPos(indexX*scale/job.scaling+job.shiftX, indexY*scale/job.scaling+job.shiftY, null);
//		if (def.lastComponent == null)
//			return 0;
//		value = def.generateProperty(supply);
//		return value;
//	}
}
