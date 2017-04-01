package de.felixperko.worldgenconfig.Generation.GenerationPath;

public class CombineAddNode extends CombineNode {
	
	double factor1 = 1;
	double factor2 = 1;
	
	public CombineAddNode() {
		super(2);
	}

	@Override
	protected double combine() {
		return factor1*inputValues[0] + factor2*inputValues[1];
	}

}
