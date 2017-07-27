package de.felixperko.worldgenconfig.GUI.TypeGUI;

class TypeStatementBuilder{
	int condition;
	double low;
	double high;
	
	public TypeStatementBuilder(int condition, double low, double high) {
		this.condition = condition;
		this.low = low;
		this.high = high;
	}
	
	public int getCondition() {
		return condition;
	}
	public void setCondition(int condition) {
		this.condition = condition;
	}
	
	public double getLow() {
		return low;
	}
	public void setLow(double low) {
		this.low = low;
	}
	
	public double getHigh() {
		return high;
	}
	public void setHigh(double high) {
		this.high = high;
	}
	
	
}
