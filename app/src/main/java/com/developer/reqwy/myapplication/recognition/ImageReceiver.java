package com.developer.reqwy.myapplication.recognition;


import android.media.Image;

import java.nio.ByteBuffer;

public class ImageReceiver implements Runnable {

    private byte[] imageBytes;

    private static ImageReceiver instance;

    private ImageReceiver(){}

    public static ImageReceiver getInstance(){
        if (instance == null){
            instance = new ImageReceiver();
        }
        return instance;
    }

    public void receive(Image image){
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        imageBytes = new byte[buffer.remaining()];
        buffer.get(imageBytes);
        image.close();
    }

    private void passToRecognizer(){

    }

    @Override
    public void run() {
        passToRecognizer();
    }
}
