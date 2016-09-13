package com.developer.reqwy.myapplication.recognition;

import android.app.Activity;
import android.util.Log;

import com.developer.reqwy.myapplication.document_templates.DocumentTemplate;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class APIRecognizer implements Recognizer {

    private static final String API_KEY = "c1c8e0112e88957";
    private static final String language = "rus";
    private Activity context;
    private Map<String, File> files;
    private DocumentTemplate template;

    private IOCRCallBack callBack = new IOCRCallBack() {

        private  Map<String, String> results = new ConcurrentHashMap<>();

        @Override
        public void getOCRCallBackResult(String field, String response) {
            onPartialResult(field, response);
            synchronized (this) {
                if (results.keySet().size() == template.getFieldNames().size()){
                    String finalRes = "";
                    for (String key : results.keySet()) {
                        finalRes += results.get(key) + "\n";
                    }
                    Log.d("API_RECOGNIZER", "RESULT IS FORMED");
                    Log.d("API_RECOGNIZER", finalRes);
                } else {
                    Log.d("API_RECOGNIZER", "Processing: "
                            + ((float)results.keySet().size() / template.getFieldNames().size()) * 100
                    + "%");
                }
            }
        }

        public synchronized void onPartialResult(String field, String response){
            results.put(field, response);
        }
    };

    public APIRecognizer(Activity context, Map<String, File> files, DocumentTemplate template){
        this.context = context;
        this.files = files;
        this.template = template;
    }
    @Override
    public Map<String, String> recognize() {
        for (String field : files.keySet()) {
            File file = files.get(field);
            String fieldLanguage = template.getField("Имя").getLanguage();
            OCRAsyncTask oCRAsyncTask = new OCRAsyncTask(context, API_KEY, false, file,
                    fieldLanguage == null ? language : fieldLanguage, field,
                    callBack);
            oCRAsyncTask.execute();
        }
        return null;
    }
}
