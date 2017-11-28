package mesPackage;

import java.io.File;
import java.util.Date;
import java.util.LinkedList;

public class GroupMessage extends FileMsg{
	private Group group;

	private static final long serialVersionUID = -4169763992201772024L;

	public GroupMessage(File content, String text, String sender, LinkedList<String> receiver, Date time, Group group) {
		super(content, text, sender, receiver, time);
		this.group = group;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

}
