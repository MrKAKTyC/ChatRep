package application;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.TreeMap;

import Client.Client;
import generated.Conversation;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import mesPackage.TextMsg;

public class Controller implements Initializable {
//	private static LinkedList<LinkedList<String>> groups;
	private static TreeMap<String, ObservableList<VBox>> convers = new TreeMap<String, ObservableList<VBox>>();
	

	@FXML
	ListView<VBox> Conversation;
	@FXML
	MenuBar menuBar;
	@FXML
	TextArea MessageField;
	@FXML
	Button AddNewFriend;
	@FXML
	Button Send;
	@FXML
	TextField FriendNameField;
	@FXML
	ListView<String> FriendsList;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		TextMsg.setConversation(Conversation);
		TextMsg.setFriendsList(FriendsList);
		// MsgXML xml = new MsgXML();

		// try {
		// for (int i = 0; i < friends.size(); i++) {
		// if (!xml.readFromFile("[" + friends.get(i) + "].xml")) {
		// ObservableList<VBox> dialog = FXCollections.observableArrayList();
		// Controller.getConvers().put(friends.get(i), dialog);
		// break;
		// }
		// ObservableList<VBox> dialog = FXCollections.observableArrayList();
		// for (int j = 0; j < xml.getConv().getMsgs().size();) {
		// VBox pane = new VBox();
		// String sender = xml.getConv().getMsgs().get(j).getSender();
		// boolean sameName = false;
		// do {
		// if (j < xml.getConv().getMsgs().size()
		// && sender.equals(xml.getConv().getMsgs().get(j).getSender())) {
		// Label time = new Label(xml.getConv().getMsgs().get(j).getTime());
		// time.setStyle("-fx-text-fill: #cccccc;");
		// Label mes = new Label(xml.getConv().getMsgs().get(j).getText());
		// if (Client.getName().equals(sender)) {
		// pane.setAlignment(Pos.CENTER_LEFT);
		// } else {
		// pane.setAlignment(Pos.CENTER_RIGHT);
		// }
		// pane.getChildren().addAll(mes, time);
		// j++;
		// sameName = true;
		// } else {
		// sameName = false;
		// }
		//
		// } while (sameName);
		// dialog.add(pane);
		// }
		// Controller.getConvers().put(friends.get(i), dialog);
		// }
		// Conversation.setStyle("-fx-control-inner-background: ddffff;");
		// FriendsList.setItems(Client.getFriends());
		// FriendsList.setStyle("-fx-control-inner-background: e6f3ff;");
		// MultipleSelectionModel<String> model = FriendsList.getSelectionModel();
		// model.selectedItemProperty().addListener(new ChangeListener<String>() {
		//
		// @Override
		// public void changed(ObservableValue<? extends String> observable, String
		// oldValue, String newValue) {
		//
		// Conversation.setItems(Controller.getConvers().get(observable.getValue()));
		// Conversation.scrollTo(Controller.getConvers().get(observable.getValue()).size()
		// - 1);
		// }
		// });
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		try {
			Client.InitializeFriends();
			TreeMap<String, Conversation> xml = Client.ReadFromXMLFriends();
			System.out.println("XML SIZE " + xml.size());
			for (Entry<String, Conversation> entry : xml.entrySet()) {
				if (entry.getValue().getMsgs().size() == 0) {
					ObservableList<VBox> dialog = FXCollections.observableArrayList();
					convers.put(entry.getKey().substring(1, entry.getKey().length()-1), dialog);
				} else {
					ObservableList<VBox> dialog = FXCollections.observableArrayList();
					for (int j = 0; j < entry.getValue().getMsgs().size();) {
						VBox pane = new VBox();
						String sender = entry.getValue().getMsgs().get(j).getSender();
						boolean sameName = false;
						do {
							if (j < entry.getValue().getMsgs().size()
									&& sender.equals(entry.getValue().getMsgs().get(j).getSender())) {
								Label time = new Label(entry.getValue().getMsgs().get(j).getTime());
								time.setStyle("-fx-text-fill: #cccccc;");
								Label mes = new Label(entry.getValue().getMsgs().get(j).getText());
								if (Client.getName().equals(sender)) {
									pane.setAlignment(Pos.CENTER_LEFT);
								} else {
									pane.setAlignment(Pos.CENTER_RIGHT);
								}
								pane.getChildren().addAll(mes, time);
								j++;
								sameName = true;
							} else {
								sameName = false;
							}

						} while (sameName);
						dialog.add(pane);
					}
					System.out.println(entry.getKey().substring(1, entry.getKey().length()-1));
					convers.put(entry.getKey().substring(1, entry.getKey().length()-1), dialog);
				}
			}
			Conversation.setStyle("-fx-control-inner-background: ddffff;");
			FriendsList.setItems(Client.getFriends());
			FriendsList.setStyle("-fx-control-inner-background: e6f3ff;");
			MultipleSelectionModel<String> model = FriendsList.getSelectionModel();
			model.selectedItemProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

					Conversation.setItems(Controller.getConvers().get(observable.getValue()));
					if (Controller.getConvers().get(observable.getValue()).size() != 0)
						Conversation.scrollTo(Controller.getConvers().get(observable.getValue()).size() - 1);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void SendMessage(ActionEvent event) {
		String message = MessageField.getText();
		LinkedList<String> to = new LinkedList<>();
		String friendName = FriendsList.getSelectionModel().getSelectedItem();
		to.add(friendName);
		Date now = new Date(System.currentTimeMillis());
		if (Client.SendMessage(to, message, now)) {
			DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
			Label time = new Label(formatter.format(now));
			time.setStyle("-fx-text-fill: #cccccc;");
			Label mes = new Label(message);
			if (convers.get(friendName).size() == 0) {
				VBox pane = new VBox();
				pane.setAlignment(Pos.CENTER_LEFT);
				pane.getChildren().addAll(mes, time);
				convers.get(friendName).add(pane);
			} else {
				if (convers.get(friendName).get(convers.get(friendName).size() - 1)
						.getAlignment() == Pos.CENTER_RIGHT) {
					VBox pane = new VBox();
					pane.setAlignment(Pos.CENTER_LEFT);
					pane.getChildren().addAll(mes, time);
					convers.get(friendName).add(pane);
				} else {
					convers.get(friendName).get(convers.get(friendName).size() - 1).getChildren().addAll(mes, time);
				}
			}
			Conversation.setItems(convers.get(friendName));
			MessageField.setText("");
		}
	}

	@FXML
	public void AddNewFriend(ActionEvent action) {
		String friendsName = FriendNameField.getText();
		if (Client.AddNewFriend(friendsName)) {
			FriendsList.setItems(Client.getFriends());
			ObservableList<VBox> dialog = FXCollections.observableArrayList();
			convers.put(friendsName, dialog);
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("There is any user with such name");
			alert.show();
		}
		FriendNameField.setText("");
	}
	public void AddNewFriend(String friendsName) {
			FriendsList.setItems(Client.getFriends());
	}
//	public static LinkedList<LinkedList<String>> getGroups() {
//		return groups;
//	}
//
//	public static void setGroups(LinkedList<LinkedList<String>> groups) {
//		Controller.groups = groups;
//	}

	public static TreeMap<String, ObservableList<VBox>> getConvers() {
		if(convers==null) {
			convers = new TreeMap<String, ObservableList<VBox>>();
		}
		return convers;
	}

	public static void setConvers(TreeMap<String, ObservableList<VBox>> convers) {
		Controller.convers = convers;
	}

}
