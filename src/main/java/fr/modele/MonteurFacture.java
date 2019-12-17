package fr.modele;

import static fr.algorithmes.Utilitaire.isContenuDansTableau;

import static fr.modele.FactureData.*;
import static fr.modele.Text.nameResult;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import fr.gui.GUI;

public class MonteurFacture {

	static boolean isCABrutTVA=false;  //chiffre d'affaire brute ou net de TVA? 
	private double total=0.0;
	private double tips=0.0;
	private double aDeduire=0.0;
	private File file=null;
	private Facture facture=new Facture();	

	//lines import
	private String[] lines=null;

	//langue
	Locale localeFacture;	 
	SimpleDateFormat dateFormatFacture;   


	public Facture getFacture() {
		return facture;
	}

	public MonteurFacture(File file) {
		assert file !=null;
		this.file=file;
		String stringFacture=null;

		if(isFactureFile(file)) {
			String s=file.getName();

			if(s.endsWith(".pdf")) {

				if(s.startsWith(nameResult))  GUI.messageConsole("Ignorer "+s);				

				else if(file.length()<20000) {
					GUI.messageConsole("Ignorer "+s+" Taille: " + file.length());
				}

				else if(file.length()>60000) {
					GUI.messageConsole("Ignorer "+s+" Taille: " + file.length());
				}	    
				else {
					stringFacture=loadFacture(file);	
				}



			}				
		}		
		getVille(file);		
		importString(stringFacture);

		facture.setShiftsNameFile(file.getName());
		controleFacture();

	}



	public MonteurFacture(String[] tabString) {
		importTabLine(tabString);			
		controleFacture();
	}

	private static Boolean isFactureFile(File file){
		if(file.getName().startsWith(nameResult)) return false;
		if(!file.exists()) return false;
		if(!file.isFile()) return false;
		if(!file.canRead()) return false;
		if(file.getName().length()<4) return false;
		return true;
	}

	private void getVille(File file) {
		if(facture.getVille().isEmpty()) {
			File fileVille=file.getParentFile().getParentFile();

			if(fileVille!=null) {
				facture.setNameVille(fileVille.getName());
			}
			else {
				GUI.messageConsole("determination ville impossible");
			}
		}
	}

	private String loadFacture(File file) {
		String pdfFileInText=null;
		try {	    	    
			PDDocument document = PDDocument.load(file);

			if (isFactureDocument(document)) {		

				PDFTextStripperByArea stripper = new PDFTextStripperByArea();
				stripper.setSortByPosition(true);		
				PDFTextStripper tStripper = new PDFTextStripper();		
				pdfFileInText = tStripper.getText(document);					
			}
			else{		
				GUI.messageConsole(file.getName()+" ignoré, version: "+ document.getVersion());
			}
			document.close();	    
		}
		catch (IOException ex) {	    
			GUI.messageConsole("erreur importation fichier "+file.getName()+" "+ex.getMessage());
		}
		return pdfFileInText;
	}

	private Boolean isFactureDocument(PDDocument document){		
		if(document.isEncrypted()) return false;
		else if(document.getNumberOfPages()>5) return false;
		else if(document.getVersion()==1.5f) return true;		
		else if(document.getVersion()==1.3f) return true;
		return false;	
	}

	private boolean importString(String stringFacture) {
		if(stringFacture==null) return false;	
		if(stringFacture.isEmpty()) return false;		
		if(stringFacture.isBlank()) return false;		
		if(stringFacture.length()<10) return false;

		String[] tabLine=null;

		if(stringFacture.contains("\\r?\\n")) {
			tabLine = stringFacture.split("\\r?\\n");
		}
		else if(stringFacture.contains("\n")) {
			tabLine = stringFacture.split("\n");
		}


		boolean res=importTabLine(tabLine);
		return res;
	}	

	boolean importTabLine(String[] tabline){
		if(tabline==null )return false; 
		if(tabline.length<2) return false; 
		if(tabline[0].startsWith("�")) {
			GUI.messageConsole("erreur encodage"
					+System.getProperty("line.separator")
					+tabline[0]);
			return false;
		}


		this.lines=tabline;
		for(int i=0;i<lines.length;i++) {				
			lines[i]=lines[i].toLowerCase().trim();	
		}
		if(!tabline[0].startsWith("client")) return false;

		detectLangAndInitialiseDates();				
		importLines();
		return true;
	}


	private void detectLangAndInitialiseDates(){
		localeFacture=Locale.FRANCE;
		dateFormatFacture=new SimpleDateFormat(patternDateFormatFacture,localeFacture);	

	}

