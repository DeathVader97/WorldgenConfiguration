package de.felixperko.worldgenconfig.MainMisc.Utilities.Events.EventSystem;

public interface WorldgenEventListener<T> {
	public void onEvent(T event);
	public void dispose();
}