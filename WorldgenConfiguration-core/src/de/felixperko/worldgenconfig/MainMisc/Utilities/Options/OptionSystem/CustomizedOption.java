package de.felixperko.worldgenconfig.MainMisc.Utilities.Options.OptionSystem;

import de.felixperko.worldgenconfig.MainMisc.Utilities.Events.EventSystem.EventManager;

public abstract class CustomizedOption<T> extends Option<T> {

	public CustomizedOption(OptionBuilder<T> builder, T startValue, EventManager eventManager, Runnable onChange) {
		super(builder, startValue, eventManager, onChange);
	}
	
	@Override
	public T getValue() {
		return customGetValue();
	}
	
	public abstract T customGetValue();
}
