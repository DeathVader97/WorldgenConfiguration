package de.felixperko.worldgenconfig.PropertyEditor.EditorMisc;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;

import de.felixperko.worldgenconfig.Communication.SocketClient;
import de.felixperko.worldgenconfig.MainMisc.Main;

public class EditorMain extends ApplicationAdapter{
	
	public EditorStage stage;
	public EditorMain editorMain;
	EditorInput input;
	
	SocketClient communication = new SocketClient();
	
	@Override
	public void create() {
		
		communication.start();
		
		editorMain = this;
		Main.editorMains.add(this);
		stage = new EditorStage(this);
		stage.init();
		InputMultiplexer mp = new InputMultiplexer();
		input = new EditorInput(this);
		mp.addProcessor(stage);
		mp.addProcessor(input);
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