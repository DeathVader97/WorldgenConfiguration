package de.felixperko.worldgenconfig.MainMisc;

public class JobContainer {
	static Job[] jobs;
	static {
		addJobs();
	}
	
	private static void addJobs() {
		jobs[0] = new Job("preview...", 0.2); //50x50 -> 10x10 (25x faster)
		jobs[1] = new Job("render...", 0.2); //50x50 -> 50x50
		jobs[2] = new Job("refine...", 2); //50x50 -> 100x100 (4x slower)
	}
	
	public JobInstance grabJobInstance(){
		JobInstance instance = null;
		for (Job job : jobs){
			instance = job.getNext();
			if (instance != null)
				return instance;
		}
		return instance;
	}
	
	public void addWorldgenImage(WorldgenImage image, double priority){
		for (Job job : jobs)
			job.openImages.add(new JobInstance(image, priority));
	}
}
