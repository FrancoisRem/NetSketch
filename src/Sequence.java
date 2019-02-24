import java.awt.Color;
import java.io.Serializable;
import java.util.*;

public class Sequence implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<Point> listePoints;
	public int taille;
	public Color couleur;
	public int numeroClient=-1;

	public Sequence(int x, int y) {
		// pour les tests
		taille=0;
		couleur=Color.BLACK;
		Point p = new Point(x,y);
		listePoints = new ArrayList<Point>();
		listePoints.add(p);
	}
	
	public Sequence(int t, Color c) {
		// pour les tests
		taille=t;
		couleur=c;
		listePoints = new ArrayList<Point>();
	}
	
	public Sequence() {
		// pour les tests
		taille=0;
		couleur=Color.BLACK;
		listePoints = new ArrayList<Point>();
	}
	
	public void ajouterPoint(Point p) {
		listePoints.add(p);
	}
	
	public void ajouterPoint(int x, int y) {
		Point p = new Point(x,y);
		listePoints.add(p);		
	}
	
	public void infoClient(int n) {
		this.numeroClient=n;
	}
}
