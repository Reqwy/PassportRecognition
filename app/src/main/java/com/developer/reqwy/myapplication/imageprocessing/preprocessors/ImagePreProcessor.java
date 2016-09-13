package com.developer.reqwy.myapplication.imageprocessing.preprocessors;

import android.content.Context;
import android.graphics.Bitmap;

import com.developer.reqwy.myapplication.document_templates.DocumentType;
import com.developer.reqwy.myapplication.document_templates.TemplateFactory;
import com.developer.reqwy.myapplication.recognition.RecognizerCallBack;
import com.developer.reqwy.myapplication.recognition.RecognizerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class ImagePreProcessor implements Runnable {

    private ImageSlicer slicer;
    private Bitmap image;
    private boolean land;
    private DocumentType docType;
    private Context context;
    private RecognizerCallBack callBack = new RecognizerCallBack() {
        @Override
        public void onRecognitionFinished(Map<String, String> document) {

            publishRecognitionResults(document);
        }
    };

    public void publishRecognitionResults(Map<String, String> document){

    }

    public ImagePreProcessor(Bitmap imageToProcess,
                             boolean land_orientation,
                             DocumentType type,
                             Context c){
        this.image = imageToProcess;
        this.land = land_orientation;
        this.docType = type;
        this.context = c;
    }

    @Override
    public void run() {
        /*
        4. passToRecognizer
         */
        saveInitial();
        slicer = new ImageSlicer(context, image);
        slicer.slice(docType, land);
        RecognizerFactory factory
                = new RecognizerFactory(context, slicer, TemplateFactory.getTemplate(docType, land));
        factory.getRecognizer(callBack).recognize();
    }

    public void saveInitial() {
        File mFile = new File(context.getExternalFilesDir(null), "initial" + ".png");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(mFile);
            image.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
