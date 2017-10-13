package de.felixperko.worldgenconfig.PropertyEditor.Elements;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisWindow;

import de.felixperko.worldgen.Generation.Components.Component;
import de.felixperko.worldgenconfig.Generation.GenPath.Misc.Annotations.AnnotationProcessor;

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
		
		Class<? extends Component>[] classes = component.getSettingClasses();
		for (int i = 0 ; i < classes.length ; i++){
			for (Field field : classes[i].getFields()){
				Annotation[] annos = field.getDeclaredAnnotations();
				if (annos.length == 0)
					continue;
				for (Annotation a : annos){
					AnnotationProcessor processor = AnnotationProcessor.getAnnotationProcessor(a);
					if (processor == null)
						continue;
					Class<? extends Setting> settingCls = processor.getSettingClass();
					Setting setting;
					try {
						setting = settingCls.getDeclaredConstructor(Field.class, Annotation.class).newInstance(field, a);
						settings.add(setting);
						setting.addSettingToWindow(this, component);
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | SecurityException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
