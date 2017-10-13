package de.felixperko.worldgenconfig.Generation.ImageGeneration;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import de.felixperko.worldgen.Generation.Misc.Parameters;

public class GenerationManager {
	
	static ImageGenerationStep[] steps;
	
	ArrayList<JobThread> jobThreads = new ArrayList<>();
	
	ImageManager imgManager;
	
	ReentrantLock lock = new ReentrantLock();
	Condition condition = lock.newCondition();

	public void setParameters(Parameters parameters, boolean regenerateValues) {
		for (JobThread jobThread : jobThreads){
			jobThread.setParameters(parameters, regenerateValues);
		}
	}

	public GenerationManager(){
		addJobs();
		addThread();
		addThread();
		addThread();
		addThread();
//		addThread();
	}
	
	private void addThread() {
		JobThread thread = new JobThread(this, condition, lock, null);
		jobThreads.add(thread);
		thread.start();
		thread.setPriority(Thread.MIN_PRIORITY);
	}
	
	public void addJobs() {
		steps = new ImageGenerationStep[3];
		steps[0] = new ImageGenerationStep(this, "preview...", 0.2); //50x50 -> 10x10 (25x faster)
		steps[1] = new ImageGenerationStep(this, "render...", 1); //50x50 -> 50x50
		steps[2] = new ImageGenerationStep(this, "refine...", 2); //50x50 -> 100x100 (4x slower)
	}
	
	public Job grabJob(){
		Job job = null;
		for (ImageGenerationStep step : steps){
			job = step.getNext();
			if (job != null)
				return job;
		}
		return job;
	}
	
	double prioShift = 0;
	
	public synchronized void addWorldgenImage(WorldgenImage image, double priority, boolean wipe){
		for (ImageGenerationStep step : steps){
			try {
				if (wipe || step.scale > image.remainingResolutionFactor){
					step.openImages.put(priority+prioShift, new Job(image, priority+prioShift, step.scale, image.pos.x, image.pos.y));
					prioShift -= 0.000001;
	//				lock.lock();
					synchronized (condition){
						condition.notify();
					}
	//				lock.unlock();
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public ImageGenerationStep getActiveGenerationStep(){
		for (ImageGenerationStep step : steps){
			if (!step.openImages.isEmpty())
				return step;
		}
		return null;
	}

	public ImageManager getImageManager() {
		return imgManager;
	}

	public void clearJobs() {
		for (ImageGenerationStep step : steps){
			step.openImages.clear();
		}
	}
}
