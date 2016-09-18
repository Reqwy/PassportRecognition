package com.developer.reqwy.myapplication.imageprocessing.preprocessors;

import android.graphics.Bitmap;
import android.graphics.Color;


public class ProcessingOperations {

    private static final double GS_RED = 0.299;
    private static final double GS_GREEN = 0.587;
    private static final double GS_BLUE = 0.114;

    static double[][] GaussianBlurConfig = new double[][] { { 1, 2, 1 },
            { 2, 4, 2 }, { 1, 2, 1 } };
    static ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
    static {
        convMatrix.applyConfig(GaussianBlurConfig);
        convMatrix.Factor = 16;
        convMatrix.Offset = 0;
    }
    public static Bitmap applyGreyscale(Bitmap src) {
        // constant factors
        final double GS_RED = 0.299;
        final double GS_GREEN = 0.587;
        final double GS_BLUE = 0.114;

        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
                src.getConfig());
        // pixel information
        int A, R, G, B;
        int pixel;

        // get image size
        int width = src.getWidth();
        int height = src.getHeight();

        // scan through every single pixel
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                // get one pixel color
                pixel = src.getPixel(x, y);
                // retrieve color of all channels
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);
                // take conversion up to one single value
                R = G = B = (int) (GS_RED * R + GS_GREEN * G + GS_BLUE * B);
                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        // return final image
        return bmOut;
    }

    public static double countGrayScale(int pixel){

        int R, G, B;
        R = Color.red(pixel);
        G = Color.green(pixel);
        B = Color.blue(pixel);
        return GS_RED * R + GS_GREEN * G + GS_BLUE * B;
    }

    public static Bitmap appblyBinarisation(Bitmap src){
        Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
                src.getConfig());

        int width = src.getWidth();
        int height = src.getHeight();
        int pixel;
        int threshold = 115; // TODO change

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                pixel = src.getPixel(x, y);
                if (countGrayScale(pixel) < threshold){
                    bmOut.setPixel(x, y, Color.rgb(0, 0, 0));
                } else {
                    bmOut.setPixel(x, y, Color.rgb(255, 255, 255));
                }
            }
        }
        return bmOut;
    }


    public static Bitmap applyGaussianBlur(Bitmap src) {
        return ConvolutionMatrix.computeConvolution3x3(src, convMatrix);
    }


}
