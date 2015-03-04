import java.rmi.Remote; 
import java.rmi.RemoteException; 

public interface HelloCallback extends Remote { 
    public void callback(String msg) throws RemoteException; 
    public String getName() throws RemoteException;
    public void setName(String value) throws RemoteException;
}