	private void importLines() {
		
		for (String line : lines) { 
	//		System.out.println(line);
			
			if(isLineContains(line, ":")) {
				importLineWithDeuxPoint(line);
			}

			else if(startWhith(line,nomJoursSemaine)) {	
				importShift(line);
			}

			else if(isLineContains(line, "€")){
				importLineWithCurrency(line);
			}

			else {		
				importAutreLignes(line);
			}

		}

		if(facture.isEmpty())	    importOldFacture();
		else{
			facture.putShiftsTips(tips); 	  
			facture.putShiftsPrime(total,tips,aDeduire);
		}
	}

	private boolean isLineContains(String line, String separateurChamp) {
		if(!line.contains(separateurChamp)) return false;
		if(line.endsWith(separateurChamp)) return false;
		if(line.startsWith(separateurChamp)) return false;
		if(line.split(separateurChamp).length!=2)  return false;
		return true;

	}

	private void importLineWithDeuxPoint(String line) {
		String seperateurChamp=":";
		String debutLine = line.split(seperateurChamp)[0].trim();
		String finLigne = (line.split(seperateurChamp)[1]).trim();
		String[] siretDeliveroo= {"810 365 817 00016","810 365 817 00156"};

		if(debutLine.startsWith("adresse")) {			
			facture.setAdresse(finLigne);
		}

		else if(debutLine.startsWith("nom")) {					
			facture.setNameBiker(finLigne);
		}

		else if(debutLine.startsWith("n° siren")) {
			facture.setSiren(finLigne);							
		}

		else if(debutLine.startsWith("n° siret") && !isContenuDansTableau(finLigne,siretDeliveroo)) {

			facture.setSiret(finLigne);
		}

		else if(debutLine.startsWith("facture pour la période")||line.startsWith("facture pour la periode")){
			setDatePeriode(finLigne);
		}				

		else if(debutLine.startsWith("ville")) {
			facture.setNameVille(finLigne);							
		}

	}

	private void setDatePeriode(String s) {

		Calendar dateFin = new GregorianCalendar(localeFacture);
		Calendar dateDebut = new GregorianCalendar(localeFacture);

		DateFormat dfDTFF=DateFormat.getDateInstance(DateFormat.LONG,localeFacture);      
		s=s.replace(" – ", " - ").replace(" au ", " - ");        
		if(s.split(" - ").length!=2) {	  
			GUI.messageConsole("erreur longueur date= "+s); 			 
		}      
		try{            
			dateDebut.setTime(dfDTFF.parse(s.split(" - ")[0]));
			dateFin.setTime(dfDTFF.parse(s.split(" - ")[1]));           
		}
		catch (ParseException e){ 
			GUI.messageConsole(e.getMessage());
		}	

		facture.setDateDebut(dateDebut);
		facture.setDateFin(dateFin);

	}

	private void importAutreLignes(String line){

		

		if(line.startsWith("avance d’installation")) {
			aDeduire+=150;
		}

		else if(line.contains("recommandation")) {
			findRecommandation();
		}
		
		else if(startWhith(line,ignoreAutre)) {
			
		}
		else {
		//	System.out.println(line);
		}
	
	}

	private void importLineWithCurrency(String line) {
		String seperateurChamp="€";
		String debutLine = line.split(seperateurChamp)[0].trim();
		String finLigne = (line.split(seperateurChamp)[1]).trim();
		double aDeduirePrecedent=aDeduire;

		if(debutLine.startsWith(pourboires)) {
			tips=Float.parseFloat(trimString(finLigne));
		}

		else if(startWhith(debutLine,totalTab)) {

			total=(Double.parseDouble(trimString(finLigne.replace(".", "").replace(",", ""))))/100;

		}

		else if(debutLine.startsWith("tva")&&!debutLine.startsWith("tva non applicable")) {
			if(isCABrutTVA) {
				aDeduire-=Float.parseFloat(trimString(finLigne));
			}	
		}

		else if(debutLine.startsWith("frais de transaction")) {		
			aDeduire+=Float.parseFloat(trimString(finLigne));			
		}

		else if(debutLine.startsWith("caution")) {
			aDeduire+= Float.parseFloat(trimString(finLigne));	

		}

		else if(line.contains("recommandation")) {

			aDeduire+= Float.parseFloat(trimString(finLigne));		   	  
		

		}

		else if(startWhith(debutLine,ignoreCurrency)) {

		}
		else {
			// System.out.println(line);
		}

		if(aDeduire!=aDeduirePrecedent) {			
			//System.out.println(line+" a deduire du total ttc:"+aDeduire);
		}


	}

	
	private void findRecommandation() {
	
		String stringAjutement="";
		for (String line : lines) { 
			line = line.toLowerCase(localeFacture).trim();

			if(line.contains("recommandation")&&!line.contains("€")) {
				stringAjutement=line;
			}

			else if(!stringAjutement.equals("")) {

				stringAjutement+=" "+line;				
				if(stringAjutement.contains("€")) {

					aDeduire+=Float.parseFloat(stringAjutement.split("€")[1]);
					GUI.messageConsole(stringAjutement+"a deduire du total ttc: "+aDeduire);
					stringAjutement="";
				}
			}
			else if(startWhith(line,resume)){ 
				stringAjutement="";
			}    
		}
		
		
	}	

