package importFacture;

import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Map.Localisation;
import Map.Track;
import Map.ValueMap;

public class ShiftImplGPS extends ShiftImpl implements Cloneable {

    /** .
     * represente l'ensemble des tracks qui comportent les positions GPS
     */
    private List<Track> listeTrack = new ArrayList<>();    

    /** .
     * 
     */

    double distancePendantTracks;
    /** .
     * 
     */
    double distanceEntreTrack;
    /** .
     * 
     */
    double distanceDomicileAller;
    /** .
     * 
     */
    double distanceDomicileRetour;
    /** .
     * 
     */
    double dureeArret;
    /** .
     * 
     */
    double dureeRoute;
    /** .
     * 
     */
    double dureeEntreTrack;
    /** .
     * 
     */
    double dureeIniale;
    /** .
     * 
     */
    double revenuGPS;
    /** .
     * 
     */
    double nbShiftGPS;
    /** .
     * 
     */
    double nbCmdGPS;

    public void addtrack(Track track) {
	listeTrack.add(track);
    }

    @Override
    public Shift clone2() {	
	return clone();
    }

    @Override
    protected Shift cloneForRegroupage() {	
	if(Value.isSupprimerShiftSansGPSfiable
		&& this.hasGPSfiable()) {
	    return clone();
	}
	return null;
    }

    @Override
    public Shift clone() {	  

	ShiftImplGPS clone=createFromShift(this);
	for(Track t:this.listeTrack) {
	    clone.listeTrack.add(t.clone());
	}
	clone.distanceDomicileAller=this.distanceDomicileAller;
	clone.distanceDomicileRetour=this.distanceDomicileRetour;
	clone.distanceEntreTrack=this.distanceEntreTrack;
	clone.distancePendantTracks=this.distancePendantTracks;
	clone.dureeArret=this.dureeArret;
	clone.dureeEntreTrack=this.dureeEntreTrack;
	clone.dureeIniale=this.dureeIniale;
	clone.dureeRoute=this.dureeRoute;
	clone.nbCmdGPS=this.nbCmdGPS;
	clone.nbShiftGPS=this.nbShiftGPS;
	return clone;
    }   

    static ShiftImplGPS createFromShift(Shift s) {
	if(s instanceof ShiftImpl) {
	    ShiftImplGPS res=new ShiftImplGPS();
	    res.calendarDebut=(Calendar) s.calendarDebut.clone();	
	    res.calendarFin=(Calendar)s.calendarFin.clone();	
	    res.duree=s.duree;
	    res.nb=s.nb;
	    res.prime=s.prime;
	    res.revenue=s.revenue;
	    res.tips=s.tips;

	    return res;
	}
	if(s instanceof ShiftImplGPS) {
	    return (ShiftImplGPS) s;
	}
	return null;


    }

    @Override
    public String toString() {

	String res=super.toString();
	if(distancePendantTracks!=0) {
	    for(int i=0;i<this.getTabDistance().length;i++) {
		res+=this.getTabDistance()[i]+Value.separateurCSV;
	    }
	}
	return res;
    }

    @Override
    public Shift addition(Shift shift){

	super.addition(shift);
	if(shift instanceof ShiftImplGPS){
	    ShiftImplGPS shiftImplGPS = (ShiftImplGPS) shift;

	    if(shiftImplGPS.distancePendantTracks>0) {
		if(this.distancePendantTracks>0) {				
		    this.distancePendantTracks+=shiftImplGPS.distancePendantTracks;
		    this.distanceEntreTrack+=shiftImplGPS.distanceEntreTrack;
		    this.distanceDomicileAller+=shiftImplGPS.distanceDomicileAller;
		    this.distanceDomicileRetour+=shiftImplGPS.distanceDomicileRetour;
		    this.dureeArret+=shiftImplGPS.dureeArret;
		    this.dureeRoute+=shiftImplGPS.dureeRoute;
		    this.dureeEntreTrack+=shiftImplGPS.dureeEntreTrack;
		    this.dureeIniale+=shiftImplGPS.dureeIniale;
		    this.revenuGPS+=shiftImplGPS.revenuGPS;
		    this.nbShiftGPS+=shiftImplGPS.nbShiftGPS;
		    this.nbCmdGPS+=shiftImplGPS.nbCmdGPS;
		}
		else {				
		    this.distancePendantTracks=shiftImplGPS.distancePendantTracks;
		    this.distanceEntreTrack=shiftImplGPS.distanceEntreTrack;
		    this.distanceDomicileAller=shiftImplGPS.distanceDomicileAller;
		    this.distanceDomicileRetour=shiftImplGPS.distanceDomicileRetour;
		    this.dureeArret=shiftImplGPS.dureeArret;
		    this.dureeRoute=shiftImplGPS.dureeRoute;
		    this.dureeEntreTrack=shiftImplGPS.dureeEntreTrack;
		    this.dureeIniale=shiftImplGPS.dureeIniale;
		    this.revenuGPS=shiftImplGPS.revenuGPS;
		    this.nbShiftGPS=shiftImplGPS.nbShiftGPS;
		    this.nbCmdGPS=shiftImplGPS.nbCmdGPS;

		}
	    }
	}
	return this;
    }


    @Override
    public Shift cloneWithCalendars(Calendar cDebut,Calendar cFin){


	ShiftImplGPS res=ShiftImplGPS.createFromShift(super.cloneWithCalendars(cDebut, cFin));

	ArrayList<Track> reslisteTrack=new ArrayList<Track>();

	for(Track t:res.listeTrack) {
	    for(Localisation l:t.getList()) {
		/*
		// TODO 	affecter localisations dans les tracks au clone
		if(false){
		    if(reslisteTrack.isEmpty()){
		    }
		    reslisteTrack.add();
		}
		 */
	    }

	}
	res.listeTrack=reslisteTrack;
	res.calculDistance();	
	res.distanceDomicileAller=0;
	res.distanceDomicileRetour=0;
	return res;
    }





