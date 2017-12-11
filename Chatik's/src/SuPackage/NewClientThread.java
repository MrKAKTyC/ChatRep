package SuPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.function.Function;

import javafx.application.Platform;
import mesPackage.*;

public class NewClientThread implements Runnable {
	private String name;
	private Socket socket;
	private Thread t;
//	private Function<Message, Boolean> ShowMessage;
	private Function<String, Boolean> Request;

	public NewClientThread(String name, Socket socket, Function<String, Boolean> Request) {
		this.Request = Request;
//		this.ShowMessage = ShowMessage;
		this.name = name;
		this.socket = socket;
		t = new Thread(this, name);
		t.start();
	}

	@Override
	public void run() {
		System.out.println("In thread");
		try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
			while (socket.isConnected()) {
				try {
					
					Message receive =   (Message) inputStream.readObject();
					receive.showMessage();
					
					if(receive instanceof TextMsg){
						if (receive.getText() == null) {
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									Request.apply(receive.getNickName());
								}
							});
						} else {
							LinkedList<String> receivers = receive.getReceivers();
							receivers.add(receive.getNickName());
							LinkedList<String> f = new LinkedList<>(receivers);
							System.out.println(f.toString());
							f.remove(name);
							System.out.println(f.toString());
							generated.TextMsg mes = new generated.TextMsg();
							mes.init(receive);
						}
					}
					

				} catch (SocketException e) {
					return;
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}
}