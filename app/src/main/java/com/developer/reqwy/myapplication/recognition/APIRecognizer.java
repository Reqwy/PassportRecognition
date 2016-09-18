package com.developer.reqwy.myapplication.recognition;

import android.app.Activity;
import android.app.ProgressDialog;
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
    private ProgressDialog mProgressDialog;
    private RecognizerCallBack resultcallBack;

    private IOCRCallBack callBack = new IOCRCallBack() {

        private  Map<String, String> results = new ConcurrentHashMap<>();

        @Override
        public void getOCRCallBackResult(String field, String response) {
            onPartialResult(field, response);
            synchronized (this) {
                if (results.keySet().size() == template.getFieldNames().size()){
                    String finalRes = "";
                    for (String key : results.keySet()) {
                        finalRes += key + ": " + results.get(key) + "\n";
                    }
                    Log.d("API_RECOGNIZER", "RESULT IS FORMED");
                    Log.d("API_RECOGNIZER", finalRes);
                    finish();
                } else {
                    Log.d("API_RECOGNIZER", "Processing: "
                            + ((float)results.keySet().size() / template.getFieldNames().size()) * 100
                    + "%");
                }
            }
        }

        public synchronized void onPartialResult(String field, String response){
            results.put(field, response);
            mProgressDialog.incrementProgressBy(1);
        }

        private void finish(){
            if (mProgressDialog != null && mProgressDialog.isShowing())
                 mProgressDialog.dismiss();
            resultcallBack.onRecognitionFinished(results);
        }
    };

    public APIRecognizer(Activity context, Map<String, File> files, DocumentTemplate template,
                         RecognizerCallBack callBack){
        this.context = context;
        this.files = files;
        this.template = template;
        this.resultcallBack = callBack;
    }

    private void initProgressBar(){
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setTitle("Документ распознаётся....");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMax(template.getFieldNames().size());
        mProgressDialog.show();

    }
    @Override
    public void recognize() {
        initProgressBar();
        for (final String field : files.keySet()) {
            final File file = files.get(field);
            final String fieldLanguage = template.getField(field).getLanguage();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    OCRAsyncTask oCRAsyncTask = new OCRAsyncTask(API_KEY, false, file,
                            fieldLanguage == null ? language : fieldLanguage, field,
                            callBack);
                    oCRAsyncTask.execute();
                }
            });
            t.start();
            Log.d("APIRecognizer", "Thread for field " + field + " started.");
        }
    }
}
