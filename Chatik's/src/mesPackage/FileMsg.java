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
public class FileMsg extends Message {

	private static final long serialVersionUID = -3019774361840502748L;
	private byte[] content;
	private String Header;
	

	public FileMsg(File content, String sender, LinkedList<String> receiver, Date time) {
		super(sender, receiver, time);
		this.Header = content.getName();
		try {
			this.content = new byte[(int) content.length()];
			FileInputStream fis = new FileInputStream(content);
			fis.read(this.content);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showMessage() {
		try {
			@SuppressWarnings({ "unused", "resource" })
			File f = new File("111"+Header);
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(this.Header);
			System.out.println(this.content.length);
			fos.write(this.content);
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public byte[] getContent() {
		return content;
	}

	public String getHeader() {
		return Header;
	}
	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}
}
