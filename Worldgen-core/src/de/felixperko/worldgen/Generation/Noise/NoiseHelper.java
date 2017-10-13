package de.felixperko.worldgen.Generation.Noise;

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
		return value/maxAmp;
	}
	
	public static PointData2D simplexNoise2D_deriv(double x, double y, double frequency, double persistance, double lacunarity, int octaves, OpenSimplexNoise[] noise){
		double amplitude = 1;
		double maxAmp = 0;
		PointData2D data = new PointData2D();
		for (int o = 0 ; o < octaves ; o++){
			PointData2D octaveData = noise[o].eval_derivative(x*frequency, y*frequency);
			data.value += octaveData.value*amplitude;
			data.ddx += octaveData.ddx;
			data.ddy += octaveData.ddy;
			maxAmp += amplitude;
			amplitude *= persistance;
			frequency *= lacunarity;
		}
		data.value /= maxAmp;
		return data;
	}
	
	public static double iqNoise2D(double x, double y, double frequency, double persistance, double lacunarity, int octaves, OpenSimplexNoise[] noise){
		double amplitude = 1;
		double value = 0;
		double maxAmp = 0;
		double dx = 0, dy = 0;
		PointData2D res = new PointData2D();
		for (int o = 0 ; o < octaves ; o++){
			PointData2D data = noise[o].eval_derivative(x*frequency, y*frequency);
			dx += data.ddx;
			dy += data.ddy;
			
			double multFactor = amplitude/(1+dx*dx+dy*dy);
			
			res.ddx += data.ddx*Math.pow(amplitude,0.1)/(1+dx*dx+dy*dy);
			res.ddy += data.ddy*Math.pow(amplitude,0.1)/(1+dx*dx+dy*dy);
			
			value += data.value*multFactor;
			maxAmp += multFactor;
			
			amplitude *= persistance;
			frequency *= lacunarity;
		}
		
		res.value = value/maxAmp;
//		res.ddx /= maxAmp;
//		res.ddy /= maxAmp;
		return value/maxAmp;
	}
	
	static double deriv_shift = 0.00001;
	
	public static PointData2D iqNoise2D_deriv(double x, double y, double frequency, double persistance, double lacunarity, int octaves, OpenSimplexNoise[] noise) {
		double v = iqNoise2D(x, y, frequency, persistance, lacunarity, octaves, noise);
		double v10 = iqNoise2D(x+deriv_shift, y, frequency, persistance, lacunarity, octaves, noise);
		double v01 = iqNoise2D(x, y+deriv_shift, frequency, persistance, lacunarity, octaves, noise);
		PointData2D data = new PointData2D();
		data.ddx = (v10-v)/deriv_shift;
		data.ddy = (v01-v)/deriv_shift;
		data.value = v;
		return data;
	}
	
	public static double swissNoise2D(double x, double y, double frequency, double persistance, double lacunarity, double warp, int octaves, OpenSimplexNoise[] noise){
		double amplitude = 1;
		double value = 0;
		double maxAmp = 0;
		double dx = 0, dy = 0;
		for (int o = 0 ; o < octaves ; o++){
			PointData2D data = noise[o].eval_derivative((x+warp*dx)*frequency, (y+warp*dy)*frequency);
			value += amplitude * (1 - Math.abs(data.value));
			dx += data.ddx * amplitude * -data.value;
			dy += data.ddy * amplitude * -data.value;
			
			maxAmp += amplitude;
			
			amplitude *= persistance * (value < 1 ? (value > 0 ? value : 0) : 1);
			frequency *= lacunarity;
		}
		
		return value/maxAmp;
	}

	public static PointData2D swissNoise2D_deriv(double x, double y, double frequency, double persistance, double lacunarity, double warp, int octaves, OpenSimplexNoise[] noise) {
		double v = swissNoise2D(x, y, frequency, persistance, lacunarity, warp, octaves, noise);
		double v10 = swissNoise2D(x+deriv_shift, y, frequency, persistance, lacunarity, warp, octaves, noise);
		double v01 = swissNoise2D(x, y+deriv_shift, frequency, persistance, lacunarity, warp, octaves, noise);
		PointData2D data = new PointData2D();
		data.ddx = (v10-v)/deriv_shift;
		data.ddy = (v01-v)/deriv_shift;
		data.value = v;
		return data;
	}
	
	public static PointData2D test_swissNoise2D_deriv(double x, double y, double frequency, double persistance, double lacunarity, double warp, int octaves, OpenSimplexNoise[] noise){
		double amplitude = 1;
		double value = 0;
		double maxAmp = 0;
		double dx = 0, dy = 0;
		PointData2D res = new PointData2D();
		double warpScale = warp*frequency;
		double[] dxValues = new double[octaves];
		double[] dyValues = new double[octaves];
		for (int o = 0 ; o < octaves ; o++){
			PointData2D data = noise[o].eval_derivative((x+warp*dx)*frequency, (y+warp*dy)*frequency);
			value += amplitude * (1 - Math.abs(data.value));
			dx += data.ddx * amplitude * -data.value;
			dy += data.ddy * amplitude * -data.value;
			
//			System.out.println(data.ddx);
			
			res.ddx += data.ddx * amplitude * frequency * -data.value * (1-warpScale + warpScale*res.ddx);
			res.ddy += data.ddy * amplitude * frequency * -data.value * (1-warpScale + warpScale*res.ddy);
//			if (o > 0){
//				res.ddx += data.ddx*(1-warpScale);
//				res.ddy += data.ddy*(1-warpScale);
//				res.ddx *= (1-warpScale) + warpScale*prev_ddx;
//				res.ddy *= (1-warpScale) + warpScale*prev_ddy;
//			} else {
//				res.ddx += data.ddx;
//				res.ddy += data.ddy;
//			}
//			prev_ddx = data.ddx;
//			prev_ddy = data.ddy;
			
			maxAmp += amplitude;
			
			amplitude *= persistance * (value < 1 ? (value > 0 ? value : 0) : 1);
			frequency *= lacunarity;
		}
		res.value = value/maxAmp;
//		System.out.println(res.ddy);

//		res.ddx = dxValues[0];
//		res.ddy = dyValues[0];
//		double scale = 1;
//		warpScale = 0.0;
//		amplitude = 1;
//		for (int i = 1 ; i < octaves ; i++){
////			scale *= value;
////			scale *= 0.5;
////			scale += 0.5;
////			res.ddx += (scale*dxValues[i])*(1-warpScale+warpScale*res.ddx)*(-value);
////			res.ddy += (scale*dyValues[i])*(1-warpScale+warpScale*res.ddy)*(-value);
//			amplitude *= persistance * (value < 1 ? (value > 0 ? value : 0) : 1);
//			frequency *= lacunarity;
//			res.ddx += dxValues[i]*(1-warpScale+warpScale*res.ddx);
//			res.ddy += dyValues[i]*(1-warpScale+warpScale*res.ddy);
//		}
		
		return res;
	}
	
	public static double erodedNoise2D(double x, double y, double frequency, double persistance1, double persistance, double lacunarity,
			double warp0, double warp, double damp0, double damp, double damp_scale, int octaves, OpenSimplexNoise[] noise){
		PointData2D n = noise[0].eval_derivative(x*frequency, y*frequency);
		PointData2D n2 = new PointData2D();
		n2.value = (persistance*n.value)*(persistance*n.value);
		n2.ddx = n.value*n.ddx;
		n2.ddy = n.value*n.ddy;
		double dsum_warpX = warp0*n2.ddx;
		double dsum_warpY = warp0*n2.ddy;
		double dsum_dampX = damp0*n2.ddx;
		double dsum_dampY = damp0*n2.ddy;
		double amplitude = persistance1;
		double value = n2.value;
		double damped_amp = amplitude*persistance;
		double maxAmp = 0;
		for (int o = 1 ; o < octaves ; o++){
			n = noise[o].eval_derivative(x*frequency+dsum_warpX, y*frequency+dsum_warpY);
			n2.value = n.value*n.value;
			n2.ddx = n.value*n.ddx;
			n2.ddy = n.value*n.ddy;
			value += damped_amp * n2.value;
			dsum_warpX = warp*n2.ddx;
			dsum_warpY = warp*n2.ddy;
			dsum_dampX = damp*n2.ddx;
			dsum_dampY = damp*n2.ddy;
			
			maxAmp += damped_amp;
			
			amplitude *= persistance;
			frequency *= lacunarity;
			damped_amp = amplitude * (1-damp_scale/(1+dsum_dampX*dsum_dampX+dsum_dampY*dsum_dampY));
		}
		return value/maxAmp;
	}
	
	
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

	public static double simplexNoise3D(double x, double y, double z, double frequency, double persistance, double lacunarity, int octaves, OpenSimplexNoise[] noises){
		double amplitude = 1;
		double value = 0;
		double maxAmp = 0;
		for (int o = 0 ; o < octaves ; o++){
			value += noises[o].eval(x*frequency, y*frequency, z*frequency)*amplitude;
			maxAmp += amplitude;
			amplitude *= persistance;
			frequency *= lacunarity;
		}
		return value/maxAmp;
	}

	public static boolean simplexNoise3DSelector(double x, double y, double z, double frequency, double persistance, double lacunarity, int octaves, OpenSimplexNoise[] noises, double border, boolean lower, boolean higher){
		double amplitude = 1;
		double value = 0;
		double maxAmp = 0;
		double relV = 0;
		for (int o = 0 ; o < octaves ; o++){
			value += noises[o].eval(x*frequency, y*frequency, z*frequency)*amplitude;
			maxAmp += amplitude;
			amplitude *= persistance;
			relV = Math.abs(value/maxAmp);
			if (value > amplitude*persistance+amplitude){
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
	
	public static PointData3D iqNoise3D(double x, double y, double z, double frequency, double persistance, double lacunarity, int octaves, OpenSimplexNoise[] noise){
		double amplitude = 1;
		double value = 0;
		double maxAmp = 0;
		double dx = 0, dy = 0, dz = 0;
		PointData3D res = new PointData3D();
		for (int o = 0 ; o < octaves ; o++){
			PointData3D data = noise[o].eval_derivative(x*frequency, y*frequency, z*frequency);
			dx += data.ddx;
			dy += data.ddy;
			dz += data.ddz;
			
			double multFactor = amplitude/(1+dx*dx+dy*dy+dz*dz);
			res.ddx += data.ddx*multFactor;
			res.ddy += data.ddy*multFactor;
			res.ddz += data.ddz*multFactor;
			
			value += data.value*multFactor;
			maxAmp += multFactor;
			
			amplitude *= persistance;
			frequency *= lacunarity;
		}
		
		res.value = value/maxAmp;
//		res.ddx /= maxAmp;
//		res.ddy /= maxAmp;
//		res.ddz /= maxAmp;
		return res;
	}

	public static double simplexNoise2D(double x, double y, double frequency, double persistance, double lacunarity, int octaves) {
		return simplexNoise2D(x, y, frequency, persistance, lacunarity, octaves, openSimplexNoise);
	}

//	public static double simplexNoise3D(double x, double y, double z, double frequency, double persistance, double lacunarity, int octaves) {
//		return simplexNoise3D(x, y, z, frequency, persistance, lacunarity, octaves, openSimplexNoise);
//	}
}
