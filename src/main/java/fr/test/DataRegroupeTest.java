package fr.test;

import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import fr.modele.Biker;
import fr.modele.DataFromDirectory;
import fr.modele.DataRegroupe;
import fr.modele.RegroupementParHeure;
import fr.modele.RegroupementParHeureEtAnnee;
import fr.modele.RegroupementParJour;
import fr.modele.RegroupementParMois;
import fr.modele.RegroupementParSemaine;
import fr.modele.Shift;
import fr.modele.Value;

public class DataRegroupeTest {
	static String pathTest=System.getProperty("user.dir")+"/dataTest/";
	static DataFromDirectory data;
	static File fileTest;
	
	@BeforeClass
	public static void setFile() {
		String pathFileTest=pathTest+"/data Regroupe/";
		fileTest=new File(pathFileTest);    
		
		data=new DataFromDirectory();
		data.importDataFromDirectory(fileTest);
		if(data.getNbBiker()!=2) {
			fail("erreur nombre Biker");
		}

		if(data.getNbFacture()!=2) {
			fail("erreur nombre facture");
		
		}		
	}	

	@Test 
	public void  testRegroupementParMois() {
		Value.regroupageSelected=RegroupementParMois.getInstance();
		DataRegroupe dataRegroupe=new DataRegroupe();
		dataRegroupe.getResult(data);
		dataRegroupe.ecrireResult(fileTest);

		Biker total=dataRegroupe.getBiker(DataRegroupe.nameTotal);
		Assert.assertEquals(2,total.getSize());

		Biker martin=dataRegroupe.getBiker("martin lepers");	
		Assert.assertNotNull(martin);
		Assert.assertEquals(1,martin.getSize());

		Shift shift=martin.getFirstShift();

		Assert.assertNotNull(shift);	
		Assert.assertEquals(45, shift.getNbCommande(), 0.01);
		Assert.assertEquals(307.75, shift.getRevenue(), 0.01);	
		Assert.assertEquals(7, shift.getTips(), 0.01);	
		Assert.assertEquals(0, shift.getPrime(), 0.01);	
		Assert.assertEquals(32.37, shift.getDuree(), 0.01);	


		Biker baptiste=dataRegroupe.getBiker("chien");
		Assert.assertEquals(1,baptiste.getSize());

		shift=baptiste.getFirstShift();
		Assert.assertEquals(4.44, shift.getPrime(), 0.01);

	}

	@Test 
	public void  testRegroupementParSemaine() {
		Value.regroupageSelected=RegroupementParSemaine.getInstance();
		DataRegroupe dataRegroupe=new DataRegroupe();
		dataRegroupe.getResult(data);
		dataRegroupe.ecrireResult(fileTest);

		Biker total=dataRegroupe.getBiker(DataRegroupe.nameTotal);	
		Assert.assertEquals(5,total.getSize());

		Biker martin=dataRegroupe.getBiker("martin lepers");
		Assert.assertEquals(3,martin.getSize());	

		Shift shift=martin.getFirstShift();
		Assert.assertNotNull(shift);
		Assert.assertEquals(2, shift.getNbCommande(), 0.01);
		Assert.assertEquals(20.25, shift.getRevenue(), 0.01);	
		Assert.assertEquals(0.31, shift.getTips(), 0.01);	
		Assert.assertEquals(0, shift.getPrime(), 0.01);	
		Assert.assertEquals(2.83, shift.getDuree(), 0.01);	

		Biker baptiste=dataRegroupe.getBiker("chien");
		Assert.assertEquals(2,baptiste.getSize());

		shift=baptiste.getFirstShift();
		Assert.assertNotNull(shift);
		Assert.assertEquals(3.42, shift.getPrime(), 0.01);

	}

	@Test 
	public void  testRegroupementParHeure() {
		Value.regroupageSelected=RegroupementParHeure.getInstance();
		DataRegroupe dataRegroupe=new DataRegroupe();
		dataRegroupe.getResult(data);
		dataRegroupe.ecrireResult(fileTest);  

		Biker total=dataRegroupe.getBiker(DataRegroupe.nameTotal);	
		Assert.assertEquals(69,total.getSize());

		Shift shift=dataRegroupe.getBiker("martin lepers").getFirstShift();
		Assert.assertNotNull(shift);
		Assert.assertEquals(1, shift.getNbCommande(), 0.01);
		Assert.assertEquals(5, shift.getRevenue(), 0.01);	
		Assert.assertEquals(0.03, shift.getDuree(), 0.01);	   	

		shift=dataRegroupe.getBiker("chien").getFirstShift();
		Assert.assertNotNull(shift);
		Assert.assertEquals(1.74, shift.getNbCommande(), 0.01);
		Assert.assertEquals(8.62, shift.getRevenue(), 0.01);	
		Assert.assertEquals(0.95, shift.getDuree(), 0.01);	


	}

