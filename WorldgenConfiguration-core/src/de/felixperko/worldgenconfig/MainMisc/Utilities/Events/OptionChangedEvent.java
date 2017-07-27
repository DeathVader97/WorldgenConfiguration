package de.felixperko.worldgenconfig.MainMisc.Utilities.Events;

import de.felixperko.worldgenconfig.MainMisc.Utilities.Events.EventSystem.WorldgenEvent;
import de.felixperko.worldgenconfig.MainMisc.Utilities.Options.OptionSystem.Option;

public class OptionChangedEvent<T> extends WorldgenEvent {
	Option<T> option;
	T oldValue;
	T newValue;
	
	public OptionChangedEvent(Option<T> option, T oldValue, T newValue) {
		this.option = option;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public Option<T> getOption() {
		return option;
	}

	public T getOldValue() {
		return oldValue;
	}

	public T getNewValue() {
		return newValue;
	}
}
