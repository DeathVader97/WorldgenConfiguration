package de.felixperko.worldgenconfig.MainMisc;

import java.util.ArrayList;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.kotcrab.vis.ui.VisUI;

import de.felixperko.worldgenconfig.Communication.SocketServer;
import de.felixperko.worldgenconfig.PropertyEditor.EditorMisc.EditorMain;

public class Main extends ApplicationAdapter {
	public MainStage stage;
	public ArrayList<Process> subProcesses = new ArrayList<>();
	public static TickThread tickThread = new TickThread();
	public static ArrayList<EditorMain> editorMains = new ArrayList<>();
	SocketServer communication = new SocketServer();
	public static Main main;
	
	public Yaml yaml;
	{
		final DumperOptions options = new DumperOptions();
		options.setDefaultFlowStyle(DumperOptions.FlowStyle.FLOW);
		options.setPrettyFlow(true);
		yaml = new Yaml(options);
	}
	
	@Override
	public void create () {
		main = this;
		communication.start();
		tickThread.start();
		VisUI.load();
		stage = new MainStage(new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()), new SpriteBatch());
		((MainStage)stage).init();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}
	
	@Override
	public void dispose () {
		communication.addOutput("/close");
		VisUI.dispose();
		stage.dispose();
	}
	
	public void addSubProcess(Process process){
		subProcesses.add(process);
	}
}