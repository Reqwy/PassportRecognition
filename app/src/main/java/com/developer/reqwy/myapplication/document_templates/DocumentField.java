package com.developer.reqwy.myapplication.document_templates;


public class DocumentField {
    public enum Orientation{
        HORIZONTAL, VERTICAL
    }

    private Orientation orientation;
    private String language;
    private boolean isNumber;
    private DocumentFieldRectangle rectangle;

    public DocumentField(DocumentFieldRectangle rectangle, Orientation orientation, String language, boolean isNumber){
        this.rectangle = rectangle;
        this.orientation = orientation;
        this.language = language;
        this.isNumber = isNumber;
    }

    public DocumentField(DocumentFieldRectangle rectangle){
        this(rectangle, Orientation.HORIZONTAL, "rus", false);
    }

    public DocumentField(DocumentFieldRectangle rectangle, Orientation orientation){
        this(rectangle, orientation, "rus", false);
    }

    public DocumentField(DocumentFieldRectangle rectangle, boolean isNumber){
        this.rectangle = rectangle;
        this.isNumber = isNumber;
    }

    public DocumentField(DocumentFieldRectangle rectangle, Orientation orientation, boolean isNumber){
        this.rectangle = rectangle;
        this.isNumber = isNumber;
        this.orientation = orientation;
    }


    public Orientation getOrientation() {
        return orientation;
    }

    public String getLanguage() {
        return language;
    }

    public boolean isNumber() {
        return isNumber;
    }

    public DocumentFieldRectangle getRectangle() {
        return rectangle;
    }
}
