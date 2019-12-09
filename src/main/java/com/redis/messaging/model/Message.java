package com.redis.messaging.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.redis.messaging.util.StringUtils;

/**
 * Base event
 * 
 * @author yathiraj
 *
 */
public class Message implements Serializable, Comparable<Message> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String body;
	private String channelName;
	private String sender;
	private String reciever;

	private LocalDateTime sentTime;

	private LocalDateTime recieveTime;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReciever() {
		return reciever;
	}

	public void setReciever(String reciever) {
		this.reciever = reciever;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public LocalDateTime getSentTime() {
		return sentTime;
	}

	public void setSentTime(LocalDateTime sentTime) {
		this.sentTime = sentTime;
	}

	public LocalDateTime getRecieveTime() {
		return recieveTime;
	}

	public void setRecieveTime(LocalDateTime recieveTime) {
		this.recieveTime = recieveTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Message [body=" + body + ", channelName=" + channelName + ", sender=" + sender + ", reciever="
				+ reciever + ", sentTime=" + sentTime + ", recieveTime=" + recieveTime + "]";
	}

	@Override
	public boolean equals(Object obj) {
		Message msg1 = (Message) obj;
		if (this.getSentTime().isAfter(msg1.getSentTime())) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public int compareTo(Message obj) {
		Message msg1 = (Message) obj;
		if (this.getSentTime().isAfter(msg1.getSentTime())) {
			return 1;
		} else {
			return 0;
		}
	}

	public boolean isEmpty() {
		if (StringUtils.isEmpty(this.body)) {
			return true;
		}
		return false;
	}

}
