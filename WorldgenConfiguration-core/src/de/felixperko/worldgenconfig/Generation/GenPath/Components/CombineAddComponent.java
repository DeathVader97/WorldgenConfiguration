package de.felixperko.worldgenconfig.Generation.GenPath.Components;

import de.felixperko.worldgenconfig.Generation.GenPath.Misc.DoubleComponentSetting;

public class CombineAddComponent extends CombineComponent {
	
	@DoubleComponentSetting
	public double factor1 = 1;
	@DoubleComponentSetting
	public double factor2 = 1;
	
	public CombineAddComponent() {
		super(2);
	}

	public double getFactor1() {
		return factor1;
	}

	public void setFactor1(double factor1) {
		this.factor1 = factor1;
	}

	public double getFactor2() {
		return factor2;
	}

	public void setFactor2(double factor2) {
		this.factor2 = factor2;
	}

	@Override
	protected double combine() {
		return factor1*inputValues[0] + factor2*inputValues[1];
	}

	@Override
	public String getDisplayName() {
		return "Combine (Add)";
	}
}
