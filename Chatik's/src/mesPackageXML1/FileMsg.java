package mesPackageXML1;

import java.io.File;

public class FileMsg extends TextMsg {

	private static final long serialVersionUID = -3019774361840502748L;
	private File content;

	public void setContent(File content) {
		this.content = content;
	}

	public File getContent() {
		return content;
	}
	
}
