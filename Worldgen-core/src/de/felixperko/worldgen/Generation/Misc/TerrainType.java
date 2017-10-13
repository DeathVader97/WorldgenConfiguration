package de.felixperko.worldgen.Generation.Misc;

import java.awt.Color;

import de.felixperko.worldgen.Generation.WorldgenMaterial;
import de.felixperko.worldgen.Generation.GenerationResources.GroundLayer;
import de.felixperko.worldgen.Generation.Noise.NoiseHelper;
import de.felixperko.worldgen.Generation.Noise.OpenSimplexNoise;

public class TerrainType {
	
	static int ID_COUNTER;
	
	public Selector selector;
	public ColorWrapper color;
	TerrainDescriptor descriptor;//TODO
	public int id;

	public String name;
	
	WorldgenMaterial groundMat;
	GroundLayer[] groundLayers;
	double baseHeight;
	
	public TerrainType(){
		this.id = ID_COUNTER;
		ID_COUNTER++;
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
		Color color = getColor().color;
		return ((color.getRed()&0x0ff)<<16)|((color.getGreen()&0x0ff)<<8)|(color.getBlue()&0x0ff);
	}
	
	int octaves = 8;
	OpenSimplexNoise[] noises = new OpenSimplexNoise[octaves];
	{
		for (int i = 0 ; i < octaves ; i++){
			noises[i] = new OpenSimplexNoise(i+id*256);
		}
	}
	
	public double getHeight(double x, double z) {
		return 64 + NoiseHelper.simplexNoise2D(x, z, 0.1, 0.5, 2, octaves, noises)*96;
	}

	public Selector getSelector() {
		return selector;
	}

	public void setSelector(Selector selector) {
		this.selector = selector;
	}

	public ColorWrapper getColor() {
		return color;
	}

	public void setColor(ColorWrapper color) {
		this.color = color;
	}

	public int getId() {
		return id;
	}

	@Deprecated
	public void setId(int id) {
		this.id = id;
		if (id >= ID_COUNTER)
			ID_COUNTER = id+1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return "Type: "+name+" ("+id+")";
	}
}
