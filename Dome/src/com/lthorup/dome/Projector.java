package com.lthorup.dome;

public class Projector {

	protected int  resX, resY;
	protected double fopX, fopY;
	protected Vec3 position;
	protected Mat3 rotation;
	protected double hResX, hResY;
	protected double scaleX, scaleY;
	
	public Projector(int resX, int resY, double fopX, double fopY) {
		this.resX = resX;
		this.resY = resY;
		hResX = resX / 2.0;
		hResY = resY / 2.0;
		scaleX = Math.tan(Math.toRadians(fopX/2.0)) / hResX;
		scaleY = Math.tan(Math.toRadians(fopY/2.0)) / hResY;
		position = new Vec3(0,0,0);
		rotation = Mat3.identity();
	}
	
	public void place(Vec3 position, Mat3 rotation) {
		this.position = position;
		this.rotation = rotation;		
	}
	
	public int resX() { return resX; }
	public int resY() { return resY; }
	public double fopX() { return fopX; }
	public double fopY() { return fopY; }
	public Vec3 position() { return position; }
	public Mat3 rotation() { return rotation; }
	public Vec3 ul() { return project(new Vec2(0, 0)); }
	public Vec3 ur() { return project(new Vec2(resX, 0)); }
	public Vec3 ll() { return project(new Vec2(0, resY)); }
	public Vec3 lr() { return project(new Vec2(resX, resY)); }
	
	public Vec3 project(Vec2 pixel) {
		return new Vec3((pixel.x - hResX) * scaleX, 1.0, (-pixel.y + hResY) * scaleY).normalized();
	}
	
	public Vec2 unProject(Vec3 v) {
		Vec3 s = Vec3.scale(v, 1.0/v.y);
		return new Vec2((s.x/scaleX) + hResX, (s.y/scaleY) - hResY);
	}
	
}
