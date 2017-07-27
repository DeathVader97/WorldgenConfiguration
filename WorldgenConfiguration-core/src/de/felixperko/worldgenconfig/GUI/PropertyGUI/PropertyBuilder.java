package de.felixperko.worldgenconfig.GUI.PropertyGUI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;

import de.felixperko.worldgenconfig.Generation.GenMisc.Parameters;
import de.felixperko.worldgenconfig.Generation.GenMisc.PropertyDefinition;
import de.felixperko.worldgenconfig.Generation.GenMisc.SelectionRuleset;
import de.felixperko.worldgenconfig.Generation.GenPath.Components.Component;
import de.felixperko.worldgenconfig.MainMisc.Main;
import de.felixperko.worldgenconfig.MainMisc.MainStage;
import de.felixperko.worldgenconfig.PropertyEditor.EditorMisc.EditorData;

public class PropertyBuilder {
	
	final PropertyBuilder thisBuilder = this;
	
	public int id;
	int pos;
	public Component endComponent;
	File file;
	
	public VisLabel nameLabel;
	VisTextButton editButton;
	VisTextButton shiftUpButton;
	VisTextButton shiftDownButton;
	VisTextButton removeButton;
	
	VisTextField editNameField;
	
	Separator separator;
	
	PropertySetupManager manager;
	VisTable table;
	
	public PropertyBuilder(final PropertySetupManager manager, PropertyDefinition def){
		init(manager, id, def.name);
		try {
			loadComponent();
		} catch (InvalidPropertyException e) {
			e.printStackTrace();
		}
	}
	
	public PropertyBuilder(final PropertySetupManager manager, int id){
		init(manager, id, "property "+(id+1));
	}
	
	private void init(final PropertySetupManager manager, int id, String name){
		this.manager = manager;
		this.id = id;
		this.pos = manager.propertyBuilders.size();
		this.table = new VisTable();
//		FileHandle path = Gdx.files.local("projects" + File.separator + Main.main.currentProject + File.separator + "properties"
//				+ File.separator + id + File.separator);
//		file = path.child("config.yml").file();
		FileHandle path = Main.main.projectDirectory.child("properties");
		path.mkdirs();
		file = path.child(id+".yml").file();
		
		separator = new Separator();

		nameLabel = new VisLabel(name);
		nameLabel.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				nameLabel.setVisible(false);
				editNameField = new VisTextField(nameLabel.getName());
				remove();
				table.addActor(editNameField);
				addToGroup();
				nameLabel.fire(new ChangedPropertiesEvent(manager.propertyBuilders));
			}
		});
		
		editButton = new VisTextButton("*");
		editButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				MainStage stage = manager.stage;
				Vector2 pos = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
				PropertyEditWindow window = new PropertyEditWindow(manager, thisBuilder);
				window.pack();
				window.setPosition(pos.x, pos.y, Align.left);
				stage.addActor(window);
			}
		});
		
		removeButton = new VisTextButton("-");
		removeButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				try {
					manager.removePropertyBuilder(thisBuilder);
				} catch (InvalidPropertyException e) {
					e.printStackTrace();
				}
			}
		});
		
		shiftUpButton = new VisTextButton("^");
		shiftUpButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				manager.moveUp(thisBuilder);
			}
		});
		
		shiftDownButton = new VisTextButton("v");
		shiftDownButton.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				manager.moveDown(thisBuilder);
			}
		});
//		table.debug();
		
		manager.flowGroup.addActor(table);
		
		addToGroup();
	}
	
	public void addToGroup(){
		table.clear();
		table.add(nameLabel).expandX().fillX().spaceRight(5).minWidth(200);
		table.add(editButton).spaceRight(5);
		table.add(removeButton).spaceRight(5);
		table.add(shiftUpButton).spaceRight(5);
		table.add(shiftDownButton);
		table.row();
		table.add(separator).minWidth(300).maxHeight(2).colspan(5).padTop(2).padBottom(2);
		manager.flowGroup.addActor(table);
	}
	
	public void remove(){
		nameLabel.remove();
		editButton.remove();
		removeButton.remove();
		shiftDownButton.remove();
		shiftUpButton.remove();
		editNameField.remove();
	}
	
	public void update(int newPos) {
		this.pos = newPos;
		if (pos == 0){
			shiftUpButton.setDisabled(true);
		} else {
			shiftUpButton.setDisabled(false);
		}
		
		if (pos == manager.propertyBuilders.size()-1) {
			shiftDownButton.setDisabled(true);
			separator.setVisible(false);
		} else {
			shiftDownButton.setDisabled(false);
			separator.setVisible(true);
		}
	}
	
	public void loadComponent() throws InvalidPropertyException{
		try {
			endComponent = ((EditorData)Main.main.yaml.load(new FileInputStream(file))).getEndComponent();
			manager.pushProperties();
		} catch (FileNotFoundException e){
			throw new InvalidPropertyException(FailureReason.FILE_NOT_FOUND);
		}
	}
	
	public boolean applyComponent() throws InvalidPropertyException{
//		if (endComponent == null)
//			return false;
		if (endComponent == null)
			loadComponent();
		if (endComponent == null)
			throw new InvalidPropertyException(FailureReason.BAD_CONFIG);
//			System.err.println("unable to read configuration for "+nameLabel.getText().toString());
		SelectionRuleset ruleset = new SelectionRuleset() {
			@Override
			protected int selectValue() {
				int v = (int)((getProperty(0)*0.5+0.5)*256);
				return ((v&0x0ff)<<16)|((v&0x0ff)<<8)|(v&0x0ff);
//				return new Color(v,v,v,1f).toIntBits();
			}
		};
		Parameters params = new Parameters(ruleset, new PropertyDefinition(id, nameLabel.getText().toString(), endComponent));
		manager.stage.setParameters(params, true);
		return endComponent != null;
	}

	public PropertyDefinition build() throws InvalidPropertyException{
		if (endComponent == null)
			loadComponent();
		if (endComponent == null){
			setInvalid(true);
			System.err.println("couldn't apply component of property "+nameLabel.getText().toString());
		} else
			setInvalid(false);
		return new PropertyDefinition(id, nameLabel.getText().toString(), endComponent);
	}

	
	boolean prevInvalid = false;
	private void setInvalid(boolean invalid) {
		if (invalid == prevInvalid)
			return;
		prevInvalid = invalid;
		if (invalid)
			nameLabel.setColor(Color.RED);
		else
			nameLabel.setColor(Color.WHITE);
	}
}
