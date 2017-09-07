/*
 * Vec3d.java
 *
 * Created on September 23, 2007, 8:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.lthorup.dome;

/**
 *
 * @author layne
 */
public class Vec2 {

    /** X component of vector */
    public double x;
    /** Y component of vector */
    public double y;
    
    public static Vec2 zero = new Vec2(0,0);
    
    /** Convert to printable string */
    @Override
    public String toString() {
        return String.format("[%f,%f]", x, y);
    }
    
    /** Creates a new instance components zeroed */
    public Vec2() {
        this.x = 0;
        this.y = 0;
    }
    
    /** Creates a new instance of with the given X/Y values
     * @param x X value of vector
     * @param y Y value of vector
     */
    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /** Sets the vector.
     * @param v vertex to get values from
     */
    public void set(Vec2 v) {
        this.x = v.x;
        this.y = v.y;
    }
    
    /** Sets the values of the vector.
     * @param x X value of vector
     * @param y Y value of vector
     */
    public void set(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    /** Compute the length of the vector
     * @return the vector length
     */
    public double length() {
        return (double)Math.sqrt(x*x+y*y);
    }
    
    /** Normalizes the vector */
    public void normalize() {
        double oneOverLen = 1.0 / Math.sqrt(x*x+y*y);
        x *= oneOverLen;
        y *= oneOverLen;
    }
    
    /** Creates a normalized vector */
    public Vec2 normalized() {
        double oneOverLen = 1.0 / Math.sqrt(x*x+y*y);
        return new Vec2((double)(x*oneOverLen), (double)(y*oneOverLen));
    }    
    
    /** Scale a vector
     * @param v vector to be scaled
     * @param scale value to scale vector by
     * @return scaled vector
     */
    public static Vec2 scale(Vec2 v, double scale) {
        return new Vec2(v.x*scale, v.y*scale);
    }
    
    public void scale(double scale) {
        x *= scale; y *= scale;
    }
    
    /** Scale a vector
     * @param v vector to be scaled
     * @param scale value to scale vector by
     * @param result scaled vector
     */
    public static void scale(Vec2 v, double scale, Vec2 result) {
        result.set(v.x*scale, v.y*scale);
    }

    /** Add two vectors
     * @param a first vector
     * @param b second vector
     * @return sum of vectors
     */
    public static Vec2 add(Vec2 a, Vec2 b) {
        return new Vec2(a.x+b.x, a.y+b.y);
    }
    
    /** Add two vectors
     * @param a first vector
     * @param b second vector
     * @param result sum of vectors
     */
    public static void add(Vec2 a, Vec2 b, Vec2 result) {
        result.set(a.x+b.x, a.y+b.y);
    }
    
    public void add(Vec2 v) {
        x += v.x; y += v.y;
    }
    
    /** Subtract two vectors
     * @param a first vector
     * @param b second vector
     * @return difference of vectors
     */
    public static Vec2 sub(Vec2 a, Vec2 b) {
        return new Vec2(a.x-b.x, a.y-b.y);
    }

    /** Add two vectors
     * @param a first vector
     * @param b second vector
     * @param result sum of vectors
     */
    public static void sub(Vec2 a, Vec2 b, Vec2 result) {
        result.set(a.x-b.x, a.y-b.y);
    }

    /** Compute the dot product of two vectors
     * @param a first vector
     * @param b second vector
     * @return dot product of vectors
     */
    public static double dot(Vec2 a, Vec2 b) {
        return a.x*b.x + a.y*b.y;
    }
    
    /** Compute the cross product of two vectors
     * @param a first vector
     * @param b second vector
     * @return cross product of vectors
     */
    public static double cross(Vec2 a, Vec2 b) {
        return a.x*b.y - a.y*b.x;
    }
}
