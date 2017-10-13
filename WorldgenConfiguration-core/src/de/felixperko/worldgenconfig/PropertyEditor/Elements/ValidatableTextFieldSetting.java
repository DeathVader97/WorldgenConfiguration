package de.felixperko.worldgenconfig.PropertyEditor.Elements;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

import de.felixperko.worldgen.Generation.Components.Component;
import de.felixperko.worldgenconfig.Generation.GenPath.Misc.Annotations.AnnotationProcessor;

public class ValidatableTextFieldSetting extends Setting{
	
	VisValidatableTextField textField;
	
	public ValidatableTextFieldSetting(Field field, Annotation annotation) {
		super(field, annotation);
	}
	
	public void addSettingToWindow(ComponentSettingWindow window, final Component component){
		
		AnnotationProcessor processor = AnnotationProcessor.getAnnotationProcessor(annotation);
		if (processor == null)
			throw new IllegalArgumentException("Annotation type not supported");
		textField = new VisValidatableTextField(processor.getValidator(annotation));
		try {
			textField.setText(""+field.get(component));
		} catch (IllegalArgumentException | IllegalAccessException e){
			e.printStackTrace();
		}
		textField.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {
				if (textField.isInputValid()){
					try {
						field.set(component, processor.getValue(textField));
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		});
		window.add(new VisLabel(field.getName())).padRight(5);
		window.add(textField).padBottom(2).row();
	}
}