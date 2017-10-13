package de.felixperko.worldgen;

public class GenerationThread extends Thread{
	
	ChunkGenerator generator;
	
	public boolean busy = true;
	
	public GenerationThread(ChunkGenerator generator){
		this.generator = generator;
	}
	
	@Override
	public void run() {
		while (generator.scheduler == null){
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		while (!Thread.interrupted()){
			Chunk taskChunk = generator.scheduler.getTask();
			if (taskChunk == null){
				generator.scheduler.lock.lock();
				try {
					busy = false;
					generator.scheduler.lockCondition.await();
					busy = true;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					generator.scheduler.lock.unlock();
				}
			} else {
				taskChunk.currentStep++;
				generator.generationSteps[taskChunk.currentStep].generate(taskChunk);
				generator.scheduler.taskDone(taskChunk.pos, taskChunk.currentStep);
			}
		}
	}
}
