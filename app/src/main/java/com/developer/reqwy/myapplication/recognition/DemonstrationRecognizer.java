package com.developer.reqwy.myapplication.recognition;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.developer.reqwy.myapplication.document_templates.DocumentTemplate;
import com.developer.reqwy.myapplication.ocrsdk.Client;
import com.developer.reqwy.myapplication.ocrsdk.Task;
import com.developer.reqwy.myapplication.ocrsdk.TextFieldSettings;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class DemonstrationRecognizer implements Recognizer {

    private Map<String, File> files;
    private DocumentTemplate template;
    private Activity context;
    private RecognizerCallBack resultcallBack;
    private ProgressDialog mProgressDialog;
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
                    Log.d("ABBY_RECOGNIZER", "RESULT IS FORMED");
                    Log.d("ABBY_RECOGNIZER", finalRes);
                    finish();
                } else {
                    Log.d("ABBY_RECOGNIZER", "Processing: "
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

    public DemonstrationRecognizer(Activity context, Map<String, File> files, DocumentTemplate template,
                         RecognizerCallBack callBack){
        this.context = context;
        this.files = files;
        this.template = template;
        this.resultcallBack = callBack;
    }

    @Override
    public void recognize() {
        initProgressBar();
        Client restClient = new Client();
        restClient.applicationId = "9479662d-8de9-417b-8a5d-54f42690fa9d";
        restClient.password = "0DSQ2HrIB3QxK+OISCkLMibc";


        for (final String field : files.keySet()) {
            final File file = files.get(field);
            String language = template.getField(field).isNumber()? "Digits" : "Russian";

            TextFieldSettings processingSettings = new TextFieldSettings();
            processingSettings.setLanguage(language);
            try {
                Task task = restClient.processTextField(file.getPath(), processingSettings);
                while( task.isTaskActive() ) {
                    Thread.sleep(5000);
                    task = restClient.getTaskStatus(task.Id);
                }
                if( task.Status == Task.TaskStatus.Completed ) {
                    String result = restClient.downloadResult(task);
                    callBack.getOCRCallBackResult(field, result);
                } else {
                    callBack.getOCRCallBackResult(field, "Unrecognized");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
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
}
