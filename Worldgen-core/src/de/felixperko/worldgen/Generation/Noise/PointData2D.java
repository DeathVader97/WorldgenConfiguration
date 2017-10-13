package de.felixperko.worldgen.Generation.Noise;

public class PointData2D {
    public double value;
    public double ddx;
    public double ddy;
    
    public float getAngle() {
        float angle = (float) Math.toDegrees(Math.atan2(ddy, ddx));

        if(angle < 0){
            angle += 360;
        }

        return angle;
    }
}