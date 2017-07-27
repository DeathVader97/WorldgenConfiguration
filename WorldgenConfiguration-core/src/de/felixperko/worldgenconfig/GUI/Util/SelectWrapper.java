package de.felixperko.worldgenconfig.GUI.Util;

public class SelectWrapper{
	
	String name;
	
	public SelectWrapper(String name){
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof SelectWrapper && ((SelectWrapper)obj).name.equals(name);
	}
	
	public void onSelect(){}
}
