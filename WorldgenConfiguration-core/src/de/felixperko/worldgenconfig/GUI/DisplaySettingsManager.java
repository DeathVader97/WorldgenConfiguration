package de.felixperko.worldgenconfig.GUI;

import java.util.ArrayList;

import de.felixperko.worldgenconfig.GUI.Util.PropertyWrapper;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import de.felixperko.worldgen.Generation.Misc.Parameters;
import de.felixperko.worldgen.Generation.Misc.PropertyDefinition;
import de.felixperko.worldgen.Generation.Misc.SelectionRuleset;
import de.felixperko.worldgen.Generation.Misc.TerrainType;
import de.felixperko.worldgenconfig.GUI.PropertyGUI.FailureReason;
import de.felixperko.worldgenconfig.GUI.PropertyGUI.InvalidPropertyException;
import de.felixperko.worldgenconfig.GUI.Util.CustomSelectionOption;
import de.felixperko.worldgenconfig.GUI.Util.PropertySelectBox;
import de.felixperko.worldgenconfig.MainMisc.Main;
import de.felixperko.worldgenconfig.MainMisc.MainStage;

public class DisplaySettingsManager {
	
	MainStage stage;
	SelectBox<String> selectType;
	PropertySelectBox selectElement;
	
	ArrayList<ArrayList<String>> options = new ArrayList<>();
	{
		options.add(new ArrayList<>());
	}
	
	public DisplaySettingsManager(MainStage stage, SelectBox<String> selectDisplayType, PropertySelectBox selectDisplayElement) {
		this.stage = stage;
		this.selectType = selectDisplayType;
		this.selectElement = selectDisplayElement;
		selectDisplayElement.addAdditionalOption(new CustomSelectionOption<>("combined",1,1));
		addListeners();
		
		selectType.setItems("overview");
	}

	private void addListeners() {
//		stage.addListener(new EventListener() {
//			@Override
//			public boolean handle(Event event) {
//				if (event instanceof ChangedPropertiesEvent){
//					ArrayList<String> list = new ArrayList<>();
//					((ChangedPropertiesEvent)event).getPropertyBuilders().forEach(builder -> list.add(builder.nameLabel.getText().toString()));
//					list.add("combined");
//					options.set(0, list);
//					if (selectType.getSelectedIndex() == 0){
//						selectElement.setItems(list.toArray(new String[list.size()]));
//					}
//				}
//				return false;
//			}
//		});
		
		selectElement.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				//TODO
				try {
					if (selectElement.getSelected() instanceof PropertyWrapper)
						stage.propertySetupManager.getPropertyBuilder(selectElement.getSelectedIndex()).applyComponent();
					else
						applyCombined();
				} catch (InvalidPropertyException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void applyCombined() throws InvalidPropertyException{
		SelectionRuleset ruleset = new SelectionRuleset() {
			@Override
			protected int selectValue() {
				double minDifference = Double.MAX_VALUE;
				TerrainType currentType = null;
				double[] features = new double[parameters.propertySize];
				for (int i = 0; i < features.length; i++) {
					features[i] = getProperty(i);
				}
				for (TerrainType type : Main.main.currentWorldConfig.getTypes()){
					double d = type.selector.getDifference(features);
					if (d < minDifference){
						minDifference = d;
						currentType = type;
					}
				}
				if (currentType != null)
					return currentType.getRGB();
				return 0;
			}
		};
		ArrayList<PropertyDefinition> properties = Main.main.currentWorldConfig.getProperties();
		for (PropertyDefinition def : properties)
			if (def.getLastComponent() == null)
				throw new InvalidPropertyException(FailureReason.BAD_CONFIG);
		ArrayList<TerrainType> types = Main.main.currentWorldConfig.getTypes();
		Parameters params = new Parameters(ruleset, properties.toArray(new PropertyDefinition[properties.size()]), types.toArray(new TerrainType[types.size()]));
		stage.setParameters(params, false);
	}
	
	public boolean isSelected(int id){
		return selectElement.getSelectedIndex() == id;
	}
}