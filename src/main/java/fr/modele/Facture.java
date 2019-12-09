package fr.modele;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Facture{

	private String nameFile="";

	private String nameVille="";   
	private String nameBiker="";
	private String siren="";
	private String siret="";
	private String adresse="";
	private Calendar dateDebut=null;
	private Calendar dateFin=null;

	private ArrayList<Shift> shifts=new ArrayList<Shift>();



	//langue
	Locale localeFacture;	 
	SimpleDateFormat dateFormatFacture;   

	Facture() {}	

	public String getNameFile() {
		return nameFile;
	}

	Calendar getDateDebut() {
		return dateDebut;
	}

	Calendar getDateFin() {
		return dateFin;
	}

	void setNameVille(String nameVille) {
		this.nameVille = nameVille;
	}

	public Boolean isEmpty() {
		return shifts.isEmpty();
	}

	void setDateDebut(Calendar dateDebut) {
		this.dateDebut = dateDebut;
	}

	void setDateFin(Calendar dateFin) {
		this.dateFin = dateFin;
	}


	public ArrayList<Shift> getShifts(){
		return shifts;
	}

	public String getNameBiker() {
		return nameBiker;
	}

	String getNameRegroupage(String type) {
		if(type==Value.typeParVille) return nameVille;
		else return nameBiker;
	}

	public void setNameBiker(String nameBiker) {
		this.nameBiker = nameBiker;
	}

	public String getAdresse() {
		return adresse;
	}

	Boolean equal(Facture f){
		return this.nameBiker.equals(f.nameBiker)&&
				this.siren.equals(f.siren)&&
				this.siret.equals(f.siret)&&
				this.dateDebut.equals(f.dateDebut)&&
				this.shifts.size()==f.shifts.size();
	}

	void addShift(Shift shift) {
		shifts.add(shift);
	}

	void setSiren(String siren) {
		this.siren = siren;
	}

	public String getVille() {
		return nameVille;
	}

	void setSiret(String siret) {
		this.siret = siret;
	}

	void setAdresse(String adresse) {
		this.adresse = adresse;
	}		

	void setShiftsNameFile(String nameFile){
		if(!nameFile.isEmpty()&&!nameFile.equals("")){
			for(Shift shift:shifts){
				shift.setNameFile(nameFile);	
			}
			this.nameFile=nameFile;
		}

	}

	public String toString() {
		String res="Facture:\n";

		DateFormat dfDTFF=DateFormat.getDateInstance(DateFormat.LONG,Locale.getDefault());
		res+="facture pour la période: "+dfDTFF.format(dateDebut.getTime())+" - "+
				dfDTFF.format(dateFin.getTime())+		"\n";


		if(!siren.isEmpty()) res+="n° siren : "+siren+"\n";		
		if(!siret.isEmpty()) res+="n° siret : "+siret+"\n";	
		res+="nom : "+nameBiker+"\n";
		res+="adresse : "+adresse+"\n";	
		res+="ville : "+nameVille+"\n";	

		for(Shift shift:shifts){
			res+=shift.toString()+";\n";
		}
		return res;
	}

	void putShiftsTips(double tips){
		if(tips>0){
			double nbCmdTotal=0;
			for(Shift shift:shifts){
				nbCmdTotal=nbCmdTotal+shift.getNbCommande();	
			}
			if(nbCmdTotal>0){
				for(Shift shift:shifts){
					shift.setTips(tips*shift.getNbCommande()/nbCmdTotal);
				}
			}
		}
	}

	void putShiftsPrime(double total,double tips,double aDeduire){

		if(total>0){			
			double dureeTotal=0;
			double ca=0;

			for(Shift shift:shifts){
				dureeTotal=dureeTotal+shift.getDuree();	
				ca=ca+shift.getRevenue();				
			}
			double prime=total-ca-tips-aDeduire;
			if(dureeTotal>0){
				for(Shift shift:shifts){
					shift.setPrime(((prime)*shift.getDuree()/dureeTotal));
				}
			}
		}
	}


}
