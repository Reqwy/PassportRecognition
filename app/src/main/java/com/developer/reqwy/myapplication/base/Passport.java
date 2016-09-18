package com.developer.reqwy.myapplication.base;


import com.developer.reqwy.myapplication.document_templates.DocumentType;

import java.util.Map;
import java.util.UUID;

public class Passport extends Document {
    private String surname;
    private String name;
    private String fatherName;
    private String gender;
    private String birthdayDate;
    private String placeOfBirth;
    private String number;

    public Passport(String docName) {
        super(docName, DocumentType.PASSPORT);
    }

    public Passport(UUID id, String docName) {
        super(id, docName, DocumentType.PASSPORT);
    }


    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthdayDate() {
        return birthdayDate;
    }

    public void setBirthdayDate(String birthdayDate) {
        this.birthdayDate = birthdayDate;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String dateOfBirth) {
        this.placeOfBirth = dateOfBirth;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public void init(Map<String, String> map) {
        surname = map.get("Фамилия");
        name = map.get("Имя");
        fatherName = map.get("Отчество");
        gender = map.get("Пол");
        birthdayDate = map.get("Дата рождения");
        placeOfBirth = map.get("Место рождения");
        number = map.get("Номер");
    }
}
