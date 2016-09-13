package com.developer.reqwy.myapplication.recognition;

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
import java.util.Map;


public class TesseractProcessing implements Recognizer {

    private static final String TAG = "TESSERACT_OCR";

    private TessBaseAPI tessBaseApi;
    private Context processingContext;
    private static final String lang = "rus";
    private Map<String, Bitmap> images;
    private DocumentTemplate template;

    private static  String DATA_PATH;
    private static final String TESSDATA = "tessdata";

    public TesseractProcessing(Context context, Map<String, Bitmap> imageMap, DocumentTemplate template){
        processingContext = context;
        this.template = template;
        this.images = imageMap;
        DATA_PATH = context.getExternalFilesDir(null).toString() + "/TesseractSample/";
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

    private void startOCR() {
        String result = "";
        try {
            for (String field: images.keySet()) {
                result += field + ": " + extractText(field, images.get(field)) + "\n";
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        Log.d("Tesseract_OCR_result", result);
    }


    private String extractText(String field, Bitmap bitmap) {
        try {
            tessBaseApi = new TessBaseAPI();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            if (tessBaseApi == null) {
                Log.e(TAG, "TessBaseAPI is null. TessFactory not returning tess object.");
            }
        }

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

    @Override
    public void recognize() {
        prepareTesseract();
        startOCR();
    }
}
