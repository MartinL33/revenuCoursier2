package fr.map;

import static fr.map.ValueMap.PRECISION_DEFAUT;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import fr.gui.GUI;

public class ImportMapFromJSON {
	Map map;
	File ficherJSON=null;

	public ImportMapFromJSON(File ficherJSON) {
		assert ficherJSON != null;
		assert ficherJSON.isFile();
		assert ficherJSON.canRead();

		this.ficherJSON=ficherJSON;	    

	}


	public Map importMap(){
		assert ficherJSON != null;
		Long time1 = System.currentTimeMillis();
		Map mapResult=new Map();
		mapResult.setFile(ficherJSON);

		Track t=new Track(null);

		String messGUI="Importation "+ ficherJSON.getName()+" en cours"+", peut prendre quelques minutes";
		GUI.updateUI(messGUI);	

		boolean lat=false;
		boolean lon=false;
		boolean date=false;
		float lonDeg=0;
		float latDeg=0;
		float precision=PRECISION_DEFAUT;		
		long dateDouble=0;			
		String line;			
		String[] words;

		try{			

			BufferedReader is =new BufferedReader(new FileReader(ficherJSON));			

			while((line = is.readLine()) != null){      

				if(line.startsWith("    \"timestampMs\"")) {
					words = line.split("\" : \"");					
					dateDouble=Long.parseLong(words[1].replace("\",", ""));	
					date=true;
				}
				else if(line.startsWith("    \"latitudeE7\"")) {
					words = line.split(" : ");	
					latDeg=Float.parseFloat(words[1].replaceAll(",", ""))/10000000;
					lat=true;

				}
				else if(line.startsWith("    \"longitudeE7\"")) {
					words = line.split(" : ");	
					lonDeg=Float.parseFloat(words[1].replaceAll(",", ""))/10000000;
					lon=true;
				}
				else if(line.startsWith("    \"accuracy\"")) {
					words = line.split(" : ");	
					precision=(Float.parseFloat(words[1].replaceAll(",", "")));
				}

				else if(line.equals("  }, {")){

					if(lat&&lon&&date){
						Localisation l=new Localisation();
						l.setTime((dateDouble));
						l.setLatitudeDegres(latDeg);
						l.setLongitudeDegres(lonDeg);					
						l.setPrecision(precision);

						t.add(l);
						precision=PRECISION_DEFAUT;
						latDeg=0;
						lat=false;
						lon=false;
						date=false;

					}
				}					
			}

			if(lat&&lon&&date){
				Localisation l=new Localisation();
				l.setTime((dateDouble));
				l.setLatitudeDegres(latDeg);
				l.setLongitudeDegres(lonDeg);					
				l.setPrecision(precision);
				t.add(l);

			}


			is.close();
			t.sortTrack();			
		}
		catch(FileNotFoundException e){
			GUI.messageConsole(e.toString());
		} 
		catch (IOException e) {
			GUI.messageConsole(e.toString());
		} 	

		if(!t.isEmpty()) {
			mapResult.addTrack(t);
		}	

		Long time2 = System.currentTimeMillis();
		System.out.println("time Import "+String.valueOf(((float)(time2-time1))/1000)+" s");

		return mapResult;
	}	
}

