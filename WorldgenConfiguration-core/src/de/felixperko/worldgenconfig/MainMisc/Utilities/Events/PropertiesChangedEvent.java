package de.felixperko.worldgenconfig.MainMisc.Utilities.Events;

import java.util.ArrayList;

import de.felixperko.worldgen.Generation.Misc.PropertyDefinition;
import de.felixperko.worldgenconfig.MainMisc.Utilities.Events.EventSystem.WorldgenEvent;

public class PropertiesChangedEvent extends WorldgenEvent {
	ArrayList<PropertyDefinition> oldProperties;
	ArrayList<PropertyDefinition> newProperties;
	ArrayList<PropertyDefinition> addedProperties = new ArrayList<>();
	ArrayList<PropertyDefinition> removedProperties = new ArrayList<>();
	
	public PropertiesChangedEvent(ArrayList<PropertyDefinition> oldProperties, ArrayList<PropertyDefinition> newProperties) {
		this.oldProperties = oldProperties;
		this.newProperties = newProperties;
		for (PropertyDefinition def : newProperties){
			if (!oldProperties.contains(def))
				addedProperties.add(def);
		}
		for (PropertyDefinition def : oldProperties){
			if (!newProperties.contains(def))
				removedProperties.add(def);
		}
	}

	public ArrayList<PropertyDefinition> getOldProperties() {
		return oldProperties;
	}

	public ArrayList<PropertyDefinition> getNewProperties() {
		return newProperties;
	}

	public ArrayList<PropertyDefinition> getAddedProperties() {
		return addedProperties;
	}

	public ArrayList<PropertyDefinition> getRemovedProperties() {
		return removedProperties;
	}
}
