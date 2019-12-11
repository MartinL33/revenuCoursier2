package fr.modele;

import static fr.map.ValueMap.nameFileLocaDomicile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import fr.gui.GUI;
import fr.map.Localisation;
import fr.map.ValueMap;

public class LocaDomicileFile {

	ArrayList<LocaDomicileBiker> listLocaDomicileBiker=new ArrayList<LocaDomicileBiker>();


	LocaDomicileFile(File f){
		importFileLocaDomicile(f);
	}




	private void importFileLocaDomicile(File f) {

		if(f.getName().equals(nameFileLocaDomicile)) {

			String messGUI="Importation "+nameFileLocaDomicile+" en cours";
			GUI.updateUI(messGUI);	

			String line;
			try{	
				BufferedReader is =new BufferedReader(new FileReader(f));			

				while((line = is.readLine()) != null){ 

					try {
						if(!line.startsWith("*")) importLine(line);
					}
					catch(Exception e) {
						GUI.messageConsole(e.toString());
					}
				}	

				is.close();
			}
			catch(FileNotFoundException e){
				GUI.messageConsole(e.toString());
			} 
			catch (IOException e) {
				GUI.messageConsole(e.toString());
			} 


		}
	}

	void importLine(String line) throws Exception {
		if(!line.contains(":")) throw new Exception("Line don't contain \":\" ");
		if(line.split(":").length!=2) throw new Exception("line.split(\":\").length!=2");
		String name=line.split(":")[0].trim();

		String sCoord=line.split(":")[1];
		if(!sCoord.contains(",")) throw new Exception("Coordonne don't contain \",\" ");
		if(sCoord.split(",").length<2) throw new Exception("line.split(\",\").length!=2");
		if(!sCoord.split(",")[0].contains(".")) throw new Exception("latitude don't contain \".\" ");
		if(!sCoord.split(",")[1].contains(".")) throw new Exception("longitude don't contain \".\" ");

		double lat=Double.parseDouble(sCoord.split(",")[0].trim());
		double lon=Double.parseDouble(sCoord.split(",")[1].trim());

		LocaDomicileBiker o= new LocaDomicileBiker();

		Localisation l=new Localisation();
		l.setLatitude((float) Math.toRadians(lat)); 
		l.setLongitude((float)Math.toRadians(lon));

		o.setLocaDomicile(l);
		o.setNameBiker(name);
		listLocaDomicileBiker.add(o);

		GUI.updateUI("import position domicile biker "+name+" terminé");
	}

	Localisation getLocaDomicileFromName(String nameBiker) {
		Localisation res=getLoca(nameBiker);
		if(res!=null) return res;
		GUI.updateUI("adresse domicile manquante pour le biker "+nameBiker);
		//TODO add Dans le fichier
		return null;
	}

	private Localisation getLoca(String nameBiker) {
		for(LocaDomicileBiker o:listLocaDomicileBiker) {
			if(o.getNameBiker().equals(nameBiker)) return o.getLocaDomicile();
		}		
		return null;
	}

	static void genereLocaDomicileFileVide(File fileExport,String[] namesBiker) {
		if(fileExport==null) return ;

		String pathFileToWrite=fileExport.getPath()+File.separator+ValueMap.nameFileLocaDomicile;

		String mess="Ecriture exemple fichier"+ValueMap.nameFileLocaDomicile;		
		GUI.updateUI(mess);

		try {
			BufferedWriter bw=new BufferedWriter(new FileWriter(pathFileToWrite));

			String line= "* Veuillez indiquer la position GPS du domicile du ou des bikers";			
			bw.write(line);
			bw.newLine();

			line= "* nom Biker:latitude,longitude,altitude";			
			bw.write(line);
			bw.newLine();
			line= "martin lepers:44.82138,-0.59319,0 ";			
			bw.write(line);
			bw.newLine();
			if(!namesBiker[0].equals("martin lepers")) {

				for(String name:namesBiker) {
					line= name+":";			
					bw.write(line);
					bw.newLine();
				}
			}

			bw.close();
			//Boîte du message d'information
			if(namesBiker.length==1) {
				GUI.updateUI("adresse domicile manquante pour le biker "+namesBiker[0]);
			}
			else if(namesBiker.length>1) {
				GUI.updateUI("adresse domicile manquante pour les bikers ");				
			}
			JOptionPane.showMessageDialog(null, "Veuillez indiquer la position de votre domicile dans le ficher "+ValueMap.nameFileLocaDomicile+" pour calculer les distances de trajet domicile-shift puis veillez relancer le programme", "Information", JOptionPane.INFORMATION_MESSAGE);


		} catch (IOException e) {			
			GUI.messageConsole(e.toString());
		} 


	}

	class LocaDomicileBiker{

		Localisation locaDomicile=null;
		String nameBiker="";

		LocaDomicileBiker() {			
		}

		Localisation getLocaDomicile() {
			return locaDomicile;
		}

		void setLocaDomicile(Localisation locaDomicile) {
			this.locaDomicile = locaDomicile;
		}

		String getNameBiker() {
			return nameBiker;
		}

		void setNameBiker(String nameBiker) {
			this.nameBiker = nameBiker;
		}
	}

}
