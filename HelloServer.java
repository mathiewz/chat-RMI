import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class HelloServer extends UnicastRemoteObject implements Hello {
	
	private ArrayList<HelloCallback> listClients;

    public HelloServer() throws RemoteException {
		super();
		listClients = new ArrayList<HelloCallback>();
    }
public void deco(HelloCallback client) throws RemoteException{
		listClients.remove(client);
		System.out.println("deconnexion de "+client.getName());
	}
	public void inscription(HelloCallback client) throws RemoteException{
		listClients.add(client);
		System.out.println("Inscription de "+client.getName());
	}

    public void call(String msg, HelloCallback obj) throws RemoteException {
	System.out.println("Remote Invokation of method call(): " + msg); 
	obj.callback(msg.toUpperCase()); // the callback
    }
    
    public void list(HelloCallback obj) throws RemoteException{
		for(HelloCallback client : listClients){
			obj.callback(client.getName());
		}
	}
    
    public void say(String msg, HelloCallback obj) throws RemoteException {
		//System.out.println("Remote Invokation of method say(): " + msg); 
		for(HelloCallback client : listClients){
			if(!client.equals(obj)){
				client.callback(obj.getName()+": "+msg); // the callback
			}
		}
    }
    
    public void sayTo(String name, String message, HelloCallback obj) throws RemoteException{
    	for(HelloCallback client : listClients){
			if(client.getName().equals(name)){
				client.callback("[private] "+obj.getName()+": "+message); // the callback
			}
		}
    }

    public static void main(String args[]) { 

	if (args.length != 1) {
	    System.out.println("Syntax - HelloServer localhost:port");
	    System.exit(1);
	}

	try { 
	    HelloServer obj = new HelloServer(); // activation

	    String host = args[0];
	    String url = "rmi://" + host + "/HelloServer"; 
	    
	    // Bind this object instance to the name "HelloServer" 
	    System.out.println("Bind the Remote Object to the RMI registry"); 
	    Naming.rebind(url, obj);
	    System.out.println("HelloServer bound in registry"); 

        } catch (Exception e) { 
	    System.out.println("HelloServer err: " + e.getMessage()); 
	    e.printStackTrace(); 
        } 
    } 
}
