package com.developer.reqwy.myapplication;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.TextureView;


public class DocumentInterfaceDrawer {

    private static DocumentInterfaceDrawer instance;

    private DocumentInterfaceDrawer(){}

    private int xCenter;
    private int yCenter;

    public static DocumentInterfaceDrawer instance(){
        if (instance == null){
            instance = new DocumentInterfaceDrawer();
        }
        return instance;
    }

    public void drawInterface(TextureView textureView, int doctype, int orientation){
        getCenterPoint(textureView);
        Canvas canvas = textureView.lockCanvas();
        if (canvas != null) {
            paint(canvas, doctype, orientation);
            textureView.unlockCanvasAndPost(canvas);
        }
    }

    private void getCenterPoint(TextureView textureView){
        xCenter = textureView.getWidth() / 2;
        yCenter = textureView.getHeight() / 2;
    }

    private void paint(Canvas canvas, int doctype, int orientation){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3);
        canvas.drawRect(0, 0, 150, 150, paint);
    }

    public void redraw(TextureView textureView, int doctype, int orientation){
        getCenterPoint(textureView);
        Canvas canvas = textureView.lockCanvas();
        if (canvas != null) {
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            paint(canvas, doctype, orientation);
            textureView.unlockCanvasAndPost(canvas);
        }
    }
}
