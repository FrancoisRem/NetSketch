import java.io.*;
import java.util.*;
import java.net.*;

public class Serveur {
	//Attributs
	private int numeroClient;
	private List<ObjectOutputStream> listeClients;
	private LinkedList<Sequence> listeSequences;
	private LinkedList<Sequence> pileSequences;
	private ArrayList<String> listeMessages;
	private boolean nouveauDessin;
	
	//Pour les listes
	private Sequence seqAcc;
	ListIterator<Sequence> iteratorListe;
	ListIterator<Sequence> iteratorPile;
	
	//Constructeur
	public Serveur() {
		numeroClient = 0;
		listeClients = new ArrayList<ObjectOutputStream>();
		listeSequences = new LinkedList<Sequence>();
		listeMessages = new ArrayList<String>();
		pileSequences = new LinkedList<Sequence>();
		nouveauDessin=true;
	}
	
	//Methodes
	public void ajouterClient(ObjectOutputStream flux) {
	    listeClients.add(flux);
	    numeroClient++;
	    envoyerEtatDessin(flux);
	}
	//Dessin
	public void envoyerEtatDessin(ObjectOutputStream out) {
	    try {
	      out.writeObject(new Donnee(listeSequences));
	      out.reset();   
	      out.flush();
	   }
	    catch(IOException e) {
	      e.printStackTrace();
	   }
	}

	public void dessiner(int client, Sequence s) {
		s.infoClient(client);
		listeSequences.add(s);
		nouveauDessin=false;
	}
	
	public void informerClientsDessin() {
		for (int i=0 ; i<listeClients.size() ; i++) {
		    envoyerEtatDessin((ObjectOutputStream)listeClients.get(i));
		    System.out.println("information du client "+ i);  // utile pour mise au point du programme !
		}    
	}
	
	public int numeroDernierClient() {
	    return numeroClient;
	}
	
	//Chat
	public void envoyerEtatChat(ObjectOutputStream out) {
	    try {
	      out.writeObject(new Donnee(listeMessages));
	      out.reset();   // IMPORTANT (voir documentation de la classe ObjectOutputStream) 
	      out.flush();
	      System.out.println("Serveur envoie listMsg");
	   }
	    catch(IOException e) {
	      e.printStackTrace();
	   }
	}
	
	public void actualiserMessage(int joueur, String m) {
		listeMessages.add(m);
	}
	
	public void informerClientsChat() {
		for (int i=0 ; i<listeClients.size() ; i++) {
		    envoyerEtatChat((ObjectOutputStream)listeClients.get(i));
		    System.out.println("information chat du client "+ i);  // utile pour mise au point du programme !
		}    
	}
	
	//Options
	public void defaire(int client) {
		//Sequence s = listeSequences.removeLast();
		iteratorListe=listeSequences.listIterator(listeSequences.size());
		while(iteratorListe.hasPrevious()){
			   seqAcc = iteratorListe.previous();
			   if(seqAcc.numeroClient==client) {
				   pileSequences.add(seqAcc);
				   iteratorListe.remove();
				   return;
			   }
			}
		System.out.println("Liste des séquences vide !");
	}
	
	public void refaire(int client) {
		if(nouveauDessin) {
			int nbSequences = pileSequences.size();
			for(int i=0;i<nbSequences;i++) {
				seqAcc = pileSequences.pop();
				listeSequences.add(seqAcc);			
			}
			nouveauDessin=false;
		}else {
			iteratorPile=pileSequences.listIterator(pileSequences.size());
			while(iteratorPile.hasPrevious()){
				   seqAcc = iteratorPile.previous();
				   if(seqAcc.numeroClient==client) {
					   listeSequences.add(seqAcc);
					   iteratorPile.remove();
					   return;
				   }
			}
		}
		
	}
	
	public void nouveau() {
		pileSequences.clear(); //Permet de bien faire fonctionner le "refaire" afin d'annuler un appuie sur "nouveau" par inadvertance
		int nbSequences = listeSequences.size();
		for(int i=0;i<nbSequences;i++) {
			seqAcc = listeSequences.pop();
			pileSequences.add(seqAcc);			
		}
		nouveauDessin=true;
	}

	//Main
	static public void main(String [] args){
	    try {
	      // Initialisations et lancement du serveur
	      Serveur serveur = new Serveur();
	      ServerSocket serveurSo= new ServerSocket(8888);
	      while (true) {
	        // Attente du client
	        System.out.println("Serveur en attente de client...");
	        Socket s = serveurSo.accept();
	        // Ouverture du flux pour l'envoi des informations au client à travers le réseau
	        ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
	        // Enregistrement du client
	        serveur.ajouterClient(out);
	        int numClient = serveur.numeroDernierClient();
	        System.out.println("Connection du client numero " + numClient + "effectuée");
	        // CrÃ©ation et lancement du thread gÃ©rant le nouveau client
	        Thread t = new ThreadClient(serveur, s);
	        t.start();
	      }
	    }
    catch(Exception e) { e.printStackTrace();}
	}
}
