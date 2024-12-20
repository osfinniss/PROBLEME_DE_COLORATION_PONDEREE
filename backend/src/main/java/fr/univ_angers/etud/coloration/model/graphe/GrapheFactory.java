package fr.univ_angers.etud.coloration.model.graphe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class GrapheFactory {
	
	static private void parseColLine(Graphe graphe, String line)
	{
		String[] fields = line.split(" ");
		if(fields.length == 0) return;
		switch(fields[0])
		{
		case "p":
		{
			if(fields.length < 4)
			{
				// TODO : Exception
				return;
			}
			int nbNoeuds = Integer.valueOf(fields[2]);
			while(graphe.getNbNoeuds() < nbNoeuds)
			{
				graphe.ajouterNoeud();
			}
			break;
		}
		case "e":
			if(fields.length < 3)
			{
				// TODO : Exception
				return;
			}
			graphe.ajouterArrete(Integer.valueOf(fields[1]), Integer.valueOf(fields[2]));
			break;
		default:
			break;
		}
	}
	
	static private Graphe readColReader(BufferedReader reader)
	{
		Graphe graphe = new Graphe();
		String line;
		try {
			while((line = reader.readLine()) != null)
			{
				//System.out.println(line);
				parseColLine(graphe, line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return graphe;
	}
	
	static private void readWReader(Graphe graphe, BufferedReader reader)
	{
		String line;
		int id = 1;
		try {
			while((line = reader.readLine()) != null && id <= graphe.getNbNoeuds())
			{
				//System.out.println(line);
				graphe.getNoeud(id).setPoids(Integer.valueOf(line));
				id++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static public Graphe grapheFromString(String str)
	{
		BufferedReader reader = new BufferedReader(new StringReader(str));
		//System.out.println("Test string");
		return readColReader(reader);
	}
	
	static public Graphe grapheFromResource(String str)
	{
		Resource res;
		BufferedReader reader;
		//System.out.println("Test file");
		try {
			res = new ClassPathResource("graphe/"+str+".col");
			reader = new BufferedReader(new InputStreamReader(res.getInputStream()));
			Graphe g = readColReader(reader);
			reader.close();
			return g;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	static public Graphe poidsFromString(Graphe g, String str)
	{
		BufferedReader reader = new BufferedReader(new StringReader(str));
		//System.out.println("Test string");
		readWReader(g, reader);
		return g;
	}
	
	static public Graphe poidsFromResource(Graphe g, String str)
	{
		Resource res;
		BufferedReader reader;
		//System.out.println("Test file");
		try {
			res = new ClassPathResource("graphe/"+str+".col.w");
			reader = new BufferedReader(new InputStreamReader(res.getInputStream()));
			readWReader(g, reader);
			reader.close();
			return g;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
