package de.felixperko.worldgenconfig.Generation.GenMisc;

import java.util.ArrayList;

import de.felixperko.worldgenconfig.Generation.GenPath.Misc.Constant;
import de.felixperko.worldgenconfig.Generation.GenPath.Misc.GenerationParameterSupply;
import de.felixperko.worldgenconfig.Generation.GenPath.Misc.GenerationPathIncompleteException;

public class Parameters {
	
	public SelectionRuleset selectionRuleset;
	
	ArrayList<Constant> constants;
	PropertyDefinition[] propertyDefinitions;
	
	public int propertySize;
	
	public Parameters(SelectionRuleset selectionRuleset, PropertyDefinition... definitions){
		selectionRuleset.setParameters(this);
		this.propertyDefinitions = definitions;
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
}
