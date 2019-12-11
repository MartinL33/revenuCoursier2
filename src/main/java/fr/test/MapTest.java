package fr.test;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import fr.map.EcrireMap;
import fr.map.ImportMapFromJSON;
import fr.map.Map;


public class MapTest {

	//le fichier Historique des positions.json fait 200Mo, trop gros pour gitHub
	//Et le Test est trop long pour tester systematiquement 
	@Test  @Ignore
	public void testMapComplete() {

		String pathFileTest="/home/martin/Bureau/Historique des positions.json";
		File fileMap=new File(pathFileTest);	

		ImportMapFromJSON importMapFromJSON = new ImportMapFromJSON(fileMap);
		Map map = importMapFromJSON.importMap();

		Assert.assertNotNull(map);
		Assert.assertEquals("erreurs track",1, map.getNbTrack());	
		Assert.assertEquals("erreurs localisation",462678, map.getNbLocalisation());

		Calendar cDebut = new GregorianCalendar(2019, 5, 11, 0, 0, 0);
		Calendar cFin = new GregorianCalendar(2019, 5, 13, 0, 0, 0);	

		Map subMap=map.subMap(cDebut.getTime(),cFin.getTime());

		Assert.assertEquals("erreurs track",1, map.getNbTrack());
		Assert.assertEquals("erreurs localisation",1963, subMap.getNbLocalisation());
		
		EcrireMap ecrireMap = new EcrireMap();
		ecrireMap.setDossier(fileMap.getParentFile());
		ecrireMap.setMap(subMap);
		ecrireMap.setFormat("json");		
		ecrireMap.ecrireCarte();

	}

	@Test 
	public void testSmallMap() {
		
		String pathFileTest=DataRegroupeTest.pathTest+"GPS small carte/carte.json";
		File fileMap=new File(pathFileTest);

		ImportMapFromJSON importMapFromJSON = new ImportMapFromJSON(fileMap);
		Map map = importMapFromJSON.importMap();

		Assert.assertNotNull(map);	
		Assert.assertEquals("erreurs track",1, map.getNbTrack());	
		Assert.assertEquals("erreurs localisation",1963, map.getNbLocalisation());	
		
		map.fixTracks();
		
		Assert.assertEquals("erreurs track",28, map.getNbTrack());	
		Assert.assertEquals("erreurs localisation",853, map.getNbLocalisation());			
		
		//ecrireMap(fileMap, map);

	}

	private void ecrireMap(File fileMap, Map map) {
		EcrireMap ecrireMap = new EcrireMap();
		ecrireMap.setDossier(fileMap.getParentFile());
		ecrireMap.setMap(map);
		ecrireMap.setFormat("kml");
		ecrireMap.setCouleur("Vitesse");
		ecrireMap.setNameMap("carte");
		ecrireMap.ecrireCarte();
	}

}
