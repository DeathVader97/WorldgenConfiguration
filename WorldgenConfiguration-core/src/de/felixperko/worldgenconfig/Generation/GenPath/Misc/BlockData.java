package de.felixperko.worldgenconfig.Generation.GenPath.Misc;

import java.util.HashMap;

import de.felixperko.worldgen.Generation.Components.Component;

public class BlockData {
	
	/*
	 * This class contains the Position and Name of a Block which describes a Component in the Editor.
	 */
	
	float editorX, editorY;
	String editorName;
	
	Component component;
	HashMap<String, String> map;

	public BlockData(Component component) {
		this.component = component;
		editorName = component.readData("editor", "name", component.getID()+"");
		editorName = component.readData("editor", "x", 0+"");
		editorName = component.readData("editor", "y", 0+"");
	}

	public float getEditorX() {
		return editorX;
	}

	public void setEditorX(float editorX) {
		this.editorX = editorX;
		component.writeData("editor", "x", editorX+"");
	}

	public float getEditorY() {
		return editorY;
	}

	public void setEditorY(float editorY) {
		this.editorY = editorY;
		component.writeData("editor", "y", editorY+"");
	}

	public String getEditorName() {
		return editorName;
	}

	public void setEditorName(String editorName) {
		this.editorName = editorName;
		component.writeData("editor", "name", editorName);
	}

}
