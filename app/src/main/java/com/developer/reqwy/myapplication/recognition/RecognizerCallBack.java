package com.developer.reqwy.myapplication.recognition;

import java.util.Map;

public interface RecognizerCallBack {
    void onRecognitionFinished(Map<String, String> document);
}
