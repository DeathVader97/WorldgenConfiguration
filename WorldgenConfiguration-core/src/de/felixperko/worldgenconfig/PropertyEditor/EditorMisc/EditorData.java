package de.felixperko.worldgenconfig.PropertyEditor.EditorMisc;

import java.util.ArrayList;
import java.util.HashMap;

import de.felixperko.worldgenconfig.Generation.GenPath.Components.Component;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.Block;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.ComponentBlock;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.Connection;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.Connector;

public class EditorData {
	ArrayList<Component> components = new ArrayList<>();
	Integer endID;
	
	public EditorData() {
	}
	
	public EditorData(EditorStage stage) {
		components = stage.endPoint.getComponents();
		endID = ((ComponentBlock) stage.endPoint.getInputs()[0].getConnectedBlock()).getComponent().getID();
	}

	public ArrayList<Component> getComponents() {
		return components;
	}
	
	public void setComponents(ArrayList<Component> components) {
		this.components = components;
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
			for (Component component : components){
				ComponentBlock componentBlock = component.constructBlock(stage);
				stage.addBlock(componentBlock);
				componentMap.put(component.getID(), component);
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
			Connection endConnection = new Connection(stage, componentMap.get(endID).blockGet().output);
			endConnection.set(stage.endPoint.getInputs()[0]);
			stage.addConnection(endConnection);
		} catch (Exception e){
			System.err.println("couldn't import editor data.");
			e.printStackTrace();
		}
	}
	
	public Component getEndComponent(){
		HashMap<Integer, Component> componentMap = new HashMap<>();
		for (Component c : components)
			componentMap.put(c.getID(), c);
		for (Component c : components)
			c.restoreReferences(componentMap);
		return componentMap.get(endID);
	}
}
