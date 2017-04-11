package de.felixperko.worldgenconfig.PropertyEditor.Elements;

import com.badlogic.gdx.math.Vector2;

public class Connector {
	
	/*
	 * A Connector serves as an interface to connect different Blocks to each other.
	 */
	
	public Block block;
	
	public Connection connection = null;
	Vector2 pos = new Vector2();
	
	boolean isOutput;
	
	public Connector(Block block, int x, int y, boolean isOutput) {
		this.isOutput = isOutput;
		this.block = block;
		pos.x = x;
		pos.y = y;
	}

	public Vector2 getPos() {
		return new Vector2(block.getX()+pos.x, block.getY()+pos.y);
	}

	public boolean isOutput() {
		return isOutput;
	}
	
	public void setConnection(Connection c){
		connection = c;
		block.drawImage();
	}
	
	public Block getConnectedBlock(){
		if (connection == null)
			return null;
		if (isOutput)
			return connection.input.block;
		return connection.output.block;
	}
}
