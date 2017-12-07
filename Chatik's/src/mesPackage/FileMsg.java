package mesPackage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.Scanner;

import Client.Client;
import application.Controller;
public class FileMsg extends TextMsg {

	private static final long serialVersionUID = -3019774361840502748L;
//	private File content;
	
	private byte[] content;
	private String Header;
	
	public FileMsg(File content, String text, String sender, LinkedList<String> receiver, Date time) {
		super(text, sender, receiver, time);
		this.content = new byte[(int) content.length()];
		this.Header = content.getName();
		try {
			FileInputStream fis = new FileInputStream(content);
			fis.read(this.content);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] getContent() {
		return content;
	}

	public static void SendMessage(ObjectOutputStream out) {
		System.out.println("Enter file name ");
		Scanner scanner = new Scanner(System.in);
		String fileName = scanner.nextLine();
		// scanner.close();
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				System.out.println("File dosent exist");
			}
			InputStream in = new FileInputStream(file);
			System.out.println("Enter your friend's name ->");
			String receiverName = scanner.nextLine();
			LinkedList<String> to = new LinkedList<>();
			to.add(receiverName);
//			if (Client.getFriends().contains(to)) {
//				System.out.println("Enter you message ->");
//				String message = scanner.nextLine();
//				Date now = new Date(System.currentTimeMillis());
//				FileMsg NewMessage = new FileMsg(file, message, Client.getName(), to, now);
//				if (!Client.getConv().containsKey(to)) {
//					LinkedList<Message> d = new LinkedList<>();
//					d.add(NewMessage);
////					Client.getConv().put(to, new Conversation(to, Client.getName(), d));
//				}
////				Client.getConv().get(to).getMsgs().add(NewMessage);
//				out.writeObject(NewMessage);
//			}USEFUL
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showMessage() {
		try {
			@SuppressWarnings({ "unused", "resource" })
//			FileOutputStream fileOut = new FileOutputStream(content);
//			System.out.println("file " + content.getName() + " was saved in " + content.getAbsolutePath());
			File f = new File("111"+Header);
				f.createNewFile();
			FileOutputStream fos = new FileOutputStream(this.Header);
			System.out.println(this.content.length);
			fos.write(this.content);
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
