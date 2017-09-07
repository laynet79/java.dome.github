/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lthorup.dome;

/**
 *
 * @author layne
 */
public class Sphere {
    public Vec3 center;
    public double radius;
    private boolean exterior;
    
    public Sphere(Vec3 center, double radius, boolean exterior) {
        this.center = center;
        this.radius = radius;
        this.exterior = exterior;
    }
    
    boolean contains(Vec3 pnt) {
        return (Vec3.sub(pnt,center).length() < radius);
    }
    
    Vec3 intersection(Vec3 origin, Vec3 rayIn) {
        Vec3 ray = rayIn.normalized();
        //Vec3 rayToCenter = Vec3.scale(origin,-1.0);
        Vec3 rayToCenter = Vec3.sub(center,origin);
        double rayToCenterSq = rayToCenter.lengthSq();
        double dot = Vec3.dot(ray, rayToCenter);
        double square = dot*dot + radius*radius - rayToCenterSq;
        double len = 0.0;
        if (square < 0.0) {
            // intersection failed so we intersect with a perpendicular plane
            // at the sphere tangent point
            double dist = (rayToCenterSq - radius*radius) / rayToCenter.length();
            len = dist / Vec3.dot(ray, rayToCenter.normalized());
        }
        else {
            double a = Math.sqrt(square);
            len = exterior ? dot - a : dot + a;
        }
        return Vec3.add(Vec3.scale(ray, len), origin);
    }
}