	@Test 
	public void  testRegroupementParHeureEtAnnee() {
		Value.regroupageSelected=RegroupementParHeureEtAnnee.getInstance();
		DataRegroupe dataRegroupe=new DataRegroupe();
		dataRegroupe.getResult(data);
		dataRegroupe.ecrireResult(fileTest);  

		Biker total=dataRegroupe.getBiker(DataRegroupe.nameTotal);
		Assert.assertEquals(69,total.getSize());

		Shift shift=dataRegroupe.getBiker("2017").getFirstShift();
		Assert.assertNotNull(shift);
		Assert.assertEquals(1, shift.getNbCommande(), 0.01);
		Assert.assertEquals(5, shift.getRevenue(), 0.01);	
		Assert.assertEquals(0.03, shift.getDuree(), 0.01);	 
		shift=dataRegroupe.getBiker("2019").getFirstShift();
		Assert.assertNotNull(shift);
		Assert.assertEquals(1.74, shift.getNbCommande(), 0.01);
		Assert.assertEquals(8.62, shift.getRevenue(), 0.01);	
		Assert.assertEquals(0.95, shift.getDuree(), 0.01);	


	}

	@Test 
	public void  testRegroupementParJour() {
		Value.regroupageSelected=RegroupementParJour.getInstance();
		DataRegroupe dataRegroupe=new DataRegroupe();
		dataRegroupe.getResult(data);
		Shift shift=dataRegroupe.getBiker("martin lepers").getFirstShift();
		Assert.assertNotNull(shift);
		Assert.assertEquals(2, shift.getNbCommande(), 0.01);
		Assert.assertEquals(20.25, shift.getRevenue(), 0.01);	
		Assert.assertEquals(0.31, shift.getTips(), 0.01);	
		Assert.assertEquals(0, shift.getPrime(), 0.01);	
		Assert.assertEquals(2.83, shift.getDuree(), 0.01);	

		shift=dataRegroupe.getBiker("chien").getFirstShift();
		Assert.assertNotNull(shift);
		Assert.assertEquals(10, shift.getNbCommande(), 0.01);
		Assert.assertEquals(59.89, shift.getRevenue(), 0.01);	
		Assert.assertEquals(4.65, shift.getDuree(), 0.01);	
		Assert.assertEquals(0.4, shift.getPrime(), 0.01);

		dataRegroupe.ecrireResult(fileTest);   	
	}	
	
	@Test @Ignore
	public void testGPSoneCoursier() {
		String pathFileTest=pathTest+"/withGPS/";
		File fileTest=new File(pathFileTest);

		DataFromDirectory data=new DataFromDirectory();
		data.importDataFromDirectory(fileTest);
		Assert.assertEquals("erreur nombre Biker",3 ,data.getNbBiker());
		Assert.assertEquals("erreur nombre facture",4 ,data.getNbFacture());

		Value.regroupageSelected=RegroupementParMois.getInstance();	

		DataRegroupe dataRegroupe=new DataRegroupe();
		dataRegroupe.getResult(data);
		dataRegroupe.ecrireResult(fileTest);

		Biker total=dataRegroupe.getBiker(DataRegroupe.nameTotal);
		Assert.assertEquals(2,total.getSize());
	}

	@Test @Ignore
	public void testGPSmulticoursier() {
		String pathFileTest=pathTest+"/withGPS/";
		File fileTest=new File(pathFileTest);

		DataFromDirectory data=new DataFromDirectory();
		data.importDataFromDirectory(fileTest);
		Assert.assertEquals("erreur nombre Biker",3 ,data.getNbBiker());
		Assert.assertEquals("erreur nombre facture",4 ,data.getNbFacture());

		Value.regroupageSelected=RegroupementParMois.getInstance();	

		DataRegroupe dataRegroupe=new DataRegroupe();
		dataRegroupe.getResult(data);
		dataRegroupe.ecrireResult(fileTest);

		Biker total=dataRegroupe.getBiker(DataRegroupe.nameTotal);
		Assert.assertEquals(2,total.getSize());
	}

}
