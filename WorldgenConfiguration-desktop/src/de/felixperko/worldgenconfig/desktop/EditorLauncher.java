package de.felixperko.worldgenconfig.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.felixperko.worldgenconfig.PropertyEditor.EditorMisc.EditorMain;

public class EditorLauncher {
	public static void main(String[] args) {
	    final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	    config.title = "Game";
		config.width = 800;
		config.height = 500; 
	    new LwjglApplication(new EditorMain(), config);  
	}
}
