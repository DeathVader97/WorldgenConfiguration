package de.felixperko.worldgenconfig.GUI.Util;

import java.util.ArrayList;

public class SelectBoxGroup {
	
	ArrayList<WorldgenSelectBox> boxes = new ArrayList<>();
	ArrayList<Integer> blackList = new ArrayList<>();
	
	public void addBox(WorldgenSelectBox box){
		boxes.add(box);
	}
	
	public void removeBox(WorldgenSelectBox box){
		boxes.remove(box);
	}
	
	public boolean isBlackisted(Integer id){
		return blackList.contains(id);
	}
	
	public boolean setBlacklisted(Integer id, boolean update){
		if (!blackList.contains(id)){
			blackList.add(id);
			if (update)
				update();
			return true;
		}
		return false;
	}
	
	public boolean removeBlacklist(Integer id, boolean update){
		if (blackList.remove(id)){
			if (update)
				update();
		}
		return false;
	}

	void update() {
		boxes.forEach(b -> b.updateProperties());
	}

	public void clearBlacklist(boolean update) {
		blackList.clear();
		if (update)
			update();
	}
}
