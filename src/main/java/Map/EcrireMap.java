package Map;

import static algorithmes.Utilitaire.isContenuDansTableau;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import GUI.GUI;

public class EcrireMap {
	public  static final String[] couleurMap= {"CA/heure","Vitesse"};   
	public static final String[] formatCarte= {"kml","gpx","json"};    

	Map map = null;
	File dossier = null;  
	String format = formatCarte[0];
	String couleur = couleurMap[1];    
	String nameMap="carte";
	long tailleMaxCarteOctet=5000000;
	Boolean limiteTaille=true;

	Boolean isSupprimerShiftSansGPSfiable = true;

	public void setMap(Map map) {
		this.map = map;
	}
	
	public void setTailleLimite(long tailleEnOctet) {
		this.tailleMaxCarteOctet=tailleEnOctet;
	}

	public void setIsSupprimerShiftSansGPSfiable(Boolean isSupprimerShiftSansGPSfiable) {
		this.isSupprimerShiftSansGPSfiable=isSupprimerShiftSansGPSfiable;
	}

	public void setDossier(File dossier) {
		assert dossier != null;
		assert dossier.isDirectory();
		assert dossier.canWrite();
		this.dossier = dossier;
	}

	public void setFormat(String format) {
		assert format != null;
		String formatCompare=format.toLowerCase().replace(".", "");
		assert isContenuDansTableau(formatCompare,formatCarte); 	
		this.format = formatCompare;
	}

	public void setNameMap(String name) {
		assert name != null;
		this.nameMap=name;
	}

	public void setCouleur(String couleur) {
		assert couleur != null;
		assert isContenuDansTableau(couleur,couleurMap); 	
		this.couleur = couleur.toLowerCase();
	}    

	

	public void ecrireCarte(){
		assert map != null;	
		assert  format != null;
		assert  dossier != null;  
		assert  couleur != null;  
		Long time1 = System.currentTimeMillis();
		String messGUI="Ecriture Map en cours en format " + format+", couleur en fonction de "+couleur; 
		GUI.updateUI(messGUI);	

		if(format.equals("gpx")) {
			ecrireGPX();
		}
		else if(format.equals("kml")) {			
			ecrireKMLLineString();
		}
		else {
			ecrireJson();
		}
		Long time2 = System.currentTimeMillis();
		System.out.println("time Ecriture Map "+String.valueOf(((float)(time2-time1))/1000)+" s");
	}
		
	private void ecrireJson() {
		String mess;		
		int tailleOctet=0;

		try {  
			int nbfichier=1;
			BufferedWriter bw=new BufferedWriter(new FileWriter(dossier.getAbsoluteFile()+File.separator+nameMap+".json"));  

			mess="{";     
			bw.write(mess);
			bw.newLine();	
			
			mess="  \"locations\" : [ ";     
			bw.write(mess);			
			
			tailleOctet+=21;

			for(Track track:map.getTracks()) {

				tailleOctet+=49*track.size()+61;

				if(limiteTaille&&tailleOctet>=tailleMaxCarteOctet){
					nbfichier++;
					mess="  ]";     
					bw.write(mess);
					bw.newLine();
					mess="}";     
					bw.write(mess);
					bw.newLine();					
					
					bw.close();
					bw=new BufferedWriter(new FileWriter(dossier.getAbsoluteFile()+File.separator+"carte"+nbfichier+".json"));
					mess="{";     
					bw.write(mess);
					bw.newLine();						
					mess="  \"locations\" : [ {";     
					bw.write(mess);
					bw.newLine();						
					tailleOctet=21;	
				}

				mess=track.json();
				bw.write(mess);				
				
			}

			mess="  ]";     
			bw.write(mess);
			bw.newLine();
			mess="}";     
			bw.write(mess);
			bw.newLine();	
			bw.close();   

		}
		catch(IOException ex){
			GUI.messageConsole(ex.toString());
		}
	}
		
		

