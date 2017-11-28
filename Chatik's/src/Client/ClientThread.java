package Client;
//package Client;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.util.Scanner;
//
//import SuPackage.Const;
//import SuPackage.NewClientThread;
//
//public class ClientThread implements Runnable{
//	private Thread t;
//	private ServerSocket socket;
//	private static int i = 1;
//
//	public ClientThread() {
//		try {
//			System.out.println("Enter port");
//			Scanner c = new Scanner(System.in);
//			ServerSocket servSocket = new ServerSocket(Const.PORT+c.nextInt());
//			i++;
//			this.socket = servSocket;
//		} catch (IOException e) {
//			System.out.println("Socket exception");
//			e.printStackTrace();
//		}
//		t = new Thread(this, "1");
//		System.out.println("New thread" + t);
//		t.start();
//	}
//	@Override
//	public void run() {
//			Socket newConvers;
//			try {
//				newConvers = socket.accept();
//				ObjectInputStream inputStream  = new ObjectInputStream(newConvers.getInputStream());
//				String userName = (String)inputStream.readObject();
//				System.out.println("You get a new message from user " + userName );
//				new NewClientThread(userName, Client.getSocket(), newConvers);
//		} catch (ClassNotFoundException | IOException e) {
//			e.printStackTrace();
//		}
//	}
//}
