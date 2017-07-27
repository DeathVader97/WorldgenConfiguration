package de.felixperko.worldgenconfig.Generation.GenMisc;

import java.awt.Color;

public class ColorWrapper {
	
	public Float[] rgb = new Float[3];
	
	Color color;
	
	public ColorWrapper(){
		this.color = new Color(0,0,0);
	}
	
	public ColorWrapper(float r, float g, float b){
		color = new Color(r,g,b,1f);
		rgb[0] = color.getRed()/255f;
		rgb[1] = color.getGreen()/255f;
		rgb[2] = color.getBlue()/255f;
	}
	
	public ColorWrapper(Color color) {
		this.color = color;
		rgb[0] = color.getRed()/255f;
		rgb[1] = color.getGreen()/255f;
		rgb[2] = color.getBlue()/255f;
	}

	public Color get(){
		return color;
	}
	
	public void set(Color color){
		this.color = color;
		rgb[0] = color.getRed()/255f;
		rgb[1] = color.getGreen()/255f;
		rgb[2] = color.getBlue()/255f;
	}

	public Float[] getRgb() {
		return rgb;
	}

	public void setRgb(Float[] rgb) {
		this.rgb = rgb;
		color = new Color(rgb[0], rgb[1], rgb[2]);
	}
}
