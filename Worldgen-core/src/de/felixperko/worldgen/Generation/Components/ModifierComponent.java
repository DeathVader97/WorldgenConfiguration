package de.felixperko.worldgen.Generation.Components;

import java.util.HashMap;

import de.felixperko.worldgen.Generation.Interpolation.Modifier;
import de.felixperko.worldgen.Generation.Misc.GenerationParameterSupply;
import de.felixperko.worldgen.Generation.Misc.GenerationPathIncompleteException;

public class ModifierComponent extends Component {
	
	private Component input;
	public Integer inputID;
	public Modifier modifier;
	
	public ModifierComponent(boolean defaultSettings) {
		if (defaultSettings){
			modifier = new Modifier(0);
			modifier.addLinear(-1000000, 0, -1, 0);
			modifier.addLinear(0, 1000000, 1, 0);
		}
		generateID();
	}
	
	public ModifierComponent(){
		generateID();
		modifier = new Modifier(0);
		modifier.addLinear(-1000000, 0, -1, 0);
		modifier.addLinear(0, 1000000, 1, 0);
	}
	
	@Override
	public Integer getInputID(int index) {
		return getInputID();
	}
	
	public Integer getInputID() {
		if (input != null)
			inputID = input.getID();
		return inputID;
	}

	public void setInputID(int inputID) {
		this.inputID = inputID;
	}

	public Modifier getModifier() {
		return modifier;
	}

	public void setModifier(Modifier modifier) {
		this.modifier = modifier;
	}
	
	@Override
	public void restoreReferences(HashMap<Integer, Component> components) {
		input = components.get(inputID);
		
//		if (blockGet() != null){
//			Connection c = new Connection(blockGet().stage, blockGet().inputs[0]);
//			c.set(input.blockGet().output);
//			blockGet().inputs[0].setConnection(c);
//		}
	}

	@Override
	public double getGenerationValue(GenerationParameterSupply supply) throws GenerationPathIncompleteException{
		if (input == null)
			throw new GenerationPathIncompleteException();
		double inputValue = input.getGenerationValue(supply);
		double outputValue = modifier.modify(inputValue);
		return outputValue;
	}

	@Override
	public int getInputCount() {
		return 1;
	}

	@Override
	public void setInput(int index, Component component) {
		inputID = component.getID();
		input = component;
	}

	@Override
	public String getDisplayName() {
		return "Modifier";
	}

}
