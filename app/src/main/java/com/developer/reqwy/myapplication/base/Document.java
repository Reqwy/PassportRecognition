package com.developer.reqwy.myapplication.base;

import com.developer.reqwy.myapplication.document_templates.DocumentType;

import java.util.Map;
import java.util.UUID;


public abstract class Document {

    protected UUID id;
    protected String documentName;
    protected DocumentType type;

    public Document(String docName, DocumentType type){
        id = UUID.randomUUID();
        this.documentName = docName;
        this.type = type;
    }

    public Document(UUID id, String docName, DocumentType type){
        this.id = id;
        this.documentName = docName;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String name) {
        this.documentName = name;
    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    public abstract void init(Map<String, String> map);
}
