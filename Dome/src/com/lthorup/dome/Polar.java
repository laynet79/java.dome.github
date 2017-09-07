/*
 * Polar.java
 *
 * Created on September 26, 2007, 1:13 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.lthorup.dome;

/**
 *
 * @author Layne
 */
public class Polar {
    /** Heading angle in degrees */
    public double heading;
    /** Pitch angle in degrees */
    public double pitch;
    
    /** Convert to printable string */
    @Override
    public String toString() {
        return String.format("[%f,%f]", heading, pitch);
    }

    /** Creates a new instance of Polar with angles zeroed out */
    public Polar() {
    }
    
    /** Creates a new instance of Polar with the given heading and pitch angles in degrees */
    public Polar(double heading, double pitch) {
        this.heading = heading;
        this.pitch = pitch;
    }
    
    /** Convert to rectangular */
    public Vec3 rectangular() {
		double RAD = Math.PI / 180.0;
	
		double z = Math.sin(pitch*RAD);
		double s = Math.cos(pitch*RAD);
		double x = Math.sin(heading*RAD) * s;
		double y = Math.cos(heading*RAD) * s;
	        return new Vec3(x, y, z);
    }
}
