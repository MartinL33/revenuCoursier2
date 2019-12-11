package fr.test;

import java.io.File;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import fr.map.EcrireMap;
import fr.map.Map;
import fr.modele.Biker;
import fr.modele.BikerImpGPS;
import fr.modele.DataFromDirectory;
import fr.modele.Shift;
import fr.modele.ShiftImplGPS;


public class DataFromDirectoryTest {

	@Test 
	public void testSansGPS() {
		String pathFileTest=DataRegroupeTest.pathTest+"modele facture/";
		File fileTest=new File(pathFileTest);

		DataFromDirectory data=new DataFromDirectory();
		data.importDataFromDirectory(fileTest);

		Assert.assertEquals("erreur nombre Biker", 7, data.getNbBiker());
	}
	
	@Test 
	public void testBikerEtGPSOneCoursier() {


		String pathFileTest=DataRegroupeTest.pathTest+"/Biker+GPS/single coursier/";
		File fileTest=new File(pathFileTest);

		DataFromDirectory data=new DataFromDirectory();
		data.importDataFromDirectory(fileTest);	

		Assert.assertEquals("erreur nombre Biker",1, data.getNbBiker());	
		Assert.assertEquals("erreur nombre Facture",1, data.getNbFacture());
		
		Biker biker =data.getBiker(0);
	
		Assert.assertTrue(biker instanceof BikerImpGPS);
		Assert.assertTrue(biker.getNameBiker().equals("martin l"));	
		
		Shift s=biker.getFirstShift();					
	
		Assert.assertTrue(s instanceof ShiftImplGPS);
		Assert.assertEquals("erreur nb commande",(int)s.getNbCommande(), 6);
		Assert.assertEquals("erreur année",s.getAnnee(),2019);
		Assert.assertEquals("erreur revenu",s.getRevenue(),29.15999,0.01);
		Assert.assertTrue(s.getNameFile().equals("invoice_e97adea9_d17d_4c1e_8765_82e15b630a5d_53_1563897934.txt"));
		
		ArrayList<Map> maps=data.getMaps();
		Assert.assertEquals("erreur nombre Map",1, maps.size());
		
		Map map=maps.get(0);
		Assert.assertNotNull(map);	
				
		Assert.assertEquals("erreurs nb track",1, map.getNbTrack());	
		Assert.assertEquals("erreurs nb localisation",1963, map.getNbLocalisation());	
				
		/*
		 * A dé-commenter pour obtenir les carte en kml
		 * 
		EcrireMap ecrireMap = new EcrireMap();
		ecrireMap.setDossier(fileTest);
		ecrireMap.setMap(map);
		ecrireMap.setFormat("kml");
		ecrireMap.setCouleur("Vitesse");
		ecrireMap.setNameMap("carte initiale");
		ecrireMap.ecrireCarte();		
		*/
		
		//intersection avec Biker
		map=null;
		for(int i=0;i<data.getNbBiker();i++) {
			if(data.getBiker(i).getNameBiker().contentEquals("martin l")) {
				Assert.assertTrue(data.getBiker(i) instanceof BikerImpGPS);
				map=((BikerImpGPS)data.getBiker(i)).getMap();
			}
		}

		Assert.assertNotNull(map);	
		
		System.out.println("nb track "+ map.getNbTrack());
		System.out.println("nb localisation "+ map.getNbLocalisation());
		Assert.assertEquals(4, map.getNbTrack());
		Assert.assertEquals(606, map.getNbLocalisation());
		
		/*
		ecrireMap.setMap(map);	
		ecrireMap.setNameMap("carte intersection");
		ecrireMap.ecrireCarte();
	*/
	}
	
	@Test 
	public void testBikerEtGPSPlusieursCoursiers() {
		String pathFileTest=DataRegroupeTest.pathTest+"/Biker+GPS/multi-coursier/";
		File fileTest=new File(pathFileTest);

		DataFromDirectory data=new DataFromDirectory();
		data.importDataFromDirectory(fileTest);	
		
		Assert.assertEquals("erreur nombre Biker",2, data.getNbBiker());	
		Assert.assertEquals("erreur nombre Facture",3, data.getNbFacture());
	}
}

