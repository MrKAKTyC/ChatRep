package server;

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
		return conv;
	}

	public void setConv(Conversation conv) {
		this.conv = conv;
	}

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
