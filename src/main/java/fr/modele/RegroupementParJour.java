package fr.modele;

import static fr.modele.Value.localeResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegroupementParJour extends Regroupement {

	private static RegroupementParJour instanceParJour=new RegroupementParJour();
	public static RegroupementParJour getInstance() {
		return instanceParJour;
	}
	
	
	RegroupementParJour(){
		enteteResultDate="Jour"+Value.separateurCSV;
		dateFormatResult=new SimpleDateFormat("dd/MM/yyyy",localeResult);
		textCombo="par jour";
	}
	@Override
	String getStringRegroupageDate() {
		return dateFormatResult.format(shift.getcDebut().getTime())+Value.separateurCSV;
	}

	@Override
	Boolean isARegrouper(Shift shift1, Shift shift2) {
		Calendar calendar1=shift1.getcDebut();
		Calendar calendar2=shift2.getcDebut();
		
		 return calendar1.get(Calendar.DAY_OF_YEAR)==calendar2.get(Calendar.DAY_OF_YEAR)&&
				 (calendar1.get(Calendar.YEAR)==calendar2.get(Calendar.YEAR));
			
	}


	@Override
	String getDateChart() {		
		return dateFormatResult.format(shift.getcDebut().getTime());
	}


	@Override
	double getDureeChart() {		
		return shift.getDuree() ;
	}


	@Override
	String getTitleDureeChart() {
		return "durée (h)";
	}

}
