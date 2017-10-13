package de.felixperko.worldgen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import de.felixperko.worldgen.Util.Math.Vector2i;

public class GenerationScheduler {
	
	public int[] stepCounter;
	
	ArrayList<ArrayList<Vector2i>> tasks = new ArrayList<>();
	ChunkManager manager;
	ArrayList<HashSet<Vector2i>> activeTasks = new ArrayList<>();
	
	ChunkGenerator generator;
	
	ArrayList<GenerationThread> threads = new ArrayList<>();
	
	ReentrantLock lock = new ReentrantLock();
	
	Condition lockCondition = lock.newCondition();
	
	public GenerationScheduler(ChunkManager manager, ChunkGenerator chunkGenerator, int size) {
		this.generator = chunkGenerator;
		this.manager = manager;
		this.stepCounter = new int[size];
		for (int i = 0 ; i < size ; i++){
			tasks.add(new ArrayList<>());
			activeTasks.add(new HashSet<>());
		}
		for (int i = 0 ; i < 1 ; i++){
			GenerationThread thread = new GenerationThread(generator);
			threads.add(thread);
			thread.start();
		}
	}
	
	public synchronized void addTask(Vector2i pos, int step){
		stepCounter[step]++;
//		System.out.println("add task "+step+" for "+pos.toString()+" ("+generator.chunkManager.getChunkData(pos).currentStep+")");
//		if (step == 0)
//			Thread.dumpStack();
		tasks.get(step).add(pos);
		lock.lock();
		lockCondition.signal();
		lock.unlock();
	}
	
//	boolean first = true;
	
	public synchronized Chunk getTask(){
		for (int i = 0 ; i < tasks.size() ; i++){
//			if (first)
//				System.out.println("size of task list for step "+i+": "+tasks.get(i).size());
//			first = false;
			HashSet<Vector2i> activeTasks = this.activeTasks.get(i);
			ArrayList<Vector2i> currentList = tasks.get(i);
			for (Vector2i vector : currentList){
				if (!activeTasks.contains(vector)){
					activeTasks.add(vector);
					currentList.remove(vector);
					if (vector.equals(new Vector2i(0,50)))
						System.out.println("starting step "+i+" for: "+vector.toString());
					return manager.getChunkData(vector);
				}
			}
		}
		System.out.println("no task available");
		return null;
	}
	
	public void taskDone(Vector2i pos, int step){
		activeTasks.get(step).remove(pos);
	}
}
