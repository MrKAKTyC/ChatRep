package SuPackage;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;

import mesPackage.Message;

public class NewServerThread implements Runnable {
	private String name;
	private Thread t;
	private Socket socket;
	private ObjectInputStream inputStream;

	public NewServerThread(Socket socket) {
		this.name = socket.toString();
		this.socket = socket;
		t = new Thread(this, name);
		// t.start();
	}

	@Override
	public void run() {
		Message text;
		ObjectOutputStream outStream = null;
		try {
			outStream = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try (ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream())) {
			String userName = (String) inputStream.readObject();
			name = userName;
			if (Server.getUsersOffline().containsKey(userName) && !Server.getUsersOffline().get(userName).isEmpty()) {
				while (!Server.getUsersOffline().get(userName).isEmpty()) {
					Message msg = Server.getUsersOffline().get(userName).removeFirst();
					outStream.writeObject(msg);
				}
			}
			Server.getUsersOffline().remove(name);
			Server.getUsersOnline().put(userName, outStream);
			t.setName(userName);
			try {
				while (socket.isConnected()) {
					text = (Message) inputStream.readObject();
					Server.MessageCounter();
					for (int i = 0; i < text.getReceivers().size(); i++) {
						String receiver = text.getReceivers().get(i);
						System.out.println(text.getTime() + " [" + text.getNickName() + "]: " + text.getText());
						if (Server.getUsersOnline().containsKey(receiver)) {
							ObjectOutputStream out = Server.getUsersOnline().get(receiver);
							out.writeObject(text);
							out.reset();
						} else {
							if (!Server.getUsersOffline().containsKey(receiver)) {
								LinkedList<Message> msgs = new LinkedList<>();
								Server.getUsersOffline().put(receiver, msgs);
							}
							Server.getUsersOffline().get(receiver).add(text);
						}
					}
				}
			} catch (SocketException | EOFException e) {

			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();

		}
		Server.getUsersOnline().remove(name, outStream);
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ObjectInputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(ObjectInputStream inputStream) {
		this.inputStream = inputStream;
	}
}
