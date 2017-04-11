package de.felixperko.worldgenconfig.Generation.GenPath.Misc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DoubleComponentSetting {
	double lowest() default -Double.MAX_VALUE;
	double highest() default Double.MAX_VALUE;
}
