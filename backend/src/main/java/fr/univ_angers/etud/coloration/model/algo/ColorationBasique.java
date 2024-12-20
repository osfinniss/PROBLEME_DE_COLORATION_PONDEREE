package fr.univ_angers.etud.coloration.model.algo;

import fr.univ_angers.etud.coloration.model.graphe.Graphe;

public class ColorationBasique extends AlgorithmeColoration {
	protected boolean ran;
	
	public ColorationBasique(Graphe graphe, boolean step) {
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
		for(int i = 1; i <= graphe.getNbNoeuds(); i++)
		{
			changerCouleur(i, i-1);
			ajouterEtape();
		}
		ran = true;
		return graphe;
	}
	
}