	private void importOldFacture(){

		String sNb="";
		String sTotal="";
		String sDuree="";
		String sPrime="";
		String sTips="";	


		for (String line : lines) {       
			line=line.toLowerCase(localeFacture);
			while(line.contains("  ")) {
				line=line.replaceAll("  "," ");
			}
			line=line.replaceAll("	"," ");			

			if(line.startsWith("prestations ")){				

				if(line.contains("hrs =")){
					//	Prestations 7,50 €/hr x 20.5hrs = 153,75 €
					sDuree=line.split("hrs =")[0];
					if(sDuree.contains("€/hr")){
						sDuree=sDuree.split("€/hr")[1].trim();
					}                    
				}
				else if(line.contains("h =")){

					//	Prestations	€7,50/h × 47.8h = €358,50
					sDuree=line.split("h =")[0];
					if(sDuree.contains("h × ")){
						sDuree=sDuree.split("h × ")[1];
					} 
					if(sDuree.contains("h x ")){
						sDuree=sDuree.split("h x ")[1];
					}
				}                
			}
			if(line.startsWith("commission"+sep)){	

				//Commission 2,00 €/livraison x 31 livraisons = 62,00€				
				if(line.contains(" livraisons =")){										
					sNb=line.split(" livraisons =")[0];
					if(sNb.contains("€/livraison")){
						sNb=sNb.split("€/livraison")[1].trim();
					}
				}

				//Commission €3,00/commande × 67 commandes = €201,00
				else if(line.contains(sep+"commandes"+sep+"=")){
					sNb=line.split(" commandes =")[0];
					if(sNb.contains("commande × ")){
						sNb=sNb.split("commande × ")[1];
					}
					if(sNb.contains("commande x ")){
						sNb=sNb.split("commande x ")[1];
					}

				}                
			}
			if(line.startsWith("total ")){

				sTotal=trimString(line.replace("total", ""));
			}
			if(line.startsWith("pourboires"+sep)){

				sTips=trimString(line.replace("pourboires", ""));
			}            
			if(line.startsWith("ajustements"+sep)){

				sPrime=trimString(line.replace("ajustements", ""));
			} 
		}  
		if(sDuree.equals("")||sNb.equals("")||sTotal.equals("")) {
			if(total<0.01) {
				GUI.messageConsole("facture avec chiffre d'affaire nul"); 
			}
			else if(Math.abs(total-aDeduire)<0.01){
				GUI.messageConsole("facture sans shift avec seulement la remise de la caution"); 
			}
			else if(total-aDeduire>0){
				GUI.messageConsole("facture sans shift mais avec revenu: création d'un shift");
				float duree=1;
				GregorianCalendar cDebut=moyenneDate();
				ShiftImpl shift=new ShiftImpl();
				shift.setRevenue(0);
				shift.setCalendars(cDebut,duree);				
				shift.setTips(tips);
				shift.setNb(0);
				shift.setPrime(total-aDeduire);
				shift.setArtificiel();
				facture.addShift(shift);
			}

			else {
				GUI.messageConsole("erreur import");
			}

		}
		else{
			GUI.messageConsole("import ancienne Facture");
			int nb=Integer.parseInt(sNb);
			float total=Float.parseFloat(sTotal);

			float duree=Float.parseFloat(sDuree);
			float prime=0;
			if(!sPrime.equals("")) prime=Float.parseFloat(sPrime);
			if(!sTips.equals("")) tips=Float.parseFloat(sTips);

			GregorianCalendar cDebut=moyenneDate();


			ShiftImpl shift=new ShiftImpl();

			shift.setCalendars(cDebut,duree);
			shift.setArtificiel();

			shift.setNb(nb);
			shift.setPrime(prime);
			shift.setRevenue(total-tips-prime);
			shift.setTips(tips);	    
			facture.addShift(shift);    
		}
	}

