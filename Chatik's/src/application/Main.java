package application;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.Map.Entry;

import Client.Client;
import Client.ServerIntf;
import SuPackage.MsgXML;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import mesPackage.Conversation;
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
	private static String IP = "192.168.1.2";

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
				boolean resultSignUp = true;
				MessageDigest md;
				try {
					md = MessageDigest.getInstance("SHA-256");
					md.update(password.getBytes());
					byte[] digest = md.digest();
					String SignUpURL = "rmi://" + IP + "/Server";
					ServerIntf signUpServerIntf = (ServerIntf) Naming.lookup(SignUpURL);
					resultSignUp = signUpServerIntf.SignIn(login, new String(digest));
					if (resultSignUp) {
						Client.setName(login);
						setAuthorized(true);
						AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("Chat.fxml"));
						Scene scene = new Scene(root, 600, 500);
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						primaryStage.setScene(scene);
						primaryStage.setTitle("Chatic");
						primaryStage.show();

					}
				} catch (NotBoundException | IOException | NoSuchAlgorithmException e) {
					System.out.println("Exception");
					e.printStackTrace();
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
				boolean resultSignUp = true;
				MessageDigest md;
				try {
					md = MessageDigest.getInstance("SHA-256");
					md.update(password.getBytes());
					byte[] digest = md.digest();
					String SignUpURL = "rmi://" + IP + "/Server";
					ServerIntf signUpServerIntf = (ServerIntf) Naming.lookup(SignUpURL);
					resultSignUp = signUpServerIntf.SignUp(login, new String(digest));
					if (resultSignUp) {
						Client.setName(login);
						setAuthorized(true);
						AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("Chat.fxml"));
						Scene scene = new Scene(root, 600, 500);
						scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
						primaryStage.setScene(scene);
						primaryStage.setTitle("Chatic");
						primaryStage.show();
					} else {
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Data isn't correct");
						alert.showAndWait();
					}
				} catch (NotBoundException | NoSuchAlgorithmException | IOException e) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("There are some problems");
					alert.showAndWait();
					System.out.println("Exception");
					e.printStackTrace();
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
			PrintWriter fr = new PrintWriter("Friends.txt");
			fr.println();
			System.out.println(Client.getFriends().size());
			for (int i = 0; i < Client.getFriends().size(); i++) {
				fr.println(Client.getFriends().get(i));
			}
			fr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MsgXML xml = new MsgXML();
		for (Entry<LinkedList<String>, Conversation> entry : Controller.getConv().entrySet()) {
			xml.writeToFile(entry.getValue(), entry.getKey() + ".xml");
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
