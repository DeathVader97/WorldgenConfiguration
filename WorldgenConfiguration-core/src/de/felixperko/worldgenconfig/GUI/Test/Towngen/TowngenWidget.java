package de.felixperko.worldgenconfig.GUI.Test.Towngen;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import de.felixperko.worldgenconfig.GUI.Test.TestManager;
import de.felixperko.worldgenconfig.GUI.Test.TestShapeWidget;
import de.felixperko.worldgenconfig.MainMisc.Main;

public class TowngenWidget extends TestShapeWidget {
	
	final static long PAUSE_TIME = 1 * 1000000000l;
	final static long RESET_TIME = 10 * 1000000000l;
	
	ArrayList<TowngenStreet> streets = new ArrayList<>();
	private ArrayList<TowngenObject> additionalObjects = new ArrayList<>();
//	ArrayList<TowngenHouse> houses = new ArrayList<>();
//	ArrayList<TowngenField> fields = new ArrayList<>();

	ArrayList<TowngenStreet> openStreetList = new ArrayList<>();
	
	public TowngenGrid grid = new TowngenGrid(40);
	
	public TowngenWidget() {
		super(ShapeType.Filled);
		Main.tickThread.setTestManager(new TestManager(this));
	}

	@Override
	protected void renderShapes(ShapeRenderer sr) {
		try {
			for (TowngenStreet street : streets)
				street.render(sr);
			for (TowngenObject o : additionalObjects)
				o.render(sr);
		} catch (Exception e){
			renderShapes(sr);
		}
	}
	
	long nextStep = 0;
	
	int stepCounter = 0;
	int stepLimit = 100000;
	
	Random random;
	
	@Override
	public void act(float delta) {
//		long time = System.nanoTime();
//		if (time >= nextStep){
//			nextStep = time+PAUSE_TIME;
//			step();
//			stepCounter++;
//			if (stepCounter > stepLimit)
//				nextStep = System.nanoTime()+RESET_TIME;
//		}
	}
	
	int seed = 0;
	public int totalComparisons = 0;
	
	@Override
	public void tick() {
		step();
		stepCounter++;
		if (stepCounter > stepLimit){
			System.out.println("placed "+streets.size()+" streets using "+totalComparisons+" comparisons.");
			System.out.println((totalComparisons/(float)streets.size())+" comparisons per street");
			for (Float f : averageComparisons)
				System.out.println((f+"").replace('.', ','));
			totalComparisons = 0;
			testManager.nextActionTime = System.nanoTime()+RESET_TIME;
		} else {
			if (testManager.jump){
				if (!Gdx.input.isKeyPressed(Keys.PLUS))
					testManager.jump = false;
			} else 
				testManager.nextActionTime = System.nanoTime()+PAUSE_TIME;
		}
	}
	
	ArrayList<Float> averageComparisons = new ArrayList<>();
	
	private void step() {
		if (stepCounter > stepLimit){
			streets.clear();
			openStreetList.clear();
			grid = new TowngenGrid(40);
//			houses.clear();
//			fields.clear();
			stepCounter = -1;
			return;
		}
		System.out.println("step: "+stepCounter);
		if (stepCounter == 0){
			random = new Random(seed++);
//			stepLimit = 25 + random.nextInt(125);
			stepLimit = 1000;
			streets.add(new TowngenStreet(this, new TowngenVertex(250, 250), TowngenAlignment.values()[random.nextInt(4)], 20, 2));
			openStreetList.add(streets.get(0));
		}
		else {
			while (!openStreetList.isEmpty()){
				int index = random.nextInt(openStreetList.size());
				TowngenStreet street = openStreetList.get(index).generateAdjecentStreet(random, streets, 20+(int)(15*(stepCounter/(double)stepLimit))+random.nextInt(5), 2);
				if (street == null){
					if (openStreetList.get(index).getFreeConnections() == 0)
						openStreetList.remove(index);
					continue;
				}
				averageComparisons.add(totalComparisons/(float)streets.size());
				System.out.println("add new street "+street.id+" (Total:"+streets.size()+") "+street.alignment+" from: "+index);
				streets.add(street);
				openStreetList.add(street);
				break;
			}
		}
			
	}

	public TowngenObject addTestObject(TowngenObject object) {
		additionalObjects.add(object);
		return object;
	}

	public TowngenObject removeTestObject(TowngenObject object) {
		if (!additionalObjects.remove(object))
			return null;
		return object;
	}
}
