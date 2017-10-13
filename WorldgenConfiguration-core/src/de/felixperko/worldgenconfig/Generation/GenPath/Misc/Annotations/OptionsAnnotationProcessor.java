package de.felixperko.worldgenconfig.Generation.GenPath.Misc.Annotations;

import java.lang.annotation.Annotation;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.widget.VisSelectBox;

import de.felixperko.worldgen.Generation.Misc.Annotations.OptionsComponentSetting;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.DropdownMenuSetting;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.Setting;

public class OptionsAnnotationProcessor extends AnnotationProcessor {
	
	@Override
	public InputValidator getValidator(Annotation annotation) {
		OptionsComponentSetting a = ((OptionsComponentSetting)annotation);
		return new InputValidator() {
			@Override
			public boolean validateInput(String input) {
				String[] options = a.options();
				for (int i = 0 ; i < options.length ; i++)
					if (options[i].equals(input))
						return true;
				return false;
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getValue(Actor actor) {
		VisSelectBox<String> box = (VisSelectBox<String>)actor;
		return box.getSelected();
	}

	@Override
	public Class<? extends Setting> getSettingClass() {
		return DropdownMenuSetting.class;
	}
}
