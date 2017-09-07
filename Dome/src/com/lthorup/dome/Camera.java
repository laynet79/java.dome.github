package com.lthorup.dome;

import com.lthorup.dome.Polar;

public class Camera extends Projector {

	private Vec3 truePos;
	private Mat3 trueRot;
	private Vec3 refADome, refBDome, refCDome;
	
	// constructor
	public Camera(int resX, int resY, double fopX, double fopY) {
		super(resX, resY, fopX, fopY);
	}
	
	@Override
	public void place(Vec3 position, Mat3 rotation) {
		super.place(position, rotation);
		
		// simulate the true position of camera
		this.truePos = Vec3.add(position, new Vec3(0.1, -0.1, 0.2));
		Vec3 angles = Mat3.toHPR(rotation);
		double heading = angles.x;
		double pitch = angles.y;
		double roll = angles.z;
		heading += 0.2;
		pitch -= 0.1;
		roll += 0.3;
		this.trueRot = Mat3.fromHPR(heading, pitch, roll);
		angles = Mat3.toHPR(this.trueRot);
	}
	
	public void setRefPoints(Vec3 refA, Vec3 refB, Vec3 refC) {
		this.refADome = refA;
		this.refBDome = refB;
		this.refCDome = refC;
	}
	
	public void calibrate() {
		Vec3[] Pw = new Vec3[3];
		Pw[0] = refADome;
		Pw[1] = refBDome;
		Pw[2] = refCDome;
		Vec3[] p = new Vec3[3];
		scanRefPoints(p);
		Vec3 camera = new Vec3(position.x, position.y, position.z);
		
		Vec3[] pn = new Vec3[3];
		double[] k = new double[3];
		for (int i = 0; i < 3; i++)
		{
			int j = (i+1) % 3;
			k[i] = Vec3.dot(p[i].normalized(), p[j].normalized());
			pn[i] = p[i].normalized();
		}
		
		Vec3[] P = new Vec3[3];
		double[] I = new double[3];
		double[] J = new double[3];
		double[] K = new double[3];
		double[] dK = new double[3];
		double[] D = new double[3];
		double[] A = new double[3];
		double[] B = new double[3];
		double[] C = new double[3];
		double[] E = new double[3];
		double[] F = new double[3];
		double[] G = new double[3];
		
		for (int iteration = 0; iteration < 10; iteration++) {
			for (int i = 0; i < 3; i++) {
				P[i] = Vec3.sub(Pw[i], camera);
				D[i] = P[i].length();
				P[i] = P[i].normalized();
			}
			for (int i = 0; i < 3; i++) {
				int j = (i+1) % 3;
				K[i] = Vec3.dot(P[i].normalized(), P[j].normalized());
				I[i] = k[i]/D[i] - (1/D[j]);
				J[i] = k[i]/D[j] - (1/D[i]);
				A[i] = P[i].x * I[i] + P[j].x * J[i];
				B[i] = P[i].y * I[i] + P[j].y * J[i];
				C[i] = P[i].z * I[i] + P[j].z * J[i];
			}
			for (int i = 0; i < 3; i++) {
				int j = (i+1) % 3;
				E[i] = A[i]*B[j] - A[j]*B[i];
				F[i] = A[i]*C[j] - A[j]*C[i];
				G[i] = B[i]*C[j] - B[j]*C[i];
				dK[i] = k[i] - K[i];
			}
			double delta = E[0]*C[2] + E[2]*C[1] + E[1]*C[0];
			double deltaX =  (G[1]*dK[0]+G[2]*dK[1]+G[0]*dK[2]) / delta;
			double deltaY = -(F[1]*dK[0]+F[2]*dK[1]+F[0]*dK[2]) / delta;
			double deltaZ =  (E[1]*dK[0]+E[2]*dK[1]+E[0]*dK[2]) / delta;
			
			camera.x += deltaX;
			camera.y += deltaY;
			camera.z += deltaZ;
		}
		
		// use Cramer's rule to solve for the rotation matrix of the camera
		double[] e = new double[3];
		double[] f = new double[3];
		double[] g = new double[3];
		double[] u = new double[3];
		double[] v = new double[3];
		double[] w = new double[3];
		
		for (int i = 0; i < 3; i++)
		{
			int j = (i+1) % 3;
			e[i] = pn[i].x*pn[j].y - pn[j].x*pn[i].y;
			f[i] = pn[i].x*pn[j].z - pn[j].x*pn[i].z;
			g[i] = pn[i].y*pn[j].z - pn[j].y*pn[i].z;
		}
		double delta = e[0]*pn[2].z + e[1]*pn[0].z + e[2]*pn[1].z;
		u[2] = (P[0].z*g[1] + P[1].z*g[2] + P[2].z*g[0]) / delta;
		v[0] = (P[0].x*f[1] + P[1].x*f[2] + P[2].x*f[0]) / (-delta);
		v[1] = (P[0].y*f[1] + P[1].y*f[2] + P[2].y*f[0]) / (-delta);
		v[2] = (P[0].z*f[1] + P[1].z*f[2] + P[2].z*f[0]) / (-delta);
		w[2] = (P[0].z*e[1] + P[1].z*e[2] + P[2].z*e[0]) / delta;
		
		// use the Y vector of the rotation matrix to determine the heading and pitch
		Polar polar = new Vec3(v[0], v[1], v[2]).polar();
		double heading = polar.heading;
		double pitch = polar.pitch;
		double roll = 0;
		
		// use the X and Z vector of the rotation matrix to determine the roll
		double SMALL_NUM = 1e-6;
		if ((90.0 - Math.abs(pitch)) > SMALL_NUM ) {
			roll = -Math.toDegrees(Math.asin(u[2] / Math.cos(Math.toRadians(pitch))));
			if (w[2] < 0.0)
			{
				if (u[2] < 0.0)
					roll = 180 - roll;
				else
					roll = -180 - roll;
			}
		}

		position = camera;
		rotation = Mat3.fromHPR(heading,  pitch,  roll);
	}
	
	private void scanRefPoints(Vec3[] p) {
		Vec3 refA = Mat3.multTranspose(trueRot, Vec3.sub(refADome, truePos));
		Vec3 refB = Mat3.multTranspose(trueRot, Vec3.sub(refBDome, truePos));
		Vec3 refC = Mat3.multTranspose(trueRot, Vec3.sub(refCDome, truePos));
		p[0] = Vec3.scale(refA,  1.0/refA.y);
		p[1] = Vec3.scale(refB , 1.0/refB.y);
		p[2] = Vec3.scale(refC,  1.0/refC.y);
	}
	
}
