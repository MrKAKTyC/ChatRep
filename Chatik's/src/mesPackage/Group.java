package mesPackage;

import java.util.LinkedList;

public class Group {
	private LinkedList <String> group;

	public LinkedList <String> getGroup() {
		if(group == null) {
			group = new LinkedList<>();
		}
		return group;
	}

	public void setGroup(LinkedList <String> group) {
		this.group = group;
	}
}
