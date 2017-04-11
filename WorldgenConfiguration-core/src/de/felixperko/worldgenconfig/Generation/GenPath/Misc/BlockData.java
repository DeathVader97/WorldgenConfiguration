package de.felixperko.worldgenconfig.Generation.GenPath.Misc;

public class BlockData {
	
	/*
	 * This class contains the Position and Name of a Block which describes a Component in the Editor.
	 */
	
	float editorX, editorY;
	String editorName;

	public float getEditorX() {
		return editorX;
	}

	public void setEditorX(float editorX) {
		this.editorX = editorX;
	}

	public float getEditorY() {
		return editorY;
	}

	public void setEditorY(float editorY) {
		this.editorY = editorY;
	}

	public String getEditorName() {
		return editorName;
	}

	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}

}
