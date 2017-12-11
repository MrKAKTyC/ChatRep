package mesPackage;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.function.Function;

import javafx.application.Platform;

public class FileMsg extends Message {

	private static final long serialVersionUID = -3019774361840502748L;
	private byte[] content;
	private String Header;
	private static Function<FileMsg, Boolean> ShowFileMsg;

	public static void setShowFileMsg(Function<FileMsg, Boolean> request) {
		ShowFileMsg = request;
	}

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

	@Override
	public void showMessage() {
		Download();
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ShowFileMsg.apply(FileMsg.this);
			}
		});
	}

	public void Download() {
		System.out.println(this.Header + " Downloading now");
		String File_Name = this.getHeader();
		if (new File(this.Header).exists()) {
			File_Name = ("NEW" + File_Name);
		}
		File dowloaded = new File(File_Name);
		try {
			dowloaded.createNewFile();
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(dowloaded));
			bos.write(this.getContent());
			bos.close();

			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void Open() {
		if( Desktop.isDesktopSupported() )
	{
	    new Thread(() -> {
	        	   File file = new File(this.Header);
					try {
						Desktop.getDesktop().open(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
	       }).start();
	}}

}