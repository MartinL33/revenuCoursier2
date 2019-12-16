package fr.test;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import fr.modele.Action;
import fr.modele.Biker;
import fr.modele.BikerImp;
import fr.modele.BikerImpGPS;
import fr.modele.DataFromDirectory;
import fr.modele.DataRegroupe;
import fr.modele.RegroupementParMois;
import fr.modele.Shift;
import fr.modele.Value;

public class DataRegroupeGPSMultiBikerTest {
	static DataFromDirectory data;
	static File fileTest;
	
	@BeforeClass
	public static void setFile() {
		String pathFileTest=DataRegroupeTest.pathTest+"/Biker+GPS/multi-coursier/";
		fileTest=new File(pathFileTest);    
		Action.setFileExport(fileTest);
		data=new DataFromDirectory();
		data.importDataFromDirectory(fileTest);
		Assert.assertEquals("erreur nombre Biker",2, data.getNbBiker());	
		Assert.assertEquals("erreur nombre Facture",3, data.getNbFacture());
	}
	
	
	@Test
	public void testRegroupementParMois() {
		Value.regroupageSelected=RegroupementParMois.getInstance();
		DataRegroupe dataRegroupe=new DataRegroupe();
		dataRegroupe.getResult(data);
		dataRegroupe.ecrireResult(fileTest);

		
		Biker total=dataRegroupe.getBiker(DataRegroupe.nameTotal);
		
		Assert.assertNotNull(total);
		Assert.assertEquals(3,total.getSize());
	
		Biker martin=dataRegroupe.getBiker("martin lepers");	
		Assert.assertNotNull(martin);	
		Assert.assertTrue(martin instanceof BikerImpGPS);
		
		Assert.assertEquals(2,martin.getSize());
		Shift shift=martin.getFirstShift();
		Assert.assertNotNull(shift);	
		
		Assert.assertEquals(45, shift.getNbCommande(), 0.01);
		Assert.assertEquals(307.75, shift.getRevenue(), 0.01);	
		Assert.assertEquals(7, shift.getTips(), 0.01);	
		Assert.assertEquals(0, shift.getPrime(), 0.01);	
		Assert.assertEquals(32.37, shift.getDuree(), 0.01);	

		
		Biker coursier=dataRegroupe.getBiker("Coursier1");
		
		Assert.assertNotNull(coursier);
		Assert.assertTrue(coursier instanceof BikerImp);
		Assert.assertEquals(1,coursier.getSize());
		shift=coursier.getFirstShift();
		Assert.assertEquals(10, (int)shift.getNbCommande());
		Assert.assertEquals(2019, shift.getAnnee());
		
	
	}

}
