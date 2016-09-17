package com.developer.reqwy.myapplication;

import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.developer.reqwy.myapplication.persistence.DocumentDBHelper;
import com.developer.reqwy.myapplication.persistence.DocumentDBHelper.PassportCursor;

import java.util.Map;


public class DocumentListFragment extends ListFragment {

    private static final String TAG = "DocumentListFragment";

    private PassportCursor cursor;
    private DocumentCursorAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Creation", "DocumentLISTFragmentCreated");
        DocumentDBHelper helper = new DocumentDBHelper(getActivity());
        cursor = helper.queryDocuments();
        Log.d("Creation", cursor.getCount() + " items in the list");
        adapter = new DocumentCursorAdapter(getActivity(), cursor);
        setListAdapter(adapter);
        ((DocumentCursorAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Map<String, String> map = adapter.getDocument(position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
    }

    @Override
    public void onResume() {
        super.onResume();
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
            String text = passport.get("Фамилия") + " " + passport.get("Имя") + " " + passport.get("Отчество");
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
