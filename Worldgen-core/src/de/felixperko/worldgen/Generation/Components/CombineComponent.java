package de.felixperko.worldgen.Generation.Components;

import java.util.HashMap;

import de.felixperko.worldgen.Generation.Misc.GenerationParameterSupply;
import de.felixperko.worldgen.Generation.Misc.GenerationPathIncompleteException;

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
