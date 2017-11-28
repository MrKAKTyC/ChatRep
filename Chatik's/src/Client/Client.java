package Client;

import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.TreeMap;

import SuPackage.Const;
import SuPackage.MsgXML;
import SuPackage.NewClientThread;
import application.Controller;
import generated.Conversation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import mesPackage.Message;
import mesPackage.TextMsg;

//package Client;
//
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.net.InetAddress;
//import java.net.MalformedURLException;
//import java.net.Socket;
//import java.net.UnknownHostException;
//import java.rmi.Naming;
//import java.rmi.NotBoundException;
//import java.rmi.RemoteException;
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//import java.util.Comparator;
//import java.util.LinkedList;
//import java.util.Map.Entry;
//import java.util.Scanner;
//import java.util.TreeMap;
//
//import SuPackage.Const;
//import SuPackage.MsgXML;
//import SuPackage.NewClientThread;
//import mesPackage.Conversation;
//import mesPackage.FileMsg;
//import mesPackage.TextMsg;
//
//
public class Client {
	private static String name;
	private static String IP = "192.168.1.4";
	private static Socket socket;
	private static ObjectOutputStream outputStream;
	private static ObservableList<String> friends = FXCollections.observableArrayList();

	public Client() {
		try {
			socket = new Socket(IP, Const.PORT);
			outputStream = new ObjectOutputStream( socket.getOutputStream());
			outputStream.writeObject(Client.getName());
			new NewClientThread(socket);
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public static boolean InitializeFriends() {
		try (Scanner scanner = new Scanner(new FileReader("Friends.txt"))) {
			scanner.nextLine();
			while (scanner.hasNext()) {
				String friend = scanner.nextLine();
				friends.add(friend);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private static boolean InitializeConversations() {

		return false;
	}

	public static boolean SendMessage(String receiver, LinkedList<String> to, String message, Date time) {
		Date now = new Date(System.currentTimeMillis());
		TextMsg NewMessage = new TextMsg(message, name, to, now);
		if (!Controller.getConv().containsKey(to)) {
			Controller.getConv().put(to, new mesPackage.Conversation());
		}
		Controller.getConv().get(to).getMsgs().add(NewMessage);
		try {
			outputStream.writeObject(NewMessage);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
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
		TreeMap<String, Conversation> result = new TreeMap<>();
		Conversation dialog;
		for (int i = 0; i < friends.size(); i++) {
			if (!xml.readFromFile("[" + friends.get(i) + "].xml")) {
				dialog = new Conversation();
				result.put(friends.get(i), dialog);
				break;
			} else {
				dialog = xml.getConv();
				result.put(friends.get(i), dialog);
			}
		}
		return result;
	}

	public static String getName() {
		return name;
	}

	public static void setName(String name) {
		Client.name = name;
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