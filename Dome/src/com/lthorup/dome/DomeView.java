package com.lthorup.dome;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import com.lthorup.dome.Polar;
import com.lthorup.dome.Sphere;

public class DomeView extends JPanel {

	final double TWOPI = Math.PI * 2.0;
	double CX, CY, W, H;
	
	Sphere dome;
	Projector[] proj = new Projector[5];
	Camera[] camera = new Camera[5];
	Polar selection;
	Font font = new Font("Serif", Font.PLAIN, 12);
	/**
	 * Create the panel.
	 */
	public DomeView() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				selection = plotToPolar(new Vec2(e.getX(), e.getY()));
				repaint();
			}
		});
		dome = new Sphere(new Vec3(0,0,0), 20, false);
		double h = -100;
		for (int i = 0; i < 5; i++) {
			proj[i] = new Projector(1920, 1200, 60, 40);
			Mat3 rot = Mat3.fromHPR(h, 0, 0);
			proj[i].place(new Vec3(0,0,0), rot);
			camera[i] = new Camera(1280, 1024, 60, 40);
			camera[i].place(new Vec3(0,0,0.6), rot);
			Vec3 refA = Mat3.mult(rot, new Vec3(0,20,1));
			Vec3 refB = Mat3.mult(rot,  new Vec3(-1, 20, -1));
			Vec3 refC = Mat3.mult(rot,  new Vec3(1, 20, -1));
			camera[i].setRefPoints(refA, refB, refC);
			camera[i].calibrate();
			
			h += 50;
		}
		selection = new Polar(0,0);
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(0, 0, (int)getBounds().getWidth(), (int)getBounds().getHeight());
		g.setColor(Color.black);
		plotDome(g);
		g.setColor(Color.red);
		for (int i = 0; i < 5; i++)
			plotProj(g, proj[i]);
	}
	
	protected void plotDome(Graphics g) {
		W = getBounds().getWidth();
		H = getBounds().getHeight();
		CX = W / 2.0;
		CY = H / 2.0;
		double STEP = 5.0;
	    Polar   p = new Polar();
	    
	    // Draw the longitude lines of the dome first, from -180 degrees to 180
	    // degrees, in 30 degree increments.
	    for (p.heading = -180; p.heading <= 180; p.heading += 30)
	    {
	    	p.pitch = -90;
			Vec2 e = polarToPlot(p);
			for (p.pitch = -90 + STEP; p.pitch <= 90; p.pitch += STEP)
			{
				Vec2 s = e;
			    e = polarToPlot(p);
			    g.drawLine((int)s.x, (int)s.y, (int)e.x, (int)e.y);
			}
	    }

	    // Draw in the latitude lines of the plot from -60 degrees to 60 degrees.
	    // The -90,90 latitudes are already drawn.
	    for (p.pitch = -60; p.pitch <= 60; p.pitch += 30)
	    {
	    	p.heading = -180;
			Vec2 e = polarToPlot(p);
			for (p.heading = -180 + STEP; p.heading <= 180; p.heading += STEP)
			{
				Vec2 s = e;
			    e = polarToPlot(p);
			    g.drawLine((int)s.x, (int)s.y, (int)e.x, (int)e.y);
			}
	    }
	    
	    Vec2 v = polarToPlot(selection);
	    g.setColor(Color.GREEN);
	    g.fillRect((int)v.x-2, (int)v.y-2, 4,4);
	    String s = String.format("%.2f %.2f", selection.heading, selection.pitch);
	    g.setFont(font);
	    g.setColor(Color.black);
	    g.drawString(s, 10, 10);
	}
	
	protected void plotProj(Graphics g, Projector proj) {
		drawProjEdge(g, proj, proj.ul(), proj.ur());
		drawProjEdge(g, proj, proj.ul(), proj.ll());
		drawProjEdge(g, proj, proj.ll(), proj.lr());
		drawProjEdge(g, proj, proj.ur(), proj.lr());
	}
	
	protected void drawProjEdge(Graphics g, Projector proj, Vec3 a, Vec3 b) {
		Vec3 d = Vec3.scale(Vec3.sub(b, a), 1.0/100);
		Vec2 e = polarToPlot(projToDome(proj, a).polar());
		Vec3 v = Vec3.add(a, d);
		for (int i = 0; i < 100; i++) {
			Vec2 s = e;
			e = polarToPlot(projToDome(proj, v).polar());
			v = Vec3.add(v, d);
		    g.drawLine((int)s.x, (int)s.y, (int)e.x, (int)e.y);
		}
	}
	
	protected Vec2 polarToPlot(Polar n) {
	    Vec2 pix = new Vec2();

		double h = Math.toRadians(n.heading);
		double p = Math.toRadians(n.pitch);

	    if (h > (Math.PI + 0.009))
	    	h -= TWOPI;
	    else if( h < -(Math.PI + 0.009) )
	    	h += TWOPI;

	    double h2 = h / 2;
	    double cosp = Math.cos(p);
	    double s2 = Math.sqrt(2.0);
	    double k = 1.0 / Math.sqrt(1 + cosp * Math.cos(h2));
	    
	    double x = 2.0 * s2 * cosp * Math.sin(h2) * k;
	    double y = s2 * Math.sin(p) * k;
	    
	    pix.x =  x * W / (4.1 * s2) + CX;
	    pix.y = -y * H / (2.1 * s2) + CY;

	    return pix;
	}
	
	protected Polar plotToPolar(Vec2 pix) {
		
		double s2 = Math.sqrt(2.0);
		double x = (pix.x - CX) * 4.1 * s2 / W;
		double y = -(pix.y - CY) * 2.1 * s2 / H;
		double z = Math.sqrt(1.0 - (0.25*x)*(0.25*x) - (0.5*y)*(0.5*y));
		
		double h = 2.0 * Math.atan(z*x/(2.0*(2.0*z*z-1.0)));
		double p = Math.asin(z*y);
		return new Polar(Math.toDegrees(h),Math.toDegrees(p));
	}
	
	/*
	protected Vec2 polarToPlot(Polar n) {
	    Vec2 pix = new Vec2();

		double h = Math.toRadians(n.heading);
		double p = Math.toRadians(n.pitch);

	    if (h > (Math.PI + 0.009))
	    	h -= TWOPI;
	    else if( h < -(Math.PI + 0.009) )
	    	h += TWOPI;

	    double h2 = h / 2;
	    double cosp = Math.cos(p);
	    double k = (H*0.48) / Math.sqrt(1 + cosp * Math.cos(h2));

	    pix.x = ((W/H) * k * cosp * Math.sin( h2 )) + CX;
	    pix.y = -(k * Math.sin(p)) + CY;

	    return pix;
	}
	*/

	
	Vec3 projToDome(Projector proj, Vec3 p) {
		Vec3 e = Mat3.mult(proj.rotation(), p);
		return dome.intersection(proj.position(), e);
	}

}
