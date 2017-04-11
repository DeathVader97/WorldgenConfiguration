package de.felixperko.worldgenconfig.PropertyEditor.Elements;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.kotcrab.vis.ui.widget.VisWindow;

import de.felixperko.worldgenconfig.Generation.GenPath.Components.Component;
import de.felixperko.worldgenconfig.Generation.GenPath.Misc.DoubleComponentSetting;
import de.felixperko.worldgenconfig.Generation.GenPath.Misc.IntComponentSetting;

public class ComponentSettingWindow extends VisWindow {
	
	ComponentBlock block;
	Component component;
	
	public ComponentSettingWindow(ComponentBlock componentBlock) {
		super("Settings - "+componentBlock.getComponent().getClass().getSimpleName());
		
		this.block = componentBlock;
		this.component = componentBlock.getComponent();
		
		Vector2 pos = componentBlock.stage.getMouseWorldPos();
		setPosition(pos.x, pos.y);
		addCloseButton();
		addSettings();
		pack();
	}

	private void addSettings() {
		
		ArrayList<Setting> settings = new ArrayList<>();
		
		final VisTextField nameField = new VisTextField(block.text);
		nameField.addListener(new ChangeListener(){
			@Override public void changed(ChangeEvent arg0, Actor arg1) {
				block.updateText(nameField.getText());
			}
		});
		
		add(new VisLabel("Name")).padRight(5);
		add(nameField).padBottom(5).row();
		
		Class<? extends Component> cls = component.getClass();
		for (Field field : cls.getFields()){
			Annotation[] annos = field.getDeclaredAnnotations();
			if (annos.length == 0)
				continue;
			for (Annotation a : annos){
				if (a instanceof DoubleComponentSetting){
					Setting setting = new Setting(field, a);
					settings.add(setting);
					setting.addSettingToWindow(this, component);
				}
				else if (a instanceof IntComponentSetting){
					Setting setting = new Setting(field, a);
					settings.add(setting);
					setting.addSettingToWindow(this, component);
				}
			}
		}
	}
}

class Setting{
	
	Field field;
	Annotation annotation;
	VisValidatableTextField textField;
	
	public Setting(Field field, Annotation annotation) {
		this.field = field;
		this.annotation = annotation;
	}
	
	public void addSettingToWindow(ComponentSettingWindow window, final Component component){
		if (annotation instanceof DoubleComponentSetting){
			textField = new VisValidatableTextField(Validators.FLOATS);
			textField.addValidator(new InputValidator() {
				@Override
				public boolean validateInput(String input) {
					double val = Double.parseDouble(input);
					return val >= ((DoubleComponentSetting)annotation).lowest() && val <= ((DoubleComponentSetting)annotation).highest();
				}
			});
			try {
				textField.setText(""+field.getDouble(component));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			textField.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent arg0, Actor arg1) {
					if (textField.isInputValid()){
						try {
							field.set(component, Double.parseDouble(textField.getText()));
						} catch (IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}
			});
			window.add(new VisLabel(field.getName())).padRight(5);
			window.add(textField).padBottom(2).row();
		} else if (annotation instanceof IntComponentSetting){

			textField = new VisValidatableTextField(Validators.INTEGERS);
			textField.addValidator(new InputValidator() {
				@Override
				public boolean validateInput(String input) {
					int val = Integer.parseInt(input);
					return val >= ((IntComponentSetting)annotation).lowest() && val <= ((IntComponentSetting)annotation).highest();
				}
			});
			try {
				textField.setText(""+field.getInt(component));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			textField.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent arg0, Actor arg1) {
					if (textField.isInputValid()){
						try {
							field.set(component, Integer.parseInt(textField.getText()));
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
}
