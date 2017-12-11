package application;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map.Entry;

import Client.Client;
import SuPackage.MsgXML;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;

public class Main extends Application {
	private static boolean authorized;
	
	private static Client client;
	public static Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		Main.client = client;
	}

	public static boolean isAuthorized() {
		return authorized;
	}

	public static void setAuthorized(boolean authorized) {
		Main.authorized = authorized;
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Chatic");
		VBox vbox = new VBox(10);
		vbox.setAlignment(Pos.CENTER);
		Label EnterName = new Label("Enter username");
		Label EnterPassword = new Label("Enter password");
		TextField UserNameField = new TextField();
		PasswordField PasswordField = new PasswordField();
		Button SignIn = new Button("Sign In");
		SignIn.setPrefWidth(300);
		Button SignUp = new Button("Sign Up");
		SignUp.setPrefWidth(300);
		Label CorrectPasswordLength = new Label();
		vbox.getChildren().addAll(EnterName, UserNameField, EnterPassword, PasswordField, CorrectPasswordLength, SignIn,
				SignUp);
		SignIn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String login = UserNameField.getText();
				String password = PasswordField.getText();
				client = new Client(login);
					if (client.SignIn(login, password)) {
						try {
							AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("Chat.fxml"));
							Scene scene = new Scene(root, 600, 500);
							scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
							primaryStage.setScene(scene);
							primaryStage.setTitle("Chatic");
							primaryStage.show();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					else {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setContentText("Wrong data");
						alert.showAndWait();
					}
			}

		});
		SignUp.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				CorrectPasswordLength.setText("");
				String login = UserNameField.getText();
				String password = PasswordField.getText();
				if (password.length() < 6) {
					CorrectPasswordLength.setText("Password must be longer");
					return;
				}
					client = new Client(login);
					if (client.SignUp(login, password)) {
						try {
							AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("Chat.fxml"));
							Scene scene = new Scene(root, 600, 500);
							scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
							primaryStage.setScene(scene);
							primaryStage.setTitle("Chatic");
							primaryStage.show();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Data isn't correct");
						alert.showAndWait();
					}
			}
		});
		Scene dialogScene = new Scene(vbox, 300, 300);
		primaryStage.setScene(dialogScene);
		primaryStage.show();
	}

	@Override
	public void stop() {
		try {
			PrintWriter fr = new PrintWriter(Main.client.getName() + " Friends.txt");
			fr.println();
			System.out.println(client.getFriends().size());
			for (int i = 0; i < client.getFriends().size(); i++) {
				fr.println("["+client.getFriends().get(i)+"]");
			}
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		MsgXML xml = new MsgXML();
		for (Entry<String, generated.Conversation> entry : client.getConv().entrySet()) {
			xml.writeToFile(entry.getValue(), entry.getKey() + ".xml");
		}
		client.disconnect();
		try {
			super.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
