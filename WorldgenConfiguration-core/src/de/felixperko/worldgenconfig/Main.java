package de.felixperko.worldgenconfig;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.kotcrab.vis.ui.VisUI;

import de.felixperko.worldgenconfig.PropertyEditor.EditorMain;

public class Main extends ApplicationAdapter {
	Stage stage;
	public ArrayList<Process> subProcesses = new ArrayList<>();
	public ArrayList<OutputStreamWriter> writers = new ArrayList<>();
	public static TickThread tickThread = new TickThread();
	public static ArrayList<EditorMain> editorMains = new ArrayList<>();
	public static boolean isRunning = true;
	
	@Override
	public void create () {
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
		for (OutputStreamWriter writer : writers){
			try {
				System.out.println("write exit...");
				writer.write("exit");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		VisUI.dispose();
		stage.dispose();
	}
	
	public void addSubProcess(Process process){
		subProcesses.add(process);
		OutputStreamWriter writer = new OutputStreamWriter(process.getOutputStream());
		writers.add(writer);
		try {
			writer.write("connected.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
