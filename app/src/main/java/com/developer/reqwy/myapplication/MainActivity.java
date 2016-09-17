package com.developer.reqwy.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.developer.reqwy.myapplication.utils.OrientationUtils;

public class  MainActivity extends AppCompatActivity {

    private static final String TAG = "MAIN_ACTIVITY";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//         setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCameraActivity();
            }
        });


        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.doc_list_fragment);

        if (fragment==null){
            fragment = new DocumentListFragment();
            fm.beginTransaction().add(R.id.doc_list_fragment, fragment).commit();
        }
        switch(OrientationUtils.getScreenOrientation(this)){
            case "A": {
                fragment = fm.findFragmentById(R.id.representation_fragment_container);
                if (fragment==null){
                    fragment = new DocumentFragment();
                    fm.beginTransaction().add(R.id.representation_fragment_container, fragment).commit();
                }
            }
            default:break;
        }
    }


    private void startCameraActivity(){
        Intent i = new Intent(this, CameraActivity.class);
        startActivity(i);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
