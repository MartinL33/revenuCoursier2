package algorithmes;

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
}
