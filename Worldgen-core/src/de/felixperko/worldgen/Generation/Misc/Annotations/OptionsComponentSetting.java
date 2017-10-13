package de.felixperko.worldgen.Generation.Misc.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionsComponentSetting {
	String[] options();
	boolean reloadSettings() default false;
}
