package de.felixperko.worldgenconfig.Generation.GenerationPath.Nodes;

import java.util.ArrayList;
import java.util.Collection;

import de.felixperko.worldgenconfig.Generation.GenerationPath.GenerationParameterSupply;
import de.felixperko.worldgenconfig.Generation.GenerationPath.GenerationPathIncompleteException;

public interface Node {
	public double getGenerationValue(GenerationParameterSupply supply) throws GenerationPathIncompleteException;
	public int getInputCount();
	public void setInput(int index, Node node);
	public String getDisplayName();
}
