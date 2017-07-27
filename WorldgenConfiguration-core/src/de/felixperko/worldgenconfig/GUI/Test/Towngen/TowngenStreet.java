package de.felixperko.worldgenconfig.GUI.Test.Towngen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.czyzby.kiwi.util.tuple.immutable.Pair;

import static de.felixperko.worldgenconfig.GUI.Test.Towngen.TowngenAlignment.*;

public class TowngenStreet extends TowngenObject {
	
	static int ID_COUNTER = 0;
	
	static float transparency = 0.3f;
	static Color lightBlueColorTransparent = new Color(.5f,.5f,1,transparency);
	static Color greenColorTransparent = new Color(0,1,0,transparency);
	static Color redColorTransparent = new Color(1,0,0,transparency);
	static Color grayColorTransparent = new Color(1,1,1,transparency);
	
	static Color lightBlueColorOpaque = new Color(.5f,.5f,1,1f);
	static Color greenColorOpaque = new Color(0,1,0,1f);
	static Color redColorOpaque = new Color(1,0,0,1f);
	static Color grayColorOpaque = new Color(1,1,1,1f);
	
	static ArrayList<TowngenVertex> vertices = new ArrayList<>();
	
	public void setEnabled(boolean enabled){
		if (color == lightBlueColorTransparent)
			color = enabled ? lightBlueColorOpaque : lightBlueColorTransparent;
		else if (color == greenColorTransparent)
			color = enabled ? greenColorOpaque : greenColorTransparent;
		else if (color == redColorTransparent)
			color = enabled ? redColorOpaque : redColorTransparent;
		else if (color == grayColorTransparent)
			color = enabled ? grayColorOpaque : grayColorTransparent;
		if (color == lightBlueColorOpaque)
			color = enabled ? lightBlueColorOpaque : lightBlueColorTransparent;
		else if (color == greenColorOpaque)
			color = enabled ? greenColorOpaque : greenColorTransparent;
		else if (color == redColorOpaque)
			color = enabled ? redColorOpaque : redColorTransparent;
		else if (color == grayColorOpaque)
			color = enabled ? grayColorOpaque : grayColorTransparent;
	}
	
	ArrayList<String> changes = new ArrayList<>();
	
	TowngenWidget widget;
	Set<TowngenGridPoint> gridPoints = null;
	
	TowngenVertex startVertex;
	TowngenVertex endVertex;
	
	int streetWidth = 2;
	
	Color color = grayColorTransparent;

	TowngenVertex[] points = new TowngenVertex[2];
	
	int minX;
	int minY;
	int maxX;
	int maxY;
	
	TowngenAlignment alignment;

	int length;

	int id = ID_COUNTER++;
	

	public TowngenStreet(TowngenWidget widget, TowngenVertex startVertex, TowngenAlignment alignment, int length, int width) {
		super(getX(startVertex.x,alignment,length,width), getY(startVertex.y,alignment,length,width), getWidth(alignment,length,width), getHeight(alignment,length,width));
		this.widget = widget;
		this.startVertex = startVertex;
		this.alignment = alignment;
		startVertex.setStreetStart(this);
		this.x = startVertex.x;
		this.y = startVertex.y;
		this.length = length;
		
		points[0] = startVertex;
		
		if (!vertices.contains(startVertex))
			vertices.add(startVertex);
		
		switch (alignment){
		case UP:
			points[1] = new TowngenVertex(x, y+length);
			minX = points[0].x;
			maxX = points[0].x;
			minY = points[0].y;
			maxY = points[1].y;
			break;
		case DOWN:
			points[1] = new TowngenVertex(x, y-length);
			minX = points[0].x;
			maxX = points[0].x;
			minY = points[1].y;
			maxY = points[0].y;
			break;
		case RIGHT:
			points[1] = new TowngenVertex(x+length, y);
			minX = points[0].x;
			maxX = points[1].x;
			minY = points[0].y;
			maxY = points[0].y;
			break;
		case LEFT:
			points[1] = new TowngenVertex(x-length, y);
			minX = points[1].x;
			maxX = points[0].x;
			minY = points[0].y;
			maxY = points[0].y;
			break;
		}
		
		endVertex = points[1];
		vertices.add(endVertex);
		endVertex.setStreetEnd(this);
		
		updateGridPoint();
	}

