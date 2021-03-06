import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner; 

class HelloCallbackServer extends UnicastRemoteObject implements HelloCallback {

	private String username;

	public HelloCallbackServer() throws RemoteException {
		super();
	}

	public void callback(String msg) throws RemoteException {
		if(!msg.equals("ping")) System.out.println(msg); 
	}
	public void kick() throws RemoteException {
		System.exit(0);
	}

}

public class HelloClient {

	public static void main(String args[]) { 

		// The remote object
		Hello obj = null; 

		String username;

		if (args.length != 1) {
			System.out.println("Syntax - HelloClient host:port");
			System.exit(1);
		}

		try { 
			String host = args[0];
			String url = "rmi://" + host + "/HelloServer"; 
			System.out.println("Look up for the Remote Object from RMI registry " + url); 
			obj = (Hello)Naming.lookup(url); 

			System.out.println("Try to export Callback object..."); 
			HelloCallbackServer objback = new HelloCallbackServer();

			Scanner sc = new Scanner(System.in);
			System.out.println("Veuillez rentrer votre nom");
			username= sc.nextLine();
			System.out.println("Inscription au serveur...");
			obj.inscription(username, objback); 

			while(sc.hasNextLine()){
				String str = sc.nextLine();
				if(str.equals("/quit")){obj.deco(objback);break;}
				else if (str.equals("/list")){ obj.list(objback);}
				else if (str.length()>=4 && str.substring(0, 4).equals("/mp ")){
					int indexMessage = str.indexOf(' ', 4);
					String name = str.substring(4, indexMessage);
					String message = str.substring(indexMessage+1);
					obj.sayTo(name, message, objback);
				} else if (str.length()>=6 && str.substring(0, 6).equals("/modo ")){
					obj.moderation(str.substring(6), objback);
				} else if (str.length()>=6 && str.substring(0, 6).equals("/kick ")){
					obj.kick(str.substring(6), objback);
				} else {
					obj.say(str, objback);
				}
			}
			System.out.println("Try to unexport the Callback object..."); 
			UnicastRemoteObject.unexportObject(objback,true); // force

		} catch (Exception e) { 
			System.out.println("HelloClient exception: " + e.getMessage()); 
			e.printStackTrace(); 
		} 

	} 
}
