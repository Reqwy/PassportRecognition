package com.developer.reqwy.myapplication.document_templates;


public class TemplateFactory {

    private static DocumentTemplate passport = new PassportTemplate(false);
    private static DocumentTemplate passport_land = new PassportTemplate(true);
    private static DocumentTemplate driverlicanse;
    private static DocumentTemplate driverlicense_land;

    public static DocumentTemplate getTemplate(DocumentType doc, boolean land){
        switch (doc){
            case PASSPORT:
                return land? passport_land : passport;
            case DRIVER_LICENCE:
                return land? driverlicense_land : driverlicanse;

            default: return null;
        }
    }
}
