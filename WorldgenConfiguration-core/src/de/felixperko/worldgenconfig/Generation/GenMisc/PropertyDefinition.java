package de.felixperko.worldgenconfig.Generation.GenMisc;

import de.felixperko.worldgenconfig.Generation.GenPath.Components.Component;
import de.felixperko.worldgenconfig.Generation.GenPath.Misc.GenerationParameterSupply;
import de.felixperko.worldgenconfig.Generation.GenPath.Misc.GenerationPathIncompleteException;

public class PropertyDefinition {
	
	public String name;
	Component lastComponent;
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

	public void setMainComponent(Component component) {
		lastComponent = component;
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
}
