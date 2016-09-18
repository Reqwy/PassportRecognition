package com.developer.reqwy.myapplication.recognition;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.developer.reqwy.myapplication.document_templates.DocumentTemplate;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class TesseractProcessing implements Recognizer {

    private static final String TAG = "TESSERACT_OCR";

    private TessBaseAPI tessBaseApi;
    private Context processingContext;
    private static final String lang = "rus";
    private Map<String, Bitmap> images;
    private DocumentTemplate template;
    private ProgressDialog mProgressDialog;
    private RecognizerCallBack resultCallback;

    private static  String DATA_PATH;
    private static final String TESSDATA = "tessdata";
    private  Map<String, String> results = new ConcurrentHashMap<>();

    public TesseractProcessing(Context context, Map<String, Bitmap> imageMap, DocumentTemplate template, RecognizerCallBack callback){
        processingContext = context;
        this.template = template;
        this.images = imageMap;
        DATA_PATH = context.getExternalFilesDir(null).toString() + "/TesseractSample/";
        resultCallback = callback;
    }

    /**
     * Prepare directory on external storage
     *
     * @param path
     * @throws Exception
     */
    private void prepareDirectory(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Log.e(TAG, "ERROR: Creation of directory " + path + " failed, check does Android Manifest have permission to write to external storage.");
            }
        } else {
            Log.i(TAG, "Created directory " + path);
        }
    }


    private void prepareTesseract() {
        try {
            prepareDirectory(DATA_PATH + TESSDATA);
        } catch (Exception e) {
            e.printStackTrace();
        }
        copyTessDataFiles(TESSDATA, processingContext);
    }

    /**
     * Copy tessdata files (located on assets/tessdata) to destination directory
     *
     * @param path - name of directory with .traineddata files
     */
    private void copyTessDataFiles(String path, Context context) {
        try {
            String fileList[] = context.getAssets().list(path);

            for (String fileName : fileList) {

                // open file within the assets folder
                // if it is not already there copy it to the sdcard
                String pathToDataFile = DATA_PATH + path + "/" + fileName;
                if (!(new File(pathToDataFile)).exists()) {

                    InputStream in = context.getAssets().open(path + "/" + fileName);

                    OutputStream out = new FileOutputStream(pathToDataFile);

                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();

                    Log.d(TAG, "Copied " + fileName + "to tessdata");
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable to copy files to tessdata " + e.toString());
        }
    }

    public void onPartialResult(String field, String response){
        results.put(field, response);
        mProgressDialog.incrementProgressBy(1);
    }

    private void finish(){
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        resultCallback.onRecognitionFinished(results);
    }


    private void startOCR() {
        initProgressBar("Документ распознаётся...", images.keySet().size());
        iniTessAPI();
        try {
            for (String field: images.keySet()) {
                String recognition = extractText(field, images.get(field));
                onPartialResult(field, recognition);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        finish();
    }

    private void startCorrection(Map<String, Bitmap> images) {
        if (results.size() > 0){
            results = new ConcurrentHashMap<>();
        }
        initProgressBar("Небольшая коррекция...", images.keySet().size());
        String result = "";
        iniTessAPI();
        try {
            for (String field: images.keySet()) {
                String recognition = extractText(field, images.get(field));
                onPartialResult(field, recognition);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        finish();
    }

    private void iniTessAPI(){
        try {
            tessBaseApi = new TessBaseAPI();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            if (tessBaseApi == null) {
                Log.e(TAG, "TessBaseAPI is null. TessFactory not returning tess object.");
            }
        }
    }


    private String extractText(String field, Bitmap bitmap) {



        File f = new File(DATA_PATH);
        if (!f.exists()){
            Log.d("TESSERACT_INIT","tessdata folder doesnt exist");
        }

//       //EXTRA SETTINGS
//        //For example if we only want to detect numbers
//        tessBaseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "1234567890");
//

        if (field.toLowerCase().contains("номер") || field.toLowerCase().contains("дата")){
            initWithNumberField();
        } else {
            init();
        }
        Log.d(TAG, "Training file loaded");
        tessBaseApi.setImage(bitmap);
        String extractedText = "empty result";
        try {
            extractedText = tessBaseApi.getUTF8Text();
        } catch (Exception e) {
            Log.e(TAG, "Error in recognizing text.");
        }
        tessBaseApi.end();
        return extractedText;
    }

    private void init(){
        //        //blackList Example
//        tessBaseApi.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-[]}{" +
//                ";:'\"\\|~`,./<>?");
        tessBaseApi.init(DATA_PATH, lang);

    }

    private void reInit(){
        tessBaseApi.end();
        init();

    }

    private void initWithNumberField(){
        tessBaseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "1234567890");
        tessBaseApi.init(DATA_PATH, lang);

    }

    private void initProgressBar(String text, int size){
        mProgressDialog = new ProgressDialog(processingContext);
        mProgressDialog.setTitle(text);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMax(size);
        mProgressDialog.show();

    }

    @Override
    public void recognize() {
        prepareTesseract();
        startOCR();
    }

    public void correctionRecognition(Map<String, String> document){
        prepareTesseract();
        Set<String> unrecognizedKeys = document.keySet();
        Map<String, Bitmap> newImages = new HashMap<>();
        for (String key : unrecognizedKeys){
            newImages.put(key, images.get(key));
        }
        startCorrection(newImages);
    }
}
