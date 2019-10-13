package com.letschat.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.ToString;

@Document
@Data
@ToString
public class Message {
	
	@Id
	private String messageId;
	
	private String recepient;
	private String sender;
	private String messageBody;
	private Long date;
	private Boolean isSeen = Boolean.FALSE;
	
	
	
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public Boolean getIsSeen() {
		return isSeen;
	}
	public void setIsSeen(Boolean isSeen) {
		this.isSeen = isSeen;
	}
	public Long getDate() {
		return date;
	}
	public void setDate(Long date) {
		this.date = date;
	}
	public Message() {
		super();
		
	}
	
	public Message(String recepient, String sender, String messageBody, Long date) {
		super();
		this.recepient = recepient;
		this.sender = sender;
		this.messageBody = messageBody;
		this.date = date;
	}
	public String getRecepient() {
		return recepient;
	}
	public void setRecepient(String recepient) {
		this.recepient = recepient;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getMessageBody() {
		return messageBody;
	}
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

}
