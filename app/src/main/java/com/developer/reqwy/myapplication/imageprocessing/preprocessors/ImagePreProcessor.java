package com.developer.reqwy.myapplication.imageprocessing.preprocessors;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.developer.reqwy.myapplication.PreviewActivity;
import com.developer.reqwy.myapplication.document_templates.DocumentType;
import com.developer.reqwy.myapplication.document_templates.PassportTemplate;
import com.developer.reqwy.myapplication.document_templates.TemplateFactory;
import com.developer.reqwy.myapplication.recognition.RecognizerCallBack;
import com.developer.reqwy.myapplication.recognition.RecognizerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImagePreProcessor implements Runnable {

    private ImageSlicer slicer;
    private Bitmap image;
    private boolean land;
    private DocumentType docType;
    private Fragment context;
    private RecognizerFactory factory;

    public static final String request = "GET_YOUR_RESULTS";
    public static final int requestCode = 777;

    public static final String DOCTYPE_EXTRA = "DOC_TYPE_EXTRA";

    private RecognizerCallBack callBack = new RecognizerCallBack() {
        private  boolean correctionRun = false;
        Map<String, String> tempDocument;
        private boolean debug = false;

        @Override
        public void onRecognitionFinished(Map<String, String> document) {

            if (debug) {
                publishRecognitionResults(document);
            } else {

                Log.d("Processing", "Recognition finished");
                if (!correctionRun) {
                    if (containsUnrecognized(document)) {
                        Log.d("Processing", "Some troubles. Starting Correction run");
                        correctionRun = true;
                        tempDocument = document;
                        factory.getTesseractRecognizer(callBack)
                                .correctionRecognition(prepareCorrectionMap(document));
                    } else {
                        Log.d("Processing", "publishing");
                        publishRecognitionResults(document);
                    }
                } else {
                    Log.d("Processing", "Correction finished");
                    for (String key : document.keySet()) {
                        tempDocument.remove(key);
                        tempDocument.put(key, document.get(key));
                    }
                    publishRecognitionResults(tempDocument);
                }
            }
        }

        public boolean containsUnrecognized(Map<String, String> document){
            for (String key : document.keySet()){
                if (document.get(key).equals("Unrecognized")){
                    return true;
                }
            }
            return false;
        }

        public Map<String, String> prepareCorrectionMap(Map<String, String> doc){
            Map<String, String> res = new HashMap<>();
            for (String key : doc.keySet()){
                if (doc.get(key).equals("Unrecognized")){
                    res.put(key, doc.get(key));
                }
            }
            return res;
        }
    };

    private void serializeDocToIntent(Intent i, Map<String, String> doc){
        i.putExtra(DOCTYPE_EXTRA, docType.name());
        for (String s : doc.keySet()){
            i.putExtra(s, doc.get(s));
        }
    }


    public void publishRecognitionResults(Map<String, String> document){
        Log.d("Processing", "In publishing.");
        Intent i = new Intent(context.getActivity(), PreviewActivity.class);
        serializeDocToIntent(i, document);
        context.startActivityForResult(i, requestCode);
    }

    public ImagePreProcessor(Bitmap imageToProcess,
                             boolean land_orientation,
                             DocumentType type,
                             Fragment c){
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
        slicer = new ImageSlicer(context.getActivity(), image);
        slicer.slice(docType, land);
        factory
                = new RecognizerFactory(context.getActivity(), slicer, TemplateFactory.getTemplate(docType, land));
        factory.getRecognizer(callBack).recognize();
    }

    public void saveInitial() {
        File mFile = new File(context.getActivity().getExternalFilesDir(null), "initial" + ".png");
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
