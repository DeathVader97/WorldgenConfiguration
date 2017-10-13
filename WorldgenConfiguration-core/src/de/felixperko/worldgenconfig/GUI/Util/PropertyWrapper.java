package de.felixperko.worldgenconfig.GUI.Util;

import de.felixperko.worldgen.Generation.Misc.PropertyDefinition;

public class PropertyWrapper extends SelectWrapper{
	
	PropertyDefinition def;
	
	public PropertyWrapper(PropertyDefinition def) {
		super(def.name);
		this.def = def;
	}
	
}