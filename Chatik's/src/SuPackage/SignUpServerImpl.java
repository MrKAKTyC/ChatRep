package SuPackage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

import Client.ServerIntf;

public class SignUpServerImpl extends UnicastRemoteObject implements ServerIntf {
	private static final long serialVersionUID = 999360773276061465L;
	
	private HashMap<String, String> UsersData;
	
	public HashMap<String, String> getUsersData() {
		if(UsersData==null) {
			UsersData = new  HashMap<String, String> ();
		}
		return UsersData;
	}

	protected SignUpServerImpl() throws RemoteException {
	}

	@Override
	public boolean SignUp(String login, String passwordHash) throws RemoteException {
		
			if(UsersData.containsKey(login)) {
				return false;
			}
			else {
				UsersData.put(login, passwordHash);
				try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("UsersData.txt"))){
					out.writeObject(UsersData);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}
		
	}

	public void setUsersData(HashMap<String, String> usersData) {
		UsersData = usersData;
	}

	@Override
	public boolean SignIn(String login, String passwordHash) throws RemoteException {
		if(UsersData.containsKey(login)) {
			if(UsersData.get(login).equals(passwordHash)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean FindFriend(String login) throws RemoteException {
		if(UsersData.containsKey(login)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isFriendOnline(String login) throws RemoteException {
		if(Server.getUsersOnline().containsKey(login)) {
			return true;
		}
		return false;
	}

//	@Override
//	public boolean setSocket(ServerSocket sS) throws RemoteException {
//		// TODO Auto-generated method stub
//		return false;
//	}

}
