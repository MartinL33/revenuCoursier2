package fr.modele;

import static fr.modele.Value.localeResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegroupementParMois extends Regroupement{

	
	
private static RegroupementParMois instanceParMois=new RegroupementParMois();
	
	RegroupementParMois(){
		enteteResultDate="Mois"+Value.separateurCSV;
		dateFormatResult=new SimpleDateFormat("MM/yyyy",localeResult);
		textCombo="par mois";
	}
	
	public static RegroupementParMois getInstance() {
		return instanceParMois;
	}

	@Override	
	public Calendar miseEnformeCDebut(Calendar c) {		
		c.set(Calendar.DAY_OF_MONTH,1);	
		return c;
	}

	@Override
	Boolean isARegrouper(Shift shift1,Shift shift2) {
		Calendar calendar1=shift1.getcDebut();
		Calendar calendar2=shift2.getcDebut();
		
		 return calendar1.get(Calendar.MONTH)==calendar2.get(Calendar.MONTH)&&
				 (calendar1.get(Calendar.YEAR)==calendar2.get(Calendar.YEAR));
			
	}	

	@Override
	String getStringRegroupageDate() {
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
	
}
