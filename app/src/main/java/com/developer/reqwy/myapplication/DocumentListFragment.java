package com.developer.reqwy.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.developer.reqwy.myapplication.document_templates.DocumentType;
import com.developer.reqwy.myapplication.imageprocessing.preprocessors.ImagePreProcessor;
import com.developer.reqwy.myapplication.persistence.DocumentDBHelper;
import com.developer.reqwy.myapplication.persistence.DocumentDBHelper.PassportCursor;
import com.developer.reqwy.myapplication.utils.OrientationUtils;

import java.util.Map;


public class DocumentListFragment extends ListFragment {

    private static final String TAG = "DocumentListFragment";

    private PassportCursor cursor;
    private DocumentCursorAdapter adapter;
    private DocumentDBHelper helper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Creation", "DocumentLISTFragmentCreated");
        if (helper == null) {
            helper = new DocumentDBHelper(getActivity());
            cursor = helper.queryDocuments();
        }
        Log.d("Creation", cursor.getCount() + " items in the list");
        adapter = new DocumentCursorAdapter(getActivity(), cursor);
        setListAdapter(adapter);
        ((DocumentCursorAdapter)getListAdapter()).notifyDataSetChanged();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Map<String, String> map = adapter.getDocument(position);
        String orientation = OrientationUtils.getScreenOrientation(getActivity());
        long doc_id = Long.valueOf(map.get("id"));

        if (orientation.equals("A")){
            Bundle args = new Bundle();
            args.putLong("id", doc_id);
            FragmentManager fm = getFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.representation_fragment_container);
            FragmentTransaction transaction = fm.beginTransaction();
            if (fragment != null){
                transaction.remove(fragment);
            }

            Fragment newFragment = new DocumentFragment();
            newFragment.setArguments(args);
            transaction.add(R.id.representation_fragment_container, newFragment);
            transaction.commit();
        } else if (orientation.equals("P")){
            // send intent to document activity
            Intent i = new Intent(getActivity(), DocumentActivity.class);
            serializeDocToIntent(i, map);
            i.putExtra("id", doc_id);
            startActivity(i);
        }


    }

    private void serializeDocToIntent(Intent i, Map<String, String> doc){
        i.putExtra(ImagePreProcessor.DOCTYPE_EXTRA, DocumentType.PASSPORT.name());
        for (String s : doc.keySet()){
            i.putExtra(s, doc.get(s));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
    }

    public void refreshList(){
        PassportCursor newCursor = helper.queryDocuments();
        if (newCursor.getCount() != cursor.getCount()) {
            cursor = newCursor;
            Log.d("Creation", cursor.getCount() + " items in the list");
            adapter = new DocumentCursorAdapter(getActivity(), cursor);
            setListAdapter(adapter);
            ((DocumentCursorAdapter) getListAdapter()).notifyDataSetChanged();
            adapter.notifyDataSetChanged();
        }

        Map<String, String> map = adapter.getDocument(0);
        if (map != null && map.size() != 0){
            long doc_id = Long.valueOf(map.get("id"));
            Bundle args = new Bundle();
            args.putLong("id", doc_id);
            FragmentManager fm = getFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.representation_fragment_container);
            FragmentTransaction transaction = fm.beginTransaction();
            if (fragment != null){
                transaction.remove(fragment);
            }
            Fragment newFragment = new DocumentFragment();
            newFragment.setArguments(args);
            transaction.add(R.id.representation_fragment_container, newFragment);
            transaction.commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        PassportCursor newCursor = helper.queryDocuments();
        if (newCursor.getCount() != cursor.getCount()) {
            cursor = newCursor;
            Log.d("Resuming", cursor.getCount() + " reloadingList");
            adapter = new DocumentCursorAdapter(getActivity(), cursor);
            setListAdapter(adapter);
            ((DocumentCursorAdapter) getListAdapter()).notifyDataSetChanged();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            ((DocumentCursorAdapter)getListAdapter()).notifyDataSetChanged();
        }
    }



    private static class DocumentCursorAdapter extends CursorAdapter{

        private PassportCursor passportCursor;

        public DocumentCursorAdapter(Context context, PassportCursor c) {
            super(context, c, 0);
            passportCursor = c;
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater)context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            return inflater
                    .inflate(R.layout.documents_list_item, viewGroup, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            Map<String, String> passport = passportCursor.getPassport();
            String text = passport.get("Наименование документа");
            TextView header = (TextView) view.findViewById(R.id.doc_list_item_header);
            header.setText(text);
            TextView docType = (TextView) view.findViewById(R.id.doc_list_item_type);
            docType.setText("Пасспорт РФ");
        }

        public Map<String, String> getDocument(int position) {
            int oldPos = passportCursor.getPosition();
            passportCursor.moveToPosition(position);
            Map<String, String> result = passportCursor.getPassport();
            passportCursor.moveToPosition(oldPos);
            return result;
        }
    }
}
