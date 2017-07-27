package de.felixperko.worldgenconfig.Generation.ImageGeneration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

import de.felixperko.worldgenconfig.MainMisc.MainStage;

public class ImageManager extends Widget{
	
	MainStage stage;
	
	HashSet<WorldgenImage> existing = new HashSet<>();
	ArrayList<WorldgenImage> drawImages = new ArrayList<>();
	
	float xMin = 0, xMax = Gdx.graphics.getWidth()-350, yMin = 0, yMax = Gdx.graphics.getHeight()-20;
	
	static float imgSize = 50;
	int minSize = 10;
	int maxSize = 100;
	
	static int border = 2;
	public GenerationManager genManager;
	
	SpriteBatch imgBatch;
	OrthographicCamera cam;
	Rectangle bounds;
	float shiftX,shiftY;

	VisTable table;
	VisLabel positionLabel;
	VisLabel amountLabel;
	VisLabel boundsLabel;
	VisLabel fpsLabel;
	
	public ImageManager(MainStage stage) {
		this.stage = stage;
		this.genManager = new GenerationManager();
		update();
		
		table = new VisTable(true);
		table.setPosition(10, 10);
		table.align(Align.bottom | Align.left);
		
		amountLabel = new VisLabel("images: 0");
		table.add(amountLabel).align(Align.left).row();
		
		positionLabel = new VisLabel("pos: 0, 0");
		table.add(positionLabel).align(Align.left).row();
		
		boundsLabel = new VisLabel("-");
		table.add(boundsLabel).align(Align.left).row();
		
		fpsLabel = new VisLabel("fps: 0");
		table.add(fpsLabel).align(Align.left);
	}
	
	public void shift(Vector2 shift){
//		xMin += shift.x;
//		xMax += shift.x;
//		yMin += shift.y;
//		yMax += shift.y;
		
		for (WorldgenImage image : existing){
			image.pos.add(shift);
			image.setPosition(image.pos.x, image.pos.y);
		}
		update();
	}
	
	public void zoomIn(float zoom){
		
		//clamp zoom
//		if (zoom > 0){
//			float maxZoom = imgSize/maxSize;
//			if (maxZoom < zoom)
//				zoom = maxZoom;
//		} else {
//			float minZoom = imgSize/minSize;
//			if (minZoom > zoom)
//				zoom = minZoom;
//		}
		if (zoom == 0)
			return;
		
		//adjust size
		int oldSize = (int)imgSize;
		int newSize = Math.round(oldSize/(1+zoom));
		if (newSize < minSize)
			newSize = minSize;
		else if (newSize > maxSize)
			newSize = maxSize;
		if (newSize == oldSize)
			return;
		imgSize = newSize;
		zoom = ((float)newSize/oldSize)-1;
		System.out.println("zoom: "+zoom+" ("+oldSize+"->"+newSize+")");
		
		//adjust bounds
//		float diffX = (xMax-xMin)*(zoom)/2f;
//		float diffY = (xMax-xMin)*(zoom)/2f;
//		xMin += diffX;
//		xMax -= diffX;
//		yMin += diffY;
//		yMax -= diffY;
		
		cam.zoom *= 1-zoom;
		cam.update();
		Vector3 min = cam.unproject(new Vector3(0, 0, 0));
		Vector3 max = cam.unproject(new Vector3(getWidth(), getHeight(), 0));
		xMin = min.x;
		xMax = max.x;
		yMin = max.y;
		yMax = min.y;
		
		//adjust positions
//		Vector2 m = new Vector2((xMax-xMin)/2, (yMax-yMin)/2);
		for (WorldgenImage image : existing){
//			image.setSize((int)imgSize, (int)imgSize);
//			Vector2 mp = new Vector2((image.pos.x-m.x)*(1-zoom), (image.pos.y-m.y)*(1-zoom));
//			image.pos = mp.add(m);
//			image.setPosition(image.pos.x, image.pos.y);
		}
		
		for (JobThread thread : genManager.jobThreads)
			thread.changeSize((int)imgSize);
		
		//update
		update();
	}
	
	public void pan(double x, double y){
		shiftX += x;
		shiftY += y;
		
		xMin -= x;
		xMax -= x;
		yMin += y;
		yMax += y;
		
		update();
	}
	
	Vector2 cursorPos = new Vector2(0,0);
	int imgAmount = 0;
	
	long lastBoundsRefresh = 0;
	int fps = 0;
	
