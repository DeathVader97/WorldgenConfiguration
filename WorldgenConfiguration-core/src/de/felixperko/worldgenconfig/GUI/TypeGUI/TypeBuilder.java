package de.felixperko.worldgenconfig.GUI.TypeGUI;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.color.ColorPicker;

import de.felixperko.worldgenconfig.GUI.Util.GuiList;
import de.felixperko.worldgenconfig.GUI.Util.GuiListElement;
import de.felixperko.worldgenconfig.Generation.GenMisc.Selector;
import de.felixperko.worldgenconfig.Generation.GenMisc.TerrainType;
import de.felixperko.worldgenconfig.MainMisc.Main;

public class TypeBuilder extends GuiListElement{
	static Drawable white = VisUI.getSkin().getDrawable("white");
	
	TypeSetupManager manager;
	
	ArrayList<TypeStatementBuilder> conditionBuilders = new ArrayList<>();
	ArrayList<TypeStatementBuilder> featureBuilders = new ArrayList<>();
	
	VisLabel label;
	VisTextButton editBtn;
	Image colorImg;
	
	Separator separator;
	
	int id;
	String name;
	
	static ColorPicker picker;
	Color color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 1f);
	
	static int ID_COUNTER;
	
	public TypeBuilder(GuiList manager){
		
		super(manager);
		this.manager = (TypeSetupManager)manager;
		
		this.id = ID_COUNTER++;
		name = "Type "+(id+1);
		init();
	}

	public TypeBuilder(TypeSetupManager manager, TerrainType type) {
		
		super(manager);
		this.manager = manager;
		
		id = type.id;
		if (id >= ID_COUNTER)
			ID_COUNTER = id+1;
		name = type.name;
		
		Selector sel = type.selector;
		for (int i = 0 ; i < sel.conditionMax.length ; i++){
			if (sel.hasCondition[i] != null && sel.hasCondition[i]){
				conditionBuilders.add(new TypeStatementBuilder(i, sel.conditionMin[i], sel.conditionMax[i]));
			}
			if (sel.enabled[i] != null && sel.enabled[i]){
				featureBuilders.add(new TypeStatementBuilder(i, sel.definiteMin[i], sel.definiteMax[i]));
			}
		}
		
		init();
	}
	
	private void init(){
		label = new VisLabel(name);
		editBtn = new VisTextButton("*", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				manager.stage.addActor(new TypeConfigurationWindow((TypeBuilder)thisElement, manager.stage));
			}
		});
		
		colorImg = new Image(white){
			@Override
			public float getPrefWidth() {
				return 16;
			}
			@Override
			public float getPrefHeight() {
				return 16;
			}
		};
		colorImg.setColor(color);

		content.add(colorImg);
		content.add(label);
		content.add(editBtn);
		
		addRemoveButton();
		addShiftButtons();
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name){
		this.name = name;
		label.setText(name);
		manager.refresh();
	}

	public TerrainType build() {
		Selector sel = new Selector(Main.main.currentWorldConfig.getProperties().size());
		conditionBuilders.forEach(b -> sel.setCondition(b.condition, b.low, b.high));
		featureBuilders.forEach(b -> sel.setFeature(b.condition, b.low, b.high));
		return new TerrainType(name, sel, null, new java.awt.Color((int)(color.r*255),(int)(color.g*255),(int)(color.b*255)));
	}

	public void setColor(Color color) {
		this.color = color;
		this.colorImg.setColor(color);
	}
}