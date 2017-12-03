package Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Comparator;
import java.util.LinkedList;

import generated.TextMsg;
import javafx.application.Platform;
import mesPackage.Message;

public class ClientThread implements Runnable{
	private String name;
	private Socket socket;
	private Thread t;

	public ClientThread(Socket socket) {
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
					final Message receive = (Message) inputStream.readObject();
					LinkedList<String> receivers = receive.getReceivers();
					receivers.add(receive.getNickName());
					LinkedList<String> f= new LinkedList<>(receivers);
					System.out.println(f.toString());
					f.remove(Client.getName());
					System.out.println(f.toString());
					f.sort(new Comparator<String>() {

						@Override
						public int compare(String o1, String o2) {
							return o1.compareTo(o2);
						}
						
					});
						TextMsg mes = new TextMsg();
						mes.init(receive);
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