	private void importShift(String line){
		while(line.contains("  ")) {
			line=line.replaceAll("  "," ");
		}

		String words[] = line.split(" ");

		if(words.length>8) {

			String date="";                    
			String heureDebut="11:30";                    
			String heureFin="19:00";
			String sNb="";
			String sCA="";
			String sCA2="";



			date=words[0]+" "+words[1]+" "+words[2]+" "+words[3];        
			if(words[4].contains(":")) heureDebut=words[4];
			if(words[5].contains(":")) heureFin=words[5];      


			if(words[7].equals("×")){

				//Jeudi 08 septembre 2016 14:35 22:31 8.0 × (7.5) 11 × (3.0) €93,00
				if(words.length>10&&words[10].equals("×")){
					sNb=words[9];
				}
				//Samedi 10 juin 2017 19:00 23:19 15 × (5.0) €75,00
				else{
					sNb=words[6];
				}

			}
			//Vendredi 26 Janvier 2018 18:09 23:07 5.0h 17: €85.00 €85.00
			else if(words[7].endsWith(":")){
				sNb=words[7].replace(":", "");
				if(words[8].startsWith("€")){
					sCA2=words[8].replace("€", "");
				}
			}
			else{
				GUI.messageConsole("import shift "+"words[7]: "+words[7]);


			}
			if(words[words.length-1].startsWith("€")) sCA=words[words.length-1];

			ShiftImpl shift=parseShift(date,heureDebut,heureFin,sNb,sCA,sCA2);

			facture.addShift(shift);
		}
	}

	private ShiftImpl parseShift(String date,String heureDebut,String heureFin, String stringNb,String stringCA,String stringCA2){

		Calendar cFin = new GregorianCalendar(localeFacture);
		Calendar cDebut = new GregorianCalendar(localeFacture);
		double nbCommande=0;
		double revenue=0;	
		String s1=date+" "+heureDebut.replace(":", " ");
		String s2=date+" "+heureFin.replace(":", " ");

		try{

			cDebut.setTime(dateFormatFacture.parse(s1));            
		}
		catch (ParseException e){

			GUI.messageConsole("erreur s1= "+s1+"\n"); 
			e.printStackTrace();
		}
		try{    
			cFin.setTime(dateFormatFacture.parse(s2));  
		}
		catch (ParseException e){ 
			GUI.messageConsole("erreur s2= "+s2+"\n");
			e.printStackTrace();            
		} 

		if(cFin.compareTo(cDebut)<0)  cFin.add(Calendar.HOUR, 24);
		stringNb=stringNb.replace(":", "");        
		nbCommande=Integer.parseInt(stringNb);

		revenue=Float.parseFloat(trimString(stringCA));
		if(!stringCA2.equals("")){	    
			float ca2=Float.parseFloat(trimString(stringCA2)); 
			if(ca2>revenue) revenue=ca2;
		}    
		ShiftImpl shift=new ShiftImpl();		
		shift.setCalendars(cDebut,cFin);		
		shift.setNb(nbCommande);
		shift.setRevenue(revenue);        
		return shift;

	}

	static String trimString (String string){
		return string.replace(currencySign , "").replace(",", ".").replace(currencySign2, "").replace(" ", "").replace(" ", "").trim();
	}

	static boolean startWhith(String line, String[] tab) {

		for(String stringTab:tab){
			if(line.startsWith(stringTab)) return true;
		}
		return false;
	}

	GregorianCalendar moyenneDate(){

		GregorianCalendar res = new GregorianCalendar(localeFacture);	
		long debut=facture.getDateDebut().getTimeInMillis();
		long fin=facture.getDateFin().getTimeInMillis();
		res.setTimeInMillis((long) ((debut+fin)/2));        
		return res;
	}

	void setNameFile(File file) {
		if(facture!=null) {
			facture.setShiftsNameFile(file.getName());
			if(facture.getVille().equals("")) {
				getVille(file);
			}
		}
	}

	private void controleFacture() {
		if(!factureValide()) {			
			facture=null;		
		}
	}

	private boolean factureValide() {

		if(facture.isEmpty()) {
			String s="facture vide: ";
			if(file!=null)  s+=file.getName();
			System.out.println(s);
			return false;
		}
		if(facture.getDateDebut()==null) {

			System.out.println("facture non valide : date null ");
			return false;
		}
		if(facture.getDateFin()==null) {

			return false;
		}
		if(facture.getNameBiker()==null||facture.getNameBiker().isEmpty()) {
			return false;
		}
		if(facture.getAdresse()==null||facture.getAdresse().isEmpty()) {
			return false;		
		}
		return true;
	}

}
