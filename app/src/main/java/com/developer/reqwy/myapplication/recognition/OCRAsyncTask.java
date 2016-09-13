package com.developer.reqwy.myapplication.recognition;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class OCRAsyncTask extends AsyncTask {

    private static final String TAG = OCRAsyncTask.class.getName();

    String url = "https://api.ocr.space/parse/image"; // OCR API Endpoints
    private static final String LINE_FEED = "\r\n";

    private String mApiKey;
    private boolean isOverlayRequired = false;
    private File mImageUrl;
    private String mLanguage;
    private Activity mActivity;
    private ProgressDialog mProgressDialog;
    private IOCRCallBack mIOCRCallBack;
    String name;

    public OCRAsyncTask(Activity activity, String apiKey, boolean isOverlayRequired, File imageUrl, String language, String name, IOCRCallBack iOCRCallBack) {
        this.mActivity = activity;
        this.mApiKey = apiKey;
        this.isOverlayRequired = isOverlayRequired;
        this.mImageUrl = imageUrl;
        this.mLanguage = language;
        this.mIOCRCallBack = iOCRCallBack;
        this.name = name;
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setTitle("Wait while processing....");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Object[] params) {

        try {
            return sendPost(mApiKey, isOverlayRequired, mImageUrl, mLanguage);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private String sendPost(String apiKey, boolean isOverlayRequired, File image, String language) throws Exception {

        URL obj = new URL(url); // OCR API Endpoints
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        JSONObject postDataParams = new JSONObject();


        postDataParams.put("apikey", apiKey);
        postDataParams.put("isOverlayRequired", isOverlayRequired);

//        JSONObject object = new JSONObject();
//        JSONArray array = new JSONArray(bytes);
//        object.put("file", array);


        //postDataParams.put("file", JSONObject.wrap(bytes));
//        postDataParams.put("url", "http://dl.a9t9.com/blog/ocr-online/screenshot.jpg");
        postDataParams.put("language", language);
//        con.setRequestProperty("Content-Length", String.valueOf(bytes.length));
        // Send post request
        String fileName = image.getName();
        con.setRequestProperty("Content-Type",
                "multipart/form-data;");
        con.setDoOutput(true);
        con.setDoInput(true);

        int size = (int) image.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(image));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataOutputStream dataOutputStream = new DataOutputStream(con.getOutputStream());
        dataOutputStream.writeBytes(getPostDataString(postDataParams));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(dataOutputStream, Charset.defaultCharset()),
                true);
        writer.append(LINE_FEED);
//        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" +
//                "name" + "\"; filename=\"" + image.getName() + "\"" + "\r\n");
//        dataOutputStream.writeBytes("ContentType: image/jpeg" + "\r\n");
//        dataOutputStream.writeBytes("\r\n");
//        dataOutputStream.write(bytes, 0 , bytes.length);
//        dataOutputStream.writeBytes("\r\n");
        writer.append(
                "Content-Disposition: form-data; name=\"" + "file"
                        + "\"; filename=\"" + fileName + "\"")
                .append(LINE_FEED);
        writer.append(
                "Content-Type: "
                        + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);

        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();
        dataOutputStream.write(bytes, 0, bytes.length);

        dataOutputStream.flush();
        writer.append(LINE_FEED);
        writer.flush();
        dataOutputStream.close();
        Log.d("OCR ASYNC", "sent request, watinng for respose");

        BufferedReader in;
        int code = con.getResponseCode();
        Log.d("OCR ASYNC", "Gor request with code " + code);
        if (200 <= code && code <= 299) {
            in = new BufferedReader(new InputStreamReader((con.getInputStream())));
        } else {
            in = new BufferedReader(new InputStreamReader((con.getErrorStream())));
        }
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //return result
        return String.valueOf(response);
    }

    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        String response = (String) result;
        mIOCRCallBack.getOCRCallBackResult(response);
        Log.d(TAG, response.toString());
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
