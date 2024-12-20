package fr.univ_angers.etud.coloration.model.algo;

import java.util.Arrays;
import java.util.Iterator;

import fr.univ_angers.etud.coloration.model.graphe.Graphe;
import fr.univ_angers.etud.coloration.model.graphe.Noeud;

public class AlgorithmeGlouton extends AlgorithmeColoration {
	protected boolean ran;
	
	public AlgorithmeGlouton(Graphe graphe, boolean step) {
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
		
		int[] couleurs = new int[graphe.getNbNoeuds()+1];
		Arrays.fill(couleurs, -1);

		boolean[] couleursDisponibles = new boolean[graphe.getNbNoeuds()+1];

		couleurs[0] = 0;

		for (int i = 1; i <= graphe.getNbNoeuds(); i++) {
			int couleurChoisie;
			Arrays.fill(couleursDisponibles, true);

			Noeud noeud = graphe.getNoeud(i);

			Iterator<Integer> voisins = noeud.getVoisins();
			while (voisins.hasNext()) {
				int voisinId = voisins.next();
				int couleurVoisin = couleurs[voisinId];
				if (couleurVoisin != -1) {
					couleursDisponibles[couleurVoisin] = false;
				}
			}


			for (couleurChoisie = 0; couleurChoisie < graphe.getNbNoeuds(); couleurChoisie++) {
				if (couleursDisponibles[couleurChoisie]) {
					break;
				}
			}

			couleurs[i] = couleurChoisie;
			changerCouleur(i, couleurChoisie);
			//noeud.setCouleur(couleurChoisie);
			ajouterEtape();
		}
		ran = true;
		return graphe;
	}
	
}
