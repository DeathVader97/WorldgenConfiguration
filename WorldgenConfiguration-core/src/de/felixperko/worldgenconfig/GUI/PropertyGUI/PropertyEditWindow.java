package de.felixperko.worldgenconfig.GUI.PropertyGUI;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.kotcrab.vis.ui.widget.VisWindow;

import de.felixperko.worldgenconfig.PropertyEditor.EditorMisc.EditorLauncher;

public class PropertyEditWindow extends VisWindow {

	PropertySetupManager manager;
	PropertyBuilder builder;
	
	VisValidatableTextField nameField;
	VisTextButton changeNameButton;
	VisTextButton openEditorButton;
	
	public PropertyEditWindow(PropertySetupManager manager, final PropertyBuilder builder) {
		super("Edit "+builder.nameLabel.getText().toString());
		this.builder = builder;
		addCloseButton();
		
//		pad(5);
		nameField = new VisValidatableTextField(builder.nameLabel.getText().toString());
		add(nameField).padRight(5).padBottom(5);
		
		changeNameButton = new VisTextButton("apply name", new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				builder.nameLabel.setText(nameField.getText());
				try {
					manager.pushProperties();
				} catch (InvalidPropertyException e) {
					e.printStackTrace();
				}
			}
		});
		add(changeNameButton).padBottom(5).row();

		openEditorButton = new VisTextButton("open editor", new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				openEditor();
				openEditorButton.setDisabled(true);
				manager.pendingPropertiesForEditor.add(builder);
			}
		});
		add();
		add(openEditorButton).align(Align.left);
	}

	private void openEditor(){
		String javaHome = System.getProperty("java.home");
		String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
		String classpath = System.getProperty("java.class.path");
		String className = EditorLauncher.class.getCanonicalName();
		
		ProcessBuilder builder = new ProcessBuilder(
		javaBin, "-cp", classpath, className);
		builder.redirectOutput(Redirect.INHERIT);
		builder.redirectError(Redirect.INHERIT);
		
		try {
			Process process = builder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
