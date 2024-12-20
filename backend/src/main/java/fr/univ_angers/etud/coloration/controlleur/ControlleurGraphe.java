package fr.univ_angers.etud.coloration.controlleur;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.BufferedReader;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.univ_angers.etud.coloration.model.algo.AlgorithmeColoration;
import fr.univ_angers.etud.coloration.model.algo.AlgorithmeFactory;
import fr.univ_angers.etud.coloration.model.convert.GrapheConverter;
import fr.univ_angers.etud.coloration.model.graphe.Graphe;
import fr.univ_angers.etud.coloration.model.graphe.GrapheFactory;
import fr.univ_angers.etud.coloration.model.graphe.json.GrapheJSON;

@RestController
@RequestMapping("/api")
public class ControlleurGraphe {
	private ArrayList<String> listeGrapheCache; //Cache pour pas avoir a le calculer a chaque fois

	public ControlleurGraphe()
	{
		listeGrapheCache = null;
	}
	
	@GetMapping("/graph/{nom}")
	public GrapheJSON getGraph(@PathVariable String nom)
	{
		Graphe graphe = GrapheFactory.poidsFromResource(GrapheFactory.grapheFromResource(nom), nom);
		if(graphe != null)
		{
			return GrapheConverter.GrapheToAPI(graphe);
		}
		return null;
		
	}
	
	@GetMapping("/coloration/{nomGraphe}/{nomAlgo}")
	public GrapheJSON coloration(@PathVariable String nomGraphe, @PathVariable String nomAlgo)
	{
		Graphe graphe = GrapheFactory.poidsFromResource(GrapheFactory.grapheFromResource(nomGraphe), nomGraphe);
		if(graphe != null)
		{
			AlgorithmeColoration coloration = AlgorithmeFactory.getAlgorithme(nomAlgo, graphe, false);
			coloration.step();
			Graphe grapheRet = coloration.getGraph();
			grapheRet.reduireCouleurs();
			GrapheJSON ret = GrapheConverter.GrapheToAPI(grapheRet);
			ret.setCout(grapheRet.coutPondere()); //TODO : Devrait retourner le nombre de couleurs
			return ret;
		}
		return null;
		
	}
	
	//TODO : Modifier ce qu'est une étape, ajouter cout, interdire le étape par étape pour gros graph
	@GetMapping("/colorationEtape/{nomGraphe}/{nomAlgo}")
	public ArrayList<GrapheJSON> colorationEtape(@PathVariable String nomGraphe, @PathVariable String nomAlgo)
	{
		Graphe graphe = GrapheFactory.poidsFromResource(GrapheFactory.grapheFromResource(nomGraphe), nomGraphe);
		if(graphe != null)
		{
			AlgorithmeColoration coloration = AlgorithmeFactory.getAlgorithme(nomAlgo, graphe, true);
			coloration.step();
			ArrayList<GrapheJSON> ret = new ArrayList<GrapheJSON>();
			ArrayList<Graphe> etapes = coloration.getEtapes();
			for(Graphe e:etapes)
			{
				ret.add(GrapheConverter.GrapheToAPI(e));
			}
			return ret;
		}
		return null;
		
	}
	
	@GetMapping("/colorationPonderee/{nomGraphe}/{nomAlgoGlouton}/{nomAlgoOpti}")
	public GrapheJSON colorationPondere(@PathVariable String nomGraphe, @PathVariable String nomAlgoGlouton, @PathVariable String nomAlgoOpti)
	{
		Graphe graphe = GrapheFactory.poidsFromResource(GrapheFactory.grapheFromResource(nomGraphe), nomGraphe);
		if(graphe != null)
		{
			AlgorithmeColoration coloration = AlgorithmeFactory.getAlgorithme(nomAlgoGlouton, graphe, false);
			coloration.step();
			AlgorithmeColoration colorationOpti = AlgorithmeFactory.getAlgorithme(nomAlgoOpti, coloration.getGraph(), false);
			colorationOpti.step();
			Graphe grapheRet = colorationOpti.getGraph();
			grapheRet.reduireCouleurs();
			GrapheJSON ret = GrapheConverter.GrapheToAPI(grapheRet);
			ret.setCout(grapheRet.coutPondere());
			return ret;
		}
		return null;
		
	}

