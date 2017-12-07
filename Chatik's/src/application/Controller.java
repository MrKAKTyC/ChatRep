package application;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TreeMap;

import Client.Client;
import SuPackage.NewClientThread;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import mesPackage.Message;

public class Controller implements Initializable {
	private TreeMap<String, ObservableList<VBox>> convers = new TreeMap<String, ObservableList<VBox>>();
	private Client client = Main.getClient();
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

	public static class MessageException extends Exception {

		public MessageException() {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("You cannot send empty message");
			alert.show();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			client.InitializeFriends();
			new NewClientThread(client.getName(), client.getSocket(), this::ShowMessage,this::Request);
			TreeMap<String, Conversation> xml = client.ReadFromXMLFriends();
			System.out.println("XML SIZE " + xml.size());
			for (Entry<String, Conversation> entry : xml.entrySet()) {
				if (entry.getValue().getMsgs().size() == 0) {
					ObservableList<VBox> dialog = FXCollections.observableArrayList();
					convers.put(entry.getKey().substring(1, entry.getKey().length() - 1), dialog);
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
								if (client.getName().equals(sender)) {
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
					System.out.println(entry.getKey().substring(1, entry.getKey().length() - 1));
					convers.put(entry.getKey().substring(1, entry.getKey().length() - 1), dialog);
				}
			}
			Conversation.setStyle("-fx-control-inner-background: ddffff;");
			FriendsList.setItems(client.getFriends());
			FriendsList.setStyle("-fx-control-inner-background: e6f3ff;");
			MultipleSelectionModel<String> model = FriendsList.getSelectionModel();
			model.selectedItemProperty().addListener(new ChangeListener<String>() {

				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

					Conversation.setItems(convers.get(observable.getValue()));
					if (convers.get(observable.getValue()).size() != 0)
						Conversation.scrollTo(convers.get(observable.getValue()).size() - 1);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean ShowMessage(Message message) {
		String friendName = message.getNickName();
		if (!client.getConv().containsKey("[" + friendName + "]")) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			ButtonType buttonTypeYes = new ButtonType("Yes");
			ButtonType buttonTypeNo = new ButtonType("No");
			alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeYes) {
				Conversation c = new Conversation();
				FriendsList.getItems().add(friendName);
				client.getFriends().add(friendName);
				c.setFriend("[" + friendName + "]");
				ArrayList<generated.Message> d = new ArrayList<>();
				c.setMsgs(d);
				client.getConv().put("[" + friendName + "]", c);
				ObservableList<VBox> dialog = FXCollections.observableArrayList();
				convers.put(friendName, dialog);
			} else {
				return false;
			}
		}
		String messageText = message.getText();
		Label time = new Label(message.getTime());
		time.setStyle("-fx-text-fill: #cccccc;");
		Label mes = new Label(messageText);
		if (!client.getFriends().contains(friendName)) {
			client.AddNewFriend(friendName);
			ObservableList<VBox> dialog = FXCollections.observableArrayList();
			convers.put(friendName, dialog);
		}
		if (convers.get(friendName).size() == 0) {
			VBox pane = new VBox();
			pane.setAlignment(Pos.CENTER_LEFT);
			pane.getChildren().addAll(mes, time);
			convers.get(friendName).add(pane);
		} else if (convers.get(friendName).get(convers.get(friendName).size() - 1).getAlignment() == Pos.CENTER_LEFT) {
			VBox pane = new VBox();
			pane.setAlignment(Pos.CENTER_RIGHT);
			pane.getChildren().addAll(mes, time);
			convers.get(friendName).add(pane);
		} else {
			convers.get(friendName).get(convers.get(friendName).size() - 1).getChildren().addAll(mes, time);
		}
		// Client.getConversation().setItems(Client.getConvers().get(friendName));
		System.out.println(message.getTime() + " [" + message.getNickName() + "]: " + message.getText());
		Conversation.setItems(convers.get(friendName));
		return false;
	}

	@FXML
	public void SendMessage(ActionEvent event) throws MessageException {
		String message = MessageField.getText();
		if (message.equals("")) {
//			throw new MessageException();
		} else {
			LinkedList<String> to = new LinkedList<>();
			String friendName = FriendsList.getSelectionModel().getSelectedItem();
			if (friendName == null) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setContentText("Previously, you must add friend");
				alert.show();
			} else {
				to.add(friendName);
				Date now = new Date(System.currentTimeMillis());
				if (client.SendMessage(to, message, now)) {
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
							convers.get(friendName).get(convers.get(friendName).size() - 1).getChildren().addAll(mes,
									time);
						}
					}
					Conversation.setItems(convers.get(friendName));
					MessageField.setText("");
				}
			}
		}
	}

	@FXML
	public void AddNewFriend(ActionEvent action) {
		String friendsName = FriendNameField.getText();
		if (client.AddNewFriend(friendsName)) {
			FriendsList.setItems(client.getFriends());
			ObservableList<VBox> dialog = FXCollections.observableArrayList();
			convers.put(friendsName, dialog);
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setContentText("There is any user with such name");
			alert.show();
		}
		FriendNameField.setText("");
	}
	public boolean Request(String friendName) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setContentText("Do you want to add " + friendName +" to yours friends list?");
		ButtonType buttonTypeYes = new ButtonType("Yes");
		ButtonType buttonTypeNo = new ButtonType("No");
		alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeYes) {
			Conversation c = new Conversation();
//			FriendsList.getItems().add(friendName);
			client.getFriends().add(friendName);
			c.setFriend("[" + friendName + "]");
			ArrayList<generated.Message> d = new ArrayList<>();
			c.setMsgs(d);
			client.getConv().put("[" + friendName + "]", c);
			ObservableList<VBox> dialog = FXCollections.observableArrayList();
			convers.put(friendName, dialog);
		}else {
			
		}
		return false;
		
	}
	public boolean setClient(Client client) {
		this.client = client;
		return false;
	}

	public void AddNewFriend(String friendsName) {
		FriendsList.setItems(client.getFriends());
	}

	public TreeMap<String, ObservableList<VBox>> getConvers() {
		if (convers == null) {
			convers = new TreeMap<String, ObservableList<VBox>>();
		}
		return convers;
	}

	public void setConvers(TreeMap<String, ObservableList<VBox>> convers) {
		this.convers = convers;
	}

	public Client getClient() {
		return client;
	}

}
