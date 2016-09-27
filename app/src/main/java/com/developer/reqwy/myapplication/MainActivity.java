package com.developer.reqwy.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.developer.reqwy.myapplication.utils.OrientationUtils;

public class  MainActivity extends Activity {

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
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.accent)));
        fab.setImageTintList(ColorStateList.valueOf(Color.WHITE));

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.doc_list_fragment);

        if (fragment==null){
            fragment = new DocumentListFragment();
            fm.beginTransaction().replace(R.id.doc_list_fragment, fragment).commit();
        }
        switch(OrientationUtils.getScreenOrientation(this)){
            case "A": {
                fragment = fm.findFragmentById(R.id.representation_fragment_container);
                if (fragment==null){
                    fragment = new DocumentFragment();
                    fm.beginTransaction().add(R.id.representation_fragment_container, fragment).commit();
                }
                break;
            }
            default:break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case Activity.RESULT_OK: {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,
                                "Новый документ успешно сохранён.",
                                Toast.LENGTH_SHORT).show();
                        String orientation = OrientationUtils.getScreenOrientation(MainActivity.this);
                        if (orientation.equals("A")) {
                            FragmentManager fm = getFragmentManager();
                            Fragment fragment = fm.findFragmentById(R.id.representation_fragment_container);
                            if (fragment == null) {
                                fragment = new DocumentFragment();//
                                Bundle args = new Bundle();
                                args.putLong("id", data.getLongExtra("id", -1L));
                                fragment.setArguments(args);
                            } else {
                                fragment = new DocumentFragment();//
                                Bundle args = new Bundle();
                                args.putLong("id", data.getLongExtra("id", -1L));
                                fragment.setArguments(args);
                                fm.beginTransaction().replace(R.id.representation_fragment_container, fragment).commit();
                            }
                        } else if (orientation.equals("P")){

                        }
                    }
                });
                break;
            }
            case Activity.RESULT_CANCELED: {
                break;
            }
            case 500: {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,
                                "При сохранении документа произошла ошибка. Попробуйте распознать его ещё раз",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
            default:
                break;
        }
    }


    public void onDelete(){
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.representation_fragment_container);
        fm.beginTransaction().remove(fragment).commit();
        DocumentListFragment listFragment = (DocumentListFragment) fm.findFragmentById(R.id.doc_list_fragment);
        listFragment.refreshList();
    }


    private void startCameraActivity(){
        Intent i = new Intent(this, CameraActivity.class);
        startActivityForResult(i, 24);
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
