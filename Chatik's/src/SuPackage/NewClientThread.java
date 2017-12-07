package SuPackage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.function.Function;

import generated.TextMsg;
import javafx.application.Platform;
import mesPackage.*;

public class NewClientThread implements Runnable {
	private String name;
	private Socket socket;
	private Thread t;
	private Function<Message, Boolean> ShowMessage;
	private Function<String, Boolean> Request;

	public NewClientThread(String name, Socket socket, Function<Message, Boolean> ShowMessage,
			Function<String, Boolean> Request) {
		this.Request = Request;
		this.ShowMessage = ShowMessage;
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
					Message receive = (Message) inputStream.readObject();
					if (receive.getText() == null) {
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								Request.apply(receive.getNickName());
								// receive.showMessage();
							}
						});
					} else {
						LinkedList<String> receivers = receive.getReceivers();
						receivers.add(receive.getNickName());
						LinkedList<String> f = new LinkedList<>(receivers);
						System.out.println(f.toString());
						f.remove(name);
						System.out.println(f.toString());
						f.sort(new Comparator<String>() {

							@Override
							public int compare(String o1, String o2) {
								return o1.compareTo(o2);
							}

						});
						// ShowMessage.apply(receive);
						TextMsg mes = new TextMsg();
						mes.init(receive);
						// if (Client.getConv().containsKey(f.toString())) {
						// Client.getConv().get(f.toString()).getMsgs().add(mes);
						// }
						// else {
						// ArrayList<generated.Message> d = new ArrayList<>();
						// Conversation c = new Conversation();
						// c.setFriend(f.toString());
						// c.setMsgs(d);
						// d.add(mes);
						// System.out.print(f.toString());
						// Client.getConv().put(f.toString(), c);
						// System.out.println("no key");
						// }
						Platform.runLater(new Runnable() {

							@Override
							public void run() {
								ShowMessage.apply(receive);
								// receive.showMessage();
							}
						});
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