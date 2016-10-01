package com.developer.reqwy.myapplication.recognition;

import android.os.AsyncTask;
import android.util.Xml;

import com.developer.reqwy.myapplication.ocrsdk.Client;
import com.developer.reqwy.myapplication.ocrsdk.Task;
import com.developer.reqwy.myapplication.ocrsdk.TextFieldSettings;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;

/**
 * Created by reqwy on 01.10.16.
 */
public class ABBYYAsyncTask extends AsyncTask {

    private Client restClient;
    private File file;
    private TextFieldSettings processingSettings;
    private IOCRCallBack callBack;
    private String field;

    public ABBYYAsyncTask(Client restClient, File file, TextFieldSettings processingSettings, IOCRCallBack callBack, String field){

        this.restClient = restClient;
        this.file = file;
        this.processingSettings = processingSettings;
        this.callBack = callBack;
        this.field = field;
    }
    @Override
    protected String doInBackground(Object[] objects) {

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
        return null;
    }
}
