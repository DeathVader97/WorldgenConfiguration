package de.felixperko.worldgenconfig.GUI.PropertyGUI;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Event;

public class ChangedPropertiesEvent extends Event {

	ArrayList<PropertyBuilder> propertyBuilders;

	public ArrayList<PropertyBuilder> getPropertyBuilders() {
		return propertyBuilders;
	}

	public ChangedPropertiesEvent(ArrayList<PropertyBuilder> propertyBuilders) {
		this.propertyBuilders = propertyBuilders;
	}
}
