package de.felixperko.worldgen.Generation.Misc;

import java.util.ArrayList;

public class GenerationParameterSupply {

	ArrayList<Constant> constants;
	double x,y;
	double[] propertyData;
	
	public double getConstant(int id){
		return constants.get(id).value;
	}
	
	public void setPos(double x, double y, double[] propertyData){
		this.x = x;
		this.y = y;
		this.propertyData = propertyData;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
	
	public double getPropertyData(int propertyID){
		return propertyData[propertyID];
	}
}
