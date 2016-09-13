package com.developer.reqwy.myapplication.document_templates;


import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public abstract class DocumentTemplate {

    protected Map<String, DocumentField> fields = new HashMap();


    public Map<String, DocumentField> getFields(){
        return fields;
    }

    public Set<String> getFieldNames(){
        return fields.keySet();
    }

    public DocumentField getField(String fieldName){
        return fields.get(fieldName);
    }

}
