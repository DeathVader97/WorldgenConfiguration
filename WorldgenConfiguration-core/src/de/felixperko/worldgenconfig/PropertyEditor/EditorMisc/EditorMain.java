package de.felixperko.worldgenconfig.PropertyEditor.EditorMisc;

import java.io.File;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;

import de.felixperko.worldgenconfig.Communication.NonBlockingClient;

public class EditorMain extends ApplicationAdapter{

	public static EditorMain editorMain;
	
	public EditorStage stage;
	EditorInput input;
	
//	SocketClient communication = new SocketClient();
	NonBlockingClient com = new NonBlockingClient();
	
	public String project = null;
	int property;
	
	File file;
	
	boolean waitForMessage = true;
	
	@Override
	public void create() {
		
		editorMain = this;
		
//		communication.start();
		com.start();
		com.writeMessage("/request destination");
		
		while (true){
			try {
				readMessages();
				if (!waitForMessage)
					break;
				Thread.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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
		readMessages();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		input.processInput();
		stage.getCamera().update();
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	private void readMessages() {
		for (String msg : com.getMessages()){
			System.out.println("[C] recieved Message: "+msg);
			if (msg.equalsIgnoreCase("/close")){
				dispose();
			}
			if (waitForMessage == true && msg.startsWith("/process")){
				waitForMessage = false;
				String[] s = msg.substring(9).split(" ");
				project = s[0];
				property = Integer.parseInt(s[1]);
				FileHandle path = Gdx.files.external("AppData/Roaming/MinecraftWorldgen/"+project+"/temp/");
				file = path.child(property+".yml").file();
			}
		}
	}

	@Override
	public void dispose() {
		stage.dispose();
		super.dispose();
		System.exit(0);
	}
}