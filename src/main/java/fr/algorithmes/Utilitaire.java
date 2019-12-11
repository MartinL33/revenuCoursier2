package fr.algorithmes;

import fr.modele.Value;

public class Utilitaire {
	
	
	public static boolean isContenuDansTableau(String stringAtester,String[] tabString) {	
		assert tabString != null;
		for(String s:tabString) {
			if(stringAtester.equalsIgnoreCase(s)) {
				return true;				
			}
		}	
		return false;
	}
	
	public static String getMultiSeparateur(int count) {
		StringBuilder res=new StringBuilder();
		for(int i=0;i<count;i++) {
			res.append(Value.separateurCSV);
		}
		return res.toString();
	}
	
	public static String formatDouble(double doubleAformate) {
		double res=(((int) (doubleAformate*100))+0.0)/100;
		return String.valueOf(res);
	}
}
