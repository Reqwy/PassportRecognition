package com.developer.reqwy.myapplication.recognition;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.developer.reqwy.myapplication.document_templates.DocumentTemplate;
import com.developer.reqwy.myapplication.imageprocessing.preprocessors.ImageSlicer;

public class RecognizerFactory {

    private Context context;
    private ImageSlicer slicer;
    private DocumentTemplate template;
    boolean lastProcessingWasInternet = false;


    public RecognizerFactory(Context context, ImageSlicer slicer,
                             DocumentTemplate template){
        this.context = context;
        this.slicer = slicer;
        this.template = template;
    }

    public Recognizer getRecognizer(RecognizerCallBack callBack){
        if (checkInternetConnection()){
            lastProcessingWasInternet = true;
            return new APIRecognizer((Activity) context, slicer.getFilesForApi(), template, callBack);
        } else {
            lastProcessingWasInternet = false;
            return new TesseractProcessing(context, slicer.getBitmapsForTesseract(), template, callBack);
        }
    }

    public TesseractProcessing getTesseractRecognizer(RecognizerCallBack callBack){
        return new TesseractProcessing(context, slicer.getBitmapsForTesseract(), template, callBack);
    }

    private boolean checkInternetConnection(){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
