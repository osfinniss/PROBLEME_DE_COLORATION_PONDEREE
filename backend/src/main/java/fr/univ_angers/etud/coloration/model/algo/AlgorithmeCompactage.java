package fr.univ_angers.etud.coloration.model.algo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Random;

import fr.univ_angers.etud.coloration.model.graphe.Graphe;
import fr.univ_angers.etud.coloration.model.graphe.Noeud;

public class AlgorithmeCompactage extends AlgorithmeColoration {
    protected boolean ran;
    private Random rand;

    private class Pair
    {
        int noeud;
        int couleur;
        public Pair(int noeud, int couleur)
        {
            this.noeud = noeud;
            this.couleur = couleur;
        }

        public int getNoeud()
        {
            return noeud;
        }

        public int getCouleur()
        {
            return couleur;
        }
    }
	
	public AlgorithmeCompactage(Graphe graphe, boolean step) {
		super(graphe, step);
		// TODO Auto-generated constructor stub
		ran = false;
        rand = new Random();
	}

	@Override
	public boolean termine() {
		// TODO Auto-generated method stub
		return ran;
	}

	@Override
	public Graphe step() {
		ArrayList<ArrayList<Integer>> couleurs = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> ordreCouleur = new ArrayList<Integer>();
        ArrayList<Integer> poidsCouleur = new ArrayList<Integer>();
        Comparator<Integer> listeColSort = new Comparator<Integer>() { //Pour trier par ordre décroissant de poids une liste de couleurs
            public int compare(Integer a, Integer b)
            {
                return poidsCouleur.get(b) - poidsCouleur.get(a);
            }
        };
        Comparator<Integer> colSort = new Comparator<Integer>() { //Pour trier par ordre décroissant de poids une liste de couleurs
            public int compare(Integer a, Integer b)
            {
                return graphe.getNoeud(b).getPoids() - graphe.getNoeud(a).getPoids();
            }
        };
        //On crée une liste de noeud pour chaque couleur(et on calcule le poids de la couleur en même temps)
        for(int i = 1; i <= graphe.getNbNoeuds(); i++)
        {
            Noeud n = graphe.getNoeud(i);
            int col = n.getCouleur();
            while(couleurs.size() <= col)
            {
                couleurs.add(null);
                ordreCouleur.add(Integer.valueOf(-1));
                poidsCouleur.add(Integer.valueOf(-1));
            }
            if(couleurs.get(col) == null)
            {
                couleurs.set(col, new ArrayList<Integer>());
                ordreCouleur.set(col, col);
            }
            couleurs.get(col).add(i);
            if(n.getPoids() > poidsCouleur.get(col))
            {
                poidsCouleur.set(col, n.getPoids());
            }
        }
        ordreCouleur.sort(listeColSort);
        /*for(Integer i: ordreCouleur)
        {
            System.out.println("Couleur " + i + " " + poidsCouleur.get(i));
            for(Integer ni: couleurs.get(i))
            {
                Noeud n = graphe.getNoeud(ni);
                System.out.println("- " + ni + " " + n.getCouleur() + " " + n.getPoids());
            } 
        }*/
        ArrayList<Pair> valide = new ArrayList<Pair>(); //Liste de noeuds qu'on peut déplacer
        boolean changement = true;
        //for(int iter = 0; iter < 30; iter++)
        while(changement)
        {
            changement = false;
            valide.clear();
            //Lister les noeuds qu'on peut décaler
            for(int c = 0; c < ordreCouleur.size(); c++) //On regarde la liste décroissante des couleurs
            {
                if(ordreCouleur.get(c) == -1) break;
                ArrayList<Integer> liste = couleurs.get(ordreCouleur.get(c));
                //System.out.println("Liste "+ordreCouleur.get(c));
                for(int n = 0; n < liste.size(); n++) //On regarde les noeuds de cette couleurs
                {
                    int nid = liste.get(n);
                    Noeud noeud = graphe.getNoeud(nid);
                    for(int c2 = 0; c2 < ordreCouleur.size(); c2++) //On regarde la liste décroissante des couleurs pour regarder si on peut le deplacer
                    {
                        Integer couleur = ordreCouleur.get(c2);
                        //System.out.println("Test " + nid + " c" + noeud.getCouleur() + "->" + couleur);
                        if(noeud.getCouleur() == couleur) //On s'arrête quand on retombe sur la couleur du noeud
                        {
                            break;
                        }
                        if(!estVoisinAListe(noeud, couleurs.get(couleur))) //Si on a pas de voisin dans la liste on le considère valide pour le déplacer
                        {
                            //System.out.println("Valide");
                            valide.add(new Pair(nid, couleur));
                            break;
                        }
                    }
                }
            }
            //On bouge un noeud
            if(valide.size() > 0)
            {
                Pair elt = valide.get(rand.nextInt(valide.size())); //On prend un élément
                int oldCouleur = graphe.getNoeud(elt.getNoeud()).getCouleur();
                int poids = graphe.getNoeud(elt.getNoeud()).getPoids();
                //System.out.println("Deplace " + elt.getNoeud() + " de " + oldCouleur + " vers " + elt.getCouleur());

                //On change la couleur
                changerCouleur(elt.getNoeud(), elt.getCouleur());
                //On le déplace dans les listes de couleurs
                couleurs.get(oldCouleur).remove(Integer.valueOf(elt.getNoeud())); //Le valueOf est là car ça ne supprime le nième noeud et non le noeud n sinon
                couleurs.get(elt.getCouleur()).add(elt.getNoeud());
                couleurs.get(oldCouleur).sort(colSort);
                couleurs.get(elt.getCouleur()).sort(colSort);

                //On recalcule les poids et on retrie
                boolean reordonner = false;
                if(poids == poidsCouleur.get(oldCouleur)) //On recalcule l'ancienne couleur si on a enlevé le plus grand poids
                {
                    reordonner = true;
                    poidsCouleur.set(oldCouleur, calculerPoidsListe(couleurs.get(oldCouleur)));
                }
                if(poids > poidsCouleur.get(elt.getCouleur())) //On recalcule la nouvelle couleur si on a mis un poids plus elevé
                {
                    reordonner = true;
                    poidsCouleur.set(elt.getCouleur(), poids);
                }
                if(reordonner) //On trie si on a modifier les poids
                {
                    ordreCouleur.sort(listeColSort);
                }
                changement = true;
            }
            ajouterEtape();
            /*System.out.println("Optimise a " + graphe.coutPondere());
            for(Integer i: ordreCouleur)
            {
                System.out.println("Couleur " + i + " " + poidsCouleur.get(i));
                for(Integer ni: couleurs.get(i))
                {
                    Noeud n = graphe.getNoeud(ni);
                    System.out.println("- " + ni + " " + n.getCouleur() + " " + n.getPoids());
                } 
            }*/
        }
        return graphe;
	}

    protected boolean estVoisinAListe(Noeud noeud, ArrayList<Integer> liste)
    {
        Iterator<Integer> iter = noeud.getVoisins();
        while(iter.hasNext())
        {
            int voisin = iter.next();
            if(liste.contains(voisin))
            {
                return true;
            }
        }
        return false;
    }

    protected int calculerPoidsListe(ArrayList<Integer> liste)
    {
        if(liste.isEmpty())
        {
            return -1;
        }
        int max = 0;
        for(Integer n: liste)
        {
            Noeud noeud = graphe.getNoeud(n);
            if(noeud.getPoids() > max)
            {
                max = noeud.getPoids();
            }
        }
        return max;
    }
}
