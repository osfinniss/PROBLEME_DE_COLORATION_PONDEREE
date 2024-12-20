package fr.univ_angers.etud.coloration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Iterator;

import fr.univ_angers.etud.coloration.model.graphe.Graphe;
import fr.univ_angers.etud.coloration.model.graphe.GrapheFactory;
import fr.univ_angers.etud.coloration.model.graphe.Noeud;

@SpringBootTest
public class TestGraphe {
	@Test
	public void test_noeud_creation()
	{
		Noeud noeud = new Noeud(1, 1, 0);
		assertEquals(noeud.getCouleur(), 0);
		assertEquals(noeud.getPoids(), 1);
	}
	
	@Test
	public void test_noeud_voisins()
	{
		Noeud noeud = new Noeud(1, 1, 0);
		noeud.ajouterVoisin(2);
		assertEquals(noeud.getNbVoisins(), 1);
		Iterator<Integer> voisins = noeud.getVoisins();
		assertEquals(voisins.next(), 2);
		assertFalse(voisins.hasNext());
	}
	
	@Test
	public void test_noeud_voisins_doublons()
	{
		Noeud noeud = new Noeud(1, 1, 0);
		noeud.ajouterVoisin(2);
		noeud.ajouterVoisin(2);
		assertEquals(noeud.getNbVoisins(), 1);
	}
	
	@Test
	public void test_noeud_voisins_del()
	{
		Noeud noeud = new Noeud(1, 1, 0);
		noeud.ajouterVoisin(2);
		noeud.ajouterVoisin(4);
		noeud.supprimerVoisin(2);
		assertEquals(noeud.getNbVoisins(), 1);
		Iterator<Integer> voisins = noeud.getVoisins();
		assertEquals(voisins.next(), 4);
		assertFalse(voisins.hasNext());
	}
	
	@Test
	public void test_graphe_add()
	{
		Graphe g = new Graphe();
		int n = g.ajouterNoeud(2);
		assertEquals(g.getNbNoeuds(), 1);
		assertEquals(n, 1);
	}
	
	@Test
	public void test_graphe_get()
	{
		Graphe g = new Graphe();
		int n = g.ajouterNoeud(2);
		assertNotNull(g.getNoeud(1));
		assertEquals(g.getNoeud(1).getPoids(), 2);
	}
	
	@Test
	public void test_graphe_get_0()
	{
		Graphe g = new Graphe();
		int n = g.ajouterNoeud(2);
		assertNull(g.getNoeud(0));
	}
	
	@Test
	public void test_graphe_get_too_high()
	{
		Graphe g = new Graphe();
		int n = g.ajouterNoeud(2);
		assertNull(g.getNoeud(2));
	}
	
	@Test
	public void test_graphe_arrete()
	{
		Graphe g = new Graphe();
		int id1 = g.ajouterNoeud(2);
		int id2 = g.ajouterNoeud(4);
		g.ajouterArrete(id1, id2);
		Noeud n1 = g.getNoeud(id1);
		Noeud n2 = g.getNoeud(id2);
		assertNotNull(n1);
		assertNotNull(n2);
		
		Iterator<Integer> v1 = n1.getVoisins();
		Iterator<Integer> v2 = n2.getVoisins();
		assertEquals(v1.next(), 2);
		assertEquals(v2.next(), 1);
	}
	
	@Test
	public void test_graphe_factory_string()
	{
		Graphe g = GrapheFactory.grapheFromString("c Graphe de test\n"
				+ "p edge 3 2\n"
				+ "e 1 2\n"
				+ "e 2 3\n");
		assertNotNull(g);
		assertEquals(g.getNbNoeuds(), 3);
		Noeud n1 = g.getNoeud(1);
		Noeud n2 = g.getNoeud(2);
		Noeud n3 = g.getNoeud(3);
		assertNotNull(n1);
		assertNotNull(n2);
		assertNotNull(n3);
		
		Iterator<Integer> v1 = n1.getVoisins();
		Iterator<Integer> v2 = n2.getVoisins();
		Iterator<Integer> v3 = n3.getVoisins();
		assertEquals(v1.next(), 2);
		assertEquals(v2.next(), 1);
		assertEquals(v2.next(), 3);
		assertEquals(v3.next(), 2);
	}
	
	@Test
	public void test_graphe_factory_fileg()
	{
		Graphe g = GrapheFactory.grapheFromResource("test");
		assertNotNull(g);
		assertEquals(3, g.getNbNoeuds());
		Noeud n1 = g.getNoeud(1);
		Noeud n2 = g.getNoeud(2);
		Noeud n3 = g.getNoeud(3);
		assertNotNull(n1);
		assertNotNull(n2);
		assertNotNull(n3);
		
		Iterator<Integer> v1 = n1.getVoisins();
		Iterator<Integer> v2 = n2.getVoisins();
		Iterator<Integer> v3 = n3.getVoisins();
		assertEquals(2, v1.next());
		assertEquals(1, v2.next());
		assertEquals(3, v2.next());
		assertEquals(2, v3.next());
	}
	
	@Test
	public void test_graphe_aarrete()
	{
		Graphe g = new Graphe();
		int id1 = g.ajouterNoeud(2);
		int id2 = g.ajouterNoeud(4);
		int id3 = g.ajouterNoeud(2);
		g.ajouterArrete(id1, id2);
		
		assertTrue(g.aArrete(id1, id2));
		assertFalse(g.aArrete(id1, id3));
	}
	
	@Test
	public void test_graph_nodeid()
	{
		Graphe g = new Graphe();
		int id = g.ajouterNoeud(2);
		assertEquals(id, g.getNoeud(id).getId());
	}
	
	@Test
	public void test_graph_w()
	{
		Graphe g = GrapheFactory.grapheFromResource("test");
		g = GrapheFactory.poidsFromResource(g, "test");
		assertNotNull(g);
		assertEquals(3, g.getNbNoeuds());
		Noeud n1 = g.getNoeud(1);
		Noeud n2 = g.getNoeud(2);
		Noeud n3 = g.getNoeud(3);
		assertEquals(2, n1.getPoids());
		assertEquals(3, n2.getPoids());
		assertEquals(8, n3.getPoids());
	}
	
	@Test
	public void test_graph_clone()
	{
		Graphe g = GrapheFactory.grapheFromResource("test");
		g = GrapheFactory.poidsFromResource(g, "test");
		assertNotNull(g);
		
		Graphe g2 = g.clone();
		g.getNoeud(1).setPoids(5);
		
		assertEquals(2, g2.getNoeud(1).getPoids());
	}
}
