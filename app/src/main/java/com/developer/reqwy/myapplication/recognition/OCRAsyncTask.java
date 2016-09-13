package com.developer.reqwy.myapplication.recognition;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

public class OCRAsyncTask extends AsyncTask {

    private static final String TAG = OCRAsyncTask.class.getName();

    String url = "https://api.ocr.space/parse/image"; // OCR API Endpoints
    //String url2 = "https://apifree2.ocr.space/parse/image";

    private final String twoHyphens = "--";
    private final String lineEnd = "\r\n";
    private final String boundary = "----apiclient-" + UUID.randomUUID().toString();

    private static String resultsArray = "ParsedResults";


    private String mApiKey;
    private boolean isOverlayRequired = false;
    private File mImageUrl;
    private String mLanguage;
    private Activity mActivity;
    private ProgressDialog mProgressDialog;
    private IOCRCallBack mIOCRCallBack;
    private String field;

    public OCRAsyncTask(Activity activity, String apiKey, boolean isOverlayRequired,
                        File imageUrl, String language, String field, IOCRCallBack iOCRCallBack) {
        this.mActivity = activity;
        this.mApiKey = apiKey;
        this.isOverlayRequired = isOverlayRequired;
        this.mImageUrl = imageUrl;
        this.mLanguage = language;
        this.mIOCRCallBack = iOCRCallBack;
        this.field = field;
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
            return requsetWithDosOnly(mApiKey, isOverlayRequired, mImageUrl, mLanguage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String requsetWithDosOnly(String apiKey, boolean isOverlayRequired, File image, String language) throws Exception {
        URL obj = new URL(url); // OCR API Endpoints
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestProperty("Accept-Language", "ru;q=0.5");
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        con.setDoOutput(true);
        con.setDoInput(true);


        Map<String, String> params = new HashMap<>();
        params.put("apikey", apiKey);
        params.put("isOverlayRequired", String.valueOf(isOverlayRequired));
        params.put("language", language);
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
        for (Map.Entry<String, String> entry : params.entrySet()) {
            buildTextPart(dataOutputStream, entry.getKey(), entry.getValue());
        }

        buildDataPart(dataOutputStream, bytes, image.getName());

        dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
        dataOutputStream.flush();

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
        return String.valueOf(response);
    }
    private void buildTextPart(DataOutputStream dataOutputStream, String parameterName, String parameterValue) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + parameterName + "\"" + lineEnd);
        //dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);
        dataOutputStream.writeBytes(parameterValue + lineEnd);
    }

    private void buildDataPart(DataOutputStream dataOutputStream, byte[] bytes, String inputName) throws IOException {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" +
                "file" + "\"; filename=\"" + inputName + "\"" + lineEnd);
        dataOutputStream.writeBytes("Content-Type: " + "image/jpeg" + lineEnd);
        dataOutputStream.writeBytes(lineEnd);
        dataOutputStream.write(bytes, 0, bytes.length);

        dataOutputStream.writeBytes(lineEnd);
    }
    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        String response = extractResultFromResponse(result);
        mIOCRCallBack.getOCRCallBackResult(field, response);
        Log.d(TAG, "Got result on field " + field);
        Log.d(TAG, response);
    }

    private String extractResultFromResponse(Object result)  {
        String resObj = (String)result;
        String res;
        try {


            JSONObject object = new JSONObject(resObj);
            JSONArray array = object.getJSONArray(resultsArray);
            if (array.length() == 0) {
                res = "Unrecognized";
                return res;
            }
            JSONObject obj = array.getJSONObject(0);
            res = obj.getString("ParsedText");
            res = res.trim();
            res = res.replace("\r", "").replace("\n", "");
        } catch (JSONException ex){
            res = "Unrecognized";
        }
        return res;
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
