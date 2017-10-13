package de.felixperko.worldgenconfig.PropertyEditor.EditorMisc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import de.felixperko.worldgen.Generation.Components.Component;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.Block;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.ComponentBlock;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.Connection;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.Connector;

public class EditorData {
	
	HashMap<Component, ComponentBlock> components = new HashMap<>();
	Integer endID;
	
	public EditorData() {
	}
	
	public EditorData(EditorStage stage) {
		for (Component c : stage.endPoint.getComponents()){
			components.put(c, null);
		}
		endID = ((ComponentBlock) stage.endPoint.getInputs()[0].getConnectedBlock()).getComponent().getID();
	}

	public Set<Component> getComponents() {
		return components.keySet();
	}
	
	public void setComponents(Set<Component> components) {
		for (Component c : components)
			this.components.put(c, null);
	}
	
	public int getEndID() {
		return endID;
	}
	
	public void setEndID(int endID) {
		this.endID = endID;
	}
	
	public void importToStage(EditorStage stage){
		
		try {
			HashMap<Integer, Component> componentMap = new HashMap<>();
			for (Entry<Component, ComponentBlock> e : components.entrySet()){
				Component component = e.getKey();
				ComponentBlock componentBlock = constructBlock(stage, component);
				stage.addBlock(componentBlock);
				componentMap.put(component.getID(), component);
				components.put(component, componentBlock);
			}
			for (Block block : stage.blocks){
				if (block instanceof ComponentBlock){
					ComponentBlock cBlock = (ComponentBlock)block;
					cBlock.getComponent().restoreReferences(componentMap);
					Connector[] inputs = cBlock.getInputs();
					for (Connector input : inputs){
						if (input != null && input.connection != null)
							stage.addConnection(input.connection);
					}
				}
			}
			Connection endConnection = new Connection(stage, components.get(componentMap.get(endID)).output);
			endConnection.set(stage.endPoint.getInputs()[0]);
			stage.addConnection(endConnection);
		} catch (Exception e){
			System.err.println("couldn't import editor data.");
			e.printStackTrace();
		}
	}
	
	private ComponentBlock constructBlock(EditorStage stage, Component component) {
		HashMap<String, String> editorData = component.additionalData.get("editor");
		ComponentBlock block = new ComponentBlock(stage, Float.parseFloat(editorData.get("x"))+75, Float.parseFloat(editorData.get("y"))+30, component.getClass());
		block.updateText(editorData.get("name"));
		return block;
	}

	public Component getEndComponent(){
		HashMap<Integer, Component> componentMap = new HashMap<>();
		for (Component c : components.keySet())
			componentMap.put(c.getID(), c);
		for (Component c : components.keySet())
			c.restoreReferences(componentMap);
		return componentMap.get(endID);
	}
}
