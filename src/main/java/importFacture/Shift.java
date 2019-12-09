package importFacture;

import static importFacture.FactureData.patternDateFormatFacture;
import static importFacture.Value.regroupageSelected;
import static java.lang.Math.abs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import GUI.GUI;
import algorithmes.GroupableAddition;

public abstract class Shift implements GroupableAddition<Shift>{

    /** .
     * date calendar de début du shift
     */
    protected Calendar calendarDebut;
    /** .
     * date calendar de fin du shift
     */
    protected Calendar calendarFin;
    /** .
     * durée du shift en heure decimale
     */
    protected double duree;
    /** .
     * nombre de commande
     */
    protected double nb;
    /** .
     * montant généré par les commandes
     */
    protected double revenue;
    /** .
     * pourboires du shift (estimation)
     */
    protected double tips;
    /** .
     * primes du shift (estimation)
     */
    protected double prime;
    /** .
     * Le shift fait-il référence à une ligne d'une facture ?
     */
    protected Boolean isArtificiel = false;
    /** .
     * nom de fichier de la facture source
     * sert pour afficher la facture dans le regroupementTousLesShifts
     */
    private String nameFile = "";


    public double getPrime() {
	return prime;
    }

    public void setPrime(final double prime) {
	this.prime = prime;
    }

    public Boolean isArtificiel() {
	return isArtificiel;
    }

    public void setArtificiel() {
	this.isArtificiel = true;
    }

    public void setNb(final double nb) {
	this.nb = nb;
    }

    public void setRevenue(final double revenue) {
	this.revenue = revenue;
    }

    public  double getTips(){
	return tips;	
    }

    public void setTips(final double tips){
	this.tips=tips;
    }

    public void setNameFile(final String nameFile){
	this.nameFile=nameFile.replace(".pdf", "");
    }

    public String getNameFile(){
	return nameFile;
    }

    public int getAnnee() {
	return calendarDebut.get(Calendar.YEAR);
    }

    public Calendar getcDebut(){
	return  calendarDebut;
    } 

    public Calendar getcFin(){
	return  calendarFin;
    }

    public double getNbCommande() {
	return nb;
    }

    public double getDuree(){
	return duree;
    }

    public double getRevenue() {
	return revenue;
    } 

    public  abstract Shift clone2();


    public void setCalendars(final Calendar cDebut, final Calendar cFin) {
	this.calendarDebut = cDebut;
	this.calendarFin = cFin;

	int nbMilliSecondInOneHour=3600*1000;
	this.duree = (0.0+calendarFin.getTimeInMillis()- calendarDebut.getTimeInMillis())/nbMilliSecondInOneHour;
	if(duree<0) {
	    GUI.messageConsole("Erreur duree: "+duree);
	}

    }

    public void setCalendars(final Calendar calendarDebut, double duree) {
	this.calendarDebut = calendarDebut;
	this.duree = duree;   	
	int nbMilliSecondInOneHour=60*60*1000;
	this.calendarFin = (Calendar) calendarDebut.clone();
	this.calendarFin.setTimeInMillis(
		(long) (this.calendarDebut.getTimeInMillis() + duree*nbMilliSecondInOneHour));
    }

    public void miseEnforme(Object modeRegroupage){
	assert this!=null;
	assert modeRegroupage instanceof Regroupement; 
	Regroupement regroupementSelected=(Regroupement) modeRegroupage;
	this.calendarDebut = regroupementSelected.miseEnformeCDebut(this.calendarDebut);


    }

    Boolean equals(Shift shift){

	if (this == shift) return true;
	if(shift == null) return false;	
	return (abs(this.revenue-shift.getRevenue())<0.01)
		&&(abs(this.duree-shift.getDuree())<0.1)
		&&this.nb==shift.getNbCommande()
		&&(abs(this.calendarDebut.getTimeInMillis()-shift.getcDebut().getTimeInMillis())<10);
    }

    public Shift addition(Shift shift){

	this.duree+=shift.duree;
	this.revenue+=shift.revenue;
	this.nb+=shift.nb;
	this.tips+=shift.tips;
	this.prime+=shift.prime;    
	return this;
    }

    @Override
    public String toString() {

	String res="";
	SimpleDateFormat dateFormatFacture=new SimpleDateFormat(patternDateFormatFacture,Locale.FRANCE);	
	res+=dateFormatFacture.format(calendarDebut.getTime())+Value.separateurCSV;
	res+=this.getDuree()+Value.separateurCSV;
	res+=this.getNbCommande()+Value.separateurCSV;
	res+=this.getRevenue()+Value.separateurCSV; 
	res+=this.getTips()+Value.separateurCSV;
	res+=this.getPrime()+Value.separateurCSV; 	
	return res;
    }