	@Override
	public void act(float delta){
		
		long t = System.currentTimeMillis();
		if (t-lastBoundsRefresh > 1000 && boundsLabel != null){
			boundsLabel.setText("xMin:"+xMin+" yMin:"+yMin+" xMax:"+xMax+" yMax:"+yMax);
			lastBoundsRefresh = t;
		}
		
		if (fps != Gdx.graphics.getFramesPerSecond()){
			fps = Gdx.graphics.getFramesPerSecond();
			fpsLabel.setText("fps: "+fps);
		}
		
		if (existing.size() != imgAmount){
			imgAmount = existing.size();
			amountLabel.setText("images: "+imgAmount);
		}
		if (cam == null)
			return;
		Vector3 newPos = cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		Vector2 pos = new Vector2(newPos.x, newPos.y);
		if (!pos.equals(cursorPos)){
			cursorPos = pos;
			positionLabel.setText("pos: "+(int)pos.x+", "+(int)pos.y);
		}
		super.act(delta);
	}

	public void update(){
		
		HashSet<WorldgenImage> need = getNeededImages();
		
		//exist, but not needed anymore
		//-> remove
		Iterator<WorldgenImage> it = existing.iterator();
		while (it.hasNext()){
			WorldgenImage img = it.next();
			if (!need.contains(img)){
				img.remove();
				drawImages.remove(img);
				it.remove();
			}
		}
		
		//needed, but don't exist
		//-> add
		for (WorldgenImage img : need){
			if (!existing.contains(img)){
				drawImages.add(img);
//				stage.addImage(img);
				existing.add(img);
				genManager.addWorldgenImage(img, getPriority(img.getX(),img.getY()), false);
			}
		}
	}
	
	public double getPriority(double x, double y){
		double dx = Math.abs((xMax-xMin)*.5-(x-xMin));
		double dy = Math.abs((yMax-yMin)*.5-(y-yMin));
		if (x < xMin || x > xMax || y < yMin || y > yMax)
			return (dx*dx+dy*dy)*10;
		return dx*dx+dy*dy;
	}
	
	public HashSet<WorldgenImage> getNeededImages(){
		HashSet<WorldgenImage> images = new HashSet<>();
		float borderSize = imgSize*border;
		for (float x = xMin-xMin%imgSize-borderSize ; x <= xMax-xMax%imgSize+borderSize+1 ; x += imgSize){
			for (float y = yMin-yMin%imgSize-borderSize ; y <= yMax-yMax%imgSize+borderSize+1 ; y += imgSize){
				images.add(getAtPos(new Vector2(x,y)));
			}
		}
		return images;
	}
	
	public WorldgenImage getAtPos(Vector2 pos){
//		int x = (int) (pos.x/imgSize);
//		int y = (int) (pos.y/imgSize);
		int x = (int) pos.x;
		int y = (int) pos.y;
		
//		if (x < 0) x--;
//		if (y < 0) y--;
		
		for (WorldgenImage img : existing){
			if (img.isAtPos(x,y))
				return img;
		}
		
		WorldgenImage res = new WorldgenImage(this, new Vector2(x,y));
		
		return res;
	}
	
	public boolean visible(WorldgenImage img){
		return img.pos.x >= xMin && img.pos.x <= xMax && img.pos.y >= yMin && img.pos.y <= yMax;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (cam == null){
			cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			shiftX = -Gdx.graphics.getWidth()/2;
			shiftY = Gdx.graphics.getHeight()/2;
			imgBatch = new SpriteBatch();
			bounds = new Rectangle();
			bounds.width = getWidth();
			bounds.height = getHeight();
		}
		batch.end();
		cam.position.x = -shiftX;
		cam.position.y = shiftY;
		cam.update();
		
		ScissorStack.pushScissors(bounds);
		
		imgBatch.setProjectionMatrix(cam.combined);
		imgBatch.begin();
		for (WorldgenImage img : drawImages){
			img.setSize(imgSize, imgSize);
			img.draw(imgBatch, parentAlpha);
		}
		imgBatch.flush();
		imgBatch.end();
		ScissorStack.popScissors();
		batch.begin();
		
		table.draw(batch, parentAlpha);
//		positionLabel.draw(batch, parentAlpha);
		
		super.draw(batch, parentAlpha);
	}

	public void refreshImages() {
		genManager.clearJobs();
		for (WorldgenImage img : existing)
			genManager.addWorldgenImage(img, getPriority(img.getX(),img.getY()), true);
	}

	public void dispose() {
		for (JobThread t : genManager.jobThreads){
			t.interrupted = true;
		}
	}
}
