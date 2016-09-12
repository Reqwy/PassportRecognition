package com.developer.reqwy.myapplication.recognition;

import android.app.Activity;

import java.io.File;
import java.util.List;
import java.util.Map;


public class APIRecognizer implements Recognizer {

    private static final String API_KEY = "c1c8e0112e88957";
    private static final String language = "rus";
    private Activity context;
    private Map<String, File> files;

    public APIRecognizer(Activity context, Map<String, File> files){
        this.context = context;
        this.files = files;
    }
    @Override
    public Map<String, String> recognize() {
//        for (String field: files.keySet()) {
            File f = files.get("Имя");
            OCRAsyncTask oCRAsyncTask = new OCRAsyncTask(context, API_KEY, false, f, language,
                    new IOCRCallBack() {
                        @Override
                        public void getOCRCallBackResult(String response) {
                            String m = response;
                        }
                    });
            oCRAsyncTask.execute();
//        }
        return null;
    }
}
