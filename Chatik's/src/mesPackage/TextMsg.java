package mesPackage;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Optional;

import Client.Client;
import application.Controller;
import generated.Conversation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class TextMsg extends Message {
	private static ListView<VBox> Conversation;
	private static ListView<String> FriendsList;
	private static final long serialVersionUID = 4490283261034661567L;
	private String text;

	public TextMsg(String text, String nicName, LinkedList<String> to, Date time) {
		super(nicName, to, time);
		this.text = text;
	}

	public TextMsg(generated.TextMsg msg) {
		super(msg);
		this.text = msg.getText();
	}

	public String getText() {
		return text;
	}

	public void showMessage() {
		String friendName = this.getNickName();
		if (!Client.getConv().containsKey("[" + friendName + "]")) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			ButtonType buttonTypeYes = new ButtonType("Yes");
			ButtonType buttonTypeNo = new ButtonType("No");

			alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeYes) {
				Conversation c = new Conversation();
				FriendsList.getItems().add(friendName);
				Client.getFriends().add(friendName);
				c.setFriend("[" + friendName + "]");
				ArrayList<generated.Message> d = new ArrayList<>();
				c.setMsgs(d);
				Client.getConv().put("[" + friendName + "]", c);
				ObservableList<VBox> dialog = FXCollections.observableArrayList();
				Controller.getConvers().put(friendName, dialog);
			} else {
				return;
			}
		}
		String message = this.getText();
		Label time = new Label(this.getTime());
		time.setStyle("-fx-text-fill: #cccccc;");
		Label mes = new Label(message);
		// if (!Client.getFriends().contains(friendName)) {
		// Client.AddNewFriend(friendName);
		// Controller.getConvers() convers.put(friendsName, dialog);
		// }
		if (Controller.getConvers().get(friendName).size() == 0) {
			VBox pane = new VBox();
			pane.setAlignment(Pos.CENTER_LEFT);
			pane.getChildren().addAll(mes, time);
			Controller.getConvers().get(friendName).add(pane);
		} else if (Controller.getConvers().get(friendName).get(Controller.getConvers().get(friendName).size() - 1)
				.getAlignment() == Pos.CENTER_LEFT) {
			VBox pane = new VBox();
			pane.setAlignment(Pos.CENTER_RIGHT);
			pane.getChildren().addAll(mes, time);
			Controller.getConvers().get(friendName).add(pane);
		} else {
			Controller.getConvers().get(friendName).get(Controller.getConvers().get(friendName).size() - 1)
					.getChildren().addAll(mes, time);
		}
		// Client.getConversation().setItems(Client.getConvers().get(friendName));
		System.out.println(this.getTime() + " [" + this.getNickName() + "]: " + this.getText());
		Conversation.setItems(Controller.getConvers().get(friendName));

	}

	// public static void SendMessage(ObjectOutputStream out) {
	// @SuppressWarnings("resource")
	// Scanner scanner = new Scanner(System.in);
	// System.out.println("If you want to send group message press 1, else - press
	// 2");
	// String answer = scanner.nextLine();
	// switch (answer) {
	// case "1":
	// System.out.println("Choose nmbr of group");
	// for (int i = 0; i < Client.getGroups().size(); i++) {
	// System.out.println(i + "\t");
	// for (int j = 0; j < Client.getGroups().get(i).size(); j++) {
	// System.out.println(Client.getGroups().get(i).get(j) + " ");
	// }
	// }
	// answer = scanner.nextLine();
	// try{
	// Client.getGroups().get(Integer.parseInt(answer));
	// System.out.println("Enter you message ->");
	// String message = scanner.nextLine();
	// Date now = new Date(System.currentTimeMillis());
	// TextMsg NewMessage = new TextMsg(message, Client.getName(),
	// Client.getGroups().get(Integer.parseInt(answer)), now);
	// LinkedList<String> convMates = new
	// LinkedList<>(Client.getGroups().get(Integer.parseInt(answer)));
	// convMates.add(Client.getName());
	// if (!Client.getConv().containsKey(convMates)) {
	// Client.getConv().put(convMates, new Conversation());
	// }
	// Client.getConv().get(convMates).getMsgs().add(NewMessage);
	// try {
	// out.writeObject(NewMessage);
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// System.out.println("Send");
	// break;
	// }catch(IndexOutOfBoundsException |NumberFormatException e) {
	// System.out.println("No such variant");
	// return;
	// }
	// case "2":
	// System.out.println("Enter your friend's name ->");
	// String receiver = scanner.nextLine();
	// LinkedList<String> to = new LinkedList<>();
	// to.add(receiver);
	// if (Client.getFriends().contains(receiver)) {
	// System.out.println("Enter you message ->");
	// String message = scanner.nextLine();
	// Date now = new Date(System.currentTimeMillis());
	// TextMsg NewMessage = new TextMsg(message, Client.getName(), to, now);
	// if (!Client.getConv().containsKey(to)) {
	// Client.getConv().put(to, new Conversation());
	// }
	// Client.getConv().get(to).getMsgs().add(NewMessage);
	// try {
	// out.writeObject(NewMessage);
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// System.out.println("Send");
	// break;
	// }
	// }
	// }

	public static ListView<VBox> getConversation() {
		return Conversation;
	}

	public static void setConversation(ListView<VBox> conversation) {
		Conversation = conversation;
	}

	public static ListView<String> getFriendsList() {
		return FriendsList;
	}

	public static void setFriendsList(ListView<String> friendsList) {
		FriendsList = friendsList;
	}
}
