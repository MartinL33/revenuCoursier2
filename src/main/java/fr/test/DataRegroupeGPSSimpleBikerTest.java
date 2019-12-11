package fr.test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.modele.Biker;
import fr.modele.BikerImpGPS;
import fr.modele.DataFromDirectory;
import fr.modele.DataRegroupe;
import fr.modele.RegroupementParMois;
import fr.modele.Shift;
import fr.modele.Value;

public class DataRegroupeGPSSimpleBikerTest {

	static DataFromDirectory data;
	static File fileTest;
	
	@BeforeClass
	public static void setFile() {
		String pathFileTest=DataRegroupeTest.pathTest+"/Biker+GPS/single coursier/";
		fileTest=new File(pathFileTest);    
		
		data=new DataFromDirectory();
		data.importDataFromDirectory(fileTest);
		Assert.assertEquals("erreur nombre Biker",1, data.getNbBiker());	
		Assert.assertEquals("erreur nombre Facture",2, data.getNbFacture());
	}
	
	@Test
	public void testRegroupementParMois() {
		Value.regroupageSelected=RegroupementParMois.getInstance();
		Value.isSupprimerShiftSansGPSfiable=false;
		DataRegroupe dataRegroupe=new DataRegroupe();
		dataRegroupe.getResult(data);
	
		
		Biker martin=dataRegroupe.getBiker("martin lepers");	
		Assert.assertNotNull(martin);	
		Assert.assertTrue(martin instanceof BikerImpGPS);
		Assert.assertEquals(2,martin.getSize());
		
		Shift shift=martin.getFirstShift();

		Assert.assertNotNull(shift);	
		Assert.assertEquals(45, shift.getNbCommande(), 0.01);
		Assert.assertEquals(307.75, shift.getRevenue(), 0.01);	
		
		dataRegroupe.ecrireResult(fileTest);
		
	}

}
