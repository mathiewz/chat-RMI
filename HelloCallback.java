import java.rmi.Remote; 
import java.rmi.RemoteException; 

public interface HelloCallback extends Remote { 
	public void callback(String msg) throws RemoteException; 
	public void kick() throws RemoteException; 
}
