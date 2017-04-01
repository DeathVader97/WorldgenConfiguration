package de.felixperko.worldgenconfig.Generation;

import java.util.ArrayList;

import de.FelixPerko.Worldgen.TerrainType;
import de.felixperko.worldgenconfig.Generation.GenerationPath.Constant;
import de.felixperko.worldgenconfig.Generation.GenerationPath.GenerationParameterSupply;
import de.felixperko.worldgenconfig.Generation.GenerationPath.GenerationPathIncompleteException;

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
