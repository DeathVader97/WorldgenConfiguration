package de.felixperko.worldgenconfig.PropertyEditor.EditorMisc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import de.felixperko.worldgenconfig.Generation.GenPath.Components.CombineAddComponent;
import de.felixperko.worldgenconfig.Generation.GenPath.Components.Component;
import de.felixperko.worldgenconfig.Generation.GenPath.Components.ModifierComponent;
import de.felixperko.worldgenconfig.Generation.GenPath.Components.NoiseGeneratorComponent;
import de.felixperko.worldgenconfig.Generation.GenPath.Misc.GenerationParameterSupply;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.Block;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.ComponentBlock;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.Connection;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.Connector;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.EndBlock;

public class EditorStage extends Stage {
	
	public ArrayList<Block> blocks = new ArrayList<>();
	public ArrayList<Connection> connections = new ArrayList<>();
	public EndBlock endPoint;
	PopupMenu menu;

	Yaml yaml;
	{
		final DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
		options.setPrettyFlow(true);
		yaml = new Yaml(options);
	}
	
	ShapeRenderer renderer = new ShapeRenderer();
	
	VisTextButton updateButton;
	
	EditorMain editorMain;
	
	public EditorStage(EditorMain editorMain) {
		this.editorMain = editorMain;
	}

	@SuppressWarnings("unchecked")
	public void init() {
		VisUI.load();
		endPoint = new EndBlock(this, (int) (Gdx.graphics.getWidth()*0.9f), Gdx.graphics.getHeight()/2);
		addActor(endPoint);
		blocks.add(endPoint);
		
		VisTable table = new VisTable();
		table.align(Align.right | Align.top);
		
		final EditorStage stage = this;
		updateButton = new VisTextButton("Update"){
			@Override
			public void act(float delta) {
				setPosition(stage.getCamera().position.x+stage.getWidth()/2-getWidth(), stage.getCamera().position.y+stage.getHeight()/2-getHeight());
				setDisabled(!endPoint.isComplete());
				super.act(delta);
			}
		};
		updateButton.addListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GenerationParameterSupply supply = new GenerationParameterSupply();
				supply.setPos(321, 941, null);
				try {
					ArrayList<Component> components = new ArrayList<>();
					for (Block block : stage.blocks){
						if (block instanceof ComponentBlock)
							components.add(((ComponentBlock)block).getComponent());
					}
					yaml.dump(new EditorData(stage) ,new FileWriter("testfile.yml"));
					editorMain.communication.addOutput("updatedProperty");
				} catch (IOException e) {
					editorMain.communication.addOutput("error while updating Property");
					e.printStackTrace();
				}
			}
		});
		
		File testFile = new File("testfile.yml");
		if (testFile.exists()){
			try {
				((EditorData) yaml.load(new FileInputStream(testFile))).importToStage(stage);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		addActor(table);
		table.setFillParent(true);
		table.add(updateButton);
		updateButton.align(Align.right | Align.top);
	}

	void addBlock(ComponentBlock block) {
		addActor(block);
		blocks.add(block);
		block.init();
	}

	public void createBlock(float x, float y, Class<? extends Component> componentClass) {
		ComponentBlock block = new ComponentBlock(this, x, y, componentClass);
		addBlock(block);
	}
	
	public void addConnection(Connection c){
		if (!connections.contains(c)){
			connections.add(c);
		}
	}
	
	public void removeConnection(Connection c){
		connections.remove(c);
	}

	public Connector getConnector(Vector2 mousePos, boolean output) {
		mousePos = screenToStageCoordinates(mousePos);
		for (Block block : blocks){
			if (output && block instanceof ComponentBlock){
				ComponentBlock cBlock = (ComponentBlock)block;
				Vector2 d = cBlock.output.getPos().sub(mousePos);
				if (d.len() <= 5)
					return cBlock.output;
			} else {
				for (Connector input : block.getInputs()){
					Vector2 d = input.getPos().sub(mousePos);
					if (d.len() <= 5)
						return input;
				}
			}
		}
		return null;
	}

	public Connection getConnection(Vector2 mousePos) {
		mousePos = screenToStageCoordinates(mousePos);
		for (Connection c : connections){
			if (c.distance(mousePos) <= 5)
				return c;
		}
		return null;
	}
	
	public Block getBlockAtMousePos(){
		Vector2 pos = getMouseWorldPos();
		for (Block block : blocks){
			if (pos.x >= block.getX() && pos.x <= block.getX()+block.getWidth() && pos.y >= block.getY() && pos.y <= block.getY()+block.getHeight())
				return block;
		}
		return null;
	}
	
	@Override
	public void draw() {
		
//		if (getBatch().isDrawing())
//			getBatch().end();
		renderer.begin(ShapeType.Line);
		renderer.setProjectionMatrix(getCamera().combined);
		
		renderer.setColor(Color.WHITE);
		for (Connection c : connections){
			if (c.isSelected())
				renderer.setColor(Color.GREEN);
			renderer.line(c.getPos1(), c.getPos2());
			if (c.isSelected())
				renderer.setColor(Color.WHITE);
		}
		
		renderer.end();
//		getBatch().begin();
		
		super.draw();
	}

	public void showPopupMenu() {
		if (menu != null)
			menu.remove();
		
		menu = new PopupMenu();
		addActor(menu);
		menu.addItem(new MenuItem("Noise", new ChangeListener() {
			@Override public void changed(ChangeEvent arg0, Actor arg1) {
				createBlock(menu.getX(), menu.getY(), NoiseGeneratorComponent.class);
		}}));
		menu.addItem(new MenuItem("Modifier", new ChangeListener() {
			@Override public void changed(ChangeEvent arg0, Actor arg1) {
				createBlock(menu.getX(), menu.getY(), ModifierComponent.class);
		}}));
		menu.addItem(new MenuItem("Combine", new ChangeListener() {
			@Override public void changed(ChangeEvent arg0, Actor arg1) {
				createBlock(menu.getX(), menu.getY(), CombineAddComponent.class);
		}}));
		
		Vector2 worldPos = getMouseWorldPos();
		menu.setPosition(worldPos.x, worldPos.y);
	}

	public Vector2 getMouseWorldPos() {
		return screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
	}
}