package de.felixperko.worldgenconfig.Generation;

import de.felixperko.worldgenconfig.Generation.GenerationPath.GenerationNode;
import de.felixperko.worldgenconfig.Generation.GenerationPath.GenerationParameterSupply;
import de.felixperko.worldgenconfig.Generation.GenerationPath.GenerationPathIncompleteException;

public class PropertyDefinition {
	
	GenerationNode lastNode;
	
	public double generateProperty(GenerationParameterSupply supply) throws GenerationPathIncompleteException{
		return lastNode.getGenerationValue(supply);
	}
}
