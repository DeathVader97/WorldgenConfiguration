package de.felixperko.worldgenconfig.MainMisc.Utilities.Various;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.badlogic.gdx.graphics.Pixmap;

public class Utils {
	public static void printPixmap(Pixmap pm, File f){
		int w = pm.getWidth();
		int h = pm.getHeight();
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		
		int[] pixel = new int[w*h];
		int index = 0;
		for (int y = 0; y < h; y++) {
			for (int x = 0; x < w; x++) {
				int p = pm.getPixel(x, y);
				int r = ((p & 0xff000000) >>> 24);
				int g = ((p & 0x00ff0000) >>> 16);
				int b = ((p & 0x0000ff00) >>> 8);
				pixel[index] = (r << 16) | (g << 8) | b;
				index++;
			}
		}
		bi.setRGB(0, 0, w, h, pixel, 0, w);
		try {
			ImageIO.write(bi, "jpg", f);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
