package de.felixperko.worldgenconfig.GUI.MenuGUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.kotcrab.vis.ui.widget.VisWindow;

import de.felixperko.worldgenconfig.MainMisc.Main;

public class NewProjectWindow extends VisWindow {
	
	public NewProjectWindow() {
		super("Create Project");
		
//		String startString = "Enter name here...";
		VisValidatableTextField nameField = new VisValidatableTextField();
		
		VisTextButton createButton = new VisTextButton("Create Project");
		
//		nameField.addListener(new ChangeListener() {
//			@Override
//			public void changed(ChangeEvent event, Actor actor) {
//				if (nameField.getText().toString().equalsIgnoreCase(startString)){
//					nameField.setText("");
//				}
//			}
//		});
		
		nameField.addValidator(new InputValidator() {
			@Override
			public boolean validateInput(String input) {
				if (input.length() == 0){
					createButton.setDisabled(true);
					return false;
				}
				for (FileHandle handle : Main.main.workDirectory.list()){
					if (handle.name().equals(input)){
						createButton.setDisabled(true);
						return false;
					}
				}				
				createButton.setDisabled(false);
				return true;
			}
		});
		
		createButton.setDisabled(true);
		
		add(nameField).padRight(5).padTop(10);
		add(createButton).padTop(10);
		addCloseButton();
		pad(10);
		padTop(30);
		pack();
		setPosition((Gdx.graphics.getWidth()-getWidth())/2, (Gdx.graphics.getHeight()-getHeight())/2);
	}
}
