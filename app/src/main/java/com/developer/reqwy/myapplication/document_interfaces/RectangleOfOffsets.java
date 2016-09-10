package com.developer.reqwy.myapplication.document_interfaces;


import android.graphics.Point;

public class RectangleOfOffsets {

    private final Point leftUpCorner;
    private final Point leftBotCorner;
    private final Point righttUpCorner;

    public Point getRightBotCorner() {
        return rightBotCorner;
    }

    public Point getLeftUpCorner() {
        return leftUpCorner;
    }

    public Point getLeftBotCorner() {
        return leftBotCorner;
    }

    public Point getRighttUpCorner() {
        return righttUpCorner;
    }

    private final Point rightBotCorner;

    public RectangleOfOffsets(Point lu, Point lb, Point ru, Point rb){
        leftUpCorner = lu;
        leftBotCorner = lb;
        righttUpCorner = ru;
        rightBotCorner = rb;
    }

    public RectangleOfOffsets(int lux, int luy, int lbx, int lby, int rux, int ruy, int rbx, int rby){
        leftUpCorner = new Point(lux, luy);
        leftBotCorner = new Point(lbx, lby);
        righttUpCorner = new Point(rux, ruy);
        rightBotCorner = new Point(rbx, rby);
    }
}
