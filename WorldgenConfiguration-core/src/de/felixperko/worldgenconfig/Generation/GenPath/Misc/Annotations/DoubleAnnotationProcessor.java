package de.felixperko.worldgenconfig.Generation.GenPath.Misc.Annotations;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

import de.felixperko.worldgen.Generation.Misc.Annotations.DoubleComponentSetting;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.Setting;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.ValidatableTextFieldSetting;

public class DoubleAnnotationProcessor extends AnnotationProcessor {

	@Override
	public InputValidator getValidator(Annotation annotation) {
		DoubleComponentSetting a = ((DoubleComponentSetting)annotation);
		return new InputValidator() {
			@Override
			public boolean validateInput(String input) {
				try {
					double value = Double.parseDouble(input);
					return value >= a.lowest() && value <= a.highest();
				} catch (NumberFormatException e){
					return false;
				}
			}
		};
	}

	@Override
	public Object getValue(Actor actor) {
		VisValidatableTextField field = (VisValidatableTextField)actor;
		return Double.parseDouble(field.getText());
	}

	@Override
	public Class<? extends Setting> getSettingClass() {
		return ValidatableTextFieldSetting.class;
	}

}
