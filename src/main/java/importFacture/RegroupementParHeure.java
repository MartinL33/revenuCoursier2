package importFacture;

import static importFacture.Value.localeResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegroupementParHeure extends Regroupement {

	
	private static RegroupementParHeure instanceParHeure=new RegroupementParHeure();
	
	RegroupementParHeure(){
		isEclaterShift=true;
		enteteResultDate="Shift"+Value.separateurCSV;
		dateFormatResult=new SimpleDateFormat("EEEE HH:mm",localeResult);
		textCombo="par heure";
		enteteResultEnd=
				"Durée (h)"+Value.separateurCSV
				+ "Nombre Commande"+Value.separateurCSV
				//par d'info sur les pourboires avec le recoupage par heure
				+ "Chiffre d'affaire-CA (€)"+Value.separateurCSV
				+ "CA horaire (€/h) sans prime, sans pourboire";
		separateurEnteteEnd=Value.separateurCSV
				+Value.separateurCSV				
				+Value.separateurCSV;
	}
	
	public static RegroupementParHeure getInstance() {
		return instanceParHeure;
	}
	
	@Override
	long mesureShift(Calendar calendar) {
		int jour=calendar.get(Calendar.DAY_OF_WEEK);
	    // debut semaine=lundi 
	    if(jour==Calendar.SUNDAY) jour=Calendar.SATURDAY+1; 
	    return calendar.get(Calendar.HOUR_OF_DAY)+24*jour;
		
	}	

	@Override
	Boolean isARegrouper(Shift shift1,Shift shift2) {
		
		Calendar calendar1=shift1.getcDebut();
		Calendar calendar2=shift2.getcDebut();
		
		return calendar1.get(Calendar.HOUR_OF_DAY)==calendar2.get(Calendar.HOUR_OF_DAY)&&
				(calendar1.get(Calendar.DAY_OF_WEEK)==calendar2.get(Calendar.DAY_OF_WEEK));
	}

	@Override //pas d'information sur la répartion des pourboires sur le regroupage par heure
	String lineBikerCA() {
		 
		 String res=Value.formatNumber(shift.getDuree())+Value.separateurCSV
				 +Value.formatNumber(shift.getNbCommande())+Value.separateurCSV			 
	     		+Value.formatNumber(shift.getRevenue())+Value.separateurCSV
	     		+Value.formatNumber(shift.getRevenue()/shift.getDuree())+Value.separateurCSV; 
	     return res;
	}
	
	@Override	
	public Calendar miseEnformeCDebut(Calendar c) {
		c.set(Calendar.MINUTE,0);
		c.set(Calendar.SECOND,0);		
		return c;
	}
	
	@Override
	String getStringRegroupageDate()  {		
		return dateFormatResult.format(shift.getcDebut().getTime())+Value.separateurCSV;
		
	}

	@Override
	String getDateChart() {
		
		return dateFormatResult.format(shift.getcDebut().getTime());
	}

	@Override
	double getDureeChart() {
		
		return shift.getDuree()/35;
	}

	@Override
	String getTitleDureeChart() {
		
		return "durée en semaine de 35h";
	}
	@Override
	double getCAhoraire() {
		return (shift.getRevenue())/shift.getDuree();
	}
	@Override
	boolean isPasserUneLigneFichierResult(Shift shiftPrecedent) {
		if(shiftPrecedent==null) return false;
		
		if(shift.getcDebut().get(Calendar.HOUR_OF_DAY)==0 &&
				shiftPrecedent.getcDebut().get(Calendar.HOUR_OF_DAY)==23) {
			return false;
		}
		
		
		if(Math.abs(
				shift.getcDebut().get(Calendar.HOUR_OF_DAY)
				-shiftPrecedent.getcDebut().get(Calendar.HOUR_OF_DAY)
				)>1){
			return true;
		}
		
		
		return false;
	}
	
}
