package fr.map;

public class MapCouleur {

	static int nbCouleur=16;
	
	
	
	static String getSyle() {
		
		int increment=(int) (0xFF)/(nbCouleur-1);
		StringBuilder res=new StringBuilder();
		for(int i=0;i<nbCouleur-1;i++) {			
			res.append("<Style id=\""
					+ "1f00"
					+formatLettre(Integer.toHexString(i*increment))
					+formatLettre(Integer.toHexString((0xFF)-i*increment))
					+ "\"><LineStyle><color>"
					+ "1f00"
					+formatLettre(Integer.toHexString(i*increment))
					+formatLettre(Integer.toHexString((0xFF)-i*increment))
					+ "</color><width>3</width></LineStyle></Style>"+System.getProperty("line.separator"));
		}
		res.append("<Style id=\""
				+ "1f00ff00"				
				+ "\"><LineStyle><color>"
				+ "1f00ff00"
				+ "</color><width>3</width></LineStyle></Style>"+System.getProperty("line.separator"));
		
		
		
		return res.toString();
	
	}
	
	private static String formatLettre(String s) {
		if (s.length()==2) return s;
		else if( s.length()==1) return "0"+s;
		else return "";
	}
	
	
	static String getStyleFromCA(float CA) {		
		return MapCouleur.getStyle((CA-5)/18);		
	}
	
	static String getStyleFromVitesse(float vitesse) {		
		return MapCouleur.getStyle((vitesse-5)/30);		
	}
	
	
	private static String getStyle(double nb) {
		double incr=1.0/(nbCouleur-1);		
		int increment=(int) (0xFF)/(nbCouleur-1);
		
		for(int i=0;i<nbCouleur-1;i++) {	
			
			if(nb>(1.0-incr*i)) {				
				return "1f00"
						+formatLettre(Integer.toHexString((0xFF)-i*increment))
					+formatLettre(Integer.toHexString(i*increment));
				}
		}		
		return "1f0000ff";   //rouge		
	}
	
}
