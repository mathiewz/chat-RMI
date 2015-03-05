import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;


public class HelloServer extends UnicastRemoteObject implements Hello {

	private HashMap<HelloCallback, String> listClients;
	private ArrayList<HelloCallback> listAdmin;

	public HelloServer() throws RemoteException {
		super();
		listClients = new HashMap<HelloCallback, String>();
		listAdmin = new ArrayList<HelloCallback>();
	}

	public void deco(HelloCallback client) throws RemoteException{
		System.out.println("deconnexion de "+listClients.get(client));
		listClients.remove(client);
	}

	public void kick(String name, HelloCallback client) throws RemoteException{
		ping();
		System.out.println(name);
		HelloCallback toKick 	= null;
		if(listAdmin.contains(client)){
			for(Entry<HelloCallback, String> entry : listClients.entrySet()) {
				if(name.equals(entry.getValue())){
					toKick = entry.getKey();
				}
			}
			if(toKick != null){
				deco(toKick);
				toKick.callback("Vous avez été ejecté du chat !");
				try{toKick.kick();}
				catch(RemoteException e){System.out.println("L'utilisateur "+name+" à été ejecté du chat");}
			}else{
				client.callback("utilisateur non trouvé");
			}
		} else {
			client.callback("Vous devez être modérateur pour faire ceci");
		}
	}

	public void inscription(String name, HelloCallback client) throws RemoteException{
		listClients.put(client, name);
		System.out.println("Inscription de "+listClients.get(client));
		say(listClients.get(client)+" à rejoint le chat", client);
		client.callback("Inscription ok!");
	}
	public void moderation(String mdp, HelloCallback client) throws RemoteException{
		if(mdp.equals("admin")){
			listAdmin.add(client);
			System.out.println(listClients.get(client)+" est passé modérateur");
			say(listClients.get(client)+" est passé modérateur", client);
			client.callback("Godness mod activate");
		} else {
			client.callback("Mot de passe erroné");
		}
	}

	public void list(HelloCallback obj) throws RemoteException{
		ping();
		for(Entry<HelloCallback, String> entry : listClients.entrySet()) {
			HelloCallback client = entry.getKey();
			obj.callback(entry.getValue());
		}
	}

	public void say(String msg, HelloCallback obj) throws RemoteException {
		ping();
		String Rang= listAdmin.contains(obj)?"@":"";
		for(Entry<HelloCallback, String> entry : listClients.entrySet()) {
			HelloCallback client = entry.getKey();
			if(!client.equals(obj)){
				client.callback(Rang+listClients.get(obj)+": "+msg); // the callback
			}
		}
	}

	public void sayTo(String name, String message, HelloCallback obj) throws RemoteException{
		ping();
		for(Entry<HelloCallback, String> entry : listClients.entrySet()) {
			HelloCallback client = entry.getKey();
			if(listClients.get(client).equals(name)){
				client.callback("[private] "+listClients.get(obj)+": "+message); // the callback
			}
		}
	}

	public void ping() throws RemoteException{
		ArrayList<HelloCallback> list = new ArrayList<HelloCallback>();
		for(Entry<HelloCallback, String> entry : listClients.entrySet()) {
			list.add(entry.getKey());
		}
		for(HelloCallback client : list){
			try{
				client.callback("ping");
			}
			catch(RemoteException e){
				deco(client);
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
