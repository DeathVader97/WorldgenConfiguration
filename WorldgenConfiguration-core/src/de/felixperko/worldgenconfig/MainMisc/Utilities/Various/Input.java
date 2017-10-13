package de.felixperko.worldgenconfig.MainMisc.Utilities.Various;

import static com.badlogic.gdx.Input.Keys.*;

import com.badlogic.gdx.input.GestureDetector;
import de.felixperko.worldgenconfig.GUI.Test.TestManager;
import de.felixperko.worldgenconfig.GUI.Test.TestWindow;
import de.felixperko.worldgenconfig.GUI.Test.xzTerrain.xzWidget;
import de.felixperko.worldgenconfig.MainMisc.Main;
import de.felixperko.worldgenconfig.MainMisc.MainStage;

public class Input extends GestureDetector {
	
	public Input(InputGestureListener inputGestureListener) {
		super(inputGestureListener);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean scrolled(int amount) {
		if (Main.tickThread.getTestManager() != null){
			if (Main.tickThread.getTestManager().widget instanceof xzWidget){
				((xzWidget)Main.tickThread.getTestManager().widget).addDrawY(amount);
				return true;
			}
		}
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
	
	@Override
	public boolean keyDown(int keycode) {

		if (Main.tickThread.getTestManager() != null){
			if (Main.tickThread.getTestManager().widget instanceof xzWidget){
				xzWidget widget = (xzWidget) Main.tickThread.getTestManager().widget;
				if (keycode == LEFT)
					widget.addChunkPos(-1, 0);
				else if (keycode == RIGHT)
					widget.addChunkPos(1, 0);
				else if (keycode == DOWN)
					widget.addChunkPos(0, 1);
				else if (keycode == UP)
					widget.addChunkPos(0, -1);
				else
					return false;
				return true;
			}
		}
		return false;
	}

	Main main = Main.main;
	MainStage stage = main.stage;
}
