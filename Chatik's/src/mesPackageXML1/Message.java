package mesPackageXML1;

import java.io.Serializable;

public abstract class Message implements Serializable {
	private static final long serialVersionUID = 1415402689274157468L;
	private String sender;
	private String receiver;
	private String time;

	public abstract String getText();

	public String getNickName() {
		return sender;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTime() {
		return time;
	}

	public String getTo() {
		return receiver;
	}

	public void setTo(String to) {
		this.receiver = to;
	}

}
