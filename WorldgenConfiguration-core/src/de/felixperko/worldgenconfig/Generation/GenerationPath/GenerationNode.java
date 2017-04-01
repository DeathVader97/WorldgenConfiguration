package de.felixperko.worldgenconfig.Generation.GenerationPath;

import java.util.ArrayList;
import java.util.Collection;

public interface GenerationNode {
	public double getGenerationValue(GenerationParameterSupply supply) throws GenerationPathIncompleteException;
	public int getInputCount();
	public void setInput(int index, GenerationNode node);
}
