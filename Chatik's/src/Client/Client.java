package Client;

import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingException;

import SuPackage.Const;
import SuPackage.MsgXML;
import generated.Conversation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import mesPackage.FileMsg;
import mesPackage.TextMsg;

public class Client {
	private String name;
	private static String IP = "192.168.1.5";
	private Socket socket;
	private ObjectOutputStream outputStream;
	private ObservableList<String> friends = FXCollections.observableArrayList();
	private TreeMap<String, generated.Conversation> conv;

	public Client(String name) {
		try {
			this.name = name;
			socket = new Socket(IP, Const.PORT);
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			conv = new TreeMap<>(new Comparator<String>() {

				@Override
				public int compare(String o1, String o2) {
					return o1.compareTo(o2);
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean SignUp(String login, String password) {

		boolean resultSignUp = true;
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
			byte[] digest = md.digest();
			String SignUpURL = "rmi://" + IP + "/Server";
			try {
				ServerIntf signUpServerIntf = (ServerIntf) Naming.lookup(SignUpURL);
				resultSignUp = signUpServerIntf.SignUp(login, new String(digest));
				if (resultSignUp) {
					setName(login);
					try {
						outputStream.writeObject(login);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return true;
				}
			} catch (MalformedURLException | RemoteException | NotBoundException e) {
				System.out.println("Exception");
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e1) {
		}
		return false;
	}

	public boolean SignIn(String login, String password) {
		boolean resultSignUp = true;
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
			byte[] digest = md.digest();
			String SignUpURL = "rmi://" + IP + "/Server";
			try {
				Context namingContext = new InitialContext();
				Enumeration<NameClassPair> e = namingContext.list( "rmi://" + IP + "/");
				while(e.hasMoreElements()) {
					System.out.println(e.nextElement().getName());
				}
				ServerIntf signUpServerIntf = (ServerIntf) namingContext.lookup(SignUpURL);
				resultSignUp = signUpServerIntf.SignIn(login, new String(digest));
				if (resultSignUp) {
					name = login;
					try {
						outputStream.writeObject(login);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					return true;
				}
			} catch ( RemoteException  | NamingException e) {
				System.out.println("Exception");
				e.printStackTrace();
			}

		} catch (NoSuchAlgorithmException e1) {
		}
		return false;
	}

	public boolean InitializeFriends() {

		try (Scanner scanner = new Scanner(new FileReader("Friends.txt"))) {
			scanner.nextLine();
			while (scanner.hasNext()) {
				String friend = scanner.nextLine();
				friends.add(friend.substring(1, friend.length() - 1));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean SendMessage(LinkedList<String> to, String message, Date time) {
		Date now = new Date(System.currentTimeMillis());
		TextMsg NewMessage = new TextMsg(message, name, to, now);
		LinkedList<String> f = new LinkedList<>(to);
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
		for (Entry<String, generated.Conversation> entry : conv.entrySet()) {
			System.out.print(entry.getKey());
		}
		System.out.print("f.toString() " + f.toString());
		if (conv.containsKey(f.toString())) {
			conv.get(f.toString()).getMsgs().add(mes);
		} else {
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
	
	// COPPYY AANNDD PPASSTTEE
	public boolean SendMessage(FileMsg F_Msg) {
		
		LinkedList<String> f = new LinkedList<>(F_Msg.getReceivers());
		ArrayList<generated.Message> d = new ArrayList<>();
		generated.FileMsg mes = new generated.FileMsg();
		mes.init(F_Msg);
		d.add(mes);
		f.sort(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}

		});
		System.out.println("conv");
		for (Entry<String, generated.Conversation> entry : conv.entrySet()) {
			System.out.print(entry.getKey());
		}
		System.out.print("f.toString() " + f.toString());
		if (conv.containsKey(f.toString())) {
			conv.get(f.toString()).getMsgs().add(mes);
		} else {
			Conversation c = new Conversation();
			c.setFriend(f.toString());
			c.setMsgs(d);
			System.out.print(f.toString());
			conv.put(f.toString(), c);
			System.out.println("no key");
		}
		try {
			outputStream.writeObject(F_Msg);
			System.out.println("Sended!!!!!!!!!!!!!!!");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	

	public boolean AddNewFriend(String friendsName) {
		String SignUpURL = "rmi://" + IP + "/Server";
		boolean isExistNickname = false;
		try {
			ServerIntf signUpServerIntf = (ServerIntf) Naming.lookup(SignUpURL);
			isExistNickname = signUpServerIntf.FindFriend(friendsName);
			if (isExistNickname) {
				friends.add(friendsName);
				LinkedList<String> f = new LinkedList<>();
				f.add(friendsName);
				Date now = new Date(System.currentTimeMillis());
				try {
					outputStream.writeObject(new TextMsg(null, name, f,now));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} catch (RemoteException | MalformedURLException | NotBoundException e) {
			e.printStackTrace();
		}
		return isExistNickname;
	}

	public TreeMap<String, Conversation> ReadFromXMLFriends() {
		MsgXML xml = new MsgXML();
		for (int i = 0; i < friends.size(); i++) {
			System.out.println("readed " + friends.get(i));
			if (!xml.readFromFile("[" + friends.get(i) + "].xml")) {
				Conversation dialog = new Conversation();
				conv.put("[" + friends.get(i) + "]", dialog);
			} else {
				Conversation dialog = xml.getConv();
				conv.put("[" + friends.get(i) + "]", dialog);
			}
		}
		return conv;
	}
	public void disconnect() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
}

	public TreeMap<String, Conversation> getConv() {
		return conv;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public ObjectOutputStream getOutputStream() {
		return outputStream;
	}

	public void setOutputStream(ObjectOutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public ObservableList<String> getFriends() {
		return friends;
	}

	public void setFriends(ObservableList<String> friends) {
		this.friends = friends;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

}
