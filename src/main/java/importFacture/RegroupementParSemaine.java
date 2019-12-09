package importFacture;

import static importFacture.Value.localeResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegroupementParSemaine extends Regroupement{

	
private static RegroupementParSemaine instanceParSemaine=new RegroupementParSemaine();
	
	RegroupementParSemaine(){
		enteteResultDate="Semaine"+Value.separateurCSV+"Date"+Value.separateurCSV;
		dateFormatResult=new SimpleDateFormat("dd/MM/yyyy",localeResult);
		textCombo="par semaine";
		
		separateurEnteteDebut= 
				Value.separateurCSV+
				Value.separateurCSV;
	}
	public static RegroupementParSemaine getInstance() {
		return instanceParSemaine;
	}

	@Override	
	public Calendar miseEnformeCDebut(Calendar c) {		
		c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);	
		return c;
	}

	
	@Override
	String getStringRegroupageDate() {
		return String.valueOf(shift.getcDebut().get(Calendar.WEEK_OF_YEAR))+Value.separateurCSV+
				dateFormatResult.format(shift.getcDebut().getTime())+Value.separateurCSV;
		
	}

	@Override
	Boolean isARegrouper(Shift shift1,Shift shift2) {
		Calendar calendar1=shift1.getcDebut();
		Calendar calendar2=shift2.getcDebut();
		if(calendar1.get(Calendar.WEEK_OF_YEAR)==1&&calendar2.get(Calendar.WEEK_OF_YEAR)==1) {
			Calendar c1=((Calendar) calendar1.clone());			
			Calendar c2=((Calendar) calendar2.clone());
			c1.add(Calendar.DATE, 10);
			c2.add(Calendar.DATE, 10);
			return c1.get(Calendar.YEAR)==c2.get(Calendar.YEAR);
		}else 
			return calendar1.get(Calendar.WEEK_OF_YEAR)==calendar2.get(Calendar.WEEK_OF_YEAR)&&
				(calendar1.get(Calendar.YEAR)==calendar2.get(Calendar.YEAR));
		
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
