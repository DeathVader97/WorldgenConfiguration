package de.felixperko.worldgenconfig.PropertyEditor;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.PopupMenu;

import de.felixperko.worldgenconfig.Generation.GenerationPath.Nodes.CombineAddNode;
import de.felixperko.worldgenconfig.Generation.GenerationPath.Nodes.ModifierNode;
import de.felixperko.worldgenconfig.Generation.GenerationPath.Nodes.Node;
import de.felixperko.worldgenconfig.Generation.GenerationPath.Nodes.NoiseGenerator;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.Connection;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.Connector;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.EditorNode;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.EndPoint;
import de.felixperko.worldgenconfig.PropertyEditor.Elements.DisplayNode;

public class EditorStage extends Stage {
	
	public ArrayList<DisplayNode> nodes = new ArrayList<>();
	public ArrayList<Connection> connections = new ArrayList<>();
	public EndPoint endPoint;
	PopupMenu menu;
	
	ShapeRenderer renderer = new ShapeRenderer();
	
	public void init() {
		VisUI.load();
		endPoint = new EndPoint(this, (int) (Gdx.graphics.getWidth()*0.9f), Gdx.graphics.getHeight()/2);
		addActor(endPoint);
		nodes.add(endPoint);
	}

	public void createNode(float x, float y, Class<? extends Node> node) {
		EditorNode en = new EditorNode(this,x,y,node);
		addActor(en);
		nodes.add(en);
		en.init();
	}
	
	public void addConnection(Connection c){
		if (!connections.contains(c)){
			connections.add(c);
		}
	}
	
	public void removeConnection(Connection c){
		connections.remove(c);
	}

	public Connector getConnector(Vector2 mousePos, boolean output) {
		mousePos = screenToStageCoordinates(mousePos);
		for (DisplayNode node : nodes){
			if (output && node instanceof EditorNode){
				EditorNode n = (EditorNode)node;
				Vector2 d = n.output.getPos().sub(mousePos);
				if (d.len() <= 5)
					return n.output;
			} else {
				for (Connector input : node.getInputs()){
					Vector2 d = input.getPos().sub(mousePos);
					if (d.len() <= 5)
						return input;
				}
			}
		}
		return null;
	}

	public Connection getConnection(Vector2 mousePos) {
		mousePos = screenToStageCoordinates(mousePos);
		for (Connection c : connections){
			if (c.distance(mousePos) <= 5)
				return c;
		}
		return null;
	}
	
	public DisplayNode getNodeAtMousePos(){
		Vector2 pos = getMouseWorldPos();
		for (DisplayNode node : nodes){
			if (pos.x >= node.getX() && pos.x <= node.getX()+node.getWidth() && pos.y >= node.getY() && pos.y <= node.getY()+node.getHeight())
				return node;
		}
		return null;
	}
	
	@Override
	public void draw() {
		
		super.draw();
		
//		if (getBatch().isDrawing())
//			getBatch().end();
		renderer.begin(ShapeType.Line);
		renderer.setProjectionMatrix(getCamera().combined);
		
		renderer.setColor(Color.WHITE);
		for (Connection c : connections){
			if (c.isSelected())
				renderer.setColor(Color.GREEN);
			renderer.line(c.getPos1(), c.getPos2());
			if (c.isSelected())
				renderer.setColor(Color.WHITE);
		}
		
		renderer.end();
//		getBatch().begin();
	}

	public void showPopupMenu() {
		if (menu != null)
			menu.remove();
		
		menu = new PopupMenu();
		addActor(menu);
		menu.addItem(new MenuItem("Noise", new ChangeListener() {
			@Override public void changed(ChangeEvent arg0, Actor arg1) {
				createNode(menu.getX(), menu.getY(), NoiseGenerator.class);
		}}));
		menu.addItem(new MenuItem("Modifier", new ChangeListener() {
			@Override public void changed(ChangeEvent arg0, Actor arg1) {
				createNode(menu.getX(), menu.getY(), ModifierNode.class);
		}}));
		menu.addItem(new MenuItem("Combine", new ChangeListener() {
			@Override public void changed(ChangeEvent arg0, Actor arg1) {
				createNode(menu.getX(), menu.getY(), CombineAddNode.class);
		}}));
		
		Vector2 worldPos = getMouseWorldPos();
		menu.setPosition(worldPos.x, worldPos.y);
	}

	public Vector2 getMouseWorldPos() {
		return screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
	}
}
