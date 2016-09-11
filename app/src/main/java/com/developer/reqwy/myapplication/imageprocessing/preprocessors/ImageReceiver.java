package com.developer.reqwy.myapplication.imageprocessing.preprocessors;


import android.content.Context;
import android.content.res.Configuration;
import android.media.Image;

import com.developer.reqwy.myapplication.document_templates.DocumentType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ImageReceiver implements Runnable {

    private int height;
    private int width;
    private Image image;
    private DocumentType type;
    private int orientation;
    private Context context;
    private File mFile;

    private static ImageReceiver instance;

    public ImageReceiver(Image image, DocumentType type, int orientation, Context context){
        this.image = image;
        this.type = type;
        this.orientation = orientation;
        this.context = context;
        mFile = new File(context.getExternalFilesDir(null), "tmp.jpg");
    }

    public void receive(){

        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            // pass like fusk true
        }
        ImageSlicer slicer = new ImageSlicer(context, width, height);
        slicer.sliceForApi(mFile.getAbsolutePath(), width, type, true);
    }

    private void passToRecognizer(){

    }

    private void save() {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(mFile);
            output.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            image.close();
            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override

    public void run() {
        save();
        receive();
    }
}
