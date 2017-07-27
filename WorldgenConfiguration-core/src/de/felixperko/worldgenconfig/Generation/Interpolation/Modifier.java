package de.felixperko.worldgenconfig.Generation.Interpolation;

import java.util.ArrayList;

public class Modifier {
	
	ArrayList<Interval> intervals = new ArrayList<>();
	
	public double defaultValue;
	
	double totalMin = Double.MAX_VALUE;
	double totalMax = -Double.MAX_VALUE;
	double totalRange;
	
	public Modifier(double defaultValue){
		this.defaultValue = defaultValue;
	}
	
	public Modifier(){
		this.defaultValue = 0;
	}
	
	public ArrayList<Interval> getIntervals() {
		return intervals;
	}

	public void setIntervals(ArrayList<Interval> intervals) {
		this.intervals = intervals;
		update();
	}

	public double getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(double defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Modifier addCos(double min, double max, double v1, double v2){
		intervals.add(new CosineInterpolationInterval(min, max, v1, v2));
		update();
		return this;
	}

	public Modifier addConst(double min, double max, double v){
		intervals.add(new ConstantInterpolationInterval(min, max, v));
		update();
		return this;
	}
	
	public Modifier addLinear(double min, double max, double m, double n){
		intervals.add(new LinearInterpolationInterval(min, max, m, n));
		update();
		return this;
	}
	
	private void update() {
		update(intervals);
	}
	
	private void update(ArrayList<Interval> intervals){
		totalMin = intervals.get(0).min;
		totalMax = intervals.get(0).max;
		for (int i = 1 ; i < intervals.size() ; i++){
			Interval in = intervals.get(i);
			if (in.min < totalMin){
				totalMin = in.min;
			}if (in.max > totalMax){
				totalMax = in.max;
			}
		}
		totalRange = totalMax-totalMin;
	}
	
	public double modify(double v){
		
//		System.out.println("totalMin="+totalMin+" totalMax="+totalMax);
		if (v < totalMin || v > totalMax){
//			System.out.println("return default value because value is out of bounds");
			return defaultValue;
		}
		int i = (int)((v-totalMin)*intervals.size()/totalRange);
		int maxI = intervals.size()-1;
//		System.out.println("estimate index="+i+" max="+maxI);
		if (i < 0)
			i = 0;
		if (i > maxI)
			i = maxI;
		while (true){
			Interval interval = intervals.get(i);
			int res = interval.inInterval(v);
//			System.out.println("checkForInterval: "+interval.min+"-"+interval.max+" "+res);
			if (res == 0)
				return interval.modify(v);
			if (res == -1){
				i--;
				if (i < 0)
					return defaultValue;
			} else if (res == 1){
				i++;
				if (i > maxI)
					return defaultValue;
			}
		}
	}
}
