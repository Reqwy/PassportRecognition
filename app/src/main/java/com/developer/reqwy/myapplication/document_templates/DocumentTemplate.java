package com.developer.reqwy.myapplication.document_templates;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public abstract class DocumentTemplate {

    protected Map<String, DocumentFieldRectangle> fields = new HashMap();


    public Map<String, DocumentFieldRectangle> getFields(){
        return fields;
    }

    public Set<String> getFieldNames(){
        return fields.keySet();
    }

    public DocumentFieldRectangle getRectangle(String fieldName){
        return fields.get(fieldName);
    }

}
