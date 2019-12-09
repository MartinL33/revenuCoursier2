package fr.test;

import java.io.File;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import fr.map.EcrireMap;
import fr.map.Map;
import fr.modele.BikerImpGPS;
import fr.modele.DataFromDirectory;


public class DataFromDirectoryTest {

	@Test 
	public void testSansGPS() {
		String pathFileTest=DataRegroupeTest.pathTest+"modele facture/";
		File fileTest=new File(pathFileTest);


		DataFromDirectory data=new DataFromDirectory();
		data.importDataFromDirectory(fileTest);

		Assert.assertEquals("erreur nombre Biker", 7, data.getNbBiker());

	}
	@Test @Ignore
	public void testGPS() {


		String pathFileTest=DataRegroupeTest.pathTest+"/withGPS/";
		File fileTest=new File(pathFileTest);


		DataFromDirectory data=new DataFromDirectory();
		data.importDataFromDirectory(fileTest);	

		Assert.assertEquals("erreur nombre Biker",3, data.getNbBiker());	

		ArrayList<Map> maps=data.getMaps();
		Assert.assertEquals("erreur nombre Map",1, maps.size());

		Map map=null;
		for(int i=0;i<data.getNbBiker();i++) {
			if(data.getBiker(i).getNameBiker().contentEquals("martin lepers")) {
				Assert.assertTrue(data.getBiker(i) instanceof BikerImpGPS);
				map=((BikerImpGPS)data.getBiker(i)).getMap();
			}
		}

		Assert.assertNotNull(map);	
		//Assert.assertEquals(4, map.getNbTrack());
		//Assert.assertEquals(1428, map.getNbLocalisation());
		System.out.println(map.getNbLocalisation());
		EcrireMap ecrireMap = new EcrireMap();
		ecrireMap.setDossier(fileTest);
		ecrireMap.setIsSupprimerShiftSansGPSfiable(false);
		ecrireMap.setMap(map);
		ecrireMap.setFormat(EcrireMap.formatCarte[0]);
		ecrireMap.setCouleur(EcrireMap.couleurMap[0]);
		ecrireMap.ecrireCarte();

		System.out.println(map.getTracks().get(0).toString());

	}
}

