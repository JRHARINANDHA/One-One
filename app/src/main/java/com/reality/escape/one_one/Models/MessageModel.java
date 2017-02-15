package com.reality.escape.one_one.Models;

/**
 * Created by JR HARI NANDHA on 14-02-2017.
 */

import java.util.Date;


public class MessageModel{
    private String messageUser;
    private String messageText;
    private long messageTime;
    private String messageType;

    public MessageModel(String messageText, String messageUser,String messageType) {
        this.messageTime = new Date().getTime();
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.messageType = messageType;
    }

    public MessageModel() {
    }


    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}

