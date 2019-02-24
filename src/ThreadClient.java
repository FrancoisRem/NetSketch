import java.io.ObjectInputStream;
import java.net.Socket;

public class ThreadClient extends Thread {
	
	//Attributs
	int numeroClient;
	Serveur serveur;
	Socket socketClient;
	Donnee data;
	boolean stop;
	
	//Constructeur	
	public ThreadClient(Serveur s, Socket so) {
	    serveur = s;
	    numeroClient = serveur.numeroDernierClient();
	    socketClient = so;
	}
	
	//M�thodes
	public void run() {
	    try {
	      // Ouvrir flux lecture des données envoyées par le joueur
	      ObjectInputStream in = new ObjectInputStream(socketClient.getInputStream());
	      // Boucle de jeu
	      while ( !stop ) {
	    	// Lecture de la derni�re s�quence entr�e par le client
	    	  
	    	  data = (Donnee) in.readObject();
	    	  switch(data.getType()) {
				case SEQUENCE:
			        Sequence s = data.getSeq();
			        serveur.dessiner(numeroClient, s);
			        serveur.informerClientsDessin();
					break;
				case MESSAGE:
					String msg = data.getMes();
					serveur.actualiserMessage(numeroClient, msg);
					System.out.println("serveur recoit " + msg);
					serveur.informerClientsChat();
					break;
				case ACTION:
				  if(data.getAction()==0) {
				    serveur.defaire(numeroClient);
				    serveur.informerClientsDessin();
				  }else if(data.getAction()==1) {
				    serveur.refaire(numeroClient);
				    serveur.informerClientsDessin();
				  }else if(data.getAction()==2) {
					serveur.nouveau();
					serveur.informerClientsDessin();
				  }
				  break;
			default:
				System.err.println("Erreur Communication Client/Serveur");
				break;
				}			
	        // Petite pause...
	        sleep(100);
	      }
	    }
	    catch(Exception e) {System.err.println(e);}
	}
	

}
