package de.felixperko.worldgenconfig.MainMisc;

import java.io.FileFilter;
import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.runtime.spriter.File;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.widget.CollapsibleWidget;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuBar.MenuBarListener;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisSlider;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;

import de.felixperko.worldgen.Generation.Misc.Parameters;
import de.felixperko.worldgen.Generation.Misc.Selector;
import de.felixperko.worldgen.Generation.Misc.TerrainType;
import de.felixperko.worldgenconfig.GUI.DisplaySettingsManager;
import de.felixperko.worldgenconfig.GUI.MenuGUI.MenuActions;
import de.felixperko.worldgenconfig.GUI.PropertyGUI.ChangedPropertiesEvent;
import de.felixperko.worldgenconfig.GUI.PropertyGUI.InvalidPropertyException;
import de.felixperko.worldgenconfig.GUI.PropertyGUI.PropertySetupManager;
import de.felixperko.worldgenconfig.GUI.TypeGUI.TypeSetupManager;
import de.felixperko.worldgenconfig.GUI.Util.PropertySelectBox;
import de.felixperko.worldgenconfig.Generation.ImageGeneration.ImageManager;
import de.felixperko.worldgenconfig.Generation.ImageGeneration.WorldgenImage;

public class MainStage extends Stage{
	
//	SpriteBatch batch;
	
	public MainStage(Viewport vp, SpriteBatch b) {
		super(vp, b);
	}
	
	public VisTable mainTable;
	VisTable rightTable;
	
	VisScrollPane scrollPane;
	
	public ArrayList<WorldgenImage> images = new ArrayList<>();
	
	VisSlider slider = new VisSlider(0, 1, 0.025f, false);
	VisLabel label = new VisLabel(slider.getValue()+"");
	int w = Gdx.graphics.getWidth()-300;
	int h = Gdx.graphics.getHeight();
	
	public ImageManager imageManager;
	
	public PropertySetupManager propertySetupManager;
	public TypeSetupManager typeSetupManager;
	
	public DisplaySettingsManager displaySettingsManager;
	
	ColorPicker colorPicker;
	Image colorPickerImage;
	
	MainStage thisStage = this;
	ArrayList<TerrainType> types = new ArrayList<>();
	
//	{
//		types.add(new TerrainType("", new Selector(2).setCondition(0, 0.0, 0.25), null, new java.awt.Color(0f,0f,1f)));
//		types.add(new TerrainType("", new Selector(2).setCondition(0, 0.25, 1.0).setFeature(1, -1, 0), null, new java.awt.Color(0f,1f,0f)));
//		types.add(new TerrainType("", new Selector(2).setCondition(0, 0.25, 1.0).setFeature(1, 0, 1), null, new java.awt.Color(1f,0f,0f)));
//		
//		slider.addListener(new ChangeListener() {
//			@Override
//			public void changed(ChangeEvent event, Actor actor) {
//				label.setText(slider.getValue()+"");
//			}
//		});
//	}
	static int propertyCounter = 1;
	
	public void init(){
		Gdx.input.setInputProcessor(this);
		
		mainTable = new VisTable(true);
		rightTable = new VisTable(true);
		
		addActor(mainTable);

		scrollPane = new VisScrollPane(null);
		scrollPane.setFillParent(true);
		
		mainTable.align(Align.top);
		mainTable.setFillParent(true);
		imageManager = new ImageManager(this);
		
		setupMenus();
		setupProperties();
		setupDrawOptions();
		setupTypes();
	}