	@GetMapping("/colorationPondereeEtape/{nomGraphe}/{nomAlgoGlouton}/{nomAlgoOpti}")
	public ArrayList<GrapheJSON> colorationPondereEtape(@PathVariable String nomGraphe, @PathVariable String nomAlgoGlouton, @PathVariable String nomAlgoOpti)
	{
		Graphe graphe = GrapheFactory.poidsFromResource(GrapheFactory.grapheFromResource(nomGraphe), nomGraphe);
		
		if(graphe != null)
		{
			AlgorithmeColoration coloration = AlgorithmeFactory.getAlgorithme(nomAlgoGlouton, graphe, false);
			coloration.step();
			AlgorithmeColoration colorationOpti = AlgorithmeFactory.getAlgorithme(nomAlgoOpti, coloration.getGraph(), true);
			colorationOpti.step();
			ArrayList<GrapheJSON> ret = new ArrayList<GrapheJSON>();
			ArrayList<Graphe> etapes = colorationOpti.getEtapes();
			for(Graphe e:etapes)
			{
				e.reduireCouleurs();
				GrapheJSON json = GrapheConverter.GrapheToAPI(e);
				json.setCout(e.coutPondere());
				ret.add(json);
			}
			return ret;
		}
		return null;
		
	}

	@GetMapping("/colorationPondereeRepete/{nomGraphe}/{nomAlgoGlouton}/{nomAlgoOpti}/{delay}")
	public GrapheJSON colorationPondereRepete(@PathVariable String nomGraphe, @PathVariable String nomAlgoGlouton, @PathVariable String nomAlgoOpti, @PathVariable int delay)
	{
		Graphe graphe = GrapheFactory.poidsFromResource(GrapheFactory.grapheFromResource(nomGraphe), nomGraphe);
		Graphe grapheFinal = null;
		int coutFinal = Integer.MAX_VALUE;
		if(graphe != null)
		{
			delay = delay * 1000;
			long temps1 = System.currentTimeMillis();
			while(delay > 0)
			{
				AlgorithmeColoration coloration = AlgorithmeFactory.getAlgorithme(nomAlgoGlouton, graphe.clone(), false);
				coloration.step();
				AlgorithmeColoration colorationOpti = AlgorithmeFactory.getAlgorithme(nomAlgoOpti, coloration.getGraph(), false);
				colorationOpti.step();
				Graphe grapheRet = colorationOpti.getGraph();
				int cout = grapheRet.coutPondere();
				if(cout < coutFinal)
				{
					grapheFinal = grapheRet;
					coutFinal = cout;
				}
				long temps2 = System.currentTimeMillis();
				delay -= (temps2 - temps1);
				temps1 = temps2;
			}

			grapheFinal.reduireCouleurs();
			GrapheJSON ret = GrapheConverter.GrapheToAPI(grapheFinal);
			ret.setCout(coutFinal);
			return ret;
		}
		return null;
		
	}

	@GetMapping("listeGraphe")
	public ArrayList<String> listerGraphes()
	{
		if(listeGrapheCache == null)
		{
			try
			{
				listeGrapheCache = new ArrayList<String>();
				ClassPathResource res = new ClassPathResource("graphe");
				File dir = res.getFile();
				File[] liste = dir.listFiles();
				if(liste != null)
				{
					for(File f:liste)
					{
						String fname = f.getName();
						if(fname.charAt(fname.length()-1) == 'l')
						{
							String str = fname.substring(0,fname.length()-4);
							System.out.println(str);
							listeGrapheCache.add(str);
						}
					}
				}
				listeGrapheCache.sort(null);
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}

		}
		return listeGrapheCache;
	}

	@GetMapping("best_scores_gcp")
	public ArrayList<String> listerScoresGCP()
	{
		ArrayList<String> scoresGCP = new ArrayList<String>();

		try
		{
			ClassPathResource res = new ClassPathResource("best_score/best_scores_gcp.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(res.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null)
			{
				scoresGCP.add(line);
			}
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return scoresGCP;
	}

	@GetMapping("best_scores_wvcp")
	public ArrayList<String> listerScoresWVCP()
	{
		ArrayList<String> scoresWVCP = new ArrayList<String>();

		try
		{
			ClassPathResource res = new ClassPathResource("best_score/best_scores_wvcp.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(res.getInputStream()));
			String line;
			while ((line = reader.readLine()) != null)
			{
				scoresWVCP.add(line);
			}
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return scoresWVCP;
	}


}
