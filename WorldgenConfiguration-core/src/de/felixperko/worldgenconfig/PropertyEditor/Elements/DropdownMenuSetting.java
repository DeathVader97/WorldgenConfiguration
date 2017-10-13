package de.felixperko.worldgenconfig.PropertyEditor.Elements;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

import de.felixperko.worldgen.Generation.Components.Component;
import de.felixperko.worldgen.Generation.Misc.Annotations.OptionsComponentSetting;
import de.felixperko.worldgenconfig.Generation.GenPath.Misc.Annotations.AnnotationProcessor;

public class DropdownMenuSetting extends Setting {

	public DropdownMenuSetting(Field field, Annotation annotation) {
		super(field, annotation);
	}
	
	VisSelectBox<String> selectBox;

	@Override
	public void addSettingToWindow(ComponentSettingWindow window, Component component) {
		AnnotationProcessor processor = AnnotationProcessor.getAnnotationProcessor(annotation);
		if (processor == null)
			throw new IllegalArgumentException("Annotation type not supported");
		selectBox = new VisSelectBox<String>();
		try {
			selectBox.setItems(((OptionsComponentSetting)annotation).options());
		} catch (IllegalArgumentException e){
			e.printStackTrace();
		}
		selectBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent arg0, Actor arg1) {
				try {
					field.set(component, processor.getValue(selectBox));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		});
		window.add(new VisLabel(field.getName())).padRight(5);
		window.add(selectBox).row();
	}

}
