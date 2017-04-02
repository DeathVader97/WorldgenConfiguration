package de.felixperko.worldgenconfig.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.felixperko.worldgenconfig.PropertyEditor.EditorMain;

public class EditorLauncher {
	public static void main(String[] args) {
	    final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
	    config.title = "Game";
		config.width = 1200;
		config.height = 900; 
	    new LwjglApplication(new EditorMain(), config);  
	}
}
