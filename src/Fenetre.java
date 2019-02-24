
import java.awt.Color;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import java.awt.Font;
import java.awt.Graphics;




public class Fenetre extends JFrame implements Runnable  {

	private static final long serialVersionUID = 1L;   // pour Serializable
	
	//Attributs pour gérer la connexion client/serveur
	private LinkedList<Sequence> listeSeq;
	private Donnee data;
	private String msg;
	private ArrayList<String> listeMsg;
	private String nomClient;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;

	//Attributs pour l'interface utilisateur
	private PanelDessin panelDessin= new PanelDessin();
	private JPanel contentPane;	
	private final JToolBar toolBar = new JToolBar();
	private final JButton btnCouleur = new JButton("Couleur");
	private final JButton btnFond = new JButton("Fond");
	private final JButton btnNouveau = new JButton("Nouveau");
	private final JButton btnEnvoyer;
	private final JButton btnAugmenteTaille = new JButton(" + ");
	private final JButton btnDiminueTaille = new JButton(" - ");
	private final JTextField afficheurTaille1 = new JTextField("Taille : ");
	private final JTextField afficheurTaille2 = new JTextField(""+panelDessin.getTaille());
	private final JTextArea textArea = new JTextArea();
	private final JButton btnDfaire = new JButton("D\u00E9faire");
	private final JButton btnRefaire = new JButton("Refaire");
	private final JButton btnEnregistrer = new JButton("Enregistrer");
	
	public Fenetre(ObjectInputStream inp, ObjectOutputStream outp, String pseudo) {
	    // Les flux lecture/ecriture
	    this.in = inp;
	    this.out = outp;
	    //Nom du Client
	    this.nomClient = pseudo;
	    // Lancement du thread
	    Thread miseAjour = new Thread(this);
	    miseAjour.start();
	    
	    //Initialisations Graphiques
	    setTitle("NetSketch - " + nomClient);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1031, 779);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//Panel de Dessin
		panelDessin.setBounds(9, 37, 725, 692);
		contentPane.add(panelDessin);
		
