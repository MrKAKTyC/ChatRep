package mesPackage;

import java.util.Comparator;
import java.util.LinkedList;

public class Conversation {
    private LinkedList<String> members;
    private String sender;
    private LinkedList<Message> msgs = new LinkedList<>();
    
    public Conversation(LinkedList<String> friends, String sender, LinkedList<Message> msgs) {
    	this.members =  new LinkedList<>(friends);
		this.members.add(sender);
		members.sort(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareToIgnoreCase(o2);
			}
			
		});
		this.sender = sender;
		this.msgs = msgs;
	}
	public LinkedList<String> getFriend() {
		return members;
	}
	public void setFriend(LinkedList<String> friends) {
		this.members = friends;
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
