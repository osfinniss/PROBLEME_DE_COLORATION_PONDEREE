package fr.univ_angers.etud.coloration.model.graphe.json;

public class NodeJSON {
	private int id;
	private int poids;
	private int couleur;
	
	public NodeJSON() {
		super();
		this.poids = 1;
		this.couleur = 0;
	}
	
	public NodeJSON(int id, int poids, int couleur) {
		super();
		this.poids = poids;
		this.couleur = couleur;
		this.id = id;
	}
	public int getPoids() {
		return poids;
	}
	public void setPoids(int poids) {
		this.poids = poids;
	}
	public int getCouleur() {
		return couleur;
	}
	public void setCouleur(int couleur) {
		this.couleur = couleur;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}
