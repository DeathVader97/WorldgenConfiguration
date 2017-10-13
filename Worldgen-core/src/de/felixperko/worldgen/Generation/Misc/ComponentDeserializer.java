package de.felixperko.worldgen.Generation.Misc;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import de.felixperko.worldgen.Generation.Components.Component;

public class ComponentDeserializer {
	
	ArrayList<String> lines = new ArrayList<>();
	int nr;
	
	boolean inputComplete;
	
	public ComponentDeserializer(int nr){
		this.nr = nr;
	}
	
	public void addLine(String line){
		if (line.equals("/endlist")){
			inputComplete = true;
		}
		else
			lines.add(line);
	}
	
	public boolean isInputComplete(){
		return inputComplete;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Component> process(){
		HashMap<Integer, Component> components = new HashMap<>();
		ArrayList<Component> res = new ArrayList<>();
		for (String s : lines){
			try {
				String[] s2 = s.split(":");
				Class<? extends Component> cls = (Class<? extends Component>) Class.forName("de.felixperko.worldgenconfig.Generation.GenerationPath.Components."+s2[0]);
				Constructor<? extends Component> c = cls.getConstructor(String.class);
				Component n = c.newInstance(s2[1]);
				res.add(n);
				components.put(n.getID(), n);
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		for (Component n : res){
			n.restoreReferences(components);
		}
		
		return res;
	}
}
