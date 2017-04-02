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

import de.felixperko.worldgenconfig.Generation.GenerationPath.DoubleNodeSetting;
import de.felixperko.worldgenconfig.Generation.GenerationPath.Nodes.Node;

public class NodeSettingWindow extends VisWindow {
	
	EditorNode eNode;
	Node node;
	
	public NodeSettingWindow(EditorNode editorNode) {
		super("Settings - "+editorNode.getNode().getClass().getSimpleName());
		
		this.eNode = editorNode;
		this.node = editorNode.getNode();
		
		Vector2 pos = editorNode.stage.getMouseWorldPos();
		setPosition(pos.x, pos.y);
		addCloseButton();
		addSettings();
		pack();
	}

	private void addSettings() {
		
		ArrayList<Setting> settings = new ArrayList<>();
		
		final VisTextField nameField = new VisTextField(eNode.text);
		nameField.addListener(new ChangeListener(){
			@Override public void changed(ChangeEvent arg0, Actor arg1) {
				eNode.updateText(nameField.getText());
			}
		});
		
		add(new VisLabel("Name")).padRight(5);
		add(nameField).padBottom(5).row();
		
		Class<? extends Node> cls = node.getClass();
		for (Field field : cls.getFields()){
			Annotation[] annos = field.getDeclaredAnnotations();
			if (annos.length == 0)
				continue;
			for (Annotation a : annos){
				if (a instanceof DoubleNodeSetting){
					Setting setting = new Setting(field, a);
					settings.add(setting);
					setting.addSettingToWindow(this, node);
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
	
	public void addSettingToWindow(NodeSettingWindow window, final Node node){
		if (annotation instanceof DoubleNodeSetting){
			textField = new VisValidatableTextField(Validators.FLOATS);
			textField.addValidator(new InputValidator() {
				@Override
				public boolean validateInput(String input) {
					double val = Double.parseDouble(input);
					return val >= ((DoubleNodeSetting)annotation).lowest() && val <= ((DoubleNodeSetting)annotation).highest();
				}
			});
			try {
				textField.setText(""+field.getDouble(node));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			textField.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent arg0, Actor arg1) {
					if (textField.isInputValid()){
						try {
							field.set(node, Double.parseDouble(textField.getText()));
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
