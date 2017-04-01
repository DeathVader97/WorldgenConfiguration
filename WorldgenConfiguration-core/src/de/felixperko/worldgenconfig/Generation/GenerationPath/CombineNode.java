package de.felixperko.worldgenconfig.Generation.GenerationPath;

public abstract class CombineNode implements GenerationNode {
	
	GenerationNode[] inputNodes;
	double[] inputValues;
	
	public CombineNode(int inputs) {
		inputNodes = new GenerationNode[inputs];
		inputValues = new double[inputs];
	}
	
	@Override
	public double getGenerationValue(GenerationParameterSupply supply) throws GenerationPathIncompleteException {
		for (int i = 0; i < inputNodes.length; i++) {
			if (inputNodes[i] == null)
				throw new GenerationPathIncompleteException();
			inputValues[i] = inputNodes[i].getGenerationValue(supply);
		}
		return combine();
	}

	@Override
	public int getInputCount() {
		return inputNodes.length;
	}

	@Override
	public void setInput(int index, GenerationNode node) {
		inputNodes[index] = node;
	}
	
	protected abstract double combine();
}
