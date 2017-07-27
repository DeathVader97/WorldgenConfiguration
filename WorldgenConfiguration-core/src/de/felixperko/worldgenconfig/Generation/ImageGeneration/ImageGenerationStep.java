package de.felixperko.worldgenconfig.Generation.ImageGeneration;

import java.util.Map.Entry;
import java.util.TreeMap;

public class ImageGenerationStep {
	
	String busyMsg;
	double scale;
	GenerationManager generationManager;
	
	TreeMap<Double, Job> openImages = new TreeMap<Double, Job>();
	
	public ImageGenerationStep(GenerationManager generationManager, String busyMsg, double scale) {
		this.busyMsg = busyMsg;
		this.scale = scale;
	}
	
	public synchronized Job getNext(){
		Entry<Double, Job> e = openImages.pollFirstEntry();
		if (e == null)
			return null;
		return e.getValue();
	}
	
	public double getProgress(){
		return 1 - (openImages.size()/(double)generationManager.getImageManager().existing.size());
	}

	public String getBusyMsg() {
		return busyMsg;
	}
}
