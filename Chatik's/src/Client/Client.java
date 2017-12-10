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
	private static String IP = "192.168.1.2";
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
// private static String name;
// private static boolean authorized;
// private static LinkedList<String> myFriends = new LinkedList<>();
// private static Socket socket;
// private static String IP = "127.0.0.1";
// private static LinkedList<LinkedList<String>> groups;
// private static TreeMap<LinkedList<String>, Conversation> conv ;
//
// public static String getName() {
// return name;
// }
//
// public static void setName(String name) {
// Client.name = name;
// }
//
// public static boolean isAuthorized() {
// return authorized;
// }
//
// public static void setAuthorized(boolean authorized) {
// Client.authorized = authorized;
// }
//
// public static LinkedList<String> getMyFriends() {
// return myFriends;
// }
//
// public static void setMyFriends(LinkedList<String> myFriends) {
// Client.myFriends = myFriends;
// }
//
// public static TreeMap<LinkedList<String>, Conversation> getConv() {
// if(conv ==null) {
// conv = new TreeMap<>(new Comparator<LinkedList<String>>() {
//
// @Override
// public int compare(LinkedList<String> o1, LinkedList<String> o2) {
// if (o1.equals(o2)) {
// return 0;
// } else {
// return -1;
// }
// }
//
// }); }
// return conv;
// }
//
// public static boolean SignUp(String serverIP) {
//
// String login;
// String password;
// boolean resultSignUp = true;
// System.out.println("Enter login =>");
// @SuppressWarnings("resource")
// Scanner scanner = new Scanner(System.in);
// login = scanner.nextLine();
// System.out.println("Enter password =>");
// password = scanner.nextLine();
// MessageDigest md;
// try {
// md = MessageDigest.getInstance("SHA-256");
// md.update(password.getBytes());
// byte[] digest = md.digest();
// String SignUpURL = "rmi://" + serverIP + "/Server";
// try {
// ServerIntf signUpServerIntf = (ServerIntf) Naming.lookup(SignUpURL);
// resultSignUp = signUpServerIntf.SignUp(login, new String(digest));
// if (resultSignUp) {
// setName(login);
// setAuthorized(true);
// return true;
// }
// } catch (MalformedURLException | RemoteException | NotBoundException e) {
// System.out.println("Exception");
// e.printStackTrace();
// }
// } catch (NoSuchAlgorithmException e1) {
// }
// return false;
// }
//
// @SuppressWarnings("resource")
// public static boolean SignIn(String serverIP) {
// String login;
// String password;
// Scanner scanner = new Scanner(System.in);
// boolean resultSignUp = true;
// System.out.println("Enter login =>");
// login = scanner.nextLine();
// System.out.println("Enter password =>");
// password = scanner.nextLine();
// MessageDigest md;
// try {
// md = MessageDigest.getInstance("SHA-256");
// md.update(password.getBytes());
// byte[] digest = md.digest();
// String SignUpURL = "rmi://" + serverIP + "/Server";
// try {
// ServerIntf signUpServerIntf = (ServerIntf) Naming.lookup(SignUpURL);
// resultSignUp = signUpServerIntf.SignIn(login, new String(digest));
// if (resultSignUp) {
// setName(login);
// setAuthorized(true);
// return true;
// }
// } catch (MalformedURLException | RemoteException | NotBoundException e) {
// System.out.println("Exception");
// e.printStackTrace();
// }
//
// } catch (NoSuchAlgorithmException e1) {
// }
// return false;
// }
//
// public static Socket getSocket() {
// return socket;
// }
//
// public static void setSocket(Socket socket) {
// Client.socket = socket;
// }
//
// public static void main(String[] args) throws UnknownHostException,
// IOException {
// try {
// System.out.println(InetAddress.getLocalHost());
// } catch (UnknownHostException e2) {
// // TODO Auto-generated catch block
// e2.printStackTrace();
// }
// MsgXML xml = new MsgXML();
// String userAnswer;
// Socket socket = new Socket(IP, Const.PORT);
// setSocket(socket);
// @SuppressWarnings("resource")
// Scanner scanner = new Scanner(System.in);
// ObjectOutputStream outputStream = new
// ObjectOutputStream(socket.getOutputStream());
// do {
// System.out.println(
// "Enter:\n0 - exit\n1 - sign up\n2 - sign in\n3 - add new friend\n4 - write
// message\n5 - create new group");
// // Scanner scanner = new Scanner(System.in);
// userAnswer = scanner.nextLine();
// switch (userAnswer) {
// case "0":
// break;
// case "1":
// if (SignUp(IP)) {
// System.out.println("Registration completed successfully");
// outputStream.writeObject(getName());
// new NewClientThread(socket);
// } else {
// System.out.println("Login already use");
// }
// break;
// case "2":
// if (SignIn(IP)) {
// System.out.println("Correct data");
// outputStream.writeObject(getName());
// xml.readFromFile(name + ".xml");
// for (int i = 0; i < xml.getConv().getMsgs().size(); i++) {
// System.out.println(xml.getConv().getMsgs().get(i).getTime() + " ["
// + xml.getConv().getMsgs().get(i).getSender() + "] "
// + xml.getConv().getMsgs().get(i).getText());
// }
// new NewClientThread(socket);
// } else {
// System.out.println("Wrong data");
// }
// break;
// case "3":
// System.out.println("Enter your friend's nickname ->");
// String friendsName = scanner.nextLine();
// String SignUpURL = "rmi://" + IP + "/Server";
// try {
// ServerIntf signUpServerIntf = (ServerIntf) Naming.lookup(SignUpURL);
// boolean isExistNickname = signUpServerIntf.FindFriend(friendsName);
// if (isExistNickname) {
// System.out.println("Do you want to add " + friendsName + " to the list of
// your friends?");
// int answer = Integer.parseInt(scanner.nextLine());
// switch (answer) {
// case 0:
// break;
// case 1:
// getMyFriends().add(friendsName);
// System.out.println("This user added to your frieds list");
// break;
// }
// }
// } catch (MalformedURLException | RemoteException | NotBoundException e) {
// System.out.println("Exception");
// e.printStackTrace();
// }
// break;
// case "4":
// if (!isAuthorized()) {
// System.out.println("Previously, you must sign in");
// break;
// }
// System.out.println(
// "Which file do you want send? Press\n1 - text message\n2 - file message\n3 -
// group message");
// System.out.print("Enter answer -> ");
// String answer = scanner.nextLine();
// switch (answer) {
// case "1":
// TextMsg.SendMessage(outputStream);
// break;
// case "2":
// FileMsg.SendMessage(outputStream);
// break;
// default:
// System.out.println("I don't know such variant");
// case "3":
// TextMsg.SendMessage(outputStream);
// break;
// }
// break;
// case "5":
// System.out.println("Enter friends name, for exit enter \"quit\"");
// String friendName = scanner.nextLine();
// LinkedList<String> group = new LinkedList<>();
// while (!friendName.equalsIgnoreCase("quit")) {
// group.add(friendName);
// System.out.println("Enter friends name, for exit enter \"quit\"");
// friendName = scanner.nextLine();
// }
// group.sort(new Comparator<String>() {
//
// @Override
// public int compare(String o1, String o2) {
// return o1.compareToIgnoreCase(o2);
// }
//
// });
// if (group != null)
// Client.getGroups().add(group);
// default:
// System.out.println("Wrong answer");
// break;
// }
// } while (!userAnswer.equals("0"));
// outputStream.close();
// for (Entry<LinkedList<String>, Conversation> entry : conv.entrySet()) {
// xml.writeToFile(entry.getValue(), name + ".xml");
// }
// }
//
// public static LinkedList<LinkedList<String>> getGroups() {
// if(groups == null) {
// groups = new LinkedList<>();
// }
// return groups;
// }
//
// public void setGroups(LinkedList<LinkedList<String>> groups) {
// Client.groups = groups;
// }
// }