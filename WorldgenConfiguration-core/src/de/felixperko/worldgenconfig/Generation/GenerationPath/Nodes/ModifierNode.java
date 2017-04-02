package de.felixperko.worldgenconfig.Generation.GenerationPath.Nodes;

import java.util.ArrayList;

import de.felixperko.worldgenconfig.Generation.GenerationPath.GenerationParameterSupply;
import de.felixperko.worldgenconfig.Generation.GenerationPath.GenerationPathIncompleteException;
import de.felixperko.worldgenconfig.Generation.Interpolation.Modifier;

public class ModifierNode implements Node {
	
	Node input;
	Modifier modifier;
	
	public ModifierNode() {
		
	}

	@Override
	public double getGenerationValue(GenerationParameterSupply supply) throws GenerationPathIncompleteException{
		if (input == null)
			throw new GenerationPathIncompleteException();
		return modifier.modify(input.getGenerationValue(supply));
	}

	@Override
	public int getInputCount() {
		return 1;
	}

	@Override
	public void setInput(int index, Node node) {
		input = node;
	}

	@Override
	public String getDisplayName() {
		return "Modifier";
	}

}
