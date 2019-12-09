package fr.modele;



public class RegroupementParHeureEtAnnee extends RegroupementParHeure {

	
private static RegroupementParHeureEtAnnee instanceParHeureEtAnnee=new RegroupementParHeureEtAnnee();
	
	RegroupementParHeureEtAnnee(){
		super();
		isParAnnee=true;
		textCombo="par heure et année";
	}
	
	public static RegroupementParHeureEtAnnee getInstance() {
		return instanceParHeureEtAnnee;
	}
	
}
