package fr.univ_angers.etud.coloration.model.algo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

import fr.univ_angers.etud.coloration.model.graphe.Graphe;
import fr.univ_angers.etud.coloration.model.graphe.Noeud;

public class AlgorithmeDSATUR extends AlgorithmeColoration {
	protected boolean ran;
	
	private class NoeudDSAT
	{
		private Noeud noeud;
		private int[] couleurVoisins;
		private int nbCouleur;
		private int id;

		public NoeudDSAT(Noeud noeud, int id)
		{
			this.noeud = noeud;
			this.couleurVoisins = new int[noeud.getNbVoisins()];
			this.nbCouleur = 0;
			this.id = id;
			Arrays.fill(couleurVoisins, Integer.MAX_VALUE); //Pour forcer que les éléments vides soient a la fin même après un sort
		}

		public int getId()
		{
			return id;
		}
		
		Noeud getNoeud()
		{
			return noeud;
		}
		
		int getNbCouleur()
		{
			return nbCouleur;
		}
		
		void coloriseVoisin(int couleur)
		{
			boolean ignore = false;
			if(nbCouleur > 0)
			{
				for(int i = 0; i < nbCouleur; i++)
				{
					if(couleurVoisins[i] == couleur)
					{
						ignore = true;
					}
				}
			}
			if(!ignore)
			{
				couleurVoisins[nbCouleur++] = couleur;
			}
		}
		
		int couleurLibre()
		{
			int couleur = 0;
			Arrays.sort(couleurVoisins);
			for(int i = 0; i < nbCouleur; i++)
			{
				if(couleur == couleurVoisins[i])
				{
					couleur = couleurVoisins[i] + 1;
				}
			}
			return couleur;
			
			
		}
	}
	
	public AlgorithmeDSATUR(Graphe graphe, boolean step) {
		super(graphe, step);
		// TODO Auto-generated constructor stub
		ran = false;
	}

	@Override
	public boolean termine() {
		// TODO Auto-generated method stub
		return ran;
	}

	@Override
	public Graphe step() {
		if(ran) return graphe;
		ArrayList<NoeudDSAT> listeDegre = new ArrayList<NoeudDSAT>();
		for(int i = 1; i <= graphe.getNbNoeuds(); ++i)
		{
			listeDegre.add(new NoeudDSAT(graphe.getNoeud(i), i));
		}
		PriorityQueue<NoeudDSAT> listDegreSort = new PriorityQueue<NoeudDSAT>(listeDegre.size(), new Comparator<AlgorithmeDSATUR.NoeudDSAT>() {
			public int compare(NoeudDSAT a, NoeudDSAT b)
			{
				if(a.getNbCouleur() == b.nbCouleur)
				{
					return b.getNoeud().getNbVoisins() - a.getNoeud().getNbVoisins();
				}
				else
				{
					return b.getNbCouleur() - a.getNbCouleur();
				}
			}
		});
		for(NoeudDSAT noeud: listeDegre)
		{
			listDegreSort.add(noeud);
		}
		while(!listDegreSort.isEmpty())
		{
			NoeudDSAT n = listDegreSort.poll();
			int couleur = n.couleurLibre();
			changerCouleur(n.getId(), couleur);
			//n.getNoeud().setCouleur(couleur);
			Iterator<Integer> voisins = n.getNoeud().getVoisins();
			while(voisins.hasNext())
			{
				listeDegre.get(voisins.next()-1).coloriseVoisin(couleur);
			}
			ajouterEtape();
		}
		ran = true;
		return graphe;
	}
	
}
