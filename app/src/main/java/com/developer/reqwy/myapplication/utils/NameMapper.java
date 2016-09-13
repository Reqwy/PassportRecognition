package com.developer.reqwy.myapplication.utils;


public class NameMapper {

    public static String mapFieldToEng(String name){
        switch (name){
            case "Имя":
                return "name";
            case "Фамилия":
                return "surname";
            case "Отчество":
                return "fathersName";
            case "Пол":
                return "gender";
            case "Дата рождения":
                return "bd_date";
            case "Место рождения1":
                return "bd_place_1";
            case "Место рождения2":
                return "bd_place_2";
            case "Номер":
                return "number";
            default: return "file_png";
        }
    }
}
