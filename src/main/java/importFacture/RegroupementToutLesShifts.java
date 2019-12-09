package importFacture;

import static importFacture.Value.localeResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegroupementToutLesShifts  extends Regroupement{

	private static RegroupementToutLesShifts instanceNePasRegrouper=new RegroupementToutLesShifts();
	
	RegroupementToutLesShifts(){
		enteteResultDate="Facture"+Value.separateurCSV+
				"Semaine"+Value.separateurCSV+
				"Date"+Value.separateurCSV;
		dateFormatResult=new SimpleDateFormat("EEEE dd/MM/yyyy HH:mm",localeResult);
		textCombo="ne pas regrouper";
		
		separateurEnteteDebut= 
				Value.separateurCSV+
				Value.separateurCSV+
				Value.separateurCSV;
	}
	
	static RegroupementToutLesShifts getInstance() {
		return instanceNePasRegrouper;
	}
	
	
	
	@Override
	Boolean isARegrouper(Shift shift1,Shift shift2) {
		
		return (shift1.getNameFile().equals(shift2.getNameFile()))&&
				Math.abs(shift1.getcDebut().getTimeInMillis()-shift2.getcDebut().getTimeInMillis())<10;
		
	}

	
	@Override
	String getStringRegroupageDate() {
		return shift.getNameFile()+Value.separateurCSV+
				String.valueOf(shift.getcDebut().get(Calendar.WEEK_OF_YEAR))+Value.separateurCSV+
				dateFormatResult.format(shift.getcDebut().getTime())+Value.separateurCSV;
		
	}

	@Override
	String getDateChart() {
		
		return dateFormatResult.format(shift.getcDebut().getTime());
	}

	@Override
	double getDureeChart() {
		
		return shift.getDuree();
	}

	@Override
	String getTitleDureeChart() {
		
		return "duree (h)";
	}
}
