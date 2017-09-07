/*
 * Vec3d.java
 *
 * Created on September 23, 2007, 8:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.lthorup.dome;


/** This class implements a 3D vector with 32 bit double point components
 *
 * @author Layne
 */
public class Vec3 {
    /** X component of vector */
    public double x;
    /** Y component of vector */
    public double y;
    /** Z component of vector */
    public double z;
    /** Zero vector */
    public static Vec3 zero = new Vec3(0,0,0);
    
    /** Convert to printable string */
    @Override
    public String toString() {
        return String.format("[%f,%f,%f]", x, y, z);
    }
    
    /** Creates a new instance components zeroed */
    public Vec3() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }
    
    /** Creates a new instance of with the given X/Y/Z values
     * @param x X value of vector
     * @param y Y value of vector
     * @param z Z value of vector
     */
    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /** Sets the vector.
     * @param v vertex to get values from
     */
    public void set(Vec3 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
    
    /** Sets the values of the vector.
     * @param x X value of vector
     * @param y Y value of vector
     * @param z Z value of vector
     */
    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /** Compute the length of the vector
     * @return the vector length
     */
    public double length() {
        return (double)Math.sqrt(x*x+y*y+z*z);
    }
    /** Compute the length squared of the vector
     * @return the vector length squared
     */
    public double lengthSq() {
        return x*x+y*y+z*z;
    }
    
    /** Normalizes the vector */
    public void normalize() {
        double oneOverLen = 1.0 / Math.sqrt(x*x+y*y+z*z);
        x *= oneOverLen;
        y *= oneOverLen;
        z *= oneOverLen;
    }
    
    /** Creates a normalized vector */
    public Vec3 normalized() {
        double oneOverLen = 1.0 / Math.sqrt(x*x+y*y+z*z);
        return new Vec3((double)(x*oneOverLen), (double)(y*oneOverLen), (double)(z*oneOverLen));
    }    
    
    /** Scale a vector
     * @param v vector to be scaled
     * @param scale value to scale vector by
     * @return scaled vector
     */
    public static Vec3 scale(Vec3 v, double scale) {
        return new Vec3(v.x*scale, v.y*scale, v.z*scale);
    }
    
    public void scale(double scale) {
        x *= scale; y *= scale; z *= scale;
    }
    
    /** Scale a vector
     * @param v vector to be scaled
     * @param scale value to scale vector by
     * @param result scaled vector
     */
    public static void scale(Vec3 v, double scale, Vec3 result) {
        result.set(v.x*scale, v.y*scale, v.z*scale);
    }

    /** Add two vectors
     * @param a first vector
     * @param b second vector
     * @return sum of vectors
     */
    public static Vec3 add(Vec3 a, Vec3 b) {
        return new Vec3(a.x+b.x, a.y+b.y, a.z+b.z);
    }
    
    /** Add two vectors
     * @param a first vector
     * @param b second vector
     * @param result sum of vectors
     */
    public static void add(Vec3 a, Vec3 b, Vec3 result) {
        result.set(a.x+b.x, a.y+b.y, a.z+b.z);
    }
    
    public void add(Vec3 v) {
        x += v.x; y += v.y; z += v.z;
    }
    
    /** Subtract two vectors
     * @param a first vector
     * @param b second vector
     * @return difference of vectors
     */
    public static Vec3 sub(Vec3 a, Vec3 b) {
        return new Vec3(a.x-b.x, a.y-b.y, a.z-b.z);
    }

    /** Add two vectors
     * @param a first vector
     * @param b second vector
     * @param result sum of vectors
     */
    public static void sub(Vec3 a, Vec3 b, Vec3 result) {
        result.set(a.x-b.x, a.y-b.y, a.z-b.z);
    }

    /** Compute the dot product of two vectors
     * @param a first vector
     * @param b second vector
     * @return dot product of vectors
     */
    public static double dot(Vec3 a, Vec3 b) {
        return a.x*b.x + a.y*b.y + a.z*b.z;
    }
    
    /** Compute the cross product of two vectors
     * @param a first vector
     * @param b second vector
     * @return cross product of vectors
     */
    public static Vec3 cross(Vec3 a, Vec3 b) {
        return new Vec3(a.y*b.z - a.z*b.y, a.z*b.x - a.x*b.z, a.x*b.y - a.y*b.x);
    }
    
    /** Compute the cross product of two vectors
     * @param a first vector
     * @param b second vector
     * @param result cross product of vectors
     */
    public static void cross(Vec3 a, Vec3 b, Vec3 result) {
        result.set(a.y*b.z - a.z*b.y, a.z*b.x - a.x*b.z, a.x*b.y - a.y*b.x);
    }

    /** Convert to polar coordinates */
    public Polar polar() {
		double SMALL_NUM = 1e-6;
	    double heading;
		Vec3 v = normalized();
		double s = Math.sqrt(v.x*v.x+v.y*v.y);
		if (s > SMALL_NUM)
		{
            heading = Math.toDegrees(Math.acos(v.y/s));
            if (x < 0.0)
            	heading = 360.0 - heading;
		}
		else
		{
            // we're pointing straight up or down (handle singularity)
            heading = 0.0;
		}
		double pitch = Math.toDegrees(Math.asin(v.z));
		Polar p = new Polar(heading, pitch);
	    return p;
    }
}
