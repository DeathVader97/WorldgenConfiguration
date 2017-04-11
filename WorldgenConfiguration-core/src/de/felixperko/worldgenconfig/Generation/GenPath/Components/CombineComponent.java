package de.felixperko.worldgenconfig.Generation.GenPath.Components;

import java.util.HashMap;

import de.felixperko.worldgenconfig.Generation.GenPath.Misc.GenerationParameterSupply;
import de.felixperko.worldgenconfig.Generation.GenPath.Misc.GenerationPathIncompleteException;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.Connection;

public abstract class CombineComponent extends Component {
	
	Component[] inputComponents;
	Integer[] inputComponentIDs;
	
	double[] inputValues;
	
	public CombineComponent(int inputs) {
		generateID();
		inputComponents = new Component[inputs];
		inputComponentIDs = new Integer[inputs];
		inputValues = new double[inputs];
		updateIDCounter(id);
	}
	
	@Override
	public void restoreReferences(HashMap<Integer, Component> components) {
		for (int i = 0; i < inputComponentIDs.length; i++) {
			inputComponents[i] = components.get(inputComponentIDs[i]);
			
			if (block != null){
				Connection c = new Connection(block.stage, block.inputs[i]);
				c.set(inputComponents[i].block.output);
				block.inputs[i].setConnection(c);
			}
		}
	}
	
	@Override
	public double getGenerationValue(GenerationParameterSupply supply) throws GenerationPathIncompleteException {
		for (int i = 0; i < inputComponents.length; i++) {
			if (inputComponents[i] == null)
				throw new GenerationPathIncompleteException();
			inputValues[i] = inputComponents[i].getGenerationValue(supply);
		}
		return combine();
	}

	@Override
	public int getInputCount() {
		return inputComponents.length;
	}

	@Override
	public void setInput(int index, Component component) {
		inputComponents[index] = component;
		inputComponentIDs[index] = component.getID();
	}
	
	@Override
	public Integer getInputID(int index) {
		return inputComponentIDs[index];
	}
	
	protected abstract double combine();

	public Integer[] getInputComponentIDs() {
		return inputComponentIDs;
	}

	public void setInputComponentIDs(Integer[] inputComponentIDs) {
		this.inputComponentIDs = inputComponentIDs;
	}
}
