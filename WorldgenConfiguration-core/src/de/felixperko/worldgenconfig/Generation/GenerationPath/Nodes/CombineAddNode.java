package de.felixperko.worldgenconfig.Generation.GenerationPath.Nodes;

import de.felixperko.worldgenconfig.Generation.GenerationPath.DoubleNodeSetting;

public class CombineAddNode extends CombineNode {
	
	@DoubleNodeSetting
	public double factor1 = 1;
	@DoubleNodeSetting
	public double factor2 = 1;
	
	public CombineAddNode() {
		super(2);
	}

	@Override
	protected double combine() {
		return factor1*inputValues[0] + factor2*inputValues[1];
	}

	@Override
	public String getDisplayName() {
		return "Combinator";
	}

}
