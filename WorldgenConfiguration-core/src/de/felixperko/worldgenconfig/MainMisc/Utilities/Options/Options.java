package de.felixperko.worldgenconfig.MainMisc.Utilities.Options;

import java.util.ArrayList;

import de.felixperko.worldgenconfig.MainMisc.Main;
import de.felixperko.worldgenconfig.MainMisc.Utilities.Events.EventSystem.EventManager;
import de.felixperko.worldgenconfig.MainMisc.Utilities.Options.OptionSystem.Option;
import de.felixperko.worldgenconfig.MainMisc.Utilities.Options.OptionSystem.OptionBuilder;

public class Options {
	
	public ArrayList<OptionBuilder<?>> builders = new ArrayList<>();
	
	EventManager eventManager;
	
	public Option<Integer> width;
	public Option<Integer> height;
	
	public Option<Integer> foregroundFPS;
	public Option<Integer> backgroundFPS;
	
	public Option<Integer> helperThreads;
	
	public Option<String> defaultProject;
	
	private void setDefault(){
		addDefaultOption("width", 1000);
		addDefaultOption("height", 700);
		addDefaultOption("foregroundFPS", 0);
		addDefaultOption("backgroundFPS", 30);
		addDefaultOption("helperThreads", 0);
		addDefaultOption("defaultProject", "default");
	}
	
	public Options(){
		eventManager = Main.main.eventManager;
		setDefault();
	}
	
	@SuppressWarnings("rawtypes")
	public void setField(OptionBuilder builder){
		try {
			getClass().getDeclaredField(builder.getFieldName()).set(this, builder.build(eventManager));
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	public <T> boolean addDefaultOption(String fieldName, T value){
		for (OptionBuilder<?> builder : builders)
			if (builder.fieldName.equals(fieldName))
				return false;
		OptionBuilder<T> builder = new OptionBuilder<>();
		builder.setFieldName(fieldName);
		builder.setStoredValue(value);
		setField(builder);
		builders.add(builder);
		return true;
	}
}