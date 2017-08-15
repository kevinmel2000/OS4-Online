package com.os4.ecb.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yanyan on 3/21/2017.
 */

public class Messages {

    private List<Message> messageList = new ArrayList<Message>();

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public void addMessage(Message message){
        if(message.getDateTime()==null) message.setDateTime(new Date());
        messageList.add(message);
    }

    public void addMessage(String id,String from,String to,String textMessage){
        Message message = new Message(id,from,to,textMessage);
        this.messageList.add(message);
    }

    public void addMessage(String id,String from,String to,FileTransferInfo fileInfo){
        Message message = new Message(id,from,to,fileInfo);
        this.messageList.add(message);
    }

    public Message newMessage(){
        return new Message();
    }

    public Message newMessage(String id,String from,String to,String textMessage){
        return new Message(id,from,to,textMessage);
    }

    public Message newMessage(String id,String from,String to,FileTransferInfo fileInfo){
        return new Message(id,from,to,fileInfo);
    }

    public class Message {

        private String id;
        private Date dateTime;
        private String from;
        private String to;
        private String subject;
        private String textMessage;
        private FileTransferInfo fileInfo;

        public Message(){}
        public Message(String id,String from,String to,String textMessage){
            this.id = id;
            this.dateTime = new Date();
            this.from = from;
            this.to = to;
            this.textMessage = textMessage;
        }
        public Message(String id,String from,String to,String subject,String textMessage){
            this.id = id;
            this.dateTime = new Date();
            this.from = from;
            this.to = to;
            this.subject = subject;
            this.textMessage = textMessage;
        }
        public Message(String id,String from,String to,FileTransferInfo fileInfo){
            this.id = id;
            this.dateTime = new Date();
            this.from = from;
            this.to = to;
            this.fileInfo = fileInfo;
        }

        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public Date getDateTime() {
            return dateTime;
        }
        public void setDateTime(Date dateTime) {
            this.dateTime = dateTime;
        }
        public String getFrom() {
            return from;
        }
        public void setFrom(String from) {
            this.from = from;
        }
        public String getTo() {
            return to;
        }
        public void setTo(String to) {
            this.to = to;
        }
        public String getSubject() {
            return subject;
        }
        public void setSubject(String subject) {
            this.subject = subject;
        }
        public String getTextMessage() {
            return textMessage;
        }
        public void setTextMessage(String textMessage) {
            this.textMessage = textMessage;
        }
        public FileTransferInfo getFileInfo() {
            return fileInfo;
        }
        public void setFileInfo(FileTransferInfo fileInfo) {
            this.fileInfo = fileInfo;
        }
        public String getResource(){
            return from.substring(from.lastIndexOf("/")+1);
        }
        public String getNickname(){
            return getResource();
        }
    }
}
