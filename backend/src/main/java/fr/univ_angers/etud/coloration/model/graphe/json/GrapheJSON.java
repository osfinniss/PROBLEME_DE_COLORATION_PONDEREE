package fr.univ_angers.etud.coloration.model.graphe.json;

import java.util.ArrayList;

public class GrapheJSON {
	private ArrayList<NodeJSON> nodes;
	private ArrayList<ArreteJSON> edges;
	private int cout;
	
	public GrapheJSON() {
		super();
		this.nodes = new ArrayList<NodeJSON>();
		this.edges = new ArrayList<ArreteJSON>();
		this.cout = 0;
	}

	public GrapheJSON(ArrayList<NodeJSON> nodes, ArrayList<ArreteJSON> edges, int cout) {
		super();
		this.nodes = nodes;
		this.edges = edges;
		this.cout = cout;
	}

	public ArrayList<NodeJSON> getNodes() {
		return nodes;
	}

	public void setNoeuds(ArrayList<NodeJSON> nodes) {
		this.nodes = nodes;
	}

	public ArrayList<ArreteJSON> getEdges() {
		return edges;
	}

	public void setArretes(ArrayList<ArreteJSON> edges) {
		this.edges = edges;
	}

	public int getCout() {
		return cout;
	}

	public void setCout(int cout) {
		this.cout = cout;
	}
}