	private void ecrireGPX() {

		String mess;		
		int tailleOctet=0;

		try {  
			int nbfichier=1;
			BufferedWriter bw=new BufferedWriter(new FileWriter(dossier.getAbsoluteFile()+File.separator+nameMap+".gpx"));  

			mess="<?xml version=\"1.0\" ?>" + 
					"<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
					+ "xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\" version=\"1.1\" creator=\"VisuGPX\">";     
			bw.write(mess);
			bw.newLine();	

			tailleOctet+=253;

			for(Track track:map.getTracks()) {

				tailleOctet+=49*track.size()+61;

				if(limiteTaille && tailleOctet>=tailleMaxCarteOctet){
					nbfichier++;
					mess="</Folder></Document></kml>";     
					bw.write(mess);
					bw.newLine();
					bw.close();
					bw=new BufferedWriter(new FileWriter(dossier.getAbsoluteFile()+File.separator+"carte"+nbfichier+".gpx"));
					mess="<?xml version=\"1.0\" ?>" + 
							"<gpx xmlns=\"http://www.topografix.com/GPX/1/1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
							+ "xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\" version=\"1.1\" creator=\"VisuGPX\">";     
					bw.write(mess);
					bw.newLine();	

					tailleOctet=1638;
					tailleOctet+=49*track.size()+61+253;
				}

				mess=track.GPXString();
				bw.write(mess);					
				bw.newLine();
			}

			mess="</gpx> ";     
			bw.write(mess);
			bw.newLine();
			bw.close();   

		}
		catch(IOException ex){
			GUI.messageConsole(ex.toString());
		}
	}

	private void ecrireKMLLineString(){

		String mess;
		String folderName="";
		int tailleOctet=0;

		try {  
			int nbfichier=1;
			BufferedWriter bw=new BufferedWriter(new FileWriter(dossier.getAbsoluteFile()+File.separator+nameMap+nbfichier+".kml"));  

			mess="<?xml version=\"1.0\" encoding=\"UTF-8\"?><kml xmlns=\"http://www.opengis.net/kml/2.2\"><Document><name>Carte shift Deliveroo</name><description/>";     
			bw.write(mess);
			bw.newLine();

			bw.write(MapCouleur.getSyle());
			bw.newLine();

			folderName=map.getFirstTrack().getTitleCalqueMap();

			mess="<Folder><name>"+folderName+"</name>"; 				
			bw.write(mess);
			bw.newLine();

			tailleOctet+=1638;

			for(Track track:map.getTracks()) {

				if(!track.getTitleCalqueMap().equals(folderName)) {
					folderName=track.getTitleCalqueMap();
					mess="</Folder><Folder><name>"+folderName+"</name>"; 	
					bw.write(mess);
					bw.newLine();
					tailleOctet+=38;
				}

				mess=track.KMLLineString(couleur,isSupprimerShiftSansGPSfiable);
				tailleOctet+=track.getTailleMapOctet();						

				if(limiteTaille&&tailleOctet>=tailleMaxCarteOctet){
					nbfichier++;
					mess="</Folder></Document></kml>";     
					bw.write(mess);
					bw.newLine();
					bw.close();
					bw=new BufferedWriter(new FileWriter(dossier.getAbsoluteFile()+File.separator+nameMap+nbfichier+".kml"));
					mess="<?xml version=\"1.0\" encoding=\"UTF-8\"?><kml xmlns=\"http://www.opengis.net/kml/2.2\"><Document><name>Carte shift Deliveroo</name><description/>";     
					bw.write(mess);
					bw.newLine();
					bw.write(MapCouleur.getSyle());
					bw.newLine();
					folderName=track.getTitleCalqueMap();
					mess="<Folder><name>"+folderName+"</name>"; 				
					bw.write(mess);
					bw.newLine();   
					tailleOctet=1638;		
					mess=track.KMLLineString(couleur,isSupprimerShiftSansGPSfiable);
					tailleOctet+=track.getTailleMapOctet();		
				}			



				bw.write(mess);					
				bw.newLine();
			}

			mess="</Folder></Document></kml>";     
			bw.write(mess);			
			bw.close();   

		}
		catch(IOException ex){
			GUI.messageConsole(ex.toString());
		}
	}


}
