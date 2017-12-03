package mesPackageXML1;

import java.util.LinkedList;

public class Conversation {
    private String friend;
    private LinkedList<Message> msgs = new LinkedList<>();
    public String getFriend() {
		return friend;
	}
	public void setFriend(String friend) {
		this.friend = friend;
	}
	public LinkedList<Message> getMsgs() {
		return msgs;
	}
	public void setMsgs(LinkedList<Message> msgs) {
		this.msgs = msgs;
	}
	
}
