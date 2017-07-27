package de.felixperko.worldgenconfig.GUI.Util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

public class GuiList {
	
	GuiList thisList = this;
	
	public Stage stage;
	
	protected VisTable table;
	
	protected VisTextButton addBtn;
	
	public ArrayList<GuiListElement> elements = new ArrayList<>();
	
	SelectBoxGroup group = null;
	
	public GuiList(Stage stage, Table table){
		this.stage = stage;
		this.table = new VisTable();
		table.add(this.table).colspan(2).row();
	}
	
	public void addAddButton(Class <? extends GuiListElement> elementClass){
		addBtn = new VisTextButton("+", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				try {
					addElement(elementClass.getDeclaredConstructor(GuiList.class).newInstance(thisList));
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					System.err.println("couldn't create "+elementClass.getSimpleName());
					e.printStackTrace();
				}
			}
		});
		this.table.add(addBtn).row();
	}
	
	public void addElement(GuiListElement element){
		elements.add(element);
		element.addToTable(table);
		refresh();
	}
	
	public void setElement(GuiListElement element, int index){
		elements.add(index, element);
		element.addToTable(table);
		refresh();
	}
	
	public void refresh(){
		table.clear();
		int i = 0;
		for (GuiListElement e : elements){
			e.addToTable(table);
			e.updatePos(i++, elements.size());
		}
		if (addBtn != null)
			table.add(addBtn).left();
	}

	public boolean moveElement(GuiListElement element, int shift) {
		int pos = -1;
		for (int i = 0 ; i < elements.size() ; i++){
			if (elements.get(i) == element){
				pos = i;
				break;
			}
		}
		if (pos == -1 || pos+shift < 0 || pos+shift >= elements.size())
			return false;
		elements.remove(pos);
		elements.add(pos+shift, element);
		refresh();
		return true;
	}

	public boolean removeElement(GuiListElement element) {
		if (elements.remove(element)){
			refresh();
			return true;
		}
		return false;
	}
	
	public SelectBoxGroup getGroup(){
		if (group == null)
			group = new SelectBoxGroup();
		return group;
	}
}
