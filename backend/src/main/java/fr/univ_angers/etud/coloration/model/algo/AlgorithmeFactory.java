package fr.univ_angers.etud.coloration.model.algo;

import fr.univ_angers.etud.coloration.model.graphe.Graphe;

public class AlgorithmeFactory {
	static public AlgorithmeColoration getAlgorithme(String nom, Graphe graphe, boolean step)
	{
		switch(nom)
		{
			//Coloration
			case "basique":
				return new ColorationBasique(graphe, step);
			case "glouton":
				return new AlgorithmeGlouton(graphe, step);
			case "dsatur":
				return new AlgorithmeDSATUR(graphe, step);
			case "random":
				return new AlgorithmeRandom(graphe, step);
			
			//Optimisation
			case "hillclimbing":
				return new AlgorithmeHillClimbing(graphe, step);
			case "compactage2":
				return new AlgorithmeCompactage(graphe, step);
			case "compactage":
				return new CompactageColoration(graphe, step);
			default:
				return null;
		}
	}
}
