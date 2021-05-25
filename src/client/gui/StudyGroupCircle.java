package client.gui;

import common.model.StudyGroup;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class StudyGroupCircle extends Ellipse2D {

    public final StudyGroup studyGroup;
    private double centreX;
    private double centreY;
    public static final double MIN_RADIUS = 20, MAX_RADIUS = 40;
    private double radius = MIN_RADIUS, STEP = 0.2;
    public final Color color;

    @Override
    public double getX() {
        return this.centreX - radius;
    }

    @Override
    public double getY() {
        return this.centreY - radius;
    }

    @Override
    public double getWidth() {
        return 2 * radius;
    }

    @Override
    public double getHeight() {
        return 2 * radius;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void setFrame(double x, double y, double w, double h) {
        this.centreX = x + radius;
        this.centreY = y + radius;
    }

    @Override
    public Rectangle2D getBounds2D() {
        return new Rectangle((int)(centreX - radius), (int)(centreY - radius), (int)(2 * radius), (int)(2 * radius));
    }

    public StudyGroupCircle(double x, double y, StudyGroup studyGroup, Color color){
        this.centreX = x;
        this.centreY = y;
        this.studyGroup = studyGroup;
        this.color = color;
    }

    public synchronized void onTick(){
        radius += STEP;
        if (radius >= MAX_RADIUS || radius <= MIN_RADIUS) STEP = -STEP;
    }
}