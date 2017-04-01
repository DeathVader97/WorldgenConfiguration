package de.felixperko.worldgenconfig.Generation.Noise;

import java.util.HashMap;

public class NoiseHelper {

	public static OpenSimplexNoise openSimplexNoise = new OpenSimplexNoise();
	
	public static double simplexNoise2D(double x, double y, double frequency, double persistance, double lacunarity, int octaves, OpenSimplexNoise noise){
		double amplitude = 1;
		double value = 0;
		double maxAmp = 0;
		for (int o = 0 ; o < octaves ; o++){
			value += noise.eval(x*frequency, y*frequency)*amplitude;
			maxAmp += amplitude;
			amplitude *= persistance;
			frequency *= lacunarity;
		}
		return value/maxAmp;
	}
	
	static int[][] count = new int[16][100];
	static int[] countAmount = new int[16];
	static int totalCountAmount = 0;
	
	public static double simplexNoise2D(double x, double y, double frequency, double persistance, double lacunarity, int octaves, OpenSimplexNoise[] noise){
		double amplitude = 1;
		double value = 0;
		double maxAmp = 0;
		for (int o = 0 ; o < octaves ; o++){
			value += noise[o].eval(x*frequency, y*frequency)*amplitude;
			maxAmp += amplitude;
			amplitude *= persistance;
			frequency *= lacunarity;
		}
//		if (octaves == 1){
			int index = (int) (((value/maxAmp)*0.5+0.5)*count[0].length);
			count[octaves][index]++;
			countAmount[octaves]++;
			totalCountAmount++;
			if (totalCountAmount % (17500000*16) == 0){
				for (int o = 0 ; o < 16 ; o++){
					if (countAmount[o] != 0){
						System.out.println("---"+o);
						for (int i = 0; i < count[0].length; i++) {
							System.out.println((((double)count[o][i]/countAmount[o])+"").replace('.', ','));
						}
					}
				}
				System.out.println("----------");
			}
//		}
		return value/maxAmp;
	}
	
//	public static double simplexNoise2D(double x, double y, double frequency, double persistance, double lacunarity, int octaves, SimplexNoise[] noise){
//		double amplitude = 1;
//		double value = 0;
//		double maxAmp = 0;
//		for (int o = 0 ; o < octaves ; o++){
//			value += noise[o].noise(x*frequency, y*frequency)*amplitude;
//			maxAmp += amplitude;
//			amplitude *= persistance;
//			frequency *= lacunarity;
//		}
////		if (octaves == 1){
//			int index = (int) (((value/maxAmp)*0.5+0.5)*count[0].length);
//			count[octaves][index]++;
//			countAmount[octaves]++;
//			totalCountAmount++;
//			if (totalCountAmount % (17500000*16) == 0){
//				for (int o = 0 ; o < 16 ; o++){
//					if (countAmount[o] != 0){
//						System.out.println("---"+o);
//						for (int i = 0; i < count[0].length; i++) {
//							System.out.println((((double)count[o][i]/countAmount[o])+"").replace('.', ','));
//						}
//					}
//				}
//				System.out.println("----------");
//			}
////		}
//		return value/maxAmp;
//	}
	
//	final static int maxDefaultSelectorSteps = 16;
//	static HashMap<Double, double[]> map = new HashMap<>();
//	static double[] defaultMap = new double[maxDefaultSelectorSteps];
//	static {
//		double v = 1;
//		for (int i = 0 ; i < maxDefaultSelectorSteps ; i++){
//			defaultMap[i] = v;
//			v += v*0.5;
//			System.out.println(i+": "+defaultMap[i]);
//		}
//	}
	
	
	public static boolean simplexNoise2DSelector(double x, double y, double frequency, double persistance, double lacunarity, int octaves, OpenSimplexNoise noise, double border, boolean lower, boolean higher){
		double amplitude = 1;
		double value = 0;
		double maxAmp = 0;
		double relV = 0;
		for (int o = 0 ; o < octaves ; o++){
			value += noise.eval(x*frequency, y*frequency)*amplitude;
			maxAmp += amplitude;
			relV = Math.abs(value/maxAmp);
			double d = relV - border;
			if (d < 0)
				d = -d;
			amplitude *= persistance;
			if (d > amplitude*persistance+amplitude){
//				System.out.println("got result after "+(o+1)+"/"+octaves);
				if (relV < border)
					return lower;
				return higher;
			}
			frequency *= lacunarity;
		}
		if (relV < border)
			return lower;
		return higher;
	}

	public static double simplexNoise3D(double x, double y, double z, double frequency, double persistance, double lacunarity, int octaves, OpenSimplexNoise noise){
		double amplitude = 1;
		double value = 0;
		double maxAmp = 0;
		for (int o = 0 ; o < octaves ; o++){
			value += noise.eval(x*frequency, y*frequency, z*frequency)*amplitude;
			maxAmp += amplitude;
			amplitude *= persistance;
			frequency *= lacunarity;
		}
		return value/maxAmp;
	}

	public static double simplexNoise2D(double x, double y, double frequency, double persistance, double lacunarity, int octaves) {
		return simplexNoise2D(x, y, frequency, persistance, lacunarity, octaves, openSimplexNoise);
	}

	public static double simplexNoise3D(double x, double y, double z, double frequency, double persistance, double lacunarity, int octaves) {
		return simplexNoise3D(x, y, z, frequency, persistance, lacunarity, octaves, openSimplexNoise);
	}
}
