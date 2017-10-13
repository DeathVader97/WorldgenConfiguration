package de.felixperko.worldgen.Generation.Misc;

import java.util.ArrayList;

public class Selector {
	
	public Double[] definiteMin;
	public Double[] definiteMax;
	public Boolean[] enabled;
	public Boolean[] hasCondition;
	public Double[] conditionMin;
	public Double[] conditionMax;
	
	public Selector() {
		
	}
	
	public Selector(int propertyCount){
		definiteMin = new Double[propertyCount];
		definiteMax = new Double[propertyCount];
		enabled = new Boolean[propertyCount];
		hasCondition = new Boolean[propertyCount];
		conditionMin = new Double[propertyCount];
		conditionMax = new Double[propertyCount];
		for (int i = 0 ; i < propertyCount ; i++)
			enabled[i] = false;
	}
	
	public Selector(int propertyCount, ArrayList<PropertySelectionBuilder> list){
		definiteMin = new Double[propertyCount];
		definiteMax = new Double[propertyCount];
		enabled = new Boolean[propertyCount];
		hasCondition = new Boolean[propertyCount];
		conditionMin = new Double[propertyCount];
		conditionMax = new Double[propertyCount];
		for(PropertySelectionBuilder b : list){
			if (b.conditionMin != null && b.conditionMax != null){
				hasCondition[b.id] = true;
				conditionMin[b.id] = b.conditionMin;
				conditionMax[b.id] = b.conditionMax;
			}
			if (b.definiteMin != null && b.definiteMax != null){
				enabled[b.id] = true;
				definiteMin[b.id] = b.definiteMin;
				definiteMax[b.id] = b.definiteMax;
			}
		}
	}
	
	public Selector setFeature(int feature, double definiteMin, double definiteMax){
		enabled[feature] = true;
		this.definiteMin[feature] = definiteMin;
		this.definiteMax[feature] = definiteMax;
		return this;
	}
	
	public Selector setCondition(int feature, double min, double max){
		hasCondition[feature] = true;
		conditionMin[feature] = min;
		conditionMax[feature] = max;
		return this;
	}
	
	public double getDifference(double[] features){
		double ret = 0;
		for (int i = 0 ; i < enabled.length ; i++){
			if (hasCondition[i]){
				double f = features[i];
				if (f < conditionMin[i] || f > conditionMax[i])
					return Double.MAX_VALUE;
			}
			if (enabled[i]){
				double f = features[i];
				double min = definiteMin[i];
				double max = definiteMax[i];
				if (f < min){
					ret += Math.pow(min-f,2);
				}if (f > max){
					ret += Math.pow(f-max,2);
				}else
					continue;
			}
		}
		return ret;
	}
}
