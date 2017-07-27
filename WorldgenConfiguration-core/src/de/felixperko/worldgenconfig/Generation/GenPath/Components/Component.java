package de.felixperko.worldgenconfig.Generation.GenPath.Components;

import java.util.HashMap;

import de.felixperko.worldgenconfig.Generation.GenPath.Misc.BlockData;
import de.felixperko.worldgenconfig.Generation.GenPath.Misc.GenerationParameterSupply;
import de.felixperko.worldgenconfig.Generation.GenPath.Misc.GenerationPathIncompleteException;
import de.felixperko.worldgenconfig.PropertyEditor.EditorMisc.EditorStage;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.ComponentBlock;

public abstract class Component {
	
	/*
	 * A component is a module for a computation path.
	 * It contains the logic and links to other components which serve as inputs for chaining.
	 * The Component can be represented by a Block in the Editor.
	 */
	
	static int id_counter;
	public Integer id;
	protected ComponentBlock block;
	
	BlockData editorData = new BlockData();
	
	protected void generateID() {
		id = id_counter;
		id_counter++;
	}
	
	public void updateIDCounter(int id){
		if (id_counter <= id)
			id_counter = id+1;
	}
	
	public Integer getID(){
		return id;
	}
	
	public void setID(Integer id){
		this.id = id;
		updateIDCounter(id);
	}
	
	public abstract double getGenerationValue(GenerationParameterSupply supply) throws GenerationPathIncompleteException;
	public abstract int getInputCount();
	public abstract void setInput(int index, Component component);
	public abstract Integer getInputID(int index);
	public abstract String getDisplayName();
	
	public void restoreReferences(HashMap<Integer, Component> components){}

	public BlockData getEditorData() {
		return editorData;
	}

	public void setEditorData(BlockData editorData) {
		this.editorData = editorData;
	}

	public ComponentBlock constructBlock(EditorStage editorStage) {
		
		ComponentBlock currentBlock = block;
		if (currentBlock == null)
			currentBlock = new ComponentBlock(editorStage, editorData.getEditorX()+75, editorData.getEditorY()+30, this);
		currentBlock.updateText(editorData.getEditorName());
		this.block = currentBlock;
		return currentBlock;
	}

	public ComponentBlock blockGet() {
		return block;
	}

	public void blockSet(ComponentBlock block) {
		this.block = block;
	}
}
