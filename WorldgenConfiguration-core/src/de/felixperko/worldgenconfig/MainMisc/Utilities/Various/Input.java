package de.felixperko.worldgenconfig.MainMisc.Utilities.Various;

import com.badlogic.gdx.input.GestureDetector;
import de.felixperko.worldgenconfig.GUI.Test.TestManager;
import de.felixperko.worldgenconfig.GUI.Test.TestWindow;
import de.felixperko.worldgenconfig.MainMisc.Main;
import de.felixperko.worldgenconfig.MainMisc.MainStage;

public class Input extends GestureDetector {
	
	public Input(InputGestureListener inputGestureListener) {
		super(inputGestureListener);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean scrolled(int amount) {
		stage.imageManager.zoomIn(amount*0.2f);
		return true;
	}
	
	@Override
	public boolean keyTyped(char character) {
		if (character == 't'){
			Main.main.stage.addActor(new TestWindow());
			return true;
		}
		else if (character == '+'){
			TestManager manager = Main.tickThread.getTestManager();
			if (manager != null){
				manager.jump = true;
				return true;
			}
		}
		return false;
	}

	Main main = Main.main;
	MainStage stage = main.stage;
}
