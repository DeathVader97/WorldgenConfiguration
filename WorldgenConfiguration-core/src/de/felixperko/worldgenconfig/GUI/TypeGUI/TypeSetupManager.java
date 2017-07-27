package de.felixperko.worldgenconfig.GUI.TypeGUI;

import com.kotcrab.vis.ui.widget.VisTable;

import de.felixperko.worldgenconfig.GUI.Util.GuiList;
import de.felixperko.worldgenconfig.Generation.GenMisc.TerrainType;
import de.felixperko.worldgenconfig.MainMisc.Main;
import de.felixperko.worldgenconfig.MainMisc.MainStage;

public class TypeSetupManager extends GuiList{
	
	TypeSetupManager thisManager = this;
	
	public TypeSetupManager(MainStage stage, VisTable table){
		super(stage,table);
		
		addAddButton(TypeBuilder.class);
		addLoadedTypes();
	}

	private void addLoadedTypes() {
		for (TerrainType type : Main.main.currentWorldConfig.types){
			addElement(new TypeBuilder(this, type));
		}
	}

	@Override
	public void refresh() {
		super.refresh();
		this.table.add(addBtn).row();
	}
}
