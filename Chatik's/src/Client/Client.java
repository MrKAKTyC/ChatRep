package Client;

import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map.Entry;

import generated.Conversation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mesPackage.TextMsg;
import server.Const;
import server.MsgXML;

public class Client {
	private static String name;
	private static String IP = "192.168.1.4";
	private static Socket socket;
	private static ObjectOutputStream outputStream;
	private static ObservableList<String> friends = FXCollections.observableArrayList();
	private static TreeMap<String, generated.Conversation> conv;

	public Client(String name) {
		try {
			Client.name = name;
			socket = new Socket(IP, Const.PORT);
			outputStream = new ObjectOutputStream( socket.getOutputStream());
			outputStream.writeObject(Client.getName());
			conv = new TreeMap<>(new Comparator<String>() {

				@Override
				public int compare(String o1, String o2) {
					return o1.compareTo(o2);
				}
			});
			
			new ClientThread(socket); 	// start new thread
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean InitializeFriends() {
	
		try (Scanner scanner = new Scanner(new FileReader("Friends.txt"))) {
			scanner.nextLine();
			while (scanner.hasNext()) {
				String friend = scanner.nextLine();
				friends.add(friend.substring(1, friend.length()-1));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public static boolean SendMessage(LinkedList<String> to, String message, Date time) {
		Date now = new Date(System.currentTimeMillis());
		TextMsg NewMessage = new TextMsg(message, name, to, now);
		LinkedList<String> f= new LinkedList<>(to);
		ArrayList<generated.Message> d = new ArrayList<>();
		generated.TextMsg mes = new generated.TextMsg();
		mes.init(NewMessage);
		d.add(mes);
		f.sort(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
			
		});
		System.out.println("conv");
		for (Entry<String, generated.Conversation> entry : Client.getConv().entrySet()) {
			System.out.print(entry.getKey());
		}
		System.out.print("f.toString() "+f.toString());
		if (conv.containsKey(f.toString())) {
			conv.get(f.toString()).getMsgs().add(mes);
		}
		else {
			Conversation c = new Conversation();
			c.setFriend(f.toString());
			c.setMsgs(d);
			System.out.print(f.toString());
			conv.put(f.toString(), c);
			System.out.println("no key");
		}
		try {
			outputStream.writeObject(NewMessage);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}


	public static boolean AddNewFriend(String friendsName) {
		String SignUpURL = "rmi://" + IP + "/Server";
		boolean isExistNickname = false;
		try {
			ServerIntf signUpServerIntf = (ServerIntf) Naming.lookup(SignUpURL);
			isExistNickname = signUpServerIntf.FindFriend(friendsName);
			if (isExistNickname) {
				friends.add(friendsName);
			}
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			e.printStackTrace();
		}
		return isExistNickname;
	}

	public static TreeMap<String, Conversation> ReadFromXMLFriends() {
		MsgXML xml = new MsgXML();
		for (int i = 0; i < friends.size(); i++) {
			System.out.println("readed " +friends.get(i) );
			if (!xml.readFromFile("[" + friends.get(i) + "].xml")) {
				Conversation dialog = new Conversation();
				conv.put("["+friends.get(i)+"]", dialog );
			} else {
				Conversation dialog = xml.getConv();
				conv.put("["+friends.get(i)+"]", dialog );
			}
		}
		return conv;
	}
	
	public static TreeMap<String, Conversation> getConv() {
		return conv;
	}

	public static String getName() {
		return name;
	}

	public static void setName(String name) {
		Client.name = name;
	}
	public static String getIP() {
		return IP;
	}
	
	public static void setIP(String iP) {
		IP = iP;
	}
	
	public static ObjectOutputStream getOutputStream() {
		return outputStream;
	}
	
	public static void setOutputStream(ObjectOutputStream outputStream) {
		Client.outputStream = outputStream;
	}
	public static ObservableList<String> getFriends() {
		return friends;
	}

	public static void setFriends(ObservableList<String> friends) {
		Client.friends = friends;
	}

	public static Socket getSocket() {
		return socket;
	}

	public static void setSocket(Socket socket) {
		Client.socket = socket;
	}

	public static void disconnect() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
