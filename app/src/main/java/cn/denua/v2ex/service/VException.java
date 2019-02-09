package cn.denua.v2ex.service;


public class VException extends Exception{

    private String reference;
    private Object referenceObject;

    VException(String msg){
        super(msg);
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Object getReferenceObject() {
        return referenceObject;
    }

    public void setReferenceObject(Object referenceObject) {
        this.referenceObject = referenceObject;
    }
}
