package fr.univ_angers.etud.coloration.model.graphe.json;

public class ArreteJSON {
	public int from;
	public int to;
	
	public ArreteJSON() {
		super();
		this.from = 1;
		this.to = 1;
	}

	public ArreteJSON(int from, int to) {
		super();
		this.from = from;
		this.to = to;
	}	
}
