package de.felixperko.worldgenconfig.desktop;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.felixperko.worldgenconfig.Main;
import de.felixperko.worldgenconfig.PropertyEditor.EditorMain;

public class DesktopLauncher {
	
	public static Main main;
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1200;
		config.height = 900;
		main = new Main();
		new LwjglApplication(main, config);
		
		try {
			exec(EditorLauncher.class);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static int exec(Class klass) throws IOException, InterruptedException {
		String javaHome = System.getProperty("java.home");
		String javaBin = javaHome +
		File.separator + "bin" +
		File.separator + "java";
		String classpath = System.getProperty("java.class.path");
		String className = klass.getCanonicalName();
		
		ProcessBuilder builder = new ProcessBuilder(
		javaBin, "-cp", classpath, className);
		builder.redirectOutput(Redirect.INHERIT);
		builder.redirectError(Redirect.INHERIT);
		
		Process process = builder.start();
		main.addSubProcess(process);
		process.waitFor();
		return process.exitValue();
	}
}