		//Panel de Chat
		JPanel panel_chat = new JPanel();
		panel_chat.setBounds(744, 11, 261, 718);
		contentPane.add(panel_chat);
		panel_chat.setLayout(null);
		
		
		//Zone de texte
		final JTextArea zoneTexte = new JTextArea();
		zoneTexte.setBounds(10, 593, 241, 125);
		zoneTexte.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					btnEnvoyer.doClick();
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					zoneTexte.setText("");
				}				
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		panel_chat.add(zoneTexte);
		

		
		//Zone où est affichée le chat
		textArea.setEditable(false);
		textArea.setBounds(1, 1, 250, 558);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		panel_chat.add(textArea);
		
		JScrollPane scrollPane = new JScrollPane(textArea,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 11, 241, 539);
		panel_chat.add(scrollPane);
		
		//Bouton pour le Chat
		
		//Envoyer
		btnEnvoyer = new JButton("Envoyer ");
		btnEnvoyer.setBounds(10, 557, 241, 29);
		btnEnvoyer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				msg = zoneTexte.getText();
				try {
					out.writeObject(new Donnee(nomClient + " : " +msg));
					System.out.println("Client envoie : " + msg);
					zoneTexte.setText("");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		panel_chat.add(btnEnvoyer);
		
		setupToolbar();
		this.setVisible(true);
		
	  }

	public void run() {
		try {
			while (true) {
				data = (Donnee) in.readObject();
				switch(data.getType()) {
				case LISTESEQUENCES:
					listeSeq = data.getListSeq();
					System.out.println("sequence recue");
					break;
				case LISTEMESSAGES:
					listeMsg = data.getListMes();
					System.out.println("message recu :" + listeMsg);
					textArea.setText("");
					for(String s : listeMsg) {
						this.afficherTexte(s);
					}
					break;
				default:
					System.err.println("Erreur Communication Client/Serveur");
					break;
				}					
				System.out.println("Attente du dernier ajout");
		        repaint();
		        Thread.sleep(100);
		      }
		    }
	    catch(Exception e) { e.printStackTrace();}
	  }
	
	
	//Méthodes
	
	//test
	public void afficherTexte(String s) {
		textArea.append(s + "\n");
	}
	
	public void miseAjourTaille(int n) {
		afficheurTaille2.setText(""+n);
	}

	//Pour la barre d'outil
	public void setupToolbar() {
		toolBar.setBounds(10, 0, 724, 37);
		contentPane.add(toolBar);
		
		//CouleurPointeur
		btnCouleur.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		btnCouleur.setForeground(panelDessin.couleurPointeur);
		btnCouleur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JColorChooser colorChooser = new JColorChooser();
				class ColorListener implements ActionListener{
					public void actionPerformed(ActionEvent e) {
						Color c = colorChooser.getColor();
						panelDessin.couleurPointeur = c;
						btnCouleur.setForeground(c);
					}
				}
				JDialog cDialog = JColorChooser.createDialog(null, "Choisir la couleur du Pointeur", false, colorChooser, new ColorListener(), null);
				cDialog.setVisible(true);
			}			
		});
		toolBar.add(btnCouleur);		
		
		
		//CouleurFond
		btnFond.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		btnFond.setBackground(panelDessin.couleurFond);
		btnFond.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final JColorChooser backgroundChooser = new JColorChooser();
				class BackgroundListener implements ActionListener{
					public void actionPerformed(ActionEvent e) {
						Color c = backgroundChooser.getColor();
						panelDessin.couleurFond = c;
						btnFond.setBackground(c);
					}
				}
				JDialog cDialog = JColorChooser.createDialog(null, "Choisir la couleur de Fond", false, backgroundChooser, new BackgroundListener(), null);
				cDialog.setVisible(true);
			}			
		});
		toolBar.add(btnFond);
		
		//Nouveau
		btnNouveau.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		btnNouveau.setBackground((Color) null);
		btnNouveau.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
			          out.writeObject(new Donnee(2));
			          panelDessin.viderSeqAcc();
				} catch(IOException e1) {
					// TODO Auto-generated catch block
			        e1.printStackTrace();
				}
			}
		});
		toolBar.add(btnNouveau);
		
		//Defaire
		btnDfaire.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		btnDfaire.setBackground((Color) null);
		btnDfaire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					out.writeObject(new Donnee(0));
					System.out.println("Client envoie ctrl-z");
					panelDessin.viderSeqAcc();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}		
			}
		});
		toolBar.add(btnDfaire);
		
		//Refaire
		btnRefaire.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		btnRefaire.setBackground((Color) null);
		btnRefaire.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					out.writeObject(new Donnee(1));
					System.out.println("Client envoie ctrl-y");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		toolBar.add(btnRefaire);
		
		//AfficheTaille1 qui affiche simplement "Taille :"
		afficheurTaille1.setEditable(false);
		afficheurTaille1.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		afficheurTaille1.setBackground((Color) null);
		toolBar.add(afficheurTaille1);
	    final Dimension dimAfficheur1 = new Dimension(toolBar.getWidth()/10, toolBar.getHeight());
	    afficheurTaille1.setPreferredSize(dimAfficheur1);
	    afficheurTaille1.setMinimumSize(dimAfficheur1);
	    afficheurTaille1.setMaximumSize(dimAfficheur1);

		//AfficheTaille2 qui affiche la taille du pinceau, et permet de la modifier
		afficheurTaille2.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		afficheurTaille2.setBackground((Color) null);
		toolBar.add(afficheurTaille2);
	    final Dimension dimAfficheur2 = new Dimension(toolBar.getWidth()/18, toolBar.getHeight());
	    afficheurTaille2.setPreferredSize(dimAfficheur2);
	    afficheurTaille2.setMinimumSize(dimAfficheur2);
	    afficheurTaille2.setMaximumSize(dimAfficheur2);
	    afficheurTaille2.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					try{
						int entreeClient = Integer.parseInt(afficheurTaille2.getText());
						panelDessin.setTaille(entreeClient);
					}catch(NumberFormatException nfe) {}					
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					afficheurTaille2.setText("");
					miseAjourTaille(panelDessin.getTaille());
				}				
			}
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		//TaillePointeurPlus
		btnAugmenteTaille.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		btnAugmenteTaille.setBackground((Color) null);
		btnAugmenteTaille.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			panelDessin.augmenterTaille();
			miseAjourTaille(panelDessin.getTaille());
			}
		});
		toolBar.add(btnAugmenteTaille);
		
		//TaillePointeurMoins
		btnDiminueTaille.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		btnDiminueTaille.setBackground((Color) null);
		btnDiminueTaille.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			panelDessin.diminuerTaille();
			miseAjourTaille(panelDessin.getTaille());
			}
		});
		toolBar.add(btnDiminueTaille);
		
		//Enregistrer
		btnEnregistrer.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		btnEnregistrer.setBackground((Color) null);
		btnEnregistrer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SaisieNomSauvegarde frameSaisie = new SaisieNomSauvegarde(panelDessin);				
			}
		});
		toolBar.add(btnEnregistrer);
		
		
	}
	
	//Méthode pour Enregistrer


	
	public class PanelDessin extends JPanel { 
		
		
		//Attributs
		private static final long serialVersionUID = 1L;
		private int taillePointeur = 10;
		public Color couleurPointeur = Color.black;
		public Color couleurFond = Color.white;
		public int x = 2;
		public int y = 2;
		public Sequence seqAcc = new Sequence();
		private static final int taillePointeurMAX = 100;
		
		
		//Constructeur
		public PanelDessin() {
			this.setPreferredSize(new Dimension(900,800));
			this.addMouseListener(new MouseAdapter() 
			{
				@Override
				public void mousePressed(MouseEvent event) 
				{	
					seqAcc = new Sequence(taillePointeur,couleurPointeur);
					x=event.getX();
					y=event.getY();
					seqAcc.ajouterPoint(x,y);
					repaint();
				}
				
				@Override
				public void mouseReleased(MouseEvent event)
				{
					//listeSequences.add(seqAcc);
					try {
						out.writeObject(new Donnee(seqAcc));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					repaint();
				}
				
			});
			
			this.addMouseMotionListener(new MouseMotionListener() {
				
				public void mouseDragged(MouseEvent event) {
					   x=event.getX();
					   y=event.getY();
					   seqAcc.ajouterPoint(x,y);
					   repaint();
					   
				   }

				public void mouseMoved(MouseEvent arg0) {
					// TODO Auto-generated method stub
				}
			});
			repaint();
		  }
		
		//Méthodes
		public void viderSeqAcc() {
		  seqAcc = new Sequence(taillePointeur,couleurPointeur);
		}
		
		public void augmenterTaille() {
			if(taillePointeur<taillePointeurMAX) {
				taillePointeur++;
			}
		}
		
		public void diminuerTaille() {
			if(taillePointeur>2) {
				taillePointeur--;
			}
		}
		
		public int getTaille() {
			return taillePointeur;
		}
		
		public boolean setTaille(int n) {
			if((n>1) && (n<=taillePointeurMAX)) {
				taillePointeur = n;
				return true;
			}else {
				return false;
			}
		}
		
		
		@Override
		public void paintComponent(Graphics g){
			g.setColor(couleurFond);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			for(Sequence s:listeSeq) {
				g.setColor(s.couleur);
				int r = s.taille;
				for(Point p:s.listePoints) {
					g.fillOval(p.x-r/2,p.y-r/2,r,r);
				}
			}
			for(Point p:seqAcc.listePoints) { //Pour ameliorer la fluidite pour le client en cours de dessin 
				g.setColor(seqAcc.couleur);
				int r = seqAcc.taille; //rayon disque
				g.fillOval(p.x-r/2,p.y-r/2,r,r);
			}
		}	  
	}
}
 
