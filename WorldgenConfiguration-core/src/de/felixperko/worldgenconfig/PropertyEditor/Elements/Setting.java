package de.felixperko.worldgenconfig.PropertyEditor.Elements;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import de.felixperko.worldgen.Generation.Components.Component;

public abstract class Setting {
	
	Field field;
	Annotation annotation;
	
	public Setting(Field field, Annotation annotation) {
		this.field = field;
		this.annotation = annotation;
	}
	
	public abstract void addSettingToWindow(ComponentSettingWindow window, final Component component);
}
