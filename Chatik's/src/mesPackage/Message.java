package mesPackage;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public abstract class Message implements Serializable {
	private static final long serialVersionUID = 1415402689274157468L;
	private String sender;
	private LinkedList <String> receivers;
	private String time;
	public abstract String getText();
	public abstract void showMessage();
	
	public Message(String nicName, LinkedList<String> to, Date time) {
		sender = nicName;
		this.receivers = to;
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		this.time = formatter.format(time);
	}
	public Message(generated.Message msg) {
		this.sender = msg.getSender();
		this.receivers = msg.getReceiver();
		this.time = msg.getTime();
	}
	
	public String getNickName() {
		return sender;
	}

	public String getTime() {
		return time;
	}

	public LinkedList<String> getReceivers() {
		return receivers;
	}

	public void setTo(LinkedList<String> to) {
		this.receivers = to;
	}

}