	public TowngenStreet(TowngenWidget widget, TowngenVertex startVertex, TowngenVertex endVertex, int streetWidth) {
		this.widget = widget;
		this.streetWidth = streetWidth;
		this.startVertex = startVertex;
		this.endVertex = endVertex;
		this.points[0] = startVertex;
		this.points[1] = endVertex;
		int dx = endVertex.x - startVertex.x;
		int dy = endVertex.y - startVertex.y;
		minX = dx <= 0 ? endVertex.x : startVertex.x;
		maxX = dx <= 0 ? startVertex.x : endVertex.x;
		minY = dy <= 0 ? endVertex.y : startVertex.y;
		maxY = dy <= 0 ? startVertex.y : endVertex.y;
		x = minX;
		y = minY;
		width = maxX-x;
		height = maxY-y;
		if (dx > 0)
			alignment = TowngenAlignment.RIGHT;
		else if (dx < 0)
			alignment = TowngenAlignment.LEFT;
		else if (dy > 0)
			alignment = TowngenAlignment.UP;
		else if (dy < 0)
			alignment = TowngenAlignment.DOWN;
		else
			throw new IllegalStateException("attempted to create a street without dimensions.");
		startVertex.setStreetStart(this);
		endVertex.setStreetEnd(this);
		updateGridPoint();
	}

	private static int getX(int x, TowngenAlignment alignment, int length, int width) {
		switch (alignment){
		case LEFT:
			return x-length;
		default:
			return x;
		}
	}

	private static int getY(int y, TowngenAlignment alignment, int length, int width) {
		switch(alignment){
		case DOWN:
			return y-length;
		default:
			return y;
		}
	}

	private static int getHeight(TowngenAlignment alignment, int length, int width) {
		switch(alignment){
		case LEFT:
		case RIGHT:
			return width;
		default:
			return length;
		}
	}

	private static int getWidth(TowngenAlignment alignment, int length, int width) {
		switch(alignment){
		case UP:
		case DOWN:
			return width;
		default:
			return length;
		}
	}

	@Override
	public void render(ShapeRenderer sr) {
		sr.setColor(color);
//		sr.rect(x, y, width, height);
		sr.rectLine(points[0].x, points[0].y, points[1].x, points[1].y, streetWidth);
	}
	
	public TowngenStreet generateAdjecentStreet(Random r, ArrayList<TowngenStreet> streets, int length, int width){
		
		int pointIndex = r.nextInt(1);
		TowngenVertex vertex = points[pointIndex];
		if (vertex.freeSlots == 0){
			pointIndex = 1 - pointIndex;
			vertex = points[pointIndex];
			if (vertex.freeSlots == 0)
				return null;
		}
		
		TowngenAlignment alignment = vertex.getAvailableAlignment(r);
		TowngenStreet street = new TowngenStreet(widget, vertex, alignment, length, width);
		if (!street.adjustToOtherStreets(streets)){
			vertex.removeStartStreet(street, null);
			vertex.setBlocked(street);
			if (endVertex.getConnectedStreetCount() == 1)
				vertices.remove(endVertex);
			for (TowngenGridPoint p : street.gridPoints)
				p.remove(street);
			return null;
		}
		return street;
	}
	
	public TowngenAlignment getRelativeAlignment(TowngenAlignment alignment, int pointIndex){
		if (pointIndex == 0)
			return alignment;
		return alignment.opposite();
	}
	
	private boolean adjustToOtherStreets(List<TowngenStreet> streets) {
		
		ArrayList<TowngenStreet> expand = new ArrayList<>();
		
		TowngenBoundingBox collisionBB = generateCollisionBB();
		TowngenBoundingBox expandBB = generateExpandBB();
		TowngenLine startLine = getStartLine();
		TowngenLine directionLine = getDirectionLine();
		int smallestStartDistance = Integer.MAX_VALUE;
		int[] directionDistance = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE};
		boolean inCollisionBB = false;
		TowngenStreet nearestOverlap[] = new TowngenStreet[2];
		for (TowngenStreet street : streets)
			street.setEnabled(false);
		
