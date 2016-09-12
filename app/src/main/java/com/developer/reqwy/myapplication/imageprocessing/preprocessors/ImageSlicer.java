package com.developer.reqwy.myapplication.imageprocessing.preprocessors;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import com.developer.reqwy.myapplication.document_templates.DocumentFieldRectangle;
import com.developer.reqwy.myapplication.document_templates.DocumentTemplate;
import com.developer.reqwy.myapplication.document_templates.DocumentType;
import com.developer.reqwy.myapplication.document_templates.TemplateFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageSlicer {

    private Context context;
    private Bitmap initialImage;

    public ImageSlicer(Context c, Bitmap initialImage){
        context = c;
        this.initialImage = initialImage;
    }

    private Map<String, File> filesForApi;
    private Map<String, Bitmap> bitmapsForTesseract;

    public void slice(DocumentType doctype, boolean land){
        DocumentTemplate template = TemplateFactory.getTemplate(doctype, land);
        filesForApi = new HashMap<>();
        bitmapsForTesseract = new HashMap<>();
        for (String field :template.getFieldNames()){
            DocumentFieldRectangle rect = template.getRectangle(field);
            // badass shit that can break our lifer
            Bitmap bmp = Bitmap.createBitmap(initialImage, dipToPix(rect.leftUp().x), dipToPix(rect.leftUp().y),
                    dipToPix(rect.getWidth()), dipToPix(rect.getHeight()));
            bitmapsForTesseract.put(field, bmp);
            File mFile = new File(context.getExternalFilesDir(null), field + ".png");
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(mFile);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                // PNG is a lossless format, the compression factor (100) is ignored
                Log.d("ImageSlicer", "Successfully saved image at " + mFile.getAbsolutePath());
                filesForApi.put(field, mFile);
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

    public Map<String, Bitmap> getBitmapsForTesseract() {
        return bitmapsForTesseract;
    }

    public Map<String, File> getFilesForApi() {
        return filesForApi;
    }

    public float dipToPixels(float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public int dipToPix(int dp){
        float density = context.getResources().getDisplayMetrics().density;
        float tmp = (dp * density);
        return (int)tmp;
    }

    public static byte[] int2byte(int[]src) {
        int srcLength = src.length;
        byte[]dst = new byte[srcLength << 2];

        for (int i=0; i<srcLength; i++) {
            int x = src[i];
            int j = i << 2;
            dst[j++] = (byte) ((x >>> 0) & 0xff);
            dst[j++] = (byte) ((x >>> 8) & 0xff);
            dst[j++] = (byte) ((x >>> 16) & 0xff);
            dst[j++] = (byte) ((x >>> 24) & 0xff);
        }
        return dst;
    }
}
