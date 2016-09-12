package com.developer.reqwy.myapplication.recognition;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.developer.reqwy.myapplication.imageprocessing.preprocessors.ImageSlicer;

public class RecognizerFactory {

    private Context context;
    private ImageSlicer slicer;


    public RecognizerFactory(Context context, ImageSlicer slicer){
        this.context = context;
        this.slicer = slicer;
    }


    public Recognizer getRecognizer(){
        if (checkInternetConnection()){
            return new APIRecognizer(slicer.getFilesForApi());
        } else {
            return new TesseractProcessing(context, slicer.getBitmapsForTesseract());
        }
    }

    private boolean checkInternetConnection(){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork.isConnected();
    }
}