		ArrayList<TowngenObject> testObjects = new ArrayList<>();
		testObjects.add(widget.addTestObject(new TowngenLinesegment(points[0], points[1], new Color(1f,1f,0f,0.5f))));
		collisionBB.setColor(new Color(1f,0f,0f,0.2f));
		testObjects.add(widget.addTestObject(collisionBB));
		expandBB.setColor(new Color(0f,1f,0f,0.2f));
		testObjects.add(widget.addTestObject(expandBB));
		int cMinX = collisionBB.minX < expandBB.minX ? collisionBB.minX : expandBB.minX;
		int cMinY = collisionBB.minY < expandBB.minY ? collisionBB.minY : expandBB.minY;
		int cMaxX = collisionBB.maxX > expandBB.maxX ? collisionBB.maxX : expandBB.maxX;
		int cMaxY = collisionBB.maxY > expandBB.maxY ? collisionBB.maxY : expandBB.maxY;
		try {
			Thread.sleep(widget.testManager.jump ? 50 : 500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		ArrayList<TowngenVertex> vertices = TowngenStreet.vertices;
		TowngenStreet previous = null;
		Set<TowngenGridPoint> set = widget.grid.getLoopGridPoints(cMinX, cMinY, cMaxX, cMaxY, false);
		ArrayList<TowngenGridPoint> list = new ArrayList<TowngenGridPoint>(set);
		Pair<Double,Double> gridPos = widget.grid.getUnroundedGridPos((maxX+minX)*0.5, (maxY+minY)*0.5);
		Collections.sort(list, new Comparator<TowngenGridPoint>() {
			@Override
			public int compare(TowngenGridPoint o1, TowngenGridPoint o2) {
				double d1 = o1.distanceSquared(gridPos);
				double d2 = o2.distanceSquared(gridPos);
				if (d1 < d2)
					return -1;
				if (d1 > d2)
					return 1;
				return 0;
			}
		});
		for (TowngenGridPoint point : list){
			TowngenBoundingBox testBox = point.getRange();
			testObjects.add(widget.addTestObject(testBox));
			testBox.setColor(new Color(0,0,1,0.2f));
			try {
				if (!widget.testManager.jump)
					Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		HashSet<Integer> alreadyChecked = new HashSet<>();
		for (TowngenGridPoint point : list){
			for (int a = 0 ; a < point.streets.size() ; a++){
				TowngenStreet street = point.streets.get(a);
//			for (TowngenStreet street : streets){
				if (street == this || alreadyChecked.contains(street.id))
					continue;
				alreadyChecked.add(street.id);
				widget.totalComparisons++;
				if (previous != null)
					previous.setEnabled(false);
				street.setEnabled(true);
				previous = street;
				
				try {
					if (!widget.testManager.jump)
						Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				boolean collisionOverlap = collisionBB.overlap(street);
				int startDistance = Math.abs(startLine.distance(street));
				if (startDistance == 0 && (collisionOverlap || (this.startVertex.equals(street.startVertex) && this.endVertex.equals(street.endVertex)))){
					for (TowngenObject testObject : testObjects)
						widget.removeTestObject(testObject);
					return false;
				}
				if (collisionOverlap || expandBB.overlap(street)){
					int dirDistance = directionLine.distance(street);
					if (startDistance > smallestStartDistance || (startDistance == smallestStartDistance && dirDistance > directionDistance[1])){
						if (collisionOverlap)
							expand.add(this);
						continue;
					} if (startDistance == smallestStartDistance && dirDistance >= directionDistance[0] && dirDistance <= directionDistance[1]){
						if (nearestOverlap[1] != null && collisionOverlap)
							expand.add(nearestOverlap[1]);
						nearestOverlap[1] = street;
						directionDistance[1] = dirDistance;
						continue;
					} else if (startDistance == smallestStartDistance && dirDistance < directionDistance[0]){
						if (nearestOverlap[1] != null && collisionOverlap)
							expand.add(nearestOverlap[1]);
						nearestOverlap[1] = nearestOverlap[0];
						directionDistance[1] = directionDistance[0];
						nearestOverlap[0] = street;
						directionDistance[0] = dirDistance;
					} else {
						nearestOverlap[0] = street;
						directionDistance[0] = dirDistance;
						nearestOverlap[1] = null;
						directionDistance[1] = Integer.MAX_VALUE;
						smallestStartDistance = startDistance;
						inCollisionBB = collisionOverlap;
					}
				}
			}
		}
		if (previous != null)
			previous.setEnabled(false);
		
		if (nearestOverlap[0] == null){
			for (TowngenObject testObject : testObjects)
				widget.removeTestObject(testObject);
			return true;
		}
		
		if (inCollisionBB)
			cutOff(smallestStartDistance, nearestOverlap);
		else{ //in expand boundingbox
			expand(smallestStartDistance);
		
			for (int i = 0 ; i < 1 ; i++){
				TowngenStreet other = nearestOverlap[i];
				if (directionDistance[i] > 0){
					if (other.alignment.isParallel(alignment))
						connect(other);
					else{
						expand.add(other);
						continue;
					}
				}
				//TODO validate algorithm still works
				else
					other.split(endVertex, this);
			}

			//TODO validate after bracket above?
			for (TowngenStreet street : expand){
				if (street.endVertex.getConnectedStreetCount() == 1) {
					street.expand(street.getStartLine().distance(this));
					vertices.remove(street.endVertex);
					street.endVertex = endVertex;
					street.points[1] = endVertex;
					endVertex.setStreetEnd(street);
				} else
					street.connect(this);
				street.color = lightBlueColorOpaque;
			}
		}

		for (TowngenObject testObject : testObjects)
			widget.removeTestObject(testObject);
		return true;
	}
	
	/**
	 * Splits a street into two. The start point of the current street is set to the provided vertex,
	 * while another street from the old start point to the provided vertex is created.
	 * @param vertex - position where the split is made
	 * @param other - another street that should be connected to the provided vertex.
	 */
	private void split(TowngenVertex vertex, TowngenStreet other) {
		if (vertex.equals(startVertex)){
			startVertex.setStreetEnd(other);
			return;
		}
		if (vertex.equals(endVertex)){
			endVertex.setStreetEnd(other);
			return;
		}
		
		if (vertex.distance(endVertex) < length*0.25 && endVertex.getConnectedStreetCount() == 1){
			endVertex = vertex;
			points[1] = vertex;
			vertex.setStreetStart(this);
			setLength(startVertex.distance(endVertex));
			return;
		}
		
		startVertex.removeStartStreet(this, other);
		TowngenStreet newStreet = new TowngenStreet(widget, startVertex, vertex, streetWidth);
		if (!newStreet.adjustToOtherStreets(widget.streets))
			return;
		widget.streets.add(newStreet);
		widget.openStreetList.add(newStreet);
		newStreet.changes.add("created by split");
		
		startVertex = vertex;
		points[0] = vertex;
		vertex.setStreetStart(this);
		
		int dx = Math.abs(endVertex.x - startVertex.x);
		int dy = Math.abs(endVertex.y - startVertex.y);
		if (dx == 0 && dy == 0)
			throw new IllegalStateException("street has no dimensions after split.");
		
		if (dx == 0 && dy != length)
			length = dy;
		else if (dy == 0 && dx != length)
			length = dx;
		x = alignment == LEFT ? endVertex.x : startVertex.x;
		y = alignment == DOWN ? endVertex.y : startVertex.y;
		width = alignment.isVertical() ? streetWidth : length;
		height = alignment.isVertical() ? length : streetWidth;
		minX = x;
		minY = y;
		maxX = alignment.isHorizontal() ? minX+length : minX;
		maxY = alignment.isVertical() ? minY+length : minY;
		updateGridPoint();
//		if (dx == 0 && dy != length)
//			setLength(dy);
//		else if (dy == 0 && dx != length)
//			setLength(dx);
		
		changes.add("split");
	}
	
	/**
	 * Sets the length of the street to newLength and sets debug markers for expansion.
	 * @param newLength - new length of the street
	 */
	private void expand(int newLength) {
		setLength(newLength);
		color = greenColorOpaque;
		changes.add("expanded");
	}
	
	/**
	 * Sets the length of the street to the newLength and sets debug markers for cutoff.
	 * Additionally connects the streets given in connectStreets to the street.
	 * @param newLength
	 * @param connectStreets
	 */
	private void cutOff(int newLength, TowngenStreet[] connectStreets) {
		setLength(newLength);
		changes.add("cut off");
		for (int i = 0 ; i < connectStreets.length ; i++){
			TowngenStreet street = connectStreets[i];
			if (street == null)
				continue;
			System.out.println("endVertex: "+endVertex.toString()+" / "+points[1].toString());
			System.out.println("other endVertex: "+street.endVertex.toString()+" / "+street.points[1].toString());
			System.out.println("equals? "+endVertex.equals(street.endVertex));
			
			System.out.println(endVertex.equals(street.endVertex));
			if (endVertex.equals(street.endVertex)){
				endVertex = street.endVertex;
				points[1] = street.endVertex;
				changes.add("combined end point with other street");
				endVertex.setStreetEnd(this);
			} else if (street.endVertex.getConnectedStreetCount() == 1 && alignment.isPerpendicular(street.alignment) &&
					((alignment.isVertical() && collide(street.minY, minY, maxY)) || alignment.isHorizontal() && collide(street.minX, minX, maxX))){
				int d = street.startVertex.distance(endVertex);
				if (d >= street.length){
					if (d > street.length)
						street.expand(d);
					street.endVertex = endVertex;
					street.points[1] = endVertex;
					endVertex.setStreetEnd(street);
				} else {
					street.split(endVertex, this);
				}
			} else {
				connect(street);
			}
		}
		color = redColorOpaque;
	}
	
	/**
	 * connects the street with the provided street by adding a street between them, if they don't collide already.
	 * Throws an IllegalStateException if the streets don't overlap on more than one axis.
	 * @param street - the street to connect to.
	 */
	private void connect(TowngenStreet street) {
		
		TowngenVertex point1 = points[0];
		TowngenVertex point2 = street.points[0];
		
		int lowestDistance = point1.distance(point2);
//		for (int i = 0 ; i < 2 ; i++){
//			System.out.println("a"+i+": "+points[i].toString());
//		}
//		for (int i = 0 ; i < 2 ; i++){
//			System.out.println("b"+i+": "+street.points[i].toString());
//		}
		for (int i = 1 ; i < 4 ; i++){
			TowngenVertex tPoint1 = points[i/2];
			TowngenVertex tPoint2 = street.points[i%2];
			int distance = tPoint1.distance(tPoint2);
			if (distance < lowestDistance){
				point1 = tPoint1;
				point2 = tPoint2;
				lowestDistance = distance;
			}
		}
		
		int dx = point2.x - point1.x;
		int dy = point2.y - point1.y;
		TowngenStreet newStreet = null;
		if (dx == 0 && dy == 0)
			return;
		if (dy == 0){
			if (dx > 0){
				newStreet = new TowngenStreet(widget, point1, point2, streetWidth);
			} else {
				newStreet = new TowngenStreet(widget, point1, point2, streetWidth);
			}
		} else if (dx == 0){
			if (dy > 0){
				newStreet = new TowngenStreet(widget, point1, point2, streetWidth);
			} else {
				newStreet = new TowngenStreet(widget, point1, point2, streetWidth);
			}
		} else {
			System.err.println("something went wrong at connect. No axis were overlapping. dx="+dx+" dy="+dy);
			return;
		}
		
		widget.streets.add(newStreet);
		widget.openStreetList.add(newStreet);
		
		newStreet.color = lightBlueColorOpaque;
	}

	private TowngenLine getDirectionLine() {
		if (alignment.isVertical())
			return new TowngenLine(points[0].x, false);
		return new TowngenLine(points[0].y, true);
	}

	private TowngenLine getStartLine() {
		if (alignment.isVertical())
			return new TowngenLine(points[0].y, true);
		return new TowngenLine(points[0].x, false);
	}

	private TowngenBoundingBox generateExpandBB() {
		switch (alignment){
		case UP:
			return new TowngenBoundingBox(minX-length/2, maxX+length/2, minY+length, maxY+length);
		case DOWN:
			return new TowngenBoundingBox(minX-length/2, maxX+length/2, minY-length, maxY-length);
		case LEFT:
			return new TowngenBoundingBox(minX-length, maxX-length, minY-length/2, maxY+length/2);
		case RIGHT:
			return new TowngenBoundingBox(minX+length, maxX+length, minY-length/2, maxY+length/2);
		}
		return null;
	}

	private TowngenBoundingBox generateCollisionBB() {
		if (alignment.isVertical())
			return new TowngenBoundingBox(minX-length/2, maxX+length/2, minY, maxY);
		return new TowngenBoundingBox(minX, maxX, minY-length/2, maxY+length/2);
	}

	private void setLength(int newLength) {
		changes.add("new length: "+length+" -> "+newLength);
		int d = newLength-length;
		if (d > 0)
			color = greenColorTransparent;
		else if (d < 0)
			color = redColorTransparent;
		this.length = newLength;
		switch (alignment){
		case UP:
			maxY = minY+length;
			height = maxY-minY;
			points[1].setXY(x, maxY);
			break;
		case DOWN:
			minY = maxY-length;
			y = minY;
			height = maxY-minY;
			points[1].setXY(x, minY);
			break;
		case LEFT:
			minX = maxX-length;
			x = minX;
			width = maxX-minX;
			points[1].setXY(minX, y);
			break;
		case RIGHT:
			maxX = minX+length;
			width = maxX-minX;
			points[1].setXY(maxX, y);
			break;
		}
		updateGridPoint();
	}

	public int getFreeConnections() {
		return startVertex.freeSlots + endVertex.freeSlots;
	}
	
	public void updateGridPoint(){
		Set<TowngenGridPoint> newPoints = widget.grid.getLoopGridPoints(minX, minY, maxX, maxY, true);
		if (gridPoints == null){
			gridPoints = newPoints;
			for (TowngenGridPoint p : newPoints)
				p.add(this);
		} else if (!newPoints.equals(gridPoints)){
			for (TowngenGridPoint p : gridPoints)
				if (!newPoints.contains(p))
					p.remove(this);
			for (TowngenGridPoint p : newPoints)
				if (!gridPoints.contains(p))
					p.add(this);
			gridPoints = newPoints;
		}
	}
	
	@Override
	public String toString() {
		return id+": "+startVertex.toString()+" "+alignment.toString()+" "+endVertex.toString();
	}
	
//	private int distanceX(TowngenStreet o) {
//		if (intersectX(o))
//			return 0;
//		int dRight = o.minX - maxX;
//		if (dRight >= 0)
//			return dRight;
//		return minX - o.maxX;
//	}
//
//	private boolean intersectX(TowngenStreet o) {
//		return overlap(minX, o.minX, o.maxX) || overlap(maxX, o.minX, o.maxX) || overlap(o.minX, minX, maxX) || overlap(o.maxX, minX, maxX);
//	}
//	
//	private boolean intersectXPerpendicular(TowngenStreet o){
//		if (minX != maxX)
//			throw new IllegalStateException("the street has to be vertical!");
//		return minX >= o.minX && minX <= o.maxX;
//	}
//
//	private int distanceY(TowngenStreet o){
//		if (intersectY(o))
//			return 0;
//		int dUp = o.minY - maxY;
//		if (dUp >= 0)
//			return dUp;
//		return minY - o.maxY;
//	}
//	
//	private boolean intersectY(TowngenStreet o){
//		return overlap(minY, o.minY, o.maxY) || overlap(maxY, o.minY, o.maxY) || overlap(o.minY, minY, maxY) || overlap(o.maxY, minY, maxY);
//	}
//	
//	private boolean intersectYPerpendicular(TowngenStreet o){
//		if (minY != maxY)
//			throw new IllegalStateException("the street has to be vertical!");
//		return minY >= o.minY && minY <= o.maxY;
//	}
//
//	public boolean parallel(TowngenStreet street){
//		return alignment.ordinal()%2 == street.alignment.ordinal()%2;
//	}
//	
//	public int parallelDistance(TowngenStreet street){
//		if (alignment == TowngenAlignment.LEFT || alignment == TowngenAlignment.RIGHT)
//			return Math.abs(y-street.y);
//		return Math.abs(x-street.x);
//	}
//	
//	public boolean OneDimensionParallelOverlap(TowngenStreet street){
//		if (alignment == TowngenAlignment.LEFT || alignment == TowngenAlignment.RIGHT){
//			return overlap(minX, street.minX, street.maxX) || overlap(maxX, street.minX, street.maxX) ||
//					overlap(street.minX, minX, maxX) || overlap(street.maxX, minX, maxX);
//		}
//		return overlap(minY, street.minY, street.maxY) || overlap(maxY, street.minY, street.maxY) ||
//				overlap(street.minY, minY, maxY) || overlap(street.maxY, minY, maxY);
//	}
//	
//	public boolean perpendicular(TowngenStreet street){
//		return alignment.ordinal()%2 != street.alignment.ordinal()%2;
//	}
//	
	private boolean collide(int value, int lower, int higher){
		return value >= lower && value <= higher;
	}

}