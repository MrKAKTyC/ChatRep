package mesPackage;

import java.util.LinkedList;

public class Conversation {
    private LinkedList<String> friends;
    private String sender;
    private LinkedList<Message> msgs = new LinkedList<>();
    public LinkedList<String> getFriend() {
		return friends;
	}
	public void setFriend(LinkedList<String> friends) {
		this.friends = friends;
	}
	public LinkedList<Message> getMsgs() {
		if(msgs == null) {
			new LinkedList<>();
		}
		return msgs;
	}
	public void setMsgs(LinkedList<Message> msgs) {
		this.msgs = msgs;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	
}
