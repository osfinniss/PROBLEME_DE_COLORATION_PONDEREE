package fr.univ_angers.etud.coloration.model.algo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import fr.univ_angers.etud.coloration.model.graphe.Graphe;
import fr.univ_angers.etud.coloration.model.graphe.Noeud;

public class AlgorithmeHillClimbing extends AlgorithmeColoration {
	
	private Random rand;
	private float chanceChange;
	
    public AlgorithmeHillClimbing(Graphe graphe, boolean step) {
        super(graphe, step);
        rand = new Random();
        chanceChange = 0.5f;
    }

    @Override
    public boolean termine() {
        return false;
    }

    @Override
    public Graphe step() {
        boolean amelioration = true;
        boolean change = false;
        int currentCost;
        int premiereCouleur = 0;
        int nbNoeud = graphe.getNbNoeuds();
        int nouvelleCouleur;
        while(amelioration)
        {
        	amelioration = false;
        	currentCost = graphe.coutPondere();

            for (int i = 1; i <= nbNoeud; i++) {
                Noeud noeud = graphe.getNoeud(i);
                int couleurActuelle = noeud.getCouleur();
                ArrayList<Integer> couleursUtilisees = couleursVoisins(noeud);

                premiereCouleur = rand.nextInt(nbNoeud);
                for (int couleurOff = 0; couleurOff < nbNoeud; couleurOff++) {
                	nouvelleCouleur = (premiereCouleur + couleurOff) % nbNoeud;
                	//System.out.println(noeud.getId() + " cost " + currentCost + " col " + couleurActuelle + " ncol " + nouvelleCouleur);
                    if (!couleursUtilisees.contains(nouvelleCouleur)) {
                    	change = false;
                        changerCouleur(i, nouvelleCouleur);
                        //noeud.setCouleur(nouvelleCouleur);
                        int newCost = graphe.coutPondere();
                        if(newCost == currentCost)
                        {
                        	change = (rand.nextFloat() < chanceChange);
                        }
                        else if (newCost < currentCost)
                        {
                        	change = true;
                        	amelioration = true;
                        }
                        if (change) {
                            currentCost = newCost;
                            break;
                        } else {
                            // Rétablir la couleur d'origine si pas d'amélioration
                            //noeud.setCouleur(couleurActuelle);
                            annulerModification();
                        }
                    }
                }
                if(change)
                {
                	ajouterEtape();
                }
            }
        }
        return graphe;
    }

    private ArrayList<Integer> couleursVoisins(Noeud noeud) {
        // Récupère les couleurs des voisins d'un nœud
        ArrayList<Integer> couleurs = new ArrayList<>();
        Iterator<Integer> voisins = noeud.getVoisins();
        while (voisins.hasNext()) {
            Noeud voisin = graphe.getNoeud(voisins.next());
            couleurs.add(voisin.getCouleur());
        }
        return couleurs;
    }
}
