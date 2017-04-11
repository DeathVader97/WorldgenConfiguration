package de.felixperko.worldgenconfig.PropertyEditor.EditorMisc;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class EditorLauncher {
	public static void main(String[] args) {
	    final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	    config.title = "Editor";
		config.width = 800;
		config.height = 500;
		config.samples = 1;
	    new LwjglApplication(new EditorMain(), config);  
	}

}
