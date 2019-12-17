package fr.test;



import java.io.File;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.modele.Facture;
import fr.modele.Factures;
import fr.modele.Shift;

public class FacturesTest {

	static File fileTest;
	static Factures factures;
	
	long timeInMillisExpected;
	String nameExpected;
	String adresseExpected;
	String villeExpected="dataTest";
	int nbShiftExpected;	
	
	double totalExpected;	
	double primeExpected;
	double tipsExpected;	
	
	double nbCMDExpectedFirstShift;
	double revenuExpectedFirstShift;
	double primeExpectedFirstShift;
	double tipsExpectedFirstShift;	
	double dureeExpectedFirstShift;	

	@BeforeClass
	public static void setFile() {
		String pathFileTest=DataRegroupeTest.pathTest+"modele facture/";	
		fileTest=new File(pathFileTest);    	
		factures=Factures.createFromDirectory(fileTest);	
		Assert.assertEquals("Erreur nb de Facture",14,factures.getNbFacture());
	}
	
	@Test     
	public void test2pdf() {	

		Assert.assertNotNull(fileTest);		
		Facture f=factures.getFacture("2.txt");
		timeInMillisExpected=1461405600000l;
		nameExpected="un syndicaliste";
		adresseExpected="rue, france, bordeaux, 33000";
		nbShiftExpected=1;
		nbCMDExpectedFirstShift=31.0;
		revenuExpectedFirstShift=215.75;
		primeExpectedFirstShift=50.0;
		tipsExpectedFirstShift=11.0;	
		dureeExpectedFirstShift=20.5;
		
		primeExpected=50.00;
		tipsExpected=11.00;	
		totalExpected=276.75;
		
		testOneFacture(f);
	}
	
	@Test   
	public void test5pdf() {	
		
		Facture f=factures.getFacture("5.txt");
		timeInMillisExpected=1465034400000l;
		nameExpected="un syndicaliste";
		adresseExpected="rue, france, bordeaux, 33000";
		nbShiftExpected=1;
		nbCMDExpectedFirstShift=62.0;
		revenuExpectedFirstShift=442.0;
		primeExpectedFirstShift=80.0;
		tipsExpectedFirstShift=7.00;	
		dureeExpectedFirstShift=42.4;
		
		primeExpected=80.00;
		tipsExpected=7.00;	
		totalExpected=529.00;
		
		testOneFacture(f);
	}
	
	@Test  
	public void  test18pdf() {	
		Facture f=factures.getFacture("18.txt");
		
		timeInMillisExpected=1480321800000l;
		nameExpected="un syndicaliste";
		adresseExpected="bordeaux";
		nbShiftExpected=10;
		nbCMDExpectedFirstShift=22.0;
		revenuExpectedFirstShift=166.5;
		primeExpectedFirstShift=1.31747;
		tipsExpectedFirstShift=1.925;	
		dureeExpectedFirstShift=13.38333;
		
		primeExpected=6.00;
		tipsExpected=7.00;
		totalExpected=734.25;		
		
		testOneFacture(f);
	}

	@Test 
	public void  testDELI14pdf() {	
		Facture f=factures.getFacture("DELI-14.txt");
		
		timeInMillisExpected=1567792860000l;
		nameExpected="coursier1";
		adresseExpected="villelumiere";
		nbShiftExpected=2;
		nbCMDExpectedFirstShift=3.0;
		revenuExpectedFirstShift=16.44000;
		primeExpectedFirstShift=8.2249108;
		tipsExpectedFirstShift=2.1;	
		dureeExpectedFirstShift=1.8;
		villeExpected="ville1";
		
		primeExpected=10.50+10.90;
		tipsExpected=7.00;
		totalExpected=72.42;	
				
		testOneFacture(f);
	}

	@Test  
	public void  testInvoice20180704() {
		Facture f=factures.getFacture("invoice_2018-07-04_2018-07-15.pdf");
		timeInMillisExpected=1530696960000l;
		nameExpected="martin lepers";
		adresseExpected="app b119, 237 rue du 14 juillet, , 33400, talence, france";
		nbShiftExpected=12;
		nbCMDExpectedFirstShift=8.0;
		revenuExpectedFirstShift=40.0;
		primeExpectedFirstShift=0.0;
		tipsExpectedFirstShift=1.1891;	
		dureeExpectedFirstShift=2.7333;
		
		primeExpected=0.0;
		tipsExpected=11.00;
		totalExpected=507.29;	
		
		testOneFacture(f);
	}

