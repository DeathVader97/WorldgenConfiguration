package de.felixperko.worldgenconfig.Generation.GenMisc;

import java.awt.Color;

public class TerrainType {
	
	/*
	 * basically biomes
	 */
	
	static int ID_COUNTER;
	
	public Selector selector;
	public ColorWrapper color;
	TerrainDescriptor descriptor;
	public int id;

	public String name;
	
	public TerrainType(){
	}
	
	public TerrainType(String name, Selector selector, Color color) {
		this.name = name;
		this.selector = selector;
		this.descriptor = new TerrainDescriptor();
		this.color = new ColorWrapper(color);
		this.id = ID_COUNTER;
		ID_COUNTER++;
	}
	
	public TerrainType(String name, Selector selector, TerrainDescriptor descriptor, Color color) {
		this.name = name;
		this.selector = selector;
		this.descriptor = descriptor;
		this.color = new ColorWrapper(color);
		this.id = ID_COUNTER;
		ID_COUNTER++;
	}
	
	public int getRGB(){
		Color color = getColor();
		return ((color.getRed()&0x0ff)<<16)|((color.getGreen()&0x0ff)<<8)|(color.getBlue()&0x0ff);
	}
	
	public Color getColor(){
		return color.get();
	}
}
