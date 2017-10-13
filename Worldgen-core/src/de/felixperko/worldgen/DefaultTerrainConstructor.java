package de.felixperko.worldgen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import de.felixperko.worldgen.Generation.Interpolation.Interpolation;
import de.felixperko.worldgen.Generation.Misc.TerrainType;
import de.felixperko.worldgen.Util.Math.Vector2i;

public class DefaultTerrainConstructor extends TerrainConstructor{
	
	public DefaultTerrainConstructor(ChunkGenerator generator) {
		super(generator);
	}

	@SuppressWarnings("unchecked")
	HashMap<Integer,Double>[][] contribs = new HashMap[4][4];
	@SuppressWarnings("unchecked")
	HashMap<Integer, Double>[] typeContributions = new HashMap[16*16];
	{
		for (int i = 0 ; i < 16*16 ; i++){
			typeContributions[i] = new HashMap<Integer, Double>();
		}
		for (int x = 0 ; x < 4 ; x++){
			for (int z = 0 ; z < 4 ; z++){
				contribs[x][z] = new HashMap<Integer, Double>();
			}
		}
	}
	double[] heightMap = new double[16*16];
	
	double[] density = new double[16*16*256];
	int[] materials = new int[16*16*256];
	
	Chunk currentChunk;
	
	static Vector2i nx = new Vector2i(-1,0);
	static Vector2i ny = new Vector2i(1,-1);
	static Vector2i px = new Vector2i(1,1);
	static Vector2i py = new Vector2i(-1,1);
	
	HashSet<Integer> relevantTypes = new HashSet<>();
	
	public void construct(Chunk c){
		currentChunk = c;
		generateTypeContributions();
		generateHeightMap();
		placeMaterials();
		generateCaves();
	}

	private void generateTypeContributions() {
		
		Vector2i pos = currentChunk.pos;
		
		for (int i = 0 ; i < 16*16 ; i++){
			typeContributions[i].clear();
		}
		
		relevantTypes.clear();
		
		for (int dx = -2 ; dx <= 1 ; dx++){
			for (int dy = -2 ; dy <= 1 ; dy++){
				int indexX = dx+2;
				int indexZ = dy+2;
				Chunk data = generator.chunkManager.getChunkData(new Vector2i(pos.getX()+dx, pos.getY()+dy));
				contribs[indexX][indexZ] = data.biomes;
				for (Entry<Integer,Double> e : data.biomes.entrySet())
					if (e.getValue() != 0)
						relevantTypes.add(e.getKey());
//				HashMap<Integer, Double> map = new HashMap<>();
//				contribs[indexX][indexZ].put(data.sampleBiome, 1.);
//				for (Entry<Integer, Double> e : data.biomes.entrySet()){
//					map.put(, value);
//				}
//				for (Entry<Integer, Double> e : contribs[dx+1][dy+1].entrySet()){
//					System.out.println((dx+1)+", "+(dy+1)+" type: "+e.getKey()+" contrib: "+e.getValue());
//				}
//				System.out.println((dx+1)+", "+(dy+1)+": ("+data.currentStep+")"+contribs[dx+1][dy+1].toString());
			}
		}
		
		for (Integer typeID : relevantTypes){
			double dx = 1./32;
			for (int x = 0 ; x < 16 ; x++){
				double dz = 1./32;
				for (int z = 0 ; z < 16 ; z++){
					double y0 = cubic(value(typeID,contribs[0][0]), value(typeID,contribs[0][1]), value(typeID,contribs[0][2]), value(typeID,contribs[0][3]), dz);
					double y1 = cubic(value(typeID,contribs[1][0]), value(typeID,contribs[1][1]), value(typeID,contribs[1][2]), value(typeID,contribs[1][3]), dz);
					double y2 = cubic(value(typeID,contribs[2][0]), value(typeID,contribs[2][1]), value(typeID,contribs[2][2]), value(typeID,contribs[2][3]), dz);
					double y3 = cubic(value(typeID,contribs[3][0]), value(typeID,contribs[3][1]), value(typeID,contribs[3][2]), value(typeID,contribs[3][3]), dz);
					double res = cubic(y0,y1,y2,y3,dx);
					typeContributions[index2D(x, z)].put(typeID, res);
					dz += 2./32;
				}
				dx += 2./32;  
			}
		}
		
		
//		interpolateContributions(contribs[0][0], contribs[1][0], contribs[0][1], contribs[1][1], 0, 0, 8, 8, 15/32., 15/32., -2/16., -2/16.);
//		interpolateContributions(contribs[1][0], contribs[2][0], contribs[1][1], contribs[2][1], 8, 0, 16, 8, 31/32., 15/32., -2/16., -2/16.);
//		interpolateContributions(contribs[0][1], contribs[1][1], contribs[0][2], contribs[1][2], 0, 8, 8, 16, 15/32., 31/32., -2/16., -2/16.);
//		interpolateContributions(contribs[1][1], contribs[2][1], contribs[1][2], contribs[2][2], 8, 8, 16, 16, 31/32., 31/32., -2/16., -2/16.);
	}

