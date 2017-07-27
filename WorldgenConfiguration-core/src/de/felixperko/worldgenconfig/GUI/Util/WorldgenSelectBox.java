package de.felixperko.worldgenconfig.GUI.Util;

import java.util.Set;
import java.util.TreeSet;

import com.kotcrab.vis.ui.widget.VisSelectBox;

public class WorldgenSelectBox extends VisSelectBox<SelectWrapper> {
	Set<CustomSelectionOption<? extends WorldgenSelectBox>> additionalOptions = new TreeSet<>();
	SelectBoxGroup group = null;
	
	public WorldgenSelectBox(SelectBoxGroup group) {
		this.group = group;
		if (group != null)
			group.addBox(this);
	}

	//TODO generalize from PropertySelectBox
	public void updateProperties(){
		
	}
	
	public void setGroup(SelectBoxGroup group){
		if (this.group != null)
			this.group.removeBox(this);
		this.group = group;
		group.addBox(this);
	}
	
	public WorldgenSelectBox withGroup(SelectBoxGroup group){
		setGroup(group);
		return this;
	}
}