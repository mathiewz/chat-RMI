import java.rmi.Remote; 
import java.rmi.RemoteException; 

public interface Hello extends Remote { 
	public void inscription(String name, HelloCallback client) throws RemoteException;
	public void deco(HelloCallback client) throws RemoteException;
	public void say(String msg, HelloCallback obj) throws RemoteException;
	public void list(HelloCallback obj) throws RemoteException;
	public void sayTo(String name, String message, HelloCallback obj) throws RemoteException;
	public void ping() throws RemoteException;
	public void moderation(String mdp, HelloCallback client) throws RemoteException;
	public void kick(String name, HelloCallback client) throws RemoteException;
}