	@Test 
	public void  testInvoice20170527() {	
		Facture f=factures.getFacture("invoice_2017-05-27.pdf");
	
		timeInMillisExpected=1494757020000l;
		nameExpected="martin lepers";
		adresseExpected="app b119, 237 rue du 14 juillet, , 33400, talence, france";
		nbShiftExpected=27;
		nbCMDExpectedFirstShift=2.0;
		revenuExpectedFirstShift=20.25;
		primeExpectedFirstShift=0.0;
		tipsExpectedFirstShift=0.31111;	
		dureeExpectedFirstShift=2.71666;
		
		primeExpected=0.0;
		tipsExpected=7.00;
		totalExpected=189.75+125;
		
		testOneFacture(f);
	}

	@Test 
	public void  testInvoice201807() {	
		Facture f=factures.getFacture("invoice_2018-07-18_2018-07-29.txt");
	
		timeInMillisExpected=1531901400000l;
		nameExpected="martin lepers";
		adresseExpected="app b119, 237 rue du 14 juillet, , 33400, talence, france";
		nbShiftExpected=4;
		nbCMDExpectedFirstShift=4.0;
		revenuExpectedFirstShift=21.7099990;
		primeExpectedFirstShift=0.0;
		tipsExpectedFirstShift=0.0;	
		dureeExpectedFirstShift=2.983333;
		
		primeExpected=0.0;
		tipsExpected=0.00;
		totalExpected=227.79-150;
		
		testOneFacture(f);
	}

	@Test   
	public void  testInvoice_d8a12731() {	
		Facture f=factures.getFacture("invoice_d8a12731_110a_49b8_9aab_48cc72a2d571_55_1568133303.pdf");

		timeInMillisExpected=1566898740000l;
		nameExpected="martin lepers";
		adresseExpected="app b119, 237 rue du 14 juillet, 33400, talence, france";
		nbShiftExpected=17;
		nbCMDExpectedFirstShift=2.0;
		revenuExpectedFirstShift=10.1899;
		primeExpectedFirstShift=0.0;
		tipsExpectedFirstShift=0.1012;	
		dureeExpectedFirstShift=1.5;
		
		primeExpected=0.0;
		tipsExpected=4.00;
		totalExpected=521.16;
		
		testOneFacture(f);
	}

	@Test 
	public void  testInvoice_e97adea9() {	
		Facture f=factures.getFacture("invoice_e97adea9_d17d_4c1e_8765_82e15b630a5d_53_1563897934.txt");
	
		timeInMillisExpected=1563289320000l;
		nameExpected="chien";
		adresseExpected="montagne2";
		nbShiftExpected=10;
		nbCMDExpectedFirstShift=10.0;
		revenuExpectedFirstShift=59.88999;
		primeExpectedFirstShift=0.3975484;
		tipsExpectedFirstShift=2.0833333;	
		dureeExpectedFirstShift=4.65;
		
		primeExpected=4.44;
		tipsExpected=20.00;
		totalExpected=536.14+0.5;
		
		testOneFacture(f);
	}
	
	@Test 
	public void  testInvoice_977dcf64_5() {	
		Facture f=factures.getFacture("invoice_977dcf64_407f_4611_9ec2_6e737caab2f7_5.txt");
	
		timeInMillisExpected=1516057200000l;
		nameExpected="unecoursiere";
		adresseExpected="douves, 33800, bordeaux, france";
		nbShiftExpected=1;
		nbCMDExpectedFirstShift=0.0;
		revenuExpectedFirstShift=0.0;
		primeExpectedFirstShift=14.75;
		tipsExpectedFirstShift=0.0;	
		dureeExpectedFirstShift=1.0;
		
		primeExpected=14.75;
		tipsExpected=0.00;
		totalExpected=14.75;
		
		testOneFacture(f);
	}

	@Test 
	public void  factureVide() {	
		Facture f=factures.getFacture("factureVide.pdf");
	
		Assert.assertNull(f);
	}
	
	@Test 
	public void  factureVide2() {	
		Facture f=factures.getFacture("factureVide2.txt");
	
		Assert.assertNull(f);
	}
	
	@Test 
	public void  factureWithWrongVersion() {	
		Facture f=factures.getFacture("factureWithWrongVersion.pdf");	
		Assert.assertNull(f);
	}
	
