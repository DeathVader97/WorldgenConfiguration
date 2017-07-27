package de.felixperko.worldgenconfig.GUI.TypeGUI;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.felixperko.worldgenconfig.GUI.Util.GuiList;

public class StatementList extends GuiList{
	
	TypeConfigurationWindow window;

	public StatementList(TypeConfigurationWindow window, Stage stage, Table table) {
		super(stage, table);
		this.window = window;
		addAddButton(Statement.class);
	}
	
	@Override
	public void refresh() {
		super.refresh();
		table.pack();
	}
	
}