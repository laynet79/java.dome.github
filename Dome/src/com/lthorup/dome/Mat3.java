/*
 * Mat3d.java
 *
 * Created on September 26, 2007, 12:01 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.lthorup.dome;

import com.lthorup.dome.Polar;

/**
 *
 * @author Layne
 */
public class Mat3 {
    /** matrix component */
    public double m00, m01, m02;
    /** matrix component */
    public double m10, m11, m12;
    /** matrix component */
    public double m20, m21, m22;
    
    /** Creates a new instance of Mat3d zeroed out */
    public Mat3() {
    }
    
    /** Creates a new instance of a Mat3d using provided values */
    public Mat3(double m00, double m01, double m02,
                 double m10, double m11, double m12,
                 double m20, double m21, double m22) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02;
        this.m10 = m10; this.m11 = m11; this.m12 = m12;
        this.m20 = m20; this.m21 = m21; this.m22 = m22;
    }
    
    /** Set a matrix to the given values */
    public void set(double m00, double m01, double m02,
                    double m10, double m11, double m12,
                    double m20, double m21, double m22) {
        this.m00 = m00; this.m01 = m01; this.m02 = m02;
        this.m10 = m10; this.m11 = m11; this.m12 = m12;
        this.m20 = m20; this.m21 = m21; this.m22 = m22;
    }
    
    /** Set a matrix to match given matrix */
    public void set(Mat3 m) {
        this.m00 = m.m00; this.m01 = m.m01; this.m02 = m.m02;
        this.m10 = m.m10; this.m11 = m.m11; this.m12 = m.m12;
        this.m20 = m.m20; this.m21 = m.m21; this.m22 = m.m22;
    }
    
    /** Create an identity matrix */
    public static Mat3 identity() {
        return new Mat3(1.0, 0.0, 0.0,
                 		0.0, 1.0, 0.0,
                 		0.0, 0.0, 1.0);
    }
    
    /** Set the matrix to identity */
    public void setIdentity() {
        m00 = 1.0; m01 = 0.0; m02 = 0.0;
        m10 = 0.0; m11 = 1.0; m12 = 0.0;
        m20 = 0.0; m21 = 0.0; m22 = 1.0;
    }

    /** Creates a new instance of Mat3d using X/Y/Z column vectors */
    public Mat3(Vec3 X, Vec3 Y, Vec3 Z) {
        m00 = X.x; m01 = Y.x; m02 = Z.x;
        m10 = X.y; m11 = Y.y; m12 = Z.y;
        m20 = X.z; m21 = Y.z; m22 = Z.z;
    }
    
    /** Set the matrix components using X/Y/Z column vectors */
    public void set(Vec3 X, Vec3 Y, Vec3 Z) {
        m00 = X.x; m01 = Y.x; m02 = Z.x;
        m10 = X.y; m11 = Y.y; m12 = Z.y;
        m20 = X.z; m21 = Y.z; m22 = Z.z;
    }

    /** Get the X column vector of the matrix 
     * @ return X column
     */
    public Vec3 x() {
        return new Vec3(m00, m10, m20);
    }
    
    /** Get the Y column vector of the matrix 
     * @ return Y column
     */
    public Vec3 y() {
        return new Vec3(m01, m11, m21);
    }
    
    /** Get the Z column vector of the matrix 
     * @ return Z column
     */
    public Vec3 z() {
        return new Vec3(m02, m12, m22);
    }

    /** Create transpose matrix */
    public void transpose() {
        double 	t = m10; m10 = m01; m01 = t;
        		t = m20; m20 = m02; m02 = t;
        		t = m21; m21 = m12; m12 = t;
    }
    
    /** Create transpose matrix
     * @return transpose of the given matrix
     */
    public Mat3 transposed() {
        return new Mat3(m00, m10, m20,
        				m01, m11, m21,
        				m02, m12, m22);
    }

    /** Creates a rotation matrix from the given angles
     * @param heading angle
     * @param pitch angle
     * @param roll angle
     * @return rotation matrix that rotates child to parent
     */
    public static Mat3 fromHPR(double heading, double pitch, double roll) {
    	double RAD = Math.PI / 180.0;
    	double H = heading * RAD;
    	double P = pitch * RAD;
        double R = roll * RAD;
        double sh = Math.sin(H); double ch = Math.cos(H);
        double sp = Math.sin(P); double cp = Math.cos(P);
        double sr = Math.sin(R); double cr = Math.cos(R);

        return new Mat3((cr*ch + sr*sp*sh), (cp*sh), (sr*ch - cr*sp*sh),
        				(sr*sp*ch - cr*sh), (cp*ch), (-sr*sh - cr*sp*ch),
						(-sr*cp),			sp,		 (cr*cp));
    }
    
    /** Set a rotation matrix from the given angles
     * @param heading angle
     * @param pitch angle
     * @param roll angle
     * @param result rotation matrix that rotates child to parent
     */
    public static void setFromHPR(double heading, double pitch, double roll, Mat3 result) {
    	double RAD = Math.PI / 180.0;
    	double H = heading * RAD;
    	double P = pitch * RAD;
        double R = roll * RAD;
        double sh = Math.sin(H); double ch = Math.cos(H);
        double sp = Math.sin(P); double cp = Math.cos(P);
        double sr = Math.sin(R); double cr = Math.cos(R);

        result.set((cr*ch + sr*sp*sh),(sr*sp*ch - cr*sh), (-sr*cp),
                   (cp*sh),           (cp*ch),            sp,
                   (sr*ch - cr*sp*sh),(-sr*sh - cr*sp*ch),(cr*cp));
    }
    
    public static Vec3 toHPR(Mat3 mat) {
		// use the Y vector of the rotation matrix to determine the heading and pitch
    	Vec3 forward = mat.y();
		Polar polar = forward.polar();
		double heading = polar.heading;
		double pitch = polar.pitch;
		double roll = 0;
		
		// use the X and Z vector of the rotation matrix to determine the roll
		Vec3 right = mat.x();
		Vec3 up = mat.z();
		double SMALL_NUM = 1e-6;
		if ((90.0 - Math.abs(pitch)) > SMALL_NUM ) {
			roll = -Math.toDegrees(Math.asin(right.z / Math.cos(Math.toRadians(pitch))));
			if (up.z < 0.0)
			{
				if (right.z < 0.0)
					roll = 180 - roll;
				else
					roll = -180 - roll;
			}
		}
    	return new Vec3(heading, pitch, roll);
    }

    /** Matrix multiplication
     * @param m first matrix
     * @param n second matrix
     * @return m x n
     */
    public static Mat3 mult(Mat3 m, Mat3 n) {
        return new Mat3(m.m00*n.m00+m.m01*n.m10+m.m02*n.m20, m.m00*n.m01+m.m01*n.m11+m.m02*n.m21, m.m00*n.m02+m.m01*n.m12+m.m02*n.m22,
                    	m.m10*n.m00+m.m11*n.m10+m.m12*n.m20, m.m10*n.m01+m.m11*n.m11+m.m12*n.m21, m.m10*n.m02+m.m11*n.m12+m.m12*n.m22,
                    	m.m20*n.m00+m.m21*n.m10+m.m22*n.m20, m.m20*n.m01+m.m21*n.m11+m.m22*n.m21, m.m20*n.m02+m.m21*n.m12+m.m22*n.m22);
    }
    
    /** Matrix multiplication
     * @param m first matrix
     * @param n second matrix
     * @param result m x n
     */
    public static void mult(Mat3 m, Mat3 n, Mat3 result) {
        result.set(m.m00*n.m00+m.m01*n.m10+m.m02*n.m20, m.m00*n.m01+m.m01*n.m11+m.m02*n.m21, m.m00*n.m02+m.m01*n.m12+m.m02*n.m22,
                   m.m10*n.m00+m.m11*n.m10+m.m12*n.m20, m.m10*n.m01+m.m11*n.m11+m.m12*n.m21, m.m10*n.m02+m.m11*n.m12+m.m12*n.m22,
                   m.m20*n.m00+m.m21*n.m10+m.m22*n.m20, m.m20*n.m01+m.m21*n.m11+m.m22*n.m21, m.m20*n.m02+m.m21*n.m12+m.m22*n.m22);
    }

    /** Matrix vector multiplication
     * @param m matrix
     * @param v vector
     * @return m . v
     */
    public static Vec3 mult(Mat3 m, Vec3 v) {
    	return new Vec3(m.m00*v.x + m.m01*v.y + m.m02*v.z,
    					m.m10*v.x + m.m11*v.y + m.m12*v.z,
    					m.m20*v.x + m.m21*v.y + m.m22*v.z);
    }

    /** Matrix vector multiplication
     * @param m matrix
     * @param v vector
     * @param result m . v
     */
    public static void mult(Mat3 m, Vec3 v, Vec3 result) {
    	result.set(m.m00*v.x + m.m01*v.y + m.m02*v.z,
                   m.m10*v.x + m.m11*v.y + m.m12*v.z,
                   m.m20*v.x + m.m21*v.y + m.m22*v.z);
    }
    
    /** Matrix vector multiplication
     * @param m matrix
     * @param v vector
     * @return m . v
     */
    public static Vec3 multTranspose(Mat3 m, Vec3 v) {
    	return new Vec3(m.m00*v.x + m.m10*v.y + m.m20*v.z,
    					m.m01*v.x + m.m11*v.y + m.m21*v.z,
    					m.m02*v.x + m.m12*v.y + m.m22*v.z);
    }

    /** Matrix vector multiplication
     * @param m matrix
     * @param v vector
     * @param result m . v
     */
    public static void multTranspose(Mat3 m, Vec3 v, Vec3 result) {
    	result.set(m.m00*v.x + m.m10*v.y + m.m20*v.z,
                   m.m01*v.x + m.m11*v.y + m.m21*v.z,
                   m.m02*v.x + m.m12*v.y + m.m22*v.z);
    }
}
