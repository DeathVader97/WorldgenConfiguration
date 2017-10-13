package de.felixperko.worldgen.Generation.Misc.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IntComponentSetting {
	int lowest() default Integer.MIN_VALUE;
	int highest() default Integer.MAX_VALUE;
	boolean reloadSettings() default false;
}

