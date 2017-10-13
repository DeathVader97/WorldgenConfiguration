package de.felixperko.worldgen;

import java.util.ArrayList;
import java.util.HashMap;

public class MaterialManager {
	
	ArrayList<Object> materials = new ArrayList<>();
	HashMap<Object,Integer> IdForMaterial = new HashMap<>();
	
	public void addMaterial(Object material){
		IdForMaterial.put(material, materials.size());
		materials.add(material);
	}
	
	public void addMaterials(ArrayList<Object> materials){
		for (Object mat : materials){
			addMaterial(mat);
		}
	}
	
	public Object getMaterial(int id){
		return materials.get(id);
	}
	
	public int getId(Object material){
		return IdForMaterial.get(material);
	}
}
