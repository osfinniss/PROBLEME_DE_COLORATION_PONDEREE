package fr.univ_angers.etud.coloration.model.algo;

import java.util.ArrayList;

import fr.univ_angers.etud.coloration.model.graphe.Graphe;
import fr.univ_angers.etud.coloration.model.graphe.GrapheModification;
import fr.univ_angers.etud.coloration.model.graphe.GrapheModification.NoeudModification;

public abstract class AlgorithmeColoration {
	protected Graphe graphe;
	protected ArrayList<Graphe> etapes;
	protected ArrayList<GrapheModification> modifs;
	protected boolean pasApas;

	//Pour annuler même sans pas-a-pas
	private GrapheModification stack[];
	private int stackTop;
	private int stackBottom;
	private final int STACK_SIZE = 10;
	
	public AlgorithmeColoration(Graphe graphe, boolean pasApas)
	{
		this.graphe = graphe;
		this.pasApas = pasApas;
		this.etapes = new ArrayList<Graphe>();
		this.modifs = new ArrayList<GrapheModification>();
		ajouterEtape();
		stack = new GrapheModification[STACK_SIZE];
		stackTop = 0;
		stackBottom = 0;
	}
	
	public Graphe getGraph() {
		return graphe;
	}
	
	public ArrayList<Graphe> getEtapes()
	{
		return etapes;
	}
	
	public void ajouterEtape()
	{
		if(pasApas)
		{
			etapes.add(graphe.clone());
		}
	}

	private void ajouterModification(GrapheModification mod)
	{
		stack[stackTop++] = mod;
		stackTop = stackTop % STACK_SIZE;
		if(stackTop == stackBottom)
		{
			stackBottom = (stackBottom + 1) % STACK_SIZE;
		}
		if(pasApas)
		{
			modifs.add(mod);
		}
	}

	public void changerCouleur(int noeud, int couleur)
	{
		GrapheModification mod = new GrapheModification();
		mod.ajouter(noeud, graphe.getNoeud(noeud).getCouleur(), couleur);
		graphe.getNoeud(noeud).setCouleur(couleur);
		ajouterModification(mod);
	}

	public void echangerCouleur(int noeud1, int noeud2)
	{
		GrapheModification mod = new GrapheModification();
		int couleur = graphe.getNoeud(noeud1).getCouleur();
		mod.ajouter(noeud1, couleur, graphe.getNoeud(noeud2).getCouleur());
		mod.ajouter(noeud2, graphe.getNoeud(noeud2).getCouleur(), couleur);
		graphe.getNoeud(noeud1).setCouleur(graphe.getNoeud(noeud2).getCouleur());
		graphe.getNoeud(noeud2).setCouleur(couleur);
		ajouterModification(mod);
	}

	public void annulerModification()
	{
		if(stackTop == stackBottom) //Pile vide
		{
			//TODO : Exception ?
			return;
		}
		if(stackTop == 0) stackTop = STACK_SIZE;
		GrapheModification mod = stack[--stackTop];
		for(int i = 0; i < mod.size(); i++)
		{
			NoeudModification noeudmod = mod.get(i);
			graphe.getNoeud(noeudmod.getNoeud()).setCouleur(noeudmod.getOldCol());
		}
		if(modifs.size() > 0)
		{
			int index = modifs.size() - 1;
			if(modifs.get(index) == mod)
			{
				modifs.remove(index);
			}
		}
	}
	
	public abstract boolean termine();
	
	public abstract Graphe step();
}
