package com.developer.reqwy.myapplication.document_templates;


public class PassportTemplate extends DocumentTemplate {

    private final boolean land;

    public PassportTemplate(boolean land){
        this.land = land;
        if (!land){
            fields.put("Фамилия", new DocumentField(new DocumentFieldRectangle(60, 135, 300, 60)));
            fields.put("Имя", new DocumentField(new DocumentFieldRectangle(40, 195, 320, 37)));
            fields.put("Отчество", new DocumentField(new DocumentFieldRectangle(48, 232, 312, 30)));
            fields.put("Пол", new DocumentField(new DocumentFieldRectangle(30, 265, 71, 30)));
            fields.put("Дата рождения", new DocumentField(new DocumentFieldRectangle(150, 262, 210, 35), true));
            fields.put("Место рождения1", new DocumentField(new DocumentFieldRectangle(56, 297, 304, 38)));
            fields.put("Место рождения2", new DocumentField(new DocumentFieldRectangle(12, 335, 348, 74)));
            fields.put("Номер", new DocumentField(new DocumentFieldRectangle(371, 140, 33, 270), DocumentField.Orientation.VERTICAL, true));
        }
        else {
            fields.put("Фамилия", new DocumentField(new DocumentFieldRectangle(193, 44, 297, 34)));
            fields.put("Имя", new DocumentField(new DocumentFieldRectangle(173, 78, 317, 31)));
            fields.put("Отчество", new DocumentField(new DocumentFieldRectangle(193, 107, 297, 27)));
            fields.put("Пол", new DocumentField(new DocumentFieldRectangle(174, 132, 66, 28)));
            fields.put("Дата рождения", new DocumentField(new DocumentFieldRectangle(285, 132, 205, 28), true));
            fields.put("Место рождения1", new DocumentField(new DocumentFieldRectangle(196, 163, 294, 37)));
            fields.put("Место рождения2", new DocumentField(new DocumentFieldRectangle(150, 187, 340, 60)));
            fields.put("Номер", new DocumentField(new DocumentFieldRectangle(497, 41, 33, 207), DocumentField.Orientation.VERTICAL, true));
        }
    }
}
