package SuPackage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.function.Function;

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
					
					Object tmp =   inputStream.readObject();
					
					Message receive;
					
					if(tmp instanceof FileMsg){
						System.out.println("else");
						receive = (FileMsg)tmp;
						Download((FileMsg)tmp);
					} else {
						System.out.println("if");
						receive = (TextMsg)tmp;
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
							f.sort(new Comparator<String>() {
								
								@Override
								public int compare(String o1, String o2) {
									return o1.compareTo(o2);
								}
								
							});
							generated.TextMsg mes = new generated.TextMsg();
							mes.init(receive);
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									ShowMessage.apply(receive);
									// receive.showMessage();
								}
							});
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
	private void Download(FileMsg F_Msg){
		String File_Name = F_Msg.getHeader();
		File_Name = ("NEW" + File_Name);
		File dowloaded = new File(File_Name);
		
		try {
			dowloaded.createNewFile();
			
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dowloaded));
			bos.write(F_Msg.getContent());
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}