	private void setupMenus() {
		VisTable FranzPeter = new VisTable();
		MenuBar bar = new MenuBar();
		
		Menu menu = new Menu("Menu");
		menu.top().left();
		FranzPeter.add(bar.getTable());
		bar.addMenu(menu);
		PopupMenu openSubMenu = new PopupMenu();
		PopupMenu exportSubMenu = new PopupMenu();
		MenuItem newProject = new MenuItem("New Project").setShortcut(Keys.CONTROL_LEFT, Keys.N);
		MenuItem openProject = new MenuItem("Open Project...").setShortcut(Keys.CONTROL_LEFT, Keys.O);
		MenuItem importProject = new MenuItem("Import Project").setShortcut(Keys.CONTROL_LEFT, Keys.I);
		MenuItem exportProject = new MenuItem("Export Project...").setShortcut(Keys.CONTROL_LEFT, Keys.E);
		MenuItem options = new MenuItem("Config Options").setShortcut(Keys.ESCAPE);
		bar.setMenuListener(new MenuBarListener() {
			@Override
			public void menuOpened(Menu menu) {
				openSubMenu.clearChildren();
				exportSubMenu.clearChildren();
				for (FileHandle fileHandle : Main.main.workDirectory.list()){
					String name = fileHandle.name();
					if (!name.equals(Main.main.currentProject)){
						openSubMenu.addItem(new MenuItem(name));
					}
					exportSubMenu.addItem(new MenuItem(name));
				}
				openProject.setDisabled(!openSubMenu.hasChildren());
				exportProject.setDisabled(!exportSubMenu.hasChildren());
			}
			@Override
			public void menuClosed(Menu menu) {
			}
		});
		
		newProject.addListener(new ChangeListener() {@Override public void changed(ChangeEvent event, Actor actor) {MenuActions.newProject();}});
		
		menu.addItem(newProject);
		menu.addItem(openProject);
		openProject.setSubMenu(openSubMenu);
		menu.addItem(importProject);
		menu.addItem(exportProject);
		exportProject.setSubMenu(exportSubMenu);
		menu.addItem(options);
		
		mainTable.add(FranzPeter).left().row();
	}

	private void setupProperties() {
		mainTable.add(imageManager).expand().fill();
		scrollPane.debug();
		mainTable.add(scrollPane).prefHeight(Gdx.graphics.getHeight()).prefWidth(300);
//		scrollPane.setFillParent(true);
		
		final VisTable propertiesTable = new VisTable(true);
		final CollapsibleWidget propertiesWidget = new CollapsibleWidget(propertiesTable);
		propertiesWidget.setCollapsed(true);
		final VisTextButton add = new VisTextButton("+");
		add.setVisible(false);
		add.addListener(new ChangeListener(){
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				try {
					propertySetupManager.addPropertyBuilder();
				} catch (InvalidPropertyException e) {
					e.printStackTrace();
				}
				add.fire(new ChangedPropertiesEvent(propertySetupManager.propertyBuilders));
			}
		});
		VisCheckBox expandPropertiesCheckbox = new VisCheckBox("Properties");
		expandPropertiesCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				propertiesWidget.setCollapsed(!propertiesWidget.isCollapsed(), false);
				add.setVisible(!add.isVisible());
			}
		});
		propertySetupManager = new PropertySetupManager(this, propertiesTable);
		
		rightTable.setFillParent(true);
		rightTable.add(expandPropertiesCheckbox).align(Align.left).row();
		rightTable.add(add).align(Align.left).row();
		rightTable.add(propertiesWidget).align(Align.left).row();
		rightTable.align(Align.top | Align.left);
		
		propertiesTable.align(Align.top | Align.left);
		
		scrollPane.setFillParent(true);
		scrollPane.setWidget(rightTable);
		
		imageManager.update();
	}

	private void setupDrawOptions() {
		final VisTable expandDrawOptionsTable = new VisTable(true);
		final CollapsibleWidget expandDrawOptionsWidget = new CollapsibleWidget(expandDrawOptionsTable);
		expandDrawOptionsWidget.setCollapsed(true);
		VisCheckBox expandDrawOptionsCheckbox = new VisCheckBox("Draw options");
		expandDrawOptionsCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				expandDrawOptionsWidget.setCollapsed(!expandDrawOptionsWidget.isCollapsed(), false);
//				add.setVisible(!add.isVisible());
			}
		});
		
		VisLabel displayTypeLabel = new VisLabel("Display Type: ");
		SelectBox<String> selectDisplayType = new SelectBox<String>(VisUI.getSkin());
		expandDrawOptionsTable.add(displayTypeLabel).align(Align.left);
		expandDrawOptionsTable.add(selectDisplayType).align(Align.left).row();

		VisLabel optionLabel = new VisLabel("Display Element: ");
		PropertySelectBox selectDisplayElement = new PropertySelectBox(null, null);
		expandDrawOptionsTable.add(optionLabel).align(Align.left);
		expandDrawOptionsTable.add(selectDisplayElement).align(Align.left).row();
		
		VisValidatableTextField valueColor1Field = new VisValidatableTextField("0");
		VisValidatableTextField valueColor2Field = new VisValidatableTextField("1");
		VisTextButton color1Button = new VisTextButton("change lower");
		VisTextButton color2Button = new VisTextButton("change higher");
		final Drawable white = VisUI.getSkin().getDrawable("white");
		final Image color1Image = new Image(white);
		final Image color2Image = new Image(white);
		color1Image.setColor(Color.BLACK);
		
		valueColor1Field.addValidator(new InputValidator(){
			@Override
			public boolean validateInput(String input) {
				try {
					return Double.parseDouble(valueColor1Field.getText()) <= Double.parseDouble(valueColor2Field.getText());
				} catch (NumberFormatException e){
					return false;
				}
			}
		});
		valueColor2Field.addValidator(new InputValidator(){
			@Override
			public boolean validateInput(String input) {
				try {
					return Double.parseDouble(valueColor2Field.getText()) >= Double.parseDouble(valueColor1Field.getText());
				} catch (NumberFormatException e){
					return false;
				}
			}
		});
		
		color1Button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				colorPicker.setColor(color1Image.getColor());
				thisStage.addActor(colorPicker);
				colorPicker.fadeIn();
				color1Button.setDisabled(true);
				color2Button.setDisabled(true);
				colorPickerImage = color1Image;
			}
		});
		color2Button.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (colorPicker.hasParent())
					return;
				colorPicker.setColor(color2Image.getColor());
				thisStage.addActor(colorPicker);
				colorPicker.fadeIn();
				color1Button.setDisabled(true);
				color2Button.setDisabled(true);
				colorPickerImage = color2Image;
			}
		});
		colorPicker = new ColorPicker(new ColorPickerAdapter(){
			@Override
			public void finished(Color newColor) {
				colorPickerImage.setColor(newColor);
				color1Button.setDisabled(false);
				color2Button.setDisabled(false);
			}
			@Override
			public void canceled(Color oldColor) {
				color1Button.setDisabled(false);
				color2Button.setDisabled(false);
			}
		});
		colorPicker.setAllowAlphaEdit(false);
		
