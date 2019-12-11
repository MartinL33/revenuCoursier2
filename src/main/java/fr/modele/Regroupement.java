package fr.modele;

import static fr.modele.Value.formatNumber;
import static fr.algorithmes.Utilitaire.getMultiSeparateur;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public abstract class Regroupement {

	public String textCombo;
	
	Boolean isEclaterShift=false;
	Boolean isParAnnee=false;
	
	String enteteResultDate;
	String enteteResultEnd=
			"Durée (h)"+Value.separateurCSV
			+ "Nombre Commande"+Value.separateurCSV
			+ "Chiffre d'affaire (CA) (€)"+Value.separateurCSV
			+ "Pourboire (€)"+Value.separateurCSV
			+ "Prime (€)"+Value.separateurCSV
			+ "Total (€)"+Value.separateurCSV
			+ "total horaire (€/h)";

	String separateurEnteteEnd=getMultiSeparateur(6);
		
	String separateurEnteteDebut=
			Value.separateurCSV;	

	SimpleDateFormat dateFormatResult;	

	Shift shift=null;
	ShiftImplGPS shiftGPS=null;
	

	void setShift(Shift shift2) {
		this.shift = shift2;
		if(shift2 instanceof ShiftImplGPS) {
			this.shiftGPS = (ShiftImplGPS)shift2 ;
		}
	}

	@Override
	public String toString(){
		return textCombo;
	}      

	String getEnteteResultDate() {
		return enteteResultDate;
	}

	String getEnteteResultEnd(Biker biker) {
		
		if(biker instanceof BikerImpGPS) { 
			if(!Value.debug) {
				return enteteResultEnd+Value.separateurCSV
						+"Distance par heure (km/h)"+Value.separateurCSV
						+"Vitesse (km/h)"+Value.separateurCSV					
						+"Proportion en route (sans unité)"+Value.separateurCSV
						+"CA par km (€/km)";
			}
			else {
				return enteteResultEnd+Value.separateurCSV
						+"distancePendantTracks"+Value.separateurCSV
						+"distanceEntreTrack"+Value.separateurCSV
						+"distanceDomicileAller"+Value.separateurCSV
						+"distanceDomicileRetour"+Value.separateurCSV
						+"dureeArret"+Value.separateurCSV
						+"dureeRoute"+Value.separateurCSV
						+"dureeEntreTrack"+Value.separateurCSV
						+"dureeIniale"+Value.separateurCSV
						+"revenuGPS"+Value.separateurCSV
						+"nbShiftGPS"+Value.separateurCSV
						+"nbCmdGPS"+Value.separateurCSV;				

			}

		}
		return enteteResultEnd;
	}

	abstract String getStringRegroupageDate(); 



	public String getTextCombo() {
		return textCombo;
	}

	public Calendar miseEnformeCDebut(Calendar c) {
		return c;
	}


	long mesureShift(Calendar calendar) {
		return (long) (calendar.getTimeInMillis()/100);
	}

	abstract Boolean isARegrouper(Shift shift2,Shift shift3);

	String lineBikerCA() {

		String res=formatNumber(shift.getDuree())+Value.separateurCSV;
		res+=formatNumber(shift.getNbCommande())+Value.separateurCSV;
		res+=formatNumber(shift.getRevenue())+Value.separateurCSV; 
		res+=formatNumber(shift.getTips())+Value.separateurCSV;
		res+=formatNumber(shift.getPrime())+Value.separateurCSV; 	
		res+=formatNumber((shift.getRevenue()+shift.getPrime()+shift.getTips()))+Value.separateurCSV;	 
		res+=formatNumber((shift.getRevenue()+shift.getPrime()+shift.getTips())/shift.getDuree())+Value.separateurCSV;
		return res;
	}





	String lineBiker() {

		if(shiftGPS!=null)	{
			String res=lineBikerCA();		



			if(shiftGPS.distancePendantTracks!=0) {			


				if(!Value.debug) {
					//distance par heure
					res+=formatNumber(
							(shiftGPS.distancePendantTracks+shiftGPS.distanceEntreTrack)/
							(shiftGPS.dureeArret+shiftGPS.dureeRoute+shiftGPS.dureeEntreTrack)
							)+Value.separateurCSV;	

					//vitesse
					res+=formatNumber(
							(shiftGPS.distancePendantTracks+shiftGPS.distanceEntreTrack)/
							(shiftGPS.dureeRoute+shiftGPS.dureeEntreTrack)
							)+Value.separateurCSV;		

					//proportion route

					res+=formatNumber(
							(shiftGPS.dureeRoute+shiftGPS.dureeEntreTrack)/
							(shiftGPS.dureeRoute+shiftGPS.dureeEntreTrack+shiftGPS.dureeArret)
							)+Value.separateurCSV;						

					//Ca par km

					res+=formatNumber(
							(shiftGPS.revenuGPS)/
							(shiftGPS.distancePendantTracks+shiftGPS.distanceEntreTrack+shiftGPS.distanceDomicileAller+shiftGPS.distanceDomicileRetour)
							)+Value.separateurCSV;	

				}				

				else {				

					for(int i=0;i<shiftGPS.getTabDistance().length;i++) {
						res+=formatNumber(shiftGPS.getTabDistance()[i])+Value.separateurCSV;
					}								

				}
			}
			return res;
		}
		return lineBikerCA();
	}

	//Chart 
	double getCAhoraire() {
		return (shift.getRevenue()+shift.getPrime()+shift.getTips())/shift.getDuree();
	}

	abstract String getDateChart();
	abstract double getDureeChart();
	abstract String getTitleDureeChart();

	boolean isPasserUneLigneFichierResult(Shift shiftTotalPrecedent) {
		return false;
	}

}
