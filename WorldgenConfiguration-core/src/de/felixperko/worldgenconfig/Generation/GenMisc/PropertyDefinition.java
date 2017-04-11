package de.felixperko.worldgenconfig.Generation.GenMisc;

import de.felixperko.worldgenconfig.Generation.GenPath.Components.Component;
import de.felixperko.worldgenconfig.Generation.GenPath.Misc.GenerationParameterSupply;
import de.felixperko.worldgenconfig.Generation.GenPath.Misc.GenerationPathIncompleteException;

public class PropertyDefinition {
	
	Component lastComponent;
	
	public double generateProperty(GenerationParameterSupply supply) throws GenerationPathIncompleteException{
		return lastComponent.getGenerationValue(supply);
	}
}
