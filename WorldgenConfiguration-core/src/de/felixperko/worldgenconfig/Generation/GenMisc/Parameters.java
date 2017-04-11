package de.felixperko.worldgenconfig.Generation.GenMisc;

import java.util.ArrayList;

import de.felixperko.worldgenconfig.Generation.GenPath.Misc.Constant;
import de.felixperko.worldgenconfig.Generation.GenPath.Misc.GenerationParameterSupply;
import de.felixperko.worldgenconfig.Generation.GenPath.Misc.GenerationPathIncompleteException;

public class Parameters {
	
	GenerationParameterSupply supply;
	
	int width;
	int height;
	
	ColoringRuleset coloringRules;
	
	ArrayList<Constant> constants;
	PropertyDefinition[] propertyDefinitions;
	ArrayList<TerrainType> types;
	
	public double[] generateProperties(double x, double y) throws GenerationPathIncompleteException{
		double[] data = new double[propertyDefinitions.length];
		supply.setPos(x, y, data);
		for (int i = 0; i < data.length; i++){
			data[i] = propertyDefinitions[i].generateProperty(supply);
		}
		return data;
	}
}
