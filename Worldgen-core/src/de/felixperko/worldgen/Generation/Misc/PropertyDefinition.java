package de.felixperko.worldgen.Generation.Misc;

import de.felixperko.worldgen.Generation.Components.Component;

public class PropertyDefinition {
	
	public String name;
	public Component lastComponent;
	public Integer id;
	
	public PropertyDefinition() {
	}
	
	public PropertyDefinition(Integer id, String name, Component lastComponent){
		this.id = id;
		this.lastComponent = lastComponent;
		this.name = name;
	}
	
	public double generateProperty(GenerationParameterSupply supply) throws GenerationPathIncompleteException, NullPointerException{
		return lastComponent.getGenerationValue(supply);
	}
	
	@Override
	public String toString() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Component getLastComponent() {
		return lastComponent;
	}

	public void setLastComponent(Component lastComponent) {
		this.lastComponent = lastComponent;
	}
}
