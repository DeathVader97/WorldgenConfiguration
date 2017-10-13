package de.felixperko.worldgenconfig.GUI.Util;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Disposable;

import de.felixperko.worldgen.Generation.Misc.PropertyDefinition;
import de.felixperko.worldgenconfig.Generation.GenMisc.WorldConfiguration;
import de.felixperko.worldgenconfig.MainMisc.Main;
import de.felixperko.worldgenconfig.MainMisc.Utilities.Events.PropertiesChangedEvent;
import de.felixperko.worldgenconfig.MainMisc.Utilities.Events.EventSystem.WorldgenEventListener;

public class PropertySelectBox extends WorldgenSelectBox implements WorldgenEventListener<PropertiesChangedEvent>, Disposable{
	
	public PropertyDefinition previousSelection = null;
	
	WorldConfiguration worldConfig;
	ArrayList<PropertyDefinition> definitions = new ArrayList<>();
	
	boolean DEFAULT_one_time_use = true;
	
	static int idc = 1;
	int id;
	{
		id = idc++;
	}
	
	public PropertySelectBox(ArrayList<GuiListElement> elements, SelectBoxGroup group, CustomSelectionOption<?>... customSelectionOptions){
		super(group);
		
		worldConfig = Main.main.currentWorldConfig;
		Main.main.eventManager.registerListener(this, PropertiesChangedEvent.class);
		
		for (CustomSelectionOption<?> customSelectionOption : customSelectionOptions) {
			addAdditionalOption(customSelectionOption);
		}
		updateProperties();
		
		addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				selectionChanged();
				if (group != null)
					group.update();
			}
		});
	}
	
	public void selectionChanged() {
		PropertyWrapper wrapper = null;
		if (getSelected() instanceof PropertyWrapper)
			wrapper = (PropertyWrapper)getSelected();
		
		if (group != null){
			if (previousSelection != null)
				group.removeBlacklist(previousSelection.id, false);
			if (wrapper != null)
				group.setBlacklisted(wrapper.def.id, false);
		}
		if (wrapper != null)
			previousSelection = wrapper.def;
	}
	
	public void addAdditionalOption(CustomSelectionOption<? extends WorldgenSelectBox> option){
		additionalOptions.add(option);
		updateProperties();
	}
	
	boolean updating = false;
	
	@Override
	public void updateProperties() {
		if (updating)
			return;
		updating = true;
		Iterator<CustomSelectionOption<? extends WorldgenSelectBox>> iterator = additionalOptions.iterator();
		CustomSelectionOption<? extends WorldgenSelectBox> addOption = null;
		
		ArrayList<PropertyDefinition> properties = worldConfig.getProperties();
		int oldID = -1;
		PropertyDefinition selected;
		if ((selected = getSelectedDefinition()) != null)
			oldID = selected.id;

		ArrayList<SelectWrapper> items = new ArrayList<>(properties.size()+additionalOptions.size());
		
		//Add additional options that have a lower priority than default
		boolean addMore = false;
		while (iterator.hasNext()){
			addOption = iterator.next();
			if (addOption.orderPriority <= 0)
				items.add(addOption);
			else {
				addMore = true;
				break;
			}
		}
		
		//Add default options
		this.definitions = properties;
		for (PropertyDefinition def : properties){
			PropertyWrapper wrapper = new PropertyWrapper(def);
			if ((selected != null && selected.id == def.id) || group == null || !group.isBlackisted(def.id)){
				items.add(wrapper);
			}
		}
		
		//Add additional options with a higher priority
		if (addMore){
			while (true) {
				items.add(addOption);
				if (iterator.hasNext())
					addOption = iterator.next();
				else
					break;
			}
		}
		setItems(items.toArray(new SelectWrapper[items.size()]));
		if (oldID != -1){
			for (PropertyDefinition def : definitions){
				if (def.id == oldID){
					setSelected(new PropertyWrapper(def));
					updating = false;
					return;
				}
			}
		} else {
			selectDefault();
		}
		updating = false;
	}
	
	@Override
	public void setSelected(SelectWrapper item) {
//		System.out.println("set selection to: "+item);
		SelectWrapper selected = getSelected();
		if (selected != null && item != null && selected.equals(item))
			return;
		if (selected != null && selected instanceof PropertyWrapper)
			removeBlackList(((PropertyWrapper)selected).def, false);
		if (item instanceof PropertyWrapper){
			addBlackList(((PropertyWrapper)item).def, true);
			System.out.println(" -added to blacklist");
		}
		super.setSelected(item);
//		selectionChanged();
	}
	
	@Override
	public void dispose() {
		Main.main.eventManager.unregisterListener(this, PropertiesChangedEvent.class);
	}
	
	public PropertyDefinition getSelectedDefinition(){
		if (getSelected() != null && getSelected() instanceof PropertyWrapper){
			for (PropertyDefinition def : this.definitions){
				if (def.id.equals(((PropertyWrapper)getSelected()).def.id))
					return def;
			}
		}
		return null;
	}
	
	public void addBlackList(PropertyDefinition item, boolean update){
		if (item == null || group == null)
			return;
		group.setBlacklisted(item.id, update);
	}
	
	public void removeBlackList(PropertyDefinition item, boolean update){
		if (item == null || group == null)
			return;
		group.removeBlacklist(item.id, update);
	}
	
	public void clearBlackList(boolean update){
		if (group == null)
			return;
		group.clearBlacklist(update);
	}

	public void selectByID(Integer condition) {
		for (PropertyDefinition def : definitions){
			if (def.id == condition){
				try {
					setSelectedIndex(getIndex(def));
				} catch (Exception e){
					e.printStackTrace();
				}
				return;
			}
		}
	}

	private int getIndex(PropertyDefinition def) throws Exception{
		int i = 0;
		for (SelectWrapper wrapper : getItems()){
			if (wrapper instanceof PropertyWrapper && ((PropertyWrapper)wrapper).def.id == def.id)
				return i;
			i++;
		}
		System.err.println("SelectBox: couldn't find the index of the definition with id="+def.id);
		throw new Exception();
	}

	public void selectDefault() {
		Iterator<CustomSelectionOption<? extends WorldgenSelectBox>> it = additionalOptions.iterator();
		if (!it.hasNext())
			return;
		CustomSelectionOption<? extends WorldgenSelectBox> element = it.next();
		while (it.hasNext()){
			CustomSelectionOption<? extends WorldgenSelectBox> element2 = it.next();
			if (element2.defaultOptionPriority > element.defaultOptionPriority){
				element = element2;
			}
		}
		if (element.defaultOptionPriority > 0){
			setSelected(element);
		}
	}
	
	@Override
	public void onEvent(PropertiesChangedEvent event) {
		updateProperties();
	}
}
