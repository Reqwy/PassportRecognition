package com.developer.reqwy.myapplication;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class DocumentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doc_representation_activity);

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.doc_activity_fragment_container);
        if (fragment==null){
            fragment = new DocumentFragment();
            Intent i = getIntent();
            Bundle args = new Bundle();
            args.putLong("id", i.getLongExtra("id", -1));
            fragment.setArguments(args);
            fm.beginTransaction().add(R.id.doc_activity_fragment_container, fragment).commit();
        }
    }
}
