package fr.univ_angers.etud.coloration.model.graphe;

import java.util.ArrayList;

public class GrapheModification {
    public class NoeudModification {
        int noeud;
        int oldCol;
        int newCol;

        public NoeudModification(int noeud, int oldCol, int newCol) {
            this.noeud = noeud;
            this.oldCol = oldCol;
            this.newCol = newCol;
        }

        public int getNoeud() {
            return noeud;
        }
        
        public int getOldCol() {
            return oldCol;
        }
        
        public int getNewCol() {
            return newCol;
        }
    }

    ArrayList<NoeudModification> liste;

    public GrapheModification()
    {
        liste = new ArrayList<NoeudModification>();
    }

    public void ajouter(int noeud, int oldCol, int newCol)
    {
        liste.add(new NoeudModification(noeud, oldCol, newCol));
    }

    public int size()
    {
        return liste.size();
    }

    public NoeudModification get(int id)
    {
        if(id < liste.size())
        {
            return liste.get(id);
        }
        return null;
    }

    public ArrayList<NoeudModification> getListe() {
        return liste;
    }
}
