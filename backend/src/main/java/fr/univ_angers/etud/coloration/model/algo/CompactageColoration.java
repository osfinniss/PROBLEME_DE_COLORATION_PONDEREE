package fr.univ_angers.etud.coloration.model.algo;

import fr.univ_angers.etud.coloration.model.graphe.Graphe;
import fr.univ_angers.etud.coloration.model.graphe.Noeud;

import java.util.*;

public class CompactageColoration extends AlgorithmeColoration {

    public CompactageColoration(Graphe graphe, boolean step) {
        super(graphe, step);
    }

    @Override
    public boolean termine() {
        return false;
    }

    @Override
    public Graphe step() {
        boolean moved = false;

        Map<Integer, Integer> poidsMaxParCouleur = calculerPoidsMaxParCouleur();

        ArrayList<Integer> couleursTriees = new ArrayList<>(poidsMaxParCouleur.keySet());
        couleursTriees.sort(Comparator.comparingInt(poidsMaxParCouleur::get).reversed());

        for (int i = 1; i <= graphe.getNbNoeuds(); i++) {
            Noeud noeud = graphe.getNoeud(i);
            int couleurActuelle = noeud.getCouleur();

            for (int nouvelleCouleur : couleursTriees) {
                if (nouvelleCouleur >= couleurActuelle) break;

                if (peutDeplacer(noeud, nouvelleCouleur)) {
                    changerCouleur(noeud.getId(), nouvelleCouleur);
                    moved = true;
                    ajouterEtape();
                    break;
                }
            }
        }

        if (!moved) {
            return graphe;
        }

        return graphe;
    }

    private boolean peutDeplacer(Noeud noeud, int nouvelleCouleur) {
        Iterator<Integer> voisins = noeud.getVoisins();
        while (voisins.hasNext()) {
            int voisinId = voisins.next();
            Noeud voisin = graphe.getNoeud(voisinId);
            if (voisin.getCouleur() == nouvelleCouleur) {
                return false;
            }
        }
        return true;
    }


    private Map<Integer, Integer> calculerPoidsMaxParCouleur() {
        Map<Integer, Integer> poidsMaxParCouleur = new HashMap<>();

        for (int i = 1; i <= graphe.getNbNoeuds(); i++) {
            Noeud noeud = graphe.getNoeud(i);
            int couleur = noeud.getCouleur();
            int poids = noeud.getPoids();

            poidsMaxParCouleur.put(couleur, Math.max(poidsMaxParCouleur.getOrDefault(couleur, 0), poids));
        }

        return poidsMaxParCouleur;
    }
}