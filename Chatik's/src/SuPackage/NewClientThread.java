package SuPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;

import application.Client;
import javafx.application.Platform;
import mesPackage.*;

public class NewClientThread implements Runnable {
	private String name;
	private Socket socket;
	private Thread t;

	public NewClientThread(Socket socket) {
		this.name = socket.toString();
		this.socket = socket;
		t = new Thread(this, name);
		t.start();
	}

	@Override
	public void run() {
		System.out.println("In thread");
		try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
			while (socket.isConnected()) {
				try{
					Message receive = (Message) inputStream.readObject();
					LinkedList<String> receivers = receive.getReceivers();
					receivers.add(receive.getNickName());
						if (!Client.getConv().containsKey(receivers)) {
							Client.getConv().put(receivers, new Conversation());
						}
						Client.getConv().get(receivers).getMsgs().add(receive);
						Platform.runLater(new Runnable() {

					        @Override
					        public void run() {
					        	receive.showMessage();
					        }
					      });
						
				}catch(SocketException e) {
					return;
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
}