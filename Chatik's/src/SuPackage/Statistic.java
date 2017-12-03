package SuPackage;

public class Statistic {
	private String time;
	private int newConnectedClientCount;
	private int messageCount;
	private int clientsOnline;
	public int getMessageCount() {
		return messageCount;
	}
	public void setMessageCount(int messageCount) {
		this.messageCount = messageCount;
	}
	
	@Override
	public String toString() {
		return "Statistic [new Connected Client Count=" + newConnectedClientCount + ", message Count=" + messageCount
				+ ", clients Online=" + clientsOnline + "]";
	}
	public int getNewConnectedClientCount() {
		return newConnectedClientCount;
	}
	public void setNewConnectedClientCount(int newConnectedClientCount) {
		this.newConnectedClientCount = newConnectedClientCount;
	}
	public int getClientsOnline() {
		return clientsOnline;
	}
	public void setClientsOnline(int clientsOnline) {
		this.clientsOnline = clientsOnline;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}
