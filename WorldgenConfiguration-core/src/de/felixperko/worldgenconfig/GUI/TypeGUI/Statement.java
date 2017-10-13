package de.felixperko.worldgenconfig.GUI.TypeGUI;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

import de.felixperko.worldgen.Generation.Misc.PropertyDefinition;
import de.felixperko.worldgenconfig.GUI.Util.CustomSelectionOption;
import de.felixperko.worldgenconfig.GUI.Util.GuiList;
import de.felixperko.worldgenconfig.GUI.Util.GuiListElement;
import de.felixperko.worldgenconfig.GUI.Util.PropertySelectBox;

public class Statement extends GuiListElement {
	
	Statement thisCondition = this;
	
	public PropertySelectBox selectPropertyBox;
	VisValidatableTextField minField;
	VisValidatableTextField maxField;
	VisCheckBox enabledCheckBox;
	
	public Statement(GuiList list) {
		super(list);
		setup();
	}

	public Statement(StatementList list, TypeStatementBuilder sb) {
		super(list);
		setup();
		
		minField.setText(sb.low+"");
		maxField.setText(sb.high+"");
		selectPropertyBox.selectByID(sb.condition);
	}
	
	private void setup() {
		selectPropertyBox = new PropertySelectBox(list.elements, list.getGroup(), new CustomSelectionOption<>("NONE", 1, 1));
//		//add entries that are already used to blacklist
//		getOthers().forEach(c -> selectPropertyBox.addBlackList(c.selectPropertyBox.getSelectedDefinition(),true));
		
		selectPropertyBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				boolean wasChecked = enabledCheckBox.isChecked();
				PropertyDefinition selection = selectPropertyBox.getSelectedDefinition();
				if (selection == null){
					System.out.println("disabled");
					enabledCheckBox.setDisabled(true);
					disable();
				} else{
					System.out.println("changed to selection: "+selection.name);
					enabledCheckBox.setDisabled(false);
					enable();
				}
				if (wasChecked || enabledCheckBox.isChecked())
					thisCondition.changed();
				
//				PropertyDefinition previousSelection = selectPropertyBox.previousSelection;
//				if (selection != previousSelection){
//					getOthers().forEach(c -> c.selectPropertyBox.removeBlackList(previousSelection, false));
//					getOthers().forEach(c -> c.selectPropertyBox.addBlackList(selection, true));
//				}
				
				selectPropertyBox.previousSelection = selection;
			}
		});
		
		minField = new VisValidatableTextField("0"){
			@Override
			public float getPrefWidth() {
				return 50;
			}
		};
		minField.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				thisCondition.changed();
			};
		});
		
		maxField = new VisValidatableTextField("1"){
			@Override
			public float getPrefWidth() {
				return 50;
			}
		};
		maxField.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				thisCondition.changed();
			};
		});
		
		minField.addValidator(new InputValidator(){
			@Override
			public boolean validateInput(String input) {
				try {
					return Double.parseDouble(minField.getText()) < Double.parseDouble(maxField.getText());
				} catch (NumberFormatException e){
					return false;
				}
			}
		});
		maxField.addValidator(new InputValidator(){
			@Override
			public boolean validateInput(String input) {
				try {
					return Double.parseDouble(minField.getText()) < Double.parseDouble(maxField.getText());
				} catch (NumberFormatException e){
					return false;
				}
			}
		});
		
		enabledCheckBox = new VisCheckBox("");
		enabledCheckBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (enabledCheckBox.isChecked())
					enable();
				else
					disable();
				thisCondition.changed();
			}
		});
		disable();
		enabledCheckBox.setDisabled(true);
		
		content.add(enabledCheckBox);
		content.add(selectPropertyBox);
		content.add(minField);
		content.add(maxField);
		
		addRemoveButton();
		removeBtn.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				selectPropertyBox.selectDefault();
			}
		});
	}

	private ArrayList<Statement> getOthers(){
		ArrayList<Statement> ans = new ArrayList<>();
		for (GuiListElement e : list.elements){
			Statement c = (Statement)e;
			if (c != thisCondition)
				ans.add(c);
		}
		return ans;
	}
	
	private void enable() {
		enabledCheckBox.setChecked(true);
		minField.setDisabled(false);
		maxField.setDisabled(false);
	}

	private void disable() {
		enabledCheckBox.setChecked(false);
		minField.setDisabled(true);
		maxField.setDisabled(true);
	}

	private void changed() {
		TypeConfigurationWindow window = ((StatementList)list).window;
		window.updateChanges();
	}
	
	public TypeStatementBuilder getBuilder(){
		if (!enabledCheckBox.isChecked())
			return null;
		try {
			if (selectPropertyBox.getSelectedDefinition() != null)
				return new TypeStatementBuilder(selectPropertyBox.getSelectedDefinition().id, Double.parseDouble(minField.getText()), Double.parseDouble(maxField.getText()));
		} catch (NumberFormatException e){
			e.printStackTrace();
		}
		return null;
	}
}
