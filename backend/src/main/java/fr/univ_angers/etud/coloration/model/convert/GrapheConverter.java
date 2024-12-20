package fr.univ_angers.etud.coloration.model.convert;

import java.util.ArrayList;
import java.util.Iterator;

import fr.univ_angers.etud.coloration.model.graphe.Graphe;
import fr.univ_angers.etud.coloration.model.graphe.Noeud;
import fr.univ_angers.etud.coloration.model.graphe.json.ArreteJSON;
import fr.univ_angers.etud.coloration.model.graphe.json.GrapheJSON;
import fr.univ_angers.etud.coloration.model.graphe.json.NodeJSON;

public class GrapheConverter {
	
	static public GrapheJSON GrapheToAPI(Graphe graphe)
	{
		GrapheJSON api = new GrapheJSON();
		ArrayList<NodeJSON> nodes = api.getNodes();
		ArrayList<ArreteJSON> arrete = api.getEdges();
		for(int i = 1; i <= graphe.getNbNoeuds(); i++)
		{
			Noeud noeud = graphe.getNoeud(i);
			nodes.add(new NodeJSON(noeud.getId(), noeud.getPoids(), noeud.getCouleur()));
			Iterator<Integer> voisins = noeud.getVoisins();
			while(voisins.hasNext())
			{
				int j = voisins.next();
				if(i < j) //On veux pas les doublons
				{
					arrete.add(new ArreteJSON(i, j));
				}
			}
		}
		return api;
	}
}
