import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class Donnee implements Serializable {
	/**
	 * 
	 */
	public enum typeD{
		SEQUENCE,LISTESEQUENCES,MESSAGE,LISTEMESSAGES,ACTION
	}
	
	private static final long serialVersionUID = 5639929285580093586L;
	
	private typeD type;
	private Sequence seq;
	private LinkedList<Sequence> listSeq;
	private String mes;
	private ArrayList<String> listMes;
	private int action;
	
	public Donnee(Sequence s) {
		type = typeD.SEQUENCE;
		seq = s;
	}
	
	public Donnee(LinkedList<Sequence> list) {
		type = typeD.LISTESEQUENCES;
		listSeq = list;
	}
	
	public Donnee(String s) {
		type = typeD.MESSAGE;
		mes = s;
	}
	
	public Donnee(ArrayList<String> list) {
		type = typeD.LISTEMESSAGES;
		listMes = list;
	}
	
	public Donnee(int a) {
	    type = typeD.ACTION;
	    action = a;
	}
	
	//Getters
	public typeD getType() {
		return type;
	}

	public Sequence getSeq() {
		return seq;
	}


	public LinkedList<Sequence> getListSeq() {
		return listSeq;
	}


	public String getMes() {
		return mes;
	}


	public ArrayList<String> getListMes() {
		return listMes;
	}


	public int getAction() {
		return action;
	}
}


