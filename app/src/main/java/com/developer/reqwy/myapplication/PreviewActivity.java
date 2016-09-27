package com.developer.reqwy.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


public class PreviewActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preview_activity);

        FragmentManager fm = getFragmentManager();
        Fragment fr = fm.findFragmentById(R.id.previewFragmentContainer);
        if (fr == null){
            fr = new PassportPreviewFragment();
            fm.beginTransaction().add(R.id.previewFragmentContainer, fr).commit();
        }

    }
}
