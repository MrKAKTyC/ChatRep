package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerIntf extends Remote{
	boolean SignUp(String login, String passwordHash) throws RemoteException;
	boolean SignIn(String login, String passwordHash) throws RemoteException;
	boolean FindFriend(String login) throws RemoteException;
	boolean isFriendOnline(String login) throws RemoteException;
}
