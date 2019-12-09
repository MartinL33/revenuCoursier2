package fr.test;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import fr.modele.Facture;
import fr.modele.Factures;
import fr.modele.MonteurFacture;
import fr.modele.Shift;

public class FacturesTest {

	static File fileTest;
	static Factures factures;
	
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
		factures=Factures.createFromDirectory(fileTest);	
		Assert.assertEquals("Erreur nb de Facture",13,factures.getNbFacture());
	}

	
	@Test     
	public void test2pdf() {	

		Assert.assertNotNull(fileTest);		
		Facture f=factures.getFacture("2.txt");
		timeInMillisExpected=1461405600000l;
		nameExpected="un syndicaliste malhonnete";
		adresseExpected="rue, france, bordeaux, 33000";
		nbShiftExpected=1;
		nbCMDExpected=31.0;
		revenuExpected=215.75;
		primeExpected=50.0;
		tipsExpected=11.0;	
		dureeExpected=20.5;
		testOneFacture(f);
	}
	
	@Test   
	public void test5pdf() {	
		
		Facture f=factures.getFacture("5.txt");
		timeInMillisExpected=1465034400000l;
		nameExpected="un syndicaliste malhonnete";
		adresseExpected="rue, france, bordeaux, 33000";
		nbShiftExpected=1;
		nbCMDExpected=62.0;
		revenuExpected=442.0;
		primeExpected=80.0;
		tipsExpected=7.00;	
		dureeExpected=42.4;
		testOneFacture(f);
	}
	
	@Test  
	public void  test18pdf() {	
		Facture f=factures.getFacture("18.txt");
		
		timeInMillisExpected=1480321800000l;
		nameExpected="un syndicaliste malhonnete";
		adresseExpected="bordeaux";
		nbShiftExpected=10;
		nbCMDExpected=22.0;
		revenuExpected=166.5;
		primeExpected=1.31747;
		tipsExpected=1.925;	
		dureeExpected=13.38333;
		testOneFacture(f);
	}

	@Test 
	public void  testDELI14pdf() {	
		Facture f=factures.getFacture("DELI-14.txt");
		
		timeInMillisExpected=1567792860000l;
		nameExpected="coursier1";
		adresseExpected="villelumiere";
		nbShiftExpected=2;
		nbCMDExpected=3.0;
		revenuExpected=16.44000;
		primeExpected=8.2249108;
		tipsExpected=2.1;	
		dureeExpected=1.8;
		villeExpected="ville1";
		testOneFacture(f);
	}

	@Test  
	public void  testInvoice20180704() {
		Facture f=factures.getFacture("invoice_2018-07-04_2018-07-15.pdf");
		timeInMillisExpected=1530696960000l;
		nameExpected="martin lepers";
		adresseExpected="app b119, 237 rue du 14 juillet, , 33400, talence, france";
		nbShiftExpected=12;
		nbCMDExpected=8.0;
		revenuExpected=40.0;
		primeExpected=0.0;
		tipsExpected=1.1891;	
		dureeExpected=2.7333;
		testOneFacture(f);
	}

	@Test 
	public void  testInvoice20170527() {	
		Facture f=factures.getFacture("invoice_2017-05-27.pdf");
	
		timeInMillisExpected=1494757020000l;
		nameExpected="martin lepers";
		adresseExpected="app b119, 237 rue du 14 juillet, , 33400, talence, france";
		nbShiftExpected=27;
		nbCMDExpected=2.0;
		revenuExpected=20.25;
		primeExpected=0.0;
		tipsExpected=0.31111;	
		dureeExpected=2.71666;
		testOneFacture(f);
	}

	@Test 
	public void  testInvoice201807() {	
		Facture f=factures.getFacture("invoice_2018-07-18_2018-07-29.txt");
	
		timeInMillisExpected=1531901400000l;
		nameExpected="martin lepers";
		adresseExpected="app b119, 237 rue du 14 juillet, , 33400, talence, france";
		nbShiftExpected=4;
		nbCMDExpected=4.0;
		revenuExpected=21.7099990;
		primeExpected=0.0;
		tipsExpected=0.0;	
		dureeExpected=2.983333;
		testOneFacture(f);
	}

	@Test   
	public void  testInvoice_d8a12731() {	
		Facture f=factures.getFacture("invoice_d8a12731_110a_49b8_9aab_48cc72a2d571_55_1568133303.pdf");

		timeInMillisExpected=1566898740000l;
		nameExpected="martin lepers";
		adresseExpected="app b119, 237 rue du 14 juillet, 33400, talence, france";
		nbShiftExpected=17;
		nbCMDExpected=2.0;
		revenuExpected=10.1899;
		primeExpected=0.0;
		tipsExpected=0.1012;	
		dureeExpected=1.5;
		testOneFacture(f);
	}

	@Test 
	public void  testInvoice_e97adea9() {	
		Facture f=factures.getFacture("invoice_e97adea9_d17d_4c1e_8765_82e15b630a5d_53_1563897934.txt");
	
		timeInMillisExpected=1563289320000l;
		nameExpected="chien";
		adresseExpected="montagne2";
		nbShiftExpected=10;
		nbCMDExpected=10.0;
		revenuExpected=59.88999;
		primeExpected=0.3975484;
		tipsExpected=2.0833333;	
		dureeExpected=4.65;
		testOneFacture(f);
	}
	
	@Test 
	public void  testInvoice_977dcf64() {	
		Facture f=factures.getFacture("invoice_977dcf64_407f_4611_9ec2_6e737caab2f7_5.txt");
	
		timeInMillisExpected=1516057200000l;
		nameExpected="unecoursiere";
		adresseExpected="douves, 33800, bordeaux, france";
		nbShiftExpected=1;
		nbCMDExpected=0.0;
		revenuExpected=0.0;
		primeExpected=14.75;
		tipsExpected=0.0;	
		dureeExpected=1.0;
		testOneFacture(f);
	}

	@Test   
	public void  testInvoice_c52c4f2b_8532() {	
		Facture f=factures.getFacture("invoice_c52c4f2b_8532_43fd_b64f_5333e29da7bd_27_1564506451.txt");

		timeInMillisExpected=1563213660000l;
		nameExpected="un arbre";
		adresseExpected="montagne";
		nbShiftExpected=4;
		nbCMDExpected=3.0;
		revenuExpected=18.90999984;
		primeExpected=0.0;
		tipsExpected=0.375;	
		dureeExpected=1.41666;
		testOneFacture(f);
	}
	
	@Test    
	public void  testInvoiceplusieursFactures() {	
		Facture f=factures.getFacture("plusieursFactures.txt");
		
		timeInMillisExpected=1556899320000l;
		nameExpected="tintin";
		adresseExpected="montagne";
		nbShiftExpected=2;
		nbCMDExpected=10.0;
		revenuExpected=61.31;
		primeExpected=0.0;
		tipsExpected=3.043;	
		dureeExpected=3.96;
		villeExpected="unevilleenfrance";
		testOneFacture(f);
	}
	
	
	private void testOneFacture(Facture facture) {		
	
		Assert.assertNotNull(facture);	

		Assert.assertTrue("ville "+facture.getNameFile()+" fausse "+facture.getVille(), 
				facture.getVille().contentEquals(villeExpected));
		Assert.assertTrue("adresse "+facture.getNameFile()+" fausse "+facture.getAdresse(), 
				facture.getAdresse().contentEquals(adresseExpected));
		Assert.assertTrue("nameBiker "+facture.getNameFile()+" faux "+facture.getNameBiker(), 
				facture.getNameBiker().contentEquals(nameExpected));
		
		ArrayList<Shift> shifts=facture.getShifts();
		Assert.assertTrue("nb de shift "+facture.getNameFile()+" faux", 
				shifts.size()==nbShiftExpected);
		Shift firstShift=shifts.get(0);
		Assert.assertTrue("nb de commande "+facture.getNameFile()+" faux", 
				firstShift.getNbCommande()==nbCMDExpected);
		Assert.assertEquals("durée "+facture.getNameFile()+" fausse", 
				firstShift.getDuree(),dureeExpected,0.01);
		Assert.assertEquals("revenu "+facture.getNameFile()+" fausse", 
				revenuExpected, firstShift.getRevenue(),0.01);
		Assert.assertEquals("prime "+facture.getNameFile()+" fausse", 
				primeExpected ,firstShift.getPrime(),0.01);
		Assert.assertEquals("tips "+facture.getNameFile()+" fausse", 
				tipsExpected ,firstShift.getTips(),0.01); 	  
		
		Assert.assertTrue("date "+facture.getNameFile()+" fausse", 
				firstShift.getcDebut().getTimeInMillis()==timeInMillisExpected);

	}

}
