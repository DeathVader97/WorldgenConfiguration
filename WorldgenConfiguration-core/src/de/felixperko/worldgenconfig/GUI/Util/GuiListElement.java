package de.felixperko.worldgenconfig.GUI.Util;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class GuiListElement {
	
	protected GuiList list;
	public GuiListElement thisElement = this;
	
	protected ArrayList<Actor> content = new ArrayList<>();
	
	VisTextButton shiftUpBtn, shiftDownBtn;
	protected VisTextButton removeBtn;
	
	public GuiListElement(GuiList list){
		this.list = list;
		
		
	}
	
	public void addToTable(VisTable table) {
		for (Actor a : content)
			table.add(a).left().padRight(5).padBottom(2);
		table.row();
	}

	public void updatePos(int newPos, int size) {
		if (shiftDownBtn != null)
			shiftDownBtn.setDisabled(newPos == size-1);
		if (shiftUpBtn != null)
			shiftUpBtn.setDisabled(newPos == 0);
		updatePosition(newPos, size);
	}
	
	protected void updatePosition(int newPos, int size){}
	
	public void addShiftButtons(){
		shiftUpBtn = new VisTextButton("^", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				thisElement.list.moveElement(thisElement, -1);
			}
		});
		content.add(shiftUpBtn);

		shiftDownBtn = new VisTextButton("v", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				thisElement.list.moveElement(thisElement, 1);
			}
		});
		content.add(shiftDownBtn);
	}
	
	public void addRemoveButton(){
		removeBtn = new VisTextButton("-", new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				thisElement.list.removeElement(thisElement);
				removed();
			}
		});
		content.add(removeBtn);
	}
	
	protected void removed(){
		for (Actor a : content)
			if (a instanceof Disposable)
				((Disposable)a).dispose();
	}
}