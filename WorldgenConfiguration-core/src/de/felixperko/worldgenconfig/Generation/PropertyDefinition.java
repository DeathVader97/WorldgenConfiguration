package de.felixperko.worldgenconfig.Generation;

import de.felixperko.worldgenconfig.Generation.GenerationPath.GenerationParameterSupply;
import de.felixperko.worldgenconfig.Generation.GenerationPath.GenerationPathIncompleteException;
import de.felixperko.worldgenconfig.Generation.GenerationPath.Nodes.Node;

public class PropertyDefinition {
	
	Node lastNode;
	
	public double generateProperty(GenerationParameterSupply supply) throws GenerationPathIncompleteException{
		return lastNode.getGenerationValue(supply);
	}
}
