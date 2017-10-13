package de.felixperko.worldgen.Generation.Components;

import java.util.HashMap;

import de.felixperko.worldgen.Generation.Misc.GenerationParameterSupply;
import de.felixperko.worldgen.Generation.Misc.GenerationPathIncompleteException;

public abstract class Component {
	
	/*
	 * A component is a module for a computation path.
	 * It contains the logic and links to other components which serve as inputs for chaining.
	 * The Component can be represented by a Block in the Editor.
	 */
	
	static int id_counter;
	public Integer id;
	
	public HashMap<String,HashMap<String,String>> additionalData = new HashMap<>();
	
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

//	public BlockData getEditorData() {
//		return editorData;
//	}
//
//	public void setEditorData(BlockData editorData) {
//		this.editorData = editorData;
//	}
//
//	public ComponentBlock constructBlock(EditorStage editorStage) {
//		
//		ComponentBlock currentBlock = block;
//		if (currentBlock == null)
//			currentBlock = new ComponentBlock(editorStage, editorData.getEditorX()+75, editorData.getEditorY()+30, this);
//		currentBlock.updateText(editorData.getEditorName());
//		this.block = currentBlock;
//		return currentBlock;
//	}
//
//	public ComponentBlock blockGet() {
//		return block;
//	}
//
//	public void blockSet(ComponentBlock block) {
//		this.block = block;
//	}

	@SuppressWarnings("unchecked")
	public Class<? extends Component>[] getSettingClasses() {
		return new Class[]{getClass()};
	}
	
	public String readData(String container, String key, String defaultValue) {
		HashMap<String,String> map = additionalData.get(container);
		if (map == null){
			map = new HashMap<>();
			additionalData.put(container, map);
		}
		String s = map.get(key);
		if (s == null){
			map.put(key, defaultValue);
			return defaultValue;
		}
		return s;
	}

	public void writeData(String container, String key, String value) {
		HashMap<String,String> map = additionalData.get(container);
		if (map == null){
			map = new HashMap<>();
			additionalData.put(container, map);
		}
		map.put(key, value);
	}
}
