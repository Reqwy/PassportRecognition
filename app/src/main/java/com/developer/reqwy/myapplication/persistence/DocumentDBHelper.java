package com.developer.reqwy.myapplication.persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.intellij.lang.annotations.Language;

import java.util.HashMap;
import java.util.Map;


public class DocumentDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "documents_recognizer.sqlite";
    private static final int VERSION = 1;

    private static final String PASSPORT_TABLE = "passport";
    private enum PASSPORTCOLUMNS{

        SURNAME("surname"),
        NAME ("name"),
        FATHERNAME("fathername"),
        GENDER("gender"),
        BD_DATE("bd_date"),
        BD_PLACE("bd_place"),
        NUMBER("number");

        public String value;

        PASSPORTCOLUMNS(String value) {
            this.value = value;
        }

        public String colName(){
            return value;
        }
    }

    private static final String DRIVER_LICENSE_TABLE = "driver_license";

    public DocumentDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table passport (" +
                "_id integer primary key autoincrement, " +
                "surname varchar(100)," +
                " name varchar(100)," +
                "fathername varchar(100)," +
                " gender varchar(100)," +
                "bd_date varchar(100)," +
                " bd_place varchar(100)," +
                "number varchar(100)" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public long savePassport(Map<String, String> passport){
        ContentValues cv = new ContentValues();
        cv.put(PASSPORTCOLUMNS.SURNAME.colName(), passport.get("Фамилия"));
        cv.put(PASSPORTCOLUMNS.NAME.colName(), passport.get("Имя"));
        cv.put(PASSPORTCOLUMNS.FATHERNAME.colName(), passport.get("Отчество"));
        cv.put(PASSPORTCOLUMNS.GENDER.colName(), passport.get("Пол"));
        cv.put(PASSPORTCOLUMNS.BD_DATE.colName(), passport.get("Дата рождения"));
        cv.put(PASSPORTCOLUMNS.BD_PLACE.colName(), passport.get("Место рождения"));
        cv.put(PASSPORTCOLUMNS.NUMBER.colName(), passport.get("Номер"));
        return getWritableDatabase().insert(PASSPORT_TABLE, null, cv);

    }

    public PassportCursor queryDocuments() {
        Cursor wrapped = getReadableDatabase().query(PASSPORT_TABLE,
                null, null, null, null, null, null);
        return new PassportCursor(wrapped);
    }

    public static class PassportCursor extends CursorWrapper implements IPassportCursor {

        public PassportCursor(Cursor cursor) {
            super(cursor);
        }


        @Override
        public Map<String, String> getPassport() {
            if (isBeforeFirst() || isAfterLast())
                return null;
            Map<String, String> passport = new HashMap<>();
            passport.put("Фамилия", getString(getColumnIndex(PASSPORTCOLUMNS.SURNAME.colName())));
            passport.put("Имя", getString(getColumnIndex(PASSPORTCOLUMNS.NAME.colName())));
            passport.put("Отчество", getString(getColumnIndex(PASSPORTCOLUMNS.FATHERNAME.colName())));
            passport.put("Пол", getString(getColumnIndex(PASSPORTCOLUMNS.GENDER.colName())));
            passport.put("Дата рождения", getString(getColumnIndex(PASSPORTCOLUMNS.BD_DATE.colName())));
            passport.put("Место рождения", getString(getColumnIndex(PASSPORTCOLUMNS.BD_PLACE.colName())));
            passport.put("Номер", getString(getColumnIndex(PASSPORTCOLUMNS.NUMBER.colName())));
            return passport;
        }
    }


}
