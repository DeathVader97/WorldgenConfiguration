package de.felixperko.worldgen.Generation.Misc;

import java.util.ArrayList;
import java.util.HashMap;

public class Parameters {
	
	public SelectionRuleset selectionRuleset;
	
	ArrayList<Constant> constants;
	PropertyDefinition[] propertyDefinitions;
	
	public TerrainType[] types;
	HashMap<Integer, TerrainType> typeMap = new HashMap<>();
	
	public int propertySize;
	
	public Parameters(SelectionRuleset selectionRuleset, PropertyDefinition[] definitions, TerrainType[] types){
		selectionRuleset.setParameters(this);
		this.propertyDefinitions = definitions;
		this.types = types;
		if (types != null){
			for (TerrainType type : types){
				typeMap.put(type.id, type);
			}
		}
		propertySize = definitions.length;
		this.selectionRuleset = selectionRuleset;
	}
	
	public double getProperty(int propertyIndex, GenerationParameterSupply supply) throws NullPointerException{
		try {
			return propertyDefinitions[propertyIndex].generateProperty(supply);
		} catch (GenerationPathIncompleteException e) {
			e.printStackTrace();
			return 0;
		}
	}

	public TerrainType getType(Integer typeID) {
		return typeMap.get(typeID);
	}
}
