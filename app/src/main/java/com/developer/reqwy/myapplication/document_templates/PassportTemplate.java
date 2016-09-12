package com.developer.reqwy.myapplication.document_templates;


public class PassportTemplate extends DocumentTemplate {

    private final boolean land;

    public PassportTemplate(boolean land){
        this.land = land;
        if (!land){
            fields.put("Фамилия", new DocumentFieldRectangle(60, 135, 300, 60));
            fields.put("Имя", new DocumentFieldRectangle(40, 195, 320, 37));
            fields.put("Отчество", new DocumentFieldRectangle(48, 232, 312, 30));
            fields.put("Пол", new DocumentFieldRectangle(30, 265, 71, 30));
            fields.put("Дата рождения", new DocumentFieldRectangle(150, 262, 210, 35));
            fields.put("Место рождения1", new DocumentFieldRectangle(56, 297, 304, 38));
            fields.put("Место рождения2", new DocumentFieldRectangle(12, 335, 348, 74));
            fields.put("Номер", new DocumentFieldRectangle(371, 140, 33, 270));
        }
        else {
            fields.put("Фамилия", new DocumentFieldRectangle(193, 44, 297, 34));
            fields.put("Имя", new DocumentFieldRectangle(173, 78, 317, 31));
            fields.put("Отчество", new DocumentFieldRectangle(193, 107, 297, 27));
            fields.put("Пол", new DocumentFieldRectangle(174, 132, 66, 28));
            fields.put("Дата рождения", new DocumentFieldRectangle(285, 132, 205, 28));
            fields.put("Место рождения1", new DocumentFieldRectangle(196, 163, 294, 37));
            fields.put("Место рождения2", new DocumentFieldRectangle(150, 187, 340, 60));
            fields.put("Номер", new DocumentFieldRectangle(497, 41, 33, 207));
        }
    }
}