    List<Track> getListeTrack() {
	return listeTrack;
    }

    public Boolean hasGPSfiable() {
	if (listeTrack.isEmpty()||distancePendantTracks==0) return false;

	double dureeTot=dureeArret+dureeRoute+dureeEntreTrack;

	//duree GPS trop courte: shift tronqué au début ou a la fin
	if(abs((duree-dureeTot)/duree)>0.2) return false;

	//pourcentage dureeEntreTrack/duree 
	if((dureeEntreTrack/dureeTot)>0.2) return false;

	//pourcentage distanceEntreTrack/distance
	double distanceTot=distancePendantTracks+distanceEntreTrack;
	if((distanceEntreTrack/distanceTot)>0.2) return false;

	return true;

    }

    void resetGPS() {

	listeTrack=new ArrayList<>();
	this.distancePendantTracks=0;
	this.distanceEntreTrack=0;
	this.distanceDomicileAller=0;
	this.distanceDomicileRetour=0;
	this.dureeArret=0;
	this.dureeRoute=0;
	this.dureeEntreTrack=0;
	this.dureeIniale=0;
	this.revenuGPS=0;
	this.nbShiftGPS=0;
	this.nbCmdGPS=0;	
    }

    void calculDistance() {
	if(!listeTrack.isEmpty()) {

	    Localisation localisationPrecedente;

	    for(Track t:listeTrack) {

		localisationPrecedente=t.getList().get(0);
		if(localisationPrecedente.getIndication()==ValueMap.IND_ARRET_INCONNU) dureeArret+=localisationPrecedente.getDuree();
		else dureeRoute+=localisationPrecedente.getDuree();



		for(int i=1; i<t.getList().size();i++) {
		    Localisation l=t.getList().get(i);
		    distancePendantTracks+=Math.sqrt(ValueMap.distance2(localisationPrecedente, l));
		    if(l.getIndication()==ValueMap.IND_ARRET_INCONNU) dureeArret+=l.getDuree();
		    else dureeRoute+=l.getDuree();
		    localisationPrecedente=l;    			
		}
	    }

	    if(listeTrack.size()>1) {
		for(int index=0;index<listeTrack.size()-1;index++) {
		    dureeEntreTrack+=Track.getDureeEntreTrack(listeTrack.get(index),listeTrack.get(index+1));
		    distanceEntreTrack+=Track.getDistanceEntreTrack(listeTrack.get(index),listeTrack.get(index+1));
		}
	    }   	

	    dureeIniale=listeTrack.get(0).getList().get(0).getDuree();

	    //conversion en Km et en Heure	  

	    distancePendantTracks=distancePendantTracks/1000;
	    distanceEntreTrack=distanceEntreTrack/1000;

	    dureeRoute=dureeRoute/(60*60*1000);
	    dureeArret=dureeArret/(60*60*1000);
	    dureeEntreTrack=dureeEntreTrack/(60*60*1000);
	    dureeIniale=dureeIniale/(60*60*1000);	

	    revenuGPS=revenue+prime+tips;
	    nbShiftGPS=1;
	    nbCmdGPS=nb;	




	}
    }

    void calulDistanceDomicile(Localisation locaDomicile) {

	if(!this.isArtificiel()&&locaDomicile!=null&&!listeTrack.isEmpty()) {

	    distanceDomicileAller=Math.sqrt(ValueMap.distance2(locaDomicile, listeTrack.get(0).getFirstLoca()));
	    distanceDomicileRetour=Math.sqrt(ValueMap.distance2(locaDomicile, listeTrack.get(listeTrack.size()-1).getLastLoca()));
	    distanceDomicileAller=distanceDomicileAller/1000;
	    distanceDomicileRetour=distanceDomicileRetour/1000;
	}
	else {
	    distanceDomicileAller=0;
	    distanceDomicileRetour=0;
	}

    }


    void setDebutEtFinLocalisation() {

	if(!listeTrack.isEmpty()&&listeTrack.get(0).size()>2) {
	    for( Track t:listeTrack) {
		for( Localisation l:t.getList()) {

		    if(l.getTime()>calendarDebut.getTimeInMillis()) {
			l.setTime(calendarDebut.getTimeInMillis());
		    }

		    if(l.getTime()<calendarFin.getTimeInMillis()) {
			l.setTime(calendarFin.getTimeInMillis());
		    }	
		}
	    }
	}
    }

    void diviseTrack(byte mode) {
	List<Track> listeTrackResult=new ArrayList<>();
	if(!listeTrack.isEmpty()) {
	    for(Track t:listeTrack) {

		listeTrackResult.addAll(t.diviseTrack(mode));

	    }
	    listeTrack=listeTrackResult;
	}
    }

    void douglasPeucker() {
	if(!listeTrack.isEmpty()) {
	    for(Track t:listeTrack) {
		t.douglasPeucker();
	    }
	}
    }

    double[] getTabDistance(){

	double[] res= {
		distancePendantTracks,
		distanceEntreTrack,
		distanceDomicileAller,
		distanceDomicileRetour,
		dureeArret,
		dureeRoute,
		dureeEntreTrack ,			 
		dureeIniale,
		revenuGPS,
		nbShiftGPS,
		nbCmdGPS 
	};

	return res;
    }










}