	private double value(Integer typeID, HashMap<Integer, Double> hashMap) {
		Double v = hashMap.get(typeID);
		if (v == null)
			return 0;
		return v;
	}

	private void interpolateContributions(HashMap<Integer, Double> current, HashMap<Integer, Double> otherX, HashMap<Integer, Double> otherZ, HashMap<Integer, Double> otherXZ, int minX, int minY, int maxX, int maxY, double startX, double startZ, double stepX, double stepY) {
		relevantTypes.clear();
		for (Integer type : current.keySet())
			relevantTypes.add(type);
		for (Integer type : otherX.keySet())
			relevantTypes.add(type);
		for (Integer type : otherZ.keySet())
			relevantTypes.add(type);
		for (Integer type : otherXZ.keySet())
			relevantTypes.add(type);
		
		for (Integer type : relevantTypes){
			double v = current.containsKey(type) ? current.get(type) : 0;
			double vx = otherX.containsKey(type) ? otherX.get(type) : 0;
			double vz = otherZ.containsKey(type) ? otherZ.get(type) : 0;
			double vxz = otherXZ.containsKey(type) ? otherXZ.get(type) : 0;
			double cx = startX;
			double cz = startZ;
			for (int x = minX ; x < maxX ; x++){
				for (int z = minY ; z < maxY ; z++){
					int i = index2D(x,z);
					typeContributions[i].put(type, lerp(lerp(v, vx, cx), lerp(vz, vxz, cx), cz));
					cz += stepY;
				}
				cz = startZ;
				cx += stepX;
			}
		}
	}
	
	private double cubic(double y0, double y1, double y2, double y3, double c){
		double cSq = c*c;
		double a0 = y3-y2-y0+y1;
		return a0 * c*cSq + (y0-y1-a0) * cSq + (y2-y0) * c + y1;
	}

	private double lerp(double v1, double v2, double c) {
//		return Interpolation.cosineInterpolation(c, 0, 1, v1, v2);
		return v1 * c + v2 * (1-c);
	}

	private void generateHeightMap() {
		
		int cx = currentChunk.pos.getX();
		int cy = currentChunk.pos.getY();
		
		for (int x = 0 ; x < 16 ; x++){
			for (int z = 0 ; z < 16 ; z++){
				double v = 0;
//				if (x == 0)
//					System.out.println(x+"/"+z);
				for (Entry<Integer, Double> e : typeContributions[index2D(x,z)].entrySet()){
					double contrib = e.getValue();
//					if (x == 0)
//						System.out.println("type: "+e.getKey()+" contrib: "+contrib);
					if (contrib == 0)
						continue;
					v += generator.generationParameters.getType(e.getKey()).getHeight(cx+x/16., cy+z/16.)*contrib;
//					v += contrib*128;
//					break;
				}
				heightMap[index2D(x, z)] = v;
			}
		}
	}
	
	double h_water = 64;
	private void placeMaterials() {
		for (int x = 0 ; x < 16 ; x++){
			for (int z = 0 ; z < 16 ; z++){
				double h = heightMap[index2D(x,z)];
				for (int y = 255 ; y >= 0 ; y--){
					int index = index3D(x, y, z);
					if (y > h){
						if (y < h_water)
							materials[index] = 2;
						else
							materials[index] = 0;
					} else if (h-y > 3) {
						materials[index] = 1;
					} else if (h-y > 1 || y < h_water-1) {
						materials[index] = 3;
					} else {
						materials[index] = 4;
					}
				}
			}
		}
		currentChunk.materials = materials;
		materials = new int[16*16*256];
	}

	private void generateCaves() {
		// TODO Auto-generated method stub
		
	}
	
	private int index2D(int x, int z){
//		System.out.println("index for "+x+", "+z+": "+x+"+"+(z << 4)+" = "+(x+z << 4));
		return x + (z << 4);
	}
	
	private int index3D(int x, int y, int z){
		return x + (z << 4) + (y << 8);
	}
}