//		expandDrawOptionsTable.debug();
		expandDrawOptionsTable.add(color1Button).left();
		expandDrawOptionsTable.add(valueColor1Field);
		expandDrawOptionsTable.add(color1Image).size(16).left().row();
		expandDrawOptionsTable.add(color2Button).left();
		expandDrawOptionsTable.add(valueColor2Field);
		expandDrawOptionsTable.add(color2Image).size(16).left().row();
		
		
		displaySettingsManager = new DisplaySettingsManager(this, selectDisplayType, selectDisplayElement);
		
		rightTable.add(expandDrawOptionsCheckbox).left().row();
		rightTable.add(expandDrawOptionsWidget).left().row();
	}

	private void setupTypes() {
		final VisTable expandTypesTable = new VisTable(true);
		final CollapsibleWidget expandTypesWidget = new CollapsibleWidget(expandTypesTable);
		expandTypesWidget.setCollapsed(true);
		VisCheckBox expandTypesCheckbox = new VisCheckBox("Types");
		expandTypesCheckbox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				expandTypesWidget.setCollapsed(!expandTypesWidget.isCollapsed(), false);
//				add.setVisible(!add.isVisible());
			}
		});
		
		typeSetupManager = new TypeSetupManager(this, expandTypesTable);
		
		rightTable.add(expandTypesCheckbox).left().row();
		rightTable.add(expandTypesWidget).left().row();
	}

	@Override
	public void draw() {
		super.draw();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	public void setParameters(Parameters parameters, boolean updateValues) {
		imageManager.genManager.setParameters(parameters, updateValues);
		imageManager.refreshImages();
//		lastComponent = component;
	}

//	public void redrawTexture() {
//		array = new Float[w][h];
//		textureImage.setDrawable(new SpriteDrawable(new Sprite(new Texture(generateTestPixmap(w, h, slider.getValue())))));
//	}
}