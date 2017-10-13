package de.felixperko.worldgen.Generation.Noise;

public class PointData3D {
    public double value;
    public double ddx;
    public double ddy;
    public double ddz;

    public float getYawAngle() {
        float angle = (float) Math.toDegrees(Math.atan2(ddy, ddx));
        if(angle < 0)
            return angle+360;
        return angle;
    }

    public float getPitchAngle() {
        float angle = (float) Math.toDegrees(Math.atan2(ddy, ddx));
        if(angle < 0)
            return angle+360;
        return angle;
    }
}
