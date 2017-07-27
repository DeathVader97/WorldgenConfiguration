package de.felixperko.worldgenconfig.Generation.GenMisc;

import java.util.ArrayList;

import org.yaml.snakeyaml.Yaml;

import com.badlogic.gdx.files.FileHandle;

import de.felixperko.worldgenconfig.GUI.PropertyGUI.InvalidPropertyException;
import de.felixperko.worldgenconfig.MainMisc.Main;
import de.felixperko.worldgenconfig.MainMisc.Utilities.Events.PropertiesChangedEvent;
import de.felixperko.worldgenconfig.MainMisc.Utilities.Events.TypesChangedEvent;

/**
 * Stores the Data of a world configuration and handles events related to modifications of said data.
 */

public class WorldConfiguration {
	
	public ArrayList<PropertyDefinition> properties = new ArrayList<>();
	
	public ArrayList<TerrainType> types = new ArrayList<>();
	
	Yaml yaml = Main.main.yaml;
	FileHandle worldConfigFile = Main.main.projectDirectory.child("worldconfig.yml");
	
	public WorldConfiguration() {
		worldConfigFile.parent().mkdirs();
	}
	
//	public void firePropertiesChangedEvent(){
//		for (PropertiesChangeListener listener : propertiesListeners)
//			listener.propertiesChanged(this);
//		try {
//			yaml.dump(this, new FileWriter(worldConfigFile.file()));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
////		System.out.println("properties changed. "+propertiesListeners.size()+" listeners notified.");
////		for (PropertyDefinition def : properties)
////			System.out.println(" - "+def.id);
//	}
//	
//	public void fireTypesChangedEvent(){
//		for (TypesChangeListener listener : typeListeners)
//			listener.terrainTypesChanged(this);
//		try {
//			yaml.dump(this, new FileWriter(worldConfigFile.file()));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	public PropertyDefinition getPropertyByName(String name){
		for (PropertyDefinition def : properties)
			if (def.name.equals(name))
				return def;
		return null;
	}
	
	public TerrainType getTypeByName(String name){
		for (TerrainType type : types)
			if (type.name.equals(name))
				return type;
		return null;
	}
	
	public ArrayList<PropertyDefinition> getProperties() {
		return properties;
	}
	
	public void addProperty(PropertyDefinition property){
		ArrayList<PropertyDefinition> oldProperties = new ArrayList<>(properties);
		properties.add(property);
		Main.main.eventManager.fireEvent(new PropertiesChangedEvent(oldProperties, properties));
	}

	public void setProperties(ArrayList<PropertyDefinition> properties) {
		ArrayList<PropertyDefinition> oldProperties = new ArrayList<>(properties);
		this.properties = properties;
		Main.main.eventManager.fireEvent(new PropertiesChangedEvent(oldProperties, properties));
	}

	public ArrayList<TerrainType> getTypes() {
		return types;
	}
	
	public void addType(TerrainType type){
		ArrayList<TerrainType> oldTypes = new ArrayList<>(types);
		types.add(type);
		Main.main.eventManager.fireEvent(new TypesChangedEvent(oldTypes, types));
	}

	public void setTypes(ArrayList<TerrainType> types) {
		ArrayList<TerrainType> oldTypes = new ArrayList<>(types);
		this.types = types;
		Main.main.eventManager.fireEvent(new TypesChangedEvent(oldTypes, types));
		try {
			Main.main.stage.displaySettingsManager.applyCombined();
		} catch (InvalidPropertyException e) {
			Main.main.configBuildFailure(e);
		}
	}
}

abstract interface WorldgenListener{}