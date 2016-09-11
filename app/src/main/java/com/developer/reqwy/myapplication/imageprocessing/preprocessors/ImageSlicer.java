package com.developer.reqwy.myapplication.imageprocessing.preprocessors;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.developer.reqwy.myapplication.document_templates.DocumentFieldRectangle;
import com.developer.reqwy.myapplication.document_templates.DocumentTemplate;
import com.developer.reqwy.myapplication.document_templates.DocumentType;
import com.developer.reqwy.myapplication.document_templates.TemplateFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageSlicer {



    private Context context;
    private int width;
    private int height;

    public ImageSlicer(Context c, int width, int height){
        context = c;
        this.width = width;
        this.height = height;
    }


    public void sliceForApi(String pathName, int imgWidth,
                      DocumentType doctype, boolean land){
        DocumentTemplate template = TemplateFactory.getTemplate(doctype, land);
        Bitmap bm = BitmapFactory.decodeFile(pathName);
        float verticalDensity = bm.getHeight() / (float) height;
        float horizontalDensity = bm.getWidth() / (float)width;

        for (String field :template.getFieldNames()){
            DocumentFieldRectangle rect = template.getRectangle(field);
            // badass shit that can break our lifer
            Bitmap bmp = Bitmap.createBitmap(bm, (int)(rect.leftUp().x * horizontalDensity),(int)(rect.leftUp().y * verticalDensity),
                    (int)(rect.getWidth() * horizontalDensity), (int)(rect.getHeight() * verticalDensity));
            FileOutputStream output = null;
            File mFile = new File(context.getExternalFilesDir(null), field + ".png");
            FileOutputStream out = null;
            try {
                out = new FileOutputStream(mFile);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
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

    public float dipToPixels(float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public int dipToPix(int dp){
        float density = context.getResources().getDisplayMetrics().density;
        float tmp = (dp * density);
        return (int)tmp;
    }

    public void sliceForTesseract(){

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
