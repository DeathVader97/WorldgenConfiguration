package de.felixperko.worldgen.Generation.Noise.NoiseFunctions;

import java.util.ArrayList;

public class FunctionManager {
	ArrayList<Class<?>> functionClasses = new ArrayList<>();
	
	public FunctionManager(){
		init();
	}
	
	protected void init() {
		addFunctionClass(SimplexNoise2D.class);
		addFunctionClass(IQNoise2D.class);
		addFunctionClass(SwissNoise2D.class);
		addFunctionClass(ErodedNoise2D.class);
	}

	public void addFunctionClass(Class<?> cls){
		functionClasses.add(cls);
	}
	
	public ArrayList<Class<?>> getApplicableFunctionClasses(Class<?> superClass){
		ArrayList<Class<?>> list = new ArrayList<>();
		for (Class<?> cls : functionClasses){
			if (cls.isAssignableFrom(superClass))
				list.add(cls);
		}
		return list;
	}
}
