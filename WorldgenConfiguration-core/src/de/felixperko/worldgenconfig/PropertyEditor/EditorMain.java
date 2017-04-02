package de.felixperko.worldgenconfig.PropertyEditor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;

import de.felixperko.worldgenconfig.Main;

public class EditorMain extends ApplicationAdapter{
	
	public EditorStage stage;
	public EditorMain editorMain;
	EditorInput input;
	
	InputThread inputThread = new InputThread();
	
	@Override
	public void create() {
		
		inputThread.start();
		
		editorMain = this;
		Main.editorMains.add(this);
		stage = new EditorStage();
		stage.init();
		InputMultiplexer mp = new InputMultiplexer();
		input = new EditorInput(this);
		mp.addProcessor(input);
		mp.addProcessor(stage);
		Gdx.input.setInputProcessor(mp);
	}
	
	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		input.processInput();
		stage.getCamera().update();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void dispose() {
		stage.dispose();
		super.dispose();
		System.exit(0);
	}
}

class InputThread extends Thread{
	@Override
	public void run() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		while (true){
			try {
				String line = reader.readLine();
				System.err.println(line);
				if (line == null)
//					continue;
//				if (line.equalsIgnoreCase("stop"))
					System.exit(0);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}