package SuPackage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import generated.*;

public class MsgXML {

	private Conversation conv = new Conversation();

	public Conversation getConv() {
//		String sender = conv.getFriend();
//		LinkedList<mesPackage.Message> msgs=new LinkedList<mesPackage.Message>();
//		for(int i = 0;i<conv.getMsgs().size();i++) {
//			DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
//			Date date = null;
//			try {
//			 date=formatter.parse(conv.getMsgs().get(i).getTime());
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			mesPackage.TextMsg txt = new mesPackage.TextMsg(conv.getMsgs().get(i).getText(), conv.getMsgs().get(i).getSender(), conv.getMsgs().get(i).getReceiver(),date );
//			msgs.add(txt);
//		}
//		LinkedList<String> f = new LinkedList<>();
//		f.add(conv.getFriend());
//		LinkedList<String> friends = new LinkedList<>(f);
//		mesPackage.Conversation result = new mesPackage.Conversation(friends, sender, msgs);
//		return result;
		return conv;
	}

	public void setConv(Conversation conv) {
		this.conv = conv;
	}

	// public LinkedList<String> getFriend() {
	// return conv.getFriend();
	// }
	//
	// public void setFriend(LinkedList<String> friend) {
	// conv.setFriend(friend);
	//
	// }

	public MsgXML() {
	}

	public boolean readFromFile(String fileName) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Conversation.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			conv = (Conversation) unmarshaller.unmarshal(new FileInputStream(fileName));
			return true;
		} catch (FileNotFoundException e) {
			System.out.println("No file!");
			return false;
		} catch (JAXBException e) {
			System.out.println("Error!");
			return false;
		}
	}

	public boolean writeToFile(Conversation conversation, String fileName) {
		try {
//			System.out.println("______________");
//			for (int i = 0; i < conversation.getMsgs().size(); i++) {
//				LinkedList<String> receivers = new LinkedList<>();
//				System.out.println("sender ->" + conversation.getSender());
//				conversation.getMsgs().get(i);
//				TextMsg tm = new TextMsg();
//				for (int j = 0; j < conversation.getFriend().size(); j++) {
//					receivers.add(conversation.getFriend().get(j));
//					System.out.println("receiver->" + conversation.getFriend().get(j));
//				}
//				tm.setReceiver(receivers);
//				tm.setSender(conversation.getMsgs().get(i).getNickName());
//				tm.setText(conversation.getMsgs().get(i).getText());
//				tm.setTime(conversation.getMsgs().get(i).getTime());
//				conv.getMsgs().add(tm);
//			}
			JAXBContext jaxbContext = JAXBContext.newInstance(generated.Conversation.class);
			Marshaller marshaller = jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			marshaller.marshal(conversation, new FileWriter(fileName));
			return true;
		} catch (JAXBException | IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
