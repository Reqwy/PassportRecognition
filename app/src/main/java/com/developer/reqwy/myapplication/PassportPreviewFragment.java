package com.developer.reqwy.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.developer.reqwy.myapplication.document_templates.DocumentTemplate;
import com.developer.reqwy.myapplication.document_templates.DocumentType;
import com.developer.reqwy.myapplication.document_templates.TemplateFactory;
import com.developer.reqwy.myapplication.imageprocessing.preprocessors.ImagePreProcessor;
import com.developer.reqwy.myapplication.persistence.DocumentDBHelper;

import java.util.HashMap;
import java.util.Map;


public class PassportPreviewFragment extends Fragment{

    private  Map<String, String> passport;

    private EditText surname;
    private EditText name;
    private EditText fatherName;
    private EditText gender;
    private EditText dateOfBirth;
    private EditText placeOfBirth;
    private EditText number;
    private Button save; // TODO some database work outa here
    private Button cancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Intent activityIntent = getActivity().getIntent();
        passport = deserializePassport(activityIntent);
    }


    private Map<String, String> deserializePassport(Intent i){
        DocumentTemplate template = TemplateFactory.getTemplate(DocumentType.PASSPORT, true);
        Map<String, String> doc = new HashMap<>();
        for (String s : template.getFieldNames()){
            doc.put(s, i.getStringExtra(s));
        }
        return  doc;
    }

    private Intent prepareResultIntent(){
        Intent i = new Intent(getActivity(), MainActivity.class);
        i.putExtra(ImagePreProcessor.DOCTYPE_EXTRA, DocumentType.PASSPORT.name());
        for (String s : passport.keySet()){
            i.putExtra(s, passport.get(s));
        }
        return i;
    }

    private void initialiseInterfaceWithData(View v) {
        surname = (EditText) v.findViewById(R.id.surnameEdit);
        name = (EditText) v.findViewById(R.id.nameEdit);
        fatherName  = (EditText) v.findViewById(R.id.fatherNameEdit);
        gender = (EditText)v.findViewById(R.id.genderEdit);
        dateOfBirth = (EditText)v.findViewById(R.id.birthdayDayEdit);
        placeOfBirth = (EditText)v.findViewById(R.id.placeOfBirthEdit);
        number = (EditText)v.findViewById(R.id.numberEdit);

        surname.setText(passport.get("Фамилия"));
        name.setText(passport.get("Имя"));
        fatherName.setText(passport.get("Отчество"));
        gender.setText(passport.get("Пол"));
        dateOfBirth.setText(passport.get("Дата рождения"));
        placeOfBirth.setText(passport.get("Место рождения"));
        number.setText(passport.get("Номер"));

        save = (Button) v.findViewById(R.id.saveButton);
        cancel = (Button) v.findViewById(R.id.cancelButton);
    }

    private void addListeners(){
        surname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                passport.put("Фамилия", editable.toString());
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
                passport.put("Имя", editable.toString());
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
                passport.put("Отчество", editable.toString());
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
                passport.put("Пол", editable.toString());
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
                passport.put("Дата рождения", editable.toString());
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
                passport.put("Место рождения", editable.toString());
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
                passport.put("Номер", editable.toString());
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentDBHelper helper = new DocumentDBHelper(getActivity());
                long code = helper.savePassport(passport);
                if (code != -1) {
                    Intent data = prepareResultIntent();
                    getActivity().setResult(Activity.RESULT_OK, data);
                    getActivity().finish();
                } else {
                    getActivity().setResult(500);
                    getActivity().finish();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().setResult(Activity.RESULT_CANCELED);
                getActivity().finish();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.passport_preview, container, false);
        initialiseInterfaceWithData(v);
        addListeners();

        return v;
    }
}
