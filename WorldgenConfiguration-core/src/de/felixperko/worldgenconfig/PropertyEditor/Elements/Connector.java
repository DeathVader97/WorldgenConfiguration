package de.felixperko.worldgenconfig.PropertyEditor.Elements;

import com.badlogic.gdx.math.Vector2;

public class Connector {
	
	DisplayNode node;
	
	public Connection connection = null;
	Vector2 pos = new Vector2();
	
	boolean isOutput;
	
	public Connector(DisplayNode node, int x, int y, boolean isOutput) {
		this.isOutput = isOutput;
		this.node = node;
		pos.x = x;
		pos.y = y;
	}

	public Vector2 getPos() {
		return new Vector2(node.getX()+pos.x, node.getY()+pos.y);
	}

	public boolean isOutput() {
		return isOutput;
	}
	
	public void setConnection(Connection c){
		connection = c;
		node.drawImage();
	}
}
