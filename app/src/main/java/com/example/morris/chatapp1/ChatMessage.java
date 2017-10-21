package com.example.morris.chatapp1;

import java.util.Date;

/**
 * Created by Ross on 10/20/2017.
 */

public class ChatMessage {
    private String messageText;
    private String messageUser;
    private long messageTime;

    public ChatMessage(String messageText, String messageUser){
        this.messageText=messageText;
        this.messageUser=messageUser;

        //initialize current time
        messageTime= new Date().getTime();
    }

    public ChatMessage(){

    }
    //Setter
    public void setMessageText(String messageText){
        this.messageText=messageText;
    }
    public void setMessageUser(String messageUser){
        this.messageUser=messageUser;
    }
    public void setMessageTime(long messageTime){
        this.messageTime=messageTime;
    }

    //Getter
    public String getMessageText(){
        return messageText;
    }
    public String getMessageUser(){
        return messageUser;
    }
    public long getMessageTime(){
        return messageTime;
    }
}
