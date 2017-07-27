package de.felixperko.worldgenconfig.MainMisc.Utilities.Events.EventSystem;

import java.util.ArrayList;
import java.util.HashMap;

public class EventManager {
	
	HashMap<Class<? extends WorldgenEvent>, ArrayList<WorldgenEventListener>> listeners = new HashMap<>();
	
	public void registerListener(WorldgenEventListener listener, Class<? extends WorldgenEvent> cls){
		getListeners(cls).add(listener);
	}
	
	public void unregisterListener(WorldgenEventListener listener, Class<? extends WorldgenEvent> cls){
		getListeners(cls).remove(listener);
	}
	
	public ArrayList<WorldgenEventListener> getListeners(Class<? extends WorldgenEvent> cls){
		ArrayList<WorldgenEventListener> list = listeners.get(cls);
		if (list == null){
			list = new ArrayList<>();
			listeners.put(cls, list);
		}
		return list;
	}
	
	public <T extends WorldgenEvent> void fireEvent(T event){
		for (WorldgenEventListener listener : getListeners(event.getClass())){
			listener.onEvent(event);
		}
	}
}
