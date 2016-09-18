package com.developer.reqwy.myapplication;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.developer.reqwy.myapplication.document_templates.DocumentType;
import com.developer.reqwy.myapplication.persistence.DocumentDBHelper;
import com.developer.reqwy.myapplication.utils.OrientationUtils;

import java.util.HashMap;
import java.util.Map;

public class DocumentFragment extends Fragment {

    private static final String TAG = "DocumentFragment";

    private Map<String, String> document;
    long id;
    private EditText surname;
    private EditText name;
    private EditText fatherName;
    private EditText gender;
    private EditText dateOfBirth;
    private EditText placeOfBirth;
    private EditText number;
    private TextView documentName;

    private Button deleteButton;
    private Button editButton;
    private Button shareButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Creation", "DocumentFragmentCreated");
        if (getArguments() != null) {
            id = getArguments().getLong("id");
        } else {
            id = -1;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.passport_fragment, container, false);
        initialiseInterfaceWithData(v, id, DocumentType.PASSPORT.name());
        return v;
    }

    private void initialiseInterfaceWithData(View v, final long id, String dtype){
        if (id != -1) {
            DocumentDBHelper.PassportCursor c = new DocumentDBHelper(getActivity()).getDocument(id, dtype);
            document = c.getPassport();

            surname = (EditText) v.findViewById(R.id.passportSurnameEdit);
            name = (EditText) v.findViewById(R.id.passportNameEdit);
            fatherName  = (EditText) v.findViewById(R.id.passportFatherNameEdit);
            gender = (EditText)v.findViewById(R.id.passportGenderEdit);
            dateOfBirth = (EditText)v.findViewById(R.id.passportBirthdayDayEdit);
            placeOfBirth = (EditText)v.findViewById(R.id.passportPlaceOfBirthEdit);
            number = (EditText)v.findViewById(R.id.passportNumberEdit);
            documentName = (TextView)v.findViewById(R.id.passportDocName);

            deleteButton = (Button)v.findViewById(R.id.passportDeleteButton);
            editButton = (Button) v.findViewById(R.id.passportEditButton);
            shareButton = (Button) v.findViewById(R.id.passportShareButton);

            surname.setText(document.get("Фамилия"));
            name.setText(document.get("Имя"));
            fatherName.setText(document.get("Отчество"));
            gender.setText(document.get("Пол"));
            dateOfBirth.setText(document.get("Дата рождения"));
            placeOfBirth.setText(document.get("Место рождения"));
            number.setText(document.get("Номер"));
            documentName.setText(document.get("Наименование документа"));

            surname.setFocusable(false);
            surname.setEnabled(false);

            name.setEnabled(false);
            name.setFocusable(false);

            fatherName.setEnabled(false);
            fatherName.setFocusable(false);

            gender.setEnabled(false);
            gender.setFocusable(false);

            dateOfBirth.setEnabled(false);
            dateOfBirth.setFocusable(false);

            placeOfBirth.setEnabled(false);
            placeOfBirth.setFocusable(false);

            number.setEnabled(false);
            number.setFocusable(false);

            surname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    int ib = 3;
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    int ib = 4;
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    document.put("Фамилия", editable.toString());
                }
            });

            name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    document.put("Имя", editable.toString());
                }
            });

            fatherName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    document.put("Отчество", editable.toString());
                }
            });

            gender.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    document.put("Пол", editable.toString());
                }
            });

            dateOfBirth.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    document.put("Дата рождения", editable.toString());
                }
            });

            placeOfBirth.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    document.put("Место рождения", editable.toString());
                }
            });

            number.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    document.put("Номер", editable.toString());
                }
            });

            editButton.setOnClickListener(new View.OnClickListener() {
                private boolean editing = false;

                @Override
                public void onClick(View view) {
                    if (!editing){
                        editButton.setText("Готово");
                        editing = true;
                        enableEditing(true);
                        Toast.makeText(getActivity(),
                                "Теперь вы можете редактировать документ.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        editButton.setText("Редактировать");
                        editing = false;
                        enableEditing(false);
                        int code = new DocumentDBHelper(getActivity()).updatePassort(id, document);
                        if (code > 0) {
                            Toast.makeText(getActivity(),
                                    "Документ успешно обновлён.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(),
                                    "Ошибка при обновлении документа.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                private void enableEditing(boolean enable){
                    surname.clearFocus();
                    name.clearFocus();
                    fatherName.clearFocus();
                    gender.clearFocus();
                    dateOfBirth.clearFocus();
                    placeOfBirth.clearFocus();
                    number.clearFocus();

                    surname.setFocusable(enable);
                    surname.setEnabled(enable);
                    surname.setFocusableInTouchMode(enable);

                    name.setEnabled(enable);
                    name.setFocusable(enable);
                    name.setFocusableInTouchMode(enable);

                    fatherName.setEnabled(enable);
                    fatherName.setFocusable(enable);
                    fatherName.setFocusableInTouchMode(enable);

                    gender.setEnabled(enable);
                    gender.setFocusable(enable);
                    gender.setFocusableInTouchMode(enable);

                    dateOfBirth.setEnabled(enable);
                    dateOfBirth.setFocusable(enable);
                    dateOfBirth.setFocusableInTouchMode(enable);

                    placeOfBirth.setEnabled(enable);
                    placeOfBirth.setFocusable(enable);
                    placeOfBirth.setFocusableInTouchMode(enable);

                    number.setEnabled(enable);
                    number.setFocusable(enable);
                    number.setFocusableInTouchMode(enable);
                }

            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Delete code here
                    delete();
                }
            });

            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Share code here
                }
            });
        } else {
            document = new HashMap<>();
        }
    }

    public void delete(){
        int code = new DocumentDBHelper(getActivity()).deletePassport(id);
        String orientation = OrientationUtils.getScreenOrientation(getActivity());
        if (orientation.equals("P")){
            getActivity().finish();
        } else {
            ((MainActivity)getActivity()).onDelete();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        switch (resultCode){
//            case Activity.RESULT_OK: {
//                String dtype = data.getStringExtra(ImagePreProcessor.DOCTYPE_EXTRA);
//                long id = data.getLongExtra("id", -1);
//                initialiseInterfaceWithData(id, dtype);
//                break;
//            }
//        }
    }
}
