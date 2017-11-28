package SuPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import mesPackage.Message;

public class Server {
	private static Map<String, ObjectOutputStream> usersOnline = new TreeMap<>();
	private static Map<String, LinkedList<Message>> usersOffline = new TreeMap<>();
	private static int ClientCount = 0;
	private static int MessageCount = 0;
	private static String DBaseName = "UsersData.txt";
	

	@SuppressWarnings({ "unchecked", "resource" })
	public static void main(String[] args) throws IOException, ClassNotFoundException {

		// System.setProperty("java.rmi.server.hostname","92.113.98.74");
		// java.rmi.registry.LocateRegistry.getRegistry("92.113.98.74", 1099);
		java.rmi.registry.LocateRegistry.createRegistry(1099);
		try {
			SignUpServerImpl o = new SignUpServerImpl();
			ObjectInputStream in = null;
			try{
				in = new ObjectInputStream(new FileInputStream(DBaseName));
			}catch (IOException e) {
				new File(DBaseName).createNewFile();
				in = new ObjectInputStream(new FileInputStream(DBaseName));
			}
			o.setUsersData((HashMap<String, String>) in.readObject());
			Naming.rebind("Server", o);
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Except");
			e.printStackTrace();
		}
		InetAddress ia = InetAddress.getLocalHost();
		System.out.println(ia.getHostAddress());
		System.out.println(ia.getHostName());
		new CalculationsThread();
		ServerSocket servSocket = new ServerSocket(Const.PORT);
		Socket clnt;
		ExecutorService exec = Executors.newFixedThreadPool(10);
		
		while (true) {
			clnt = servSocket.accept();
			System.out.println("Clint " + clnt.toString() + " connected");
			setClientCount(getClientCount() + 1);
			exec.execute(new NewServerThread(clnt));
		}
	}
	public static void MessageCounter() {
		setMessageCount(getMessageCount() + 1);
	}
	
	public static Map<String, ObjectOutputStream> getUsersOnline() {
		return usersOnline;
	}

	public static void setUsersOnline(Map<String, ObjectOutputStream> usersOnline) {
		Server.usersOnline = usersOnline;
	}

	public static Map<String, LinkedList<Message>> getUsersOffline() {
		return usersOffline;
	}

	public static void setUsersOffline(Map<String, LinkedList<Message>> usersOffline) {
		Server.usersOffline = usersOffline;
	}

	public static int getMessageCount() {
		return MessageCount;
	}

	public static void setMessageCount(int messageCount) {
		MessageCount = messageCount;
	}

	public static int getClientCount() {
		return ClientCount;
	}

	public static void setClientCount(int clientCount) {
		ClientCount = clientCount;
	}

}