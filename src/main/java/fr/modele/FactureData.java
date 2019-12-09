package fr.modele;

public class FactureData {
	
	
	static  final String[] nomJoursSemaine={"lundi","mardi","mercredi","jeudi","vendredi","samedi","dimanche"};
	static final String[] result={"résumé<","résumé","resumé","resume","résume"};
	static final String pourboires="pourboires";
	static final String pourboire="pourboire";
	static final String total="total";
	static final String currencySign="€";
	static final String currencySign2="e";
	static final String ttc="ttc";
	static final String sep=" ";
	static final String[] ignore={"1facture n°","€"};
	static final String[] totalTab ={total+sep+ttc+sep+currencySign,total+sep+ currencySign,"total ttc"};
	static final String[] caution={"caution","avance d’installation"};
	static String patternDateFormatFacture="EEEE dd MMMM yyyy HH mm";

	
	

}
