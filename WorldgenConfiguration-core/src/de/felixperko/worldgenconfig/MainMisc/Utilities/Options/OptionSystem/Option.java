package de.felixperko.worldgenconfig.MainMisc.Utilities.Options.OptionSystem;

import de.felixperko.worldgenconfig.MainMisc.Utilities.Events.OptionChangedEvent;
import de.felixperko.worldgenconfig.MainMisc.Utilities.Events.EventSystem.EventManager;

public class Option<T>{
	
	static int ID_COUNTER = 0;
	int id;
	T value;
	Runnable onChange = null;
	
	EventManager eventManager;
	
	boolean cancelChange = false;
	private OptionBuilder<T> builder;
	
	public Option(OptionBuilder<T> builder, T startValue, EventManager eventManager) {
		init(builder, startValue, eventManager);
	}
	
	public Option(OptionBuilder<T> builder, T startValue, EventManager eventManager, Runnable onChange){
		init(builder, startValue, eventManager);
		this.onChange = onChange;
	}
	
	private void init(OptionBuilder<T> builder, T startValue, EventManager eventManager){
		this.builder = builder;
		this.id = ID_COUNTER++;
		this.value = startValue;
		this.eventManager = eventManager;
	}
	
	public void setOnChange(Runnable onChange){
		this.onChange = onChange;
	}
	
	public boolean changeValue(T newValue){
		if (onChange != null)
			onChange.run();
		OptionChangedEvent<T> event = new OptionChangedEvent<T>(this, value, newValue);
		event.setCancelled(cancelChange);
		if (cancelChange)
			cancelChange = false;
		eventManager.fireEvent(event);
		if (event.isCancelled()){
			return false;
		}
		this.value = newValue;
		builder.setStoredValue(value);
		return true;
	}
	
	public T getValue(){
		return value;
	}
	
	public void cancelChange(boolean cancel){
		this.cancelChange = cancel;
	}
	
	public int getID(){
		return id;
	}
}
