package fr.univ_angers.etud.coloration.model.graphe;

import java.util.ArrayList;
import java.util.Iterator;
import java.lang.CloneNotSupportedException;


public class Noeud implements Cloneable {
	private int poids;
	private ArrayList<Integer> arretes;
	private int couleur;
	private int id;
	
	public Noeud(int id, int poids, int couleur)
	{
		this.setPoids(poids);
		this.couleur = couleur;
		this.arretes = new ArrayList<Integer>();
		this.id = id;
	}
	
	public void ajouterVoisin(int id)
	{
		if(!arretes.contains(id))
		{
			arretes.add(id);
		}
	}
	
	public void supprimerVoisin(int id)
	{
		arretes.remove(Integer.valueOf(id));
	}
	
	public void setCouleur(int couleur)
	{
		this.couleur = couleur;
	}
	
	public int getCouleur()
	{
		return this.couleur;
	}
	
	public Iterator<Integer> getVoisins()
	{
		return arretes.iterator();
	}

	public int getPoids() {
		return poids;
	}

	public void setPoids(int poids) {
		this.poids = poids;
	}
	
	public int getNbVoisins()
	{
		return arretes.size();
	}

	public int getId() {
		return id;
	}
	
	public Noeud clone()
	{
		try {
			Noeud copie = (Noeud) super.clone();
			copie.arretes = new ArrayList<Integer>(this.arretes);
			return copie;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			throw new AssertionError();
		}		
	}
}
