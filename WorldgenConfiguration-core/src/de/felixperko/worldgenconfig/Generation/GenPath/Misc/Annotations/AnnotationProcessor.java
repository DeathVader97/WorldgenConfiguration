package de.felixperko.worldgenconfig.Generation.GenPath.Misc.Annotations;

import java.lang.annotation.Annotation;
import java.util.HashMap;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.util.InputValidator;

import de.felixperko.worldgen.Generation.Misc.Annotations.DoubleComponentSetting;
import de.felixperko.worldgen.Generation.Misc.Annotations.IntComponentSetting;
import de.felixperko.worldgen.Generation.Misc.Annotations.OptionsComponentSetting;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.Setting;

public abstract class AnnotationProcessor {
	
	public static HashMap<Class<?>, AnnotationProcessor> processors = new HashMap<>();
	
	static {
		processors.put(DoubleComponentSetting.class, new DoubleAnnotationProcessor());
		processors.put(IntComponentSetting.class, new IntAnnotationProcessor());
		processors.put(OptionsComponentSetting.class, new OptionsAnnotationProcessor());
	}
	
	public static AnnotationProcessor getAnnotationProcessor(Annotation a){
		return processors.get(a.annotationType());
	}
	
	public abstract InputValidator getValidator(Annotation annotation);
	
	public abstract Object getValue(Actor actor);

	public abstract Class<? extends Setting> getSettingClass();
}
