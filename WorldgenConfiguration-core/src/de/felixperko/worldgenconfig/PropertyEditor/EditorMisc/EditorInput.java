package de.felixperko.worldgenconfig.PropertyEditor.EditorMisc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

import de.felixperko.worldgenconfig.PropertyEditor.Elements.Block;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.ComponentBlock;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.Connection;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.Connector;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.EndBlock;

public class EditorInput implements InputProcessor {
	
	EditorMain eMain;
	EditorStage stage;

	int lastX = 0;
	int lastY = 0;
	
	Connection currentConnection = null;
	Block selectedBlock = null;
	
	boolean[] pressed = new boolean[4];

	public EditorInput(EditorMain editorMain) {
		eMain = editorMain;
		stage = eMain.stage;
	}

	@Override
	public boolean keyDown(int key) {
		if (key > 18 && key < 23){
			pressed[key-19] = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int key) {
		if (key > 18 && key < 23){
			pressed[key-19] = false;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		lastX = arg0;
		lastY = arg1;
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (button == 1){
			stage.showPopupMenu();
			return true;
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Vector2 v = stage.getMouseWorldPos();
		if (selectedBlock != null && stage.getBlockAtMousePos() == selectedBlock){
			selectedBlock.setPosition(v.x-selectedBlock.getWidth()/2, v.y-selectedBlock.getHeight()/2);
			return true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (button == 0){
			if (currentConnection == null){
				Connector c = stage.getConnector(new Vector2(screenX, screenY), false);
				if (c == null)
					c = stage.getConnector(new Vector2(screenX, screenY), true);
				if (c != null){
					if (selectedBlock != null){
						selectedBlock.setSelected(false);
						selectedBlock = null;
					}
					if (c.connection == null){
						currentConnection = new Connection(stage, c);
						stage.addConnection(currentConnection);
					} else {
						currentConnection = c.connection;
						c.setConnection(null);
						currentConnection.removeConnector(c);
					}
					currentConnection.selected = true;
				} else {
					for (Connection connection : stage.connections)
						connection.selected = false;
					Connection connection = stage.getConnection(new Vector2(screenX, screenY));
					if (connection != null){
						if (selectedBlock != null){
							selectedBlock.setSelected(false);
							selectedBlock = null;
						}
						connection.setSelected(true);
						currentConnection = connection;
					} else {
						Block block = stage.getBlockAtMousePos();
						if (block != null){
							if (block.isSelected())
								block.clicked();
							else if (selectedBlock != null)
								selectedBlock.setSelected(false);
							selectedBlock = block;
							block.setSelected(true);
						} else {
							for (Block block2 : stage.blocks)
								block2.setSelected(false);
						}
					}
				}
				
			} else {
				boolean searchOutput = currentConnection.output == null;
				Connector c = stage.getConnector(new Vector2(screenX, screenY), searchOutput);
				if (c != null){
					currentConnection.set(c);
					currentConnection.selected = false;
					currentConnection = null;
				} else if (currentConnection.isComplete()){
					for (Block block : stage.blocks)
						block.setSelected(false);
					Block block = stage.getBlockAtMousePos();
					if (block != null){
						currentConnection.selected = false;
						currentConnection = null;
						block.setSelected(true);
						selectedBlock = block;
					}
				}
			}
			return false;
		}
		return false;
	}

	public void processInput() {
		Input in = Gdx.input;
		
		if (in.isKeyPressed(Input.Keys.ESCAPE)){
			if (currentConnection != null){
				stage.removeConnection(currentConnection);
				if (currentConnection.input != null)
					currentConnection.input.setConnection(null);
				if (currentConnection.output != null)
					currentConnection.output.setConnection(null);
				currentConnection = null;
			}
		}
		if (in.isKeyJustPressed(Input.Keys.FORWARD_DEL)){
			if (currentConnection != null){
				currentConnection.remove();
				currentConnection = null;
			}
			if (selectedBlock != null && !(selectedBlock instanceof EndBlock)){
				for (Connector c : selectedBlock.getInputs()){
					if (c != null && c.connection != null){
						c.connection.remove();
						c.connection = null;
					}
				}
				if (selectedBlock instanceof ComponentBlock){
					Connector out = ((ComponentBlock)selectedBlock).output;
					if (out != null && out.connection != null){
						out.connection.remove();
						out.connection = null;
					}
				}
				selectedBlock.remove();
				stage.blocks.remove(selectedBlock);
				selectedBlock = null;
			}
		}
		
		float x = 0,y = 0;
		if (pressed[0])
			y++;
		if (pressed[1])
			y--;
		if (pressed[2])
			x--;
		if (pressed[3])
			x++;
//		if (in.isKeyPressed(Input.Keys.LEFT))
//			x--;
//		if (in.isKeyPressed(Input.Keys.RIGHT))
//			x++;
//		if (in.isKeyPressed(Input.Keys.DOWN))
//			y--;
//		if (in.isKeyPressed(Input.Keys.UP))
//			y++;
		if (x == 0 && y == 0)
			return;
		double factor = Gdx.graphics.getDeltaTime()*500;
		x *= factor;
		y *= factor;
//		drawField.renderer.getTransformMatrix().translate((float)(move.getX()*factor), (float)(move.getY()*factor), 0);
		OrthographicCamera camera = (OrthographicCamera) stage.getCamera();
		camera.translate(x, y, 0);
//        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
//        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;
//		camera.position.x = MathUtils.clamp(camera.position.x, effectiveViewportWidth/2f, 1600-effectiveViewportWidth/2f);
//		camera.position.y = MathUtils.clamp(camera.position.y, effectiveViewportHeight/2f, 943-effectiveViewportHeight/2f);
//		drawField.updateView();
//		pos.add(move);
	}

}
