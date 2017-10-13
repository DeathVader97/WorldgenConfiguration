package de.felixperko.worldgenconfig.GUI.PropertyGUI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.layout.HorizontalFlowGroup;
import com.kotcrab.vis.ui.widget.VisTable;

import de.felixperko.worldgen.Generation.Misc.PropertyDefinition;
import de.felixperko.worldgenconfig.Generation.GenMisc.WorldConfiguration;
import de.felixperko.worldgenconfig.MainMisc.Main;
import de.felixperko.worldgenconfig.MainMisc.MainStage;

public class PropertySetupManager extends Actor{
	
	MainStage stage;
	
	HorizontalFlowGroup flowGroup;
	public Queue<PropertyBuilder> pendingPropertiesForEditor = new LinkedList<>();
	
	public ArrayList<PropertyBuilder> propertyBuilders = new ArrayList<>();
	
	WorldConfiguration config;
	
	int ID_COUNTER = 0;
	
	public PropertySetupManager(MainStage stage, VisTable table) {
		this.config = Main.main.currentWorldConfig;
		this.stage = stage;
		setStage(stage);
		flowGroup = new HorizontalFlowGroup(0);
		table.add(flowGroup).expandX().fillX().align(Align.left | Align.top);
		loadBuilders(config.properties);
	}
	
	public void addPropertyBuilder() throws InvalidPropertyException{
		propertyBuilders.add(new PropertyBuilder(this, ID_COUNTER++));
		for (int i = 0 ; i < propertyBuilders.size() ; i++){
			PropertyBuilder builder = propertyBuilders.get(i);
			builder.update(i);
		}
		pushProperties();
	}
	
	public void loadBuilders(ArrayList<PropertyDefinition> defs){
		for (PropertyDefinition def : defs){
			propertyBuilders.add(new PropertyBuilder(this, def));
			if (def.id <= ID_COUNTER)
				ID_COUNTER = def.id+1;
		}
		for (int i = 0 ; i < propertyBuilders.size() ; i++){
			PropertyBuilder builder = propertyBuilders.get(i);
			builder.update(i);
		}
		try {
			pushProperties();
		} catch (InvalidPropertyException e) {
			Main.main.configBuildFailure(e);
		}
	}
	
	public void pushProperties() throws InvalidPropertyException{
		ArrayList<PropertyDefinition> properties = new ArrayList<>();
		for (PropertyBuilder builder : propertyBuilders)
			properties.add(builder.build());
		config.setProperties(properties);
	}

	public void removePropertyBuilder(PropertyBuilder builder) throws InvalidPropertyException{
		int index = propertyBuilders.indexOf(builder);
		if (index == -1)
			return;
		propertyBuilders.remove(index);
		rebuild();
		pushProperties();
	}
	
	private void rebuild() {
		flowGroup.clearChildren();
		for (int i = 0 ; i < propertyBuilders.size() ; i++){
			PropertyBuilder builder2 = propertyBuilders.get(i);
			builder2.addToGroup();
			builder2.update(i);
		}
		if (propertyBuilders.size() > 0){
			propertyBuilders.get(0).nameLabel.fire(new ChangedPropertiesEvent(propertyBuilders));
		}
	}

	public void movePropertyBuilder(int oldIndex, int index, PropertyBuilder builder){
		if (oldIndex == -1)
			return;
		propertyBuilders.remove(oldIndex);
		propertyBuilders.add(index, builder);
		rebuild();
	}
	
	public void moveUp(PropertyBuilder builder){
		int index = propertyBuilders.indexOf(builder);
		movePropertyBuilder(index, index-1, builder);
	}
	
	public void moveDown(PropertyBuilder builder){
		int index = propertyBuilders.indexOf(builder);
		movePropertyBuilder(index, index+1, builder);
	}

	public PropertyBuilder getPropertyBuilder(int id) {
		for (PropertyBuilder b : propertyBuilders)
			if (b.id == id)
				return b;
		return null;
	}

//	public PropertyDefinition[] getPropertyDefintions() {
//		PropertyDefinition[] ans = new PropertyDefinition[propertyBuilders.size()];
//		for (int i = 0; i < ans.length; i++) {
//			ans[i] = propertyBuilders.get(i).build();
//		}
//		return ans;
//	}
}