	@Test 
	public void  facturefactureWithTooManyPage() {	
		Facture f=factures.getFacture("factureWithTooManyPage.pdf");
	
		Assert.assertNull(f);
	}
	
	
	@Test 
	public void facturePbEncodage() {	
		Facture f=factures.getFacture("facturePbEncodage.txt");
	
		Assert.assertNull(f);
	}
	

	
	@Test 
	public void  testInvoice_977dcf64_15() {	
		Facture f=factures.getFacture("invoice_977dcf64_407f_4611_9ec2_6e737caab2f7_15.txt");
	
		Assert.assertNull(f);
	}
	
	@Test   
	public void  testInvoice_invoice_fca206ca_74f2_433a_87e5_206a24986f63_1() {	
		Facture f=factures.getFacture("invoice_fca206ca_74f2_433a_87e5_206a24986f63_1.txt");

		timeInMillisExpected=1516304280000l;
		nameExpected="nd";
		adresseExpected="rue chevreul, , 95000,";
		nbShiftExpected=5;
		nbCMDExpectedFirstShift=4.0;
		revenuExpectedFirstShift=23;
		primeExpectedFirstShift=-1.017;
		tipsExpectedFirstShift=0.666;	
		dureeExpectedFirstShift=1.6833;
		
		primeExpected=-5.75;
		tipsExpected=3.00;
		totalExpected=150.75-50.00;
		
		testOneFacture(f);
	}
	
	@Test   
	public void  testInvoice_c52c4f2b_8532() {	
		Facture f=factures.getFacture("invoice_c52c4f2b_8532_43fd_b64f_5333e29da7bd_27_1564506451.txt");

		timeInMillisExpected=1563213660000l;
		nameExpected="un arbre";
		adresseExpected="montagne";
		nbShiftExpected=4;
		nbCMDExpectedFirstShift=3.0;
		revenuExpectedFirstShift=18.90999984;
		primeExpectedFirstShift=0.0;
		tipsExpectedFirstShift=0.375;	
		dureeExpectedFirstShift=1.41666;
		
		primeExpected=0.0;
		tipsExpected=2.00;
		totalExpected=301.09-200;
		
		testOneFacture(f);
	}
	
	@Test    
	public void  testInvoiceplusieursFactures() {	
		Facture f=factures.getFacture("plusieursFactures.txt");
		
		timeInMillisExpected=1556899320000l;
		nameExpected="tintin";
		adresseExpected="montagne";
		nbShiftExpected=2;
		nbCMDExpectedFirstShift=10.0;
		revenuExpectedFirstShift=61.31;
		primeExpectedFirstShift=0.0;
		tipsExpectedFirstShift=3.043;	
		dureeExpectedFirstShift=3.96;
		villeExpected="unevilleenfrance";
		
		
		primeExpected=0.00;
		tipsExpected=7.0;
		totalExpected=158.29;
		
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
		
		
		Assert.assertEquals("tips "+facture.getTips()+" fausse", 
				tipsExpected ,facture.getTips(),0.01); 	
		
		Assert.assertEquals("prime "+facture.getTips()+" fausse", 
				primeExpected ,facture.getPrime(),0.01); 	
		
		Assert.assertEquals("total faux", 
				totalExpected ,facture.getTotal(),0.01); 	
		
		
		ArrayList<Shift> shifts=facture.getShifts();
		Assert.assertTrue("nb de shift "+facture.getNameFile()+" faux", 
				shifts.size()==nbShiftExpected);
		Shift firstShift=shifts.get(0);
		Assert.assertTrue("nb de commande "+facture.getNameFile()+" faux", 
				firstShift.getNbCommande()==nbCMDExpectedFirstShift);
		Assert.assertEquals("durée "+facture.getNameFile()+" fausse", 
				dureeExpectedFirstShift,firstShift.getDuree(),0.01);
		Assert.assertEquals("revenu "+facture.getNameFile()+" fausse", 
				revenuExpectedFirstShift, firstShift.getRevenue(),0.01);
		Assert.assertEquals("prime "+facture.getNameFile()+" fausse", 
				primeExpectedFirstShift ,firstShift.getPrime(),0.01);
		Assert.assertEquals("tips "+facture.getNameFile()+" fausse", 
				tipsExpectedFirstShift ,firstShift.getTips(),0.01); 	  

		Assert.assertTrue("date "+facture.getNameFile()+" fausse", 
				firstShift.getcDebut().getTimeInMillis()==timeInMillisExpected);
		
		
		
		
	}

}
