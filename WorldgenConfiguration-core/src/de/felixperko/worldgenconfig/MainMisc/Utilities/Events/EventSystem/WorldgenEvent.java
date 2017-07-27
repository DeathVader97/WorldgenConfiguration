package de.felixperko.worldgenconfig.MainMisc.Utilities.Events.EventSystem;

public class WorldgenEvent {
	
	boolean cancelled = false;

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
