package de.felixperko.worldgenconfig.MainMisc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.github.czyzby.kiwi.util.tuple.immutable.Pair;
import com.kotcrab.vis.ui.VisUI;

import de.felixperko.worldgenconfig.Communication.NonBlockingServer;
import de.felixperko.worldgenconfig.GUI.PropertyGUI.InvalidPropertyException;
import de.felixperko.worldgenconfig.GUI.PropertyGUI.PropertyBuilder;
import de.felixperko.worldgenconfig.Generation.GenMisc.PropertyDefinition;
import de.felixperko.worldgenconfig.Generation.GenMisc.TerrainType;
import de.felixperko.worldgenconfig.Generation.GenMisc.WorldConfiguration;
import de.felixperko.worldgenconfig.MainMisc.Utilities.Events.PropertiesChangedEvent;
import de.felixperko.worldgenconfig.MainMisc.Utilities.Events.TypesChangedEvent;
import de.felixperko.worldgenconfig.MainMisc.Utilities.Events.EventSystem.EventManager;
import de.felixperko.worldgenconfig.MainMisc.Utilities.Various.Input;
import de.felixperko.worldgenconfig.MainMisc.Utilities.Various.InputGestureListener;

public class Main extends ApplicationAdapter {
	public MainStage stage;
	public ArrayList<Process> subProcesses = new ArrayList<>();
	public static TickThread tickThread = new TickThread();
	
	NonBlockingServer com;
	
	public static Main main;
	
	public FileHandle workDirectory;
	public FileHandle projectDirectory;
	
	public HashMap<String, Integer> editorUsedIDs = new HashMap<>();
	
	public String currentProject = "unnamed";
	public WorldConfiguration currentWorldConfig;
	public EventManager eventManager = new EventManager();
	
	public Yaml yaml;
	
	@Override
	public void create () {
		main = this;
		configureYaml();
		setupIO();
		com = new NonBlockingServer();
		com.start();
		tickThread.start();
		VisUI.load();
		stage = new MainStage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), new SpriteBatch());
		stage.init();
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, new Input(new InputGestureListener())));
	}

	private void setupIO() {
		workDirectory = Gdx.files.external("AppData/Roaming/MinecraftWorldgen");
		setProject("unnamed");
	}

	private void configureYaml() {
		final DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
		options.setPrettyFlow(true);
		yaml = new Yaml(options);
	}

	@Override
	public void render () {
		readMessages();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}
	
	private void readMessages() {
		for (Pair<String, String> pair : com.getMessages()){
			System.out.println("[S] read msg: "+pair);
			String sender = pair.getFirst();
			String msg = pair.getSecond();
			if (msg.equalsIgnoreCase("/request destination")){
				int id = stage.propertySetupManager.pendingPropertiesForEditor.poll().id;
				editorUsedIDs.put(sender, id);
				com.writeMessage(sender, "/process "+currentProject+" "+id);
			} else if (msg.equalsIgnoreCase("updatedProperty")){
				int id = editorUsedIDs.get(sender);
				PropertyBuilder builder = stage.propertySetupManager.getPropertyBuilder(id);
				try {
					builder.loadComponent();
					if (stage.displaySettingsManager.isSelected(id)){
						builder.applyComponent();
					}
				} catch (InvalidPropertyException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void dispose () {
		com.close();
		stage.imageManager.dispose();
		VisUI.dispose();
		stage.dispose();
	}
	
	public void addSubProcess(Process process){
		subProcesses.add(process);
	}
	
	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
		stage.getCamera().update();
		super.resize(width, height);
	}

	public void configBuildFailure(InvalidPropertyException e) {
		System.err.println("couldn't build config: "+e.reason);
	}
	
	boolean first = true;
	
	public void setProject(String projectName){
		ArrayList<PropertyDefinition> previousProperties = null;
		ArrayList<TerrainType> previousTypes = null;
		if (!first){
			previousProperties = new ArrayList<>(currentWorldConfig.properties);
			previousTypes = new ArrayList<>(currentWorldConfig.types);
		}
		
		currentProject = projectName;
		projectDirectory = workDirectory.child(currentProject);
		Gdx.graphics.setTitle(currentProject+" - WorldgenConfigurator");
		FileHandle worldConfigFile = projectDirectory.child("worldconfig.yml");
		try {
			currentWorldConfig = (WorldConfiguration)Main.main.yaml.load(new FileInputStream(worldConfigFile.file()));
		} catch (NullPointerException | FileNotFoundException e){
			System.out.println("couldn't load config, generating new...");
			currentWorldConfig = new WorldConfiguration();
		}
		if (!first){
			eventManager.fireEvent(new PropertiesChangedEvent(previousProperties, currentWorldConfig.properties));
			eventManager.fireEvent(new TypesChangedEvent(previousTypes, currentWorldConfig.types));
		} else
			first = false;
	}
	
}