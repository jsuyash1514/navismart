package com.navismart.navismart.model;

public class MsgNameModel {

    private String msgName;
    private String ID;
    private String marinaToken;
    private String boaterToken;

    public MsgNameModel() {
    }

    public String getBoaterToken() {

        return boaterToken;
    }

    public void setBoaterToken(String boaterToken) {
        this.boaterToken = boaterToken;
    }

    public String getMarinaToken() {

        return marinaToken;
    }

    public void setMarinaToken(String marinaToken) {
        this.marinaToken = marinaToken;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getMsgName() {
        return msgName;
    }

    public void setMsgName(String msgName) {
        this.msgName = msgName;
    }
}