    @Override
    public int compareTo(Shift shift,Object mode) {	
	assert(mode instanceof Regroupement);

	return (int) (this.mesureShift()-shift.mesureShift());

    }

    protected long mesureShift(){    	
	return regroupageSelected.mesureShift(this.calendarDebut);		
    }

    public boolean isARegouper(Shift shift, Object regroupageSelected){ 	
	assert (regroupageSelected instanceof Regroupement);
	Regroupement regroupage= (Regroupement) regroupageSelected;
	return regroupage.isARegrouper(shift, this);		
    }

    public Shift cloneWithCalendars(Calendar cDebut,Calendar cFin){
	Shift clone=this.clone2();	
	clone.setCalendars((Calendar) cDebut.clone(), (Calendar) cFin.clone());	
	clone.revenue=this.revenue*clone.duree/this.duree;
	clone.nb=this.nb*clone.duree/this.duree;
	clone.tips=this.tips*clone.duree/this.duree;
	clone.prime=this.prime*clone.duree/this.duree;
	return clone;
    }




    @Override
    public ArrayList<Shift> cloneEclate(Object modeSelected) {

	assert(modeSelected instanceof Regroupement);
	Regroupement regroupementSelected= (Regroupement) modeSelected;

	ArrayList<Shift> res= new ArrayList<Shift>();

	if(regroupementSelected.isEclaterShift
		&&!this.isArtificiel){			

	    res.addAll(this.eclaterShift());

	}
	else {
	    Shift s= this.cloneForRegroupage();
	    if(s!=null) res.add(s);	    
	}

	return res;
    }

    protected abstract Shift cloneForRegroupage();

    public ArrayList<Shift> eclaterShift(){
	ArrayList<Shift> res= new ArrayList<Shift>();

	if(this.duree<=1 
		&& (
			this.calendarDebut.get(Calendar.HOUR_OF_DAY)==this.calendarFin.get(Calendar.HOUR_OF_DAY)
			||(this.calendarFin.get(Calendar.MINUTE)==0&&this.calendarFin.get(Calendar.SECOND)==0)
			)) {
	    res.add(this.clone2());
	}


	else if (this.duree<15) {

	    //debut	  
	    Calendar calendarDebut=(Calendar) this.calendarDebut.clone();
	    Calendar calendarFin=(Calendar) this.calendarFin.clone();

	    calendarFin.setTime(this.calendarDebut.getTime());
	    calendarFin.set(Calendar.MINUTE,0);
	    calendarFin.set(Calendar.SECOND,0);
	    calendarFin.add(Calendar.HOUR, 1);
	    Shift shift=this.cloneWithCalendars(calendarDebut, calendarFin);	    
	    res.add(shift);

	    calendarDebut.add(Calendar.HOUR, 1);
	    calendarDebut.set(Calendar.MINUTE,0);
	    calendarDebut.set(Calendar.SECOND,0);

	    calendarFin.add(Calendar.HOUR, 1);

	    while(calendarFin.getTimeInMillis()<(this.calendarFin.getTimeInMillis())){
		shift=this.cloneWithCalendars(calendarDebut, calendarFin);		
		res.add(shift);
		calendarDebut.add(Calendar.HOUR, 1);
		calendarFin.add(Calendar.HOUR, 1);


	    }

	    calendarDebut.setTime(this.calendarFin.getTime());
	    if(this.calendarFin.get(Calendar.MINUTE)==0&&this.calendarFin.get(Calendar.SECOND)==0){
		calendarDebut.add(Calendar.HOUR, -1);
	    }
	    else{
		calendarDebut.set(Calendar.MINUTE,0);
		calendarDebut.set(Calendar.SECOND,0);
	    }
	    if(calendarDebut.getTimeInMillis()<this.calendarFin.getTimeInMillis()){
		shift=this.cloneWithCalendars(calendarDebut, this.calendarFin);		
		res.add(shift);
	    }

	    double ca=0;
	    for(Shift s:res){
		ca+=s.getRevenue();
	    }
	    if(abs(ca-this.getRevenue())>0.001){
		GUI.messageConsole("shift A Faire:\n"+this.toString());
		GUI.messageConsole("res: \n"+res.toString());
	    }

	}
	return res;
    }


}
