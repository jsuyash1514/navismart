package com.navismart.navismart.model;

public class ChatModel {

    private String msgName;
    private String msgDate;
    private String msgTime;
    private String msg;
    private String boaterID;
    private String marinaID;
    private int SENDER_TYPE = 0;

    public ChatModel() {
    }

    public int getSENDER_TYPE() {

        return SENDER_TYPE;
    }

    public void setSENDER_TYPE(int SENDER_TYPE) {
        this.SENDER_TYPE = SENDER_TYPE;
    }

    public String getMsgName() {

        return msgName;
    }

    public void setMsgName(String msgName) {
        this.msgName = msgName;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getBoaterID() {
        return boaterID;
    }

    public void setBoaterID(String boaterID) {
        this.boaterID = boaterID;
    }

    public String getMarinaID() {
        return marinaID;
    }

    public void setMarinaID(String marinaID) {
        this.marinaID = marinaID;
    }
}
