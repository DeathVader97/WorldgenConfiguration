package de.felixperko.worldgenconfig.PropertyEditor.Elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;

import de.felixperko.worldgenconfig.PropertyEditor.EditorMisc.EditorStage;

public class Connection {
	
	EditorStage stage;
	
	public Connector input = null;
	public Connector output = null;
	
	public boolean selected = false;
	
	public Connection(EditorStage stage, Connector connector){
		this.stage = stage;
		set(connector);
	}
	
	public Vector2 getPos1(){
		if (input == null){
			return output.getPos();
		} else
			return input.getPos();
	}
	
	public Vector2 getPos2(){
		if (output == null || input == null){
			return stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
		} else
			return output.getPos();
	}

	public void set(Connector c) {
		if (c.isOutput){
			output = c;
		} else {
			input = c;
		}
		c.setConnection(this);
	}

	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean value){
		selected = value;
	}

	public double distance(Vector2 p3) {
		return Intersector.distanceSegmentPoint(getPos1(), getPos2(), p3);
//		Vector2 p1 = getPos1();
//		Vector2 p2 = getPos2();
//		double normalLength = Math.sqrt((p2.x-p1.x)*(p2.x-p1.x)+(p2.y-p1.y)*(p2.y-p1.y));
//	    double distanceToLine = Math.abs((p3.x-p1.x)*(p2.y-p1.y)-(p3.y-p1.y)*(p2.x-p1.x))/normalLength;
//	    double dx1 = p3.x-p1.x;
//	    double dy1 = p3.y-p1.y;
//	    double d1 = Math.sqrt(dx1*dx1+dy1*dy1);
//	    double dx2 = p3.x-p2.x;
//	    double dy2 = p3.y-p2.y;
//	    double d2 = Math.sqrt(dx2*dx2+dy2*dy2);
//	    
//	    double lowest = distanceToLine;
//	    if (d1 < distanceToLine)
//	    	lowest = d1;
//	    if (d2 < distanceToLine)
//	    	lowest = d2;
//	    return lowest;
	}

	public void removeConnector(Connector c) {
		if (input == c){
			input.setConnection(null);
			input = null;
		} else if (output == c){
			output.setConnection(null);
			output = null;
		}
	}

	public boolean isComplete() {
		return input != null && output != null;
	}
	
	public void remove(){
		if (input != null){
			input.setConnection(null);
			input = null;
		} if (output != null){
			output.setConnection(null);
			output = null;
		}
		stage.removeConnection(this);
	}
}
