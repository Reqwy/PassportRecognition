package com.developer.reqwy.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;


public class DocumentListFragment extends ListFragment {

    private static final String TAG = "DocumentListFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Creation", "DocumentLISTFragmentCreated");
    }

}
