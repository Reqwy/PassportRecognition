package com.developer.reqwy.myapplication.document_templates;

import android.graphics.Point;


public class DocumentFieldRectangle {


    private Point leftUpCorner;
    private Point leftButtomCorner;
    private Point rightUpCorner;
    private Point rightBottomCorner;

    private int width;
    private int height;

    public DocumentFieldRectangle(int lux, int luy, int lbx,
                                  int lby, int rux, int ruy, int rbx, int rby){
        this(
                new Point(lux, luy),
                new Point(lbx, lby),
                new Point(rux, ruy),
                new Point(rbx, rby));
    }

    public DocumentFieldRectangle(Point lu, Point lb, Point ru, Point rb){
        this.leftUpCorner = lu;
        this.leftButtomCorner = lb;
        this.rightUpCorner = ru;
        this.rightBottomCorner = rb;
    }

    public DocumentFieldRectangle(int lx, int ly, int width, int height){
        this(new Point(lx, ly), new Point(lx, ly + height),
                new Point(lx + width, ly), new Point(lx + width, ly + height));
        setWidth(width);
        setHeight(height);
    }

    public Point leftUp(){
        return leftUpCorner;
    }

    public Point leftBottom(){
        return leftButtomCorner;
    }

    public Point rightUp(){
        return rightUpCorner;
    }

    public Point rightBottom(){
        return rightBottomCorner;
    }
    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    private void setWidth(int width) {
        this.width = width;
    }

    private void setHeight(int height) {
        this.height = height;
    }
}
