package test;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import importFacture.Facture;
import importFacture.MonteurFacture;
import importFacture.Shift;

public class FactureTest {

	static File fileTest;

	long timeInMillisExpected;
	String nameExpected;
	String adresseExpected;
	String villeExpected="dataTest";
	int nbShiftExpected;
	double nbCMDExpected;
	double revenuExpected;
	double primeExpected;
	double tipsExpected;	
	double dureeExpected;

	@BeforeClass
	public static void setFile() {
		String pathFileTest=DataRegroupeTest.pathTest+"modele facture/";
		fileTest=new File(pathFileTest);    	
	}
	
	
	
	@Test   
	public void  testInvoice20180704() {	
		File fileTest1=new File(fileTest.getAbsolutePath()+ "/invoice_2018-07-04_2018-07-15.pdf");
		timeInMillisExpected=1530696960000l;
		nameExpected="martin lepers";
		adresseExpected="app b119, 237 rue du 14 juillet, , 33400, talence, france";
		nbShiftExpected=12;
		nbCMDExpected=8.0;
		revenuExpected=40.0;
		primeExpected=0.0;
		tipsExpected=1.1891;	
		dureeExpected=2.7333;
		testOneFacture(fileTest1);
	}

	@Test   
	public void  testInvoice20170527() {	
		File fileTest1=new File(fileTest.getAbsolutePath()+ "/invoice_2017-05-27.pdf");
		timeInMillisExpected=1494757020000l;
		nameExpected="martin lepers";
		adresseExpected="app b119, 237 rue du 14 juillet, , 33400, talence, france";
		nbShiftExpected=27;
		nbCMDExpected=2.0;
		revenuExpected=20.25;
		primeExpected=0.0;
		tipsExpected=0.31111;	
		dureeExpected=2.71666;
		testOneFacture(fileTest1);
	}



	@Test   
	public void  testInvoice_d8a12731() {	
		File fileTest1=new File(fileTest.getAbsolutePath()+ "/invoice_d8a12731_110a_49b8_9aab_48cc72a2d571_55_1568133303.pdf");
		timeInMillisExpected=1566898740000l;
		nameExpected="martin lepers";
		adresseExpected="app b119, 237 rue du 14 juillet, 33400, talence, france";
		nbShiftExpected=17;
		nbCMDExpected=2.0;
		revenuExpected=10.1899;
		primeExpected=0.0;
		tipsExpected=0.1012;	
		dureeExpected=1.5;
		testOneFacture(fileTest1);
	}

	
	

	

	private void testOneFacture(File fileTest1) {
		if(!fileTest1.isFile())	  fail("fichier absent "+fileTest1.getName());

		MonteurFacture monteur=new MonteurFacture(fileTest1);			
		Facture facture=monteur.getFacture();	
	
		Assert.assertNotNull(facture);

		Assert.assertTrue("ville "+fileTest1.getName()+" fausse", 
				facture.getVille().contentEquals(villeExpected));
		Assert.assertTrue("adresse "+fileTest1.getName()+" fausse "+facture.getAdresse(), 
				facture.getAdresse().contentEquals(adresseExpected));
		Assert.assertTrue("nameBiker "+fileTest1.getName()+" faux "+facture.getNameBiker(), 
				facture.getNameBiker().contentEquals(nameExpected));
		
		ArrayList<Shift> shifts=facture.getShifts();
		Assert.assertTrue("nb de shift "+fileTest1.getName()+" faux", 
				shifts.size()==nbShiftExpected);
		Shift firstShift=shifts.get(0);
		Assert.assertTrue("nb de commande "+fileTest1.getName()+" faux", 
				firstShift.getNbCommande()==nbCMDExpected);
		Assert.assertEquals("durée "+fileTest1.getName()+" fausse", 
				firstShift.getDuree(),dureeExpected,0.01);
		Assert.assertEquals("revenu "+fileTest1.getName()+" fausse", 
				revenuExpected, firstShift.getRevenue(),0.01);
		Assert.assertEquals("prime "+fileTest1.getName()+" fausse", 
				primeExpected ,firstShift.getPrime(),0.01);
		Assert.assertEquals("tips "+fileTest1.getName()+" fausse", 
				tipsExpected ,firstShift.getTips(),0.01); 	  

		Assert.assertTrue("date "+fileTest1.getName()+" fausse", 
				firstShift.getcDebut().getTimeInMillis()==timeInMillisExpected);

	}

}
