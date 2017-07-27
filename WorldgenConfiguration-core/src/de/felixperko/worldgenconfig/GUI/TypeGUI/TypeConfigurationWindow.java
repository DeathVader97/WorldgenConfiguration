package de.felixperko.worldgenconfig.GUI.TypeGUI;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Disposable;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;

import de.felixperko.worldgenconfig.GUI.Util.GuiListElement;
import de.felixperko.worldgenconfig.Generation.GenMisc.TerrainType;
import de.felixperko.worldgenconfig.MainMisc.Main;

public class TypeConfigurationWindow extends VisWindow {

	static Drawable white = VisUI.getSkin().getDrawable("white");
	static ColorPicker colorPicker = new ColorPicker();
	
	Color color;
	
	TypeBuilder builder;
	
	VisLabel nameLabel;
	VisValidatableTextField editNameField;
	
	VisTextButton editColorButton;
	Image colorImage;
	
	VisLabel conditionLabel;
	StatementList conditionList;
	
	VisLabel featureLabel;
	StatementList featureList;
	
	VisWindow thisWindow = this;
	
	Stage stage;
	
	boolean doneBuilding = false;
	
	public TypeConfigurationWindow(TypeBuilder builder, Stage stage) {
		
		super("");
		this.stage = stage;
		centerWindow();
		this.builder = builder;
		
		addCloseButton();
		
		String name = builder.getName();
		nameLabel = new VisLabel("Name: ");
		editNameField = new VisValidatableTextField(name);
		editNameField.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				String newName = editNameField.getText();
				builder.setName(newName);
			}
		});
		
		addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (actor != thisWindow)
					return;
				System.out.println("Window closed?");
				ArrayList<Group> parentActors = new ArrayList<>();
				parentActors.add(thisWindow);
				while (!parentActors.isEmpty()){
					int pos = parentActors.size()-1;
					Actor parent = parentActors.remove(pos);
					if (parent instanceof Group){
						for (Actor a : ((Group)parent).getChildren()){
							if (a instanceof Group)
								parentActors.add((Group)a);
							if (a instanceof Disposable)
								((Disposable)a).dispose();
						}
					}
					if (parent instanceof Disposable)
						((Disposable)parent).dispose();
				}
			}
		});
		
		color = builder.color;
		editColorButton = new VisTextButton("Change Color", new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				colorPicker.setColor(color);
				colorPicker.setListener(new ColorPickerAdapter(){
					@Override
					public void changed(Color newColor) {
						color = newColor;
						colorImage.setColor(newColor);
					}
					@Override
					public void finished(Color newColor) {
						updateChanges();
					}
				});
				stage.addActor(colorPicker);
			}
		});
		colorImage = new Image(white);
		colorImage.setColor(color);
		
		VisTable scrollTable = new VisTable();
		ScrollPane pane = new ScrollPane(scrollTable);
		add(pane);
		
//		scrollTable.debug();
		scrollTable.setFillParent(true);
		scrollTable.add(nameLabel).padRight(5).padTop(10);
		scrollTable.add(editNameField).row();

		scrollTable.add(colorImage).padRight(5).padTop(3).size(16);
		scrollTable.add(editColorButton).row();
		
		conditionLabel = new VisLabel("Conditions");
		scrollTable.add(conditionLabel).colspan(2).left().padTop(10).row();
		conditionList = new StatementList(this, stage, scrollTable);
		for (TypeStatementBuilder sb : builder.conditionBuilders)
			conditionList.addElement(new Statement(conditionList, sb));
		
		featureLabel = new VisLabel("Features");
		scrollTable.add(featureLabel).colspan(2).left().padTop(10).row();
		featureList = new StatementList(this, stage, scrollTable);
		for (TypeStatementBuilder sb : builder.featureBuilders)
			featureList.addElement(new Statement(featureList, sb));
		
		left().top();
		setWidth(500);
		setHeight(500);
		
		doneBuilding = true;
	}

	public void updateChanges() {
		if (!doneBuilding)
			return;
		ArrayList<TypeStatementBuilder> conditions = new ArrayList<>();
		for (GuiListElement e : conditionList.elements){
			TypeStatementBuilder builder = ((Statement)e).getBuilder();
			if (builder != null){
				System.out.println("builder for: "+builder.condition);
				conditions.add(builder);
			}
		}
		builder.conditionBuilders = conditions;
		
		try {
			ArrayList<TypeStatementBuilder> features = new ArrayList<>();
			for (GuiListElement e : featureList.elements){
				TypeStatementBuilder builder = ((Statement)e).getBuilder();
				if (builder != null)
					features.add(builder);
			}
			builder.featureBuilders = features;
		} catch (Exception e){
			e.printStackTrace();
			return;
		}
		builder.setColor(color);
		
		ArrayList<TerrainType> types = new ArrayList<>();
		for (GuiListElement e : Main.main.stage.typeSetupManager.elements)
			types.add(((TypeBuilder)e).build());
		Main.main.currentWorldConfig.setTypes(types);
	}
	
	@Override
	protected void close() {
		ArrayList<Group> parentActors = new ArrayList<>();
		parentActors.add(thisWindow);
		while (!parentActors.isEmpty()){
			int pos = parentActors.size()-1;
			Actor parent = parentActors.remove(pos);
			if (parent instanceof Group){
				for (Actor a : ((Group)parent).getChildren()){
					if (a instanceof Group)
						parentActors.add((Group)a);
					if (a instanceof Disposable)
						((Disposable)a).dispose();
				}
			}
			if (parent instanceof Disposable)
				((Disposable)parent).dispose();
		}
		super.close();
	}
}