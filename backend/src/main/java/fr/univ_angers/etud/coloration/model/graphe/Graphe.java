package fr.univ_angers.etud.coloration.model.graphe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.lang.CloneNotSupportedException;

public class Graphe implements Cloneable {
	private ArrayList<Noeud> noeuds;
	
	public Graphe()
	{
		noeuds = new ArrayList<Noeud>();
	}
	
	public int ajouterNoeud()
	{
		return ajouterNoeud(1);
	}
	
	public int ajouterNoeud(int poids)
	{
		int id = noeuds.size() + 1;
		noeuds.add(new Noeud(id, poids,-1));
		return id;
	}
	
	public Noeud getNoeud(int id)
	{
		id -= 1; //Les fichiers sont indexé en 1 et on fera pareil avec l'API donc on décale ici
		if(id < 0 || id >= noeuds.size())
		{
			return null;
		}
		return noeuds.get(id);
	}
	
	public void ajouterArrete(int id1, int id2)
	{
		Noeud n1 = getNoeud(id1);
		Noeud n2 = getNoeud(id2);
		if(n1 == null || n2 == null)
		{
			// TODO: Ajouter exceptions
			return;
		}
		n1.ajouterVoisin(id2);
		n2.ajouterVoisin(id1);
	}
	
	public void supprimerArrete(int id1, int id2)
	{
		Noeud n1 = getNoeud(id1);
		Noeud n2 = getNoeud(id2);
		if(n1 == null || n2 == null)
		{
			// TODO: Ajouter exceptions
			return;
		}
		n1.supprimerVoisin(id2);
		n2.supprimerVoisin(id1);
	}
	
	public int getNbNoeuds()
	{
		return noeuds.size();
	}
	
	public boolean aArrete(int id1, int id2)
	{
		Noeud n1 = getNoeud(id1);
		if(n1 == null)
		{
			// TODO: Ajouter exceptions
			return false;
		}
		Iterator<Integer> iter = n1.getVoisins();
		while(iter.hasNext())
		{
			if(iter.next().equals(id2))
			{
				return true;
			}
		}
		return false;
	}
	
	public int coutPondere()
	{
		int[] poidsCouleurs = new int[getNbNoeuds()];
		int cout = 0;
		Arrays.fill(poidsCouleurs, 0);
		int error = 0;
		for(Noeud n: noeuds)
		{
			int col = n.getCouleur();
			if(col < 0 || col >= getNbNoeuds())
			{
				//TODO : Mieux gérer ce cas
				error++;
				continue;
			}
			if(poidsCouleurs[col] < n.getPoids())
			{
				poidsCouleurs[col] = n.getPoids();
			}
		}
		for(int p: poidsCouleurs)
		{
			cout += p;
		}
		return ((error > 0) ? 1000000000 + error : 0) + cout;
		
	}
	
	public Graphe clone()
	{
		try {
			Graphe copie = (Graphe)super.clone();
			copie.noeuds = new ArrayList<Noeud>();
			for(Noeud n: this.noeuds)
			{
				copie.noeuds.add(n.clone());
			}
			return copie;
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			throw new AssertionError();
		}
	}
	
	/*
	 * Change les couleurs pour enlever les potentiels "trous"(tel couleur 1 inutilisé mais 2 si).
	 * Retourne le nombre de couleurs
	 */
	public int reduireCouleurs()
	{
		int nbNoeuds = getNbNoeuds();
		int nbCouleurs = 0;
		for(Noeud n: noeuds)
		{
			if(n.getCouleur() > nbCouleurs)
			{
				nbCouleurs = n.getCouleur();
			}
		}
		nbCouleurs++;
		boolean[] couleurs = new boolean[nbCouleurs];
		Arrays.fill(couleurs, false);
		for(Noeud n: noeuds)
		{
			couleurs[n.getCouleur()] = true;
		}
		int nouvCouleur = 0;
		for(int i = 0; i < nbCouleurs; i++)
		{
			if(!couleurs[i])
			{
				continue;
			}
			for(Noeud n: noeuds)
			{
				if(n.getCouleur() == i)
				{
					n.setCouleur(nouvCouleur);
				}
			}
			nouvCouleur++;
		}
		return nouvCouleur;
	}
}
