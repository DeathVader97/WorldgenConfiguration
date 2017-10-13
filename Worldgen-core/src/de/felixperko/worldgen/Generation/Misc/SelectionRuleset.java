package de.felixperko.worldgen.Generation.Misc;

public abstract class SelectionRuleset {
	
	public Parameters parameters;
	public ThreadLocal<GenerationParameterSupply> supplies = new ThreadLocal<>();
	
	public void setParameters(Parameters parameters){
		this.parameters = parameters;
	}
	
	public int getValue(double x, double y) throws NullPointerException{
		GenerationParameterSupply supply = supplies.get();
		if (supply == null){
			supply = new GenerationParameterSupply();
			supplies.set(supply);
		}
		supply.setPos(x, y, null);
		return selectValue();
	}
	
	protected abstract int selectValue() throws NullPointerException;
	
	protected double getProperty(int propertyIndex) throws NullPointerException{
		return parameters.getProperty(propertyIndex, supplies.get());
	}
}
