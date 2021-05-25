package client.gui;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class StudyGroupCircle extends Ellipse2D {

    private double centreX;
    private double centreY;
    private final double radius;
    private final double targetX;
    private final double targetY;
    private double velocity_x;
    private double velocity_y;
    public final Color color;
    public final String key;

    @Override
    public double getX() {
        return this.centreX;
    }

    @Override
    public double getY() {
        return this.centreY;
    }

    @Override
    public double getWidth() {
        return 2*radius;
    }

    @Override
    public double getHeight() {
        return 2*radius;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void setFrame(double x, double y, double w, double h) {
        this.centreX = x;
        this.centreY = y;
    }

    @Override
    public Rectangle2D getBounds2D() {
        return new Rectangle((int)(centreX-radius), (int)(centreY-radius), (int)(2*radius), (int)(2*radius));
    }



    public StudyGroupCircle (double x, double y, double radius, double targetX, double targetY, double velocity_x, double velocity_y, String key, Color color){
        this.centreX = x;
        this.centreY = y;
        this.velocity_x = velocity_x;
        this.velocity_y = velocity_y;
        this.targetX = targetX;
        this.targetY = targetY;
        this.radius = radius;
        this.key = key;
        this.color = color;
    }

    public void onTick(){
        double range = Math.sqrt(Math.pow((int)centreX - (int)targetX, 2) +Math.pow((int)centreY - (int)targetY, 2));

        double accel = 100/(range + 1);
        if (range < 50) {
            velocity_y = velocity_y * 0.995;
            velocity_x = velocity_x * 0.995;

        }
        if(range < 10){
            return;
        }

        velocity_x *= 0.9995;
        velocity_y *= 0.9995;
        double x_accel = accel * (targetX - centreX)/(range+1);
        double y_accel = accel * (targetY - centreY)/(range+1);

        velocity_x += x_accel;
        velocity_y += y_accel;
        this.centreX += velocity_x;
        this.centreY += velocity_y;
    }
}