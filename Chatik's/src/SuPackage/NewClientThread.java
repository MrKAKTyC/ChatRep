package SuPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Comparator;
import java.util.LinkedList;

import Client.Client;
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
					LinkedList<String> f= new LinkedList<>(receivers);
					f.add(receive.getNickName());
					f.remove(Client.getName());
					f.sort(new Comparator<String>() {

						@Override
						public int compare(String o1, String o2) {
							return o1.compareToIgnoreCase(o2);
						}
						
					});
						if (!Client.getConv().containsKey(receivers.toString())) {
//							Client.getConv().get(f).getMsgs().add(receive);!!!!!!!!!!!!!!!!!!!!
						}
						else {
							LinkedList<Message> d = new LinkedList<>();
							d.add(receive);
//							Client.getConv().put(f, new Conversation(receivers, name, d));!!!!!!!!!!!!!!!!!!!
						}
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