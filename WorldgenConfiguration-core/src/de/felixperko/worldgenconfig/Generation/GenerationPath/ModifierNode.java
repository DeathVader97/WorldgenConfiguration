package de.felixperko.worldgenconfig.Generation.GenerationPath;

import java.util.ArrayList;

import de.felixperko.worldgenconfig.Generation.Interpolation.Modifier;

public class ModifierNode extends Modifier implements GenerationNode {
	
	GenerationNode input;
	
	public ModifierNode(double defaultValue) {
		super(defaultValue);
	}

	@Override
	public double getGenerationValue(GenerationParameterSupply supply) throws GenerationPathIncompleteException{
		if (input == null)
			throw new GenerationPathIncompleteException();
		return modify(input.getGenerationValue(supply));
	}

	@Override
	public int getInputCount() {
		return 1;
	}

	@Override
	public void setInput(int index, GenerationNode node) {
		input = node;
	}

}
