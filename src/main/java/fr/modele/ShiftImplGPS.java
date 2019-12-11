package fr.modele;

import static java.lang.Math.abs;
import static fr.algorithmes.Utilitaire.formatDouble;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.map.Localisation;
import fr.map.Track;
import fr.map.ValueMap;

public class ShiftImplGPS extends ShiftImpl implements Cloneable {

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

	@Override
	public Shift clone2() {	
		return clone();
	}

	@Override
	protected Shift cloneForRegroupage() {	

		return clone();		
	}

	@Override
	public Shift clone() {	  

		ShiftImplGPS clone=createFromShift(this);

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
		if(s instanceof ShiftImplGPS) {
			return (ShiftImplGPS) s;
		}

		if(s instanceof ShiftImpl) {
			ShiftImplGPS res=new ShiftImplGPS();
			res.calendarDebut=(Calendar) s.calendarDebut.clone();	
			res.calendarFin=(Calendar)s.calendarFin.clone();	
			res.duree=s.duree;
			res.nb=s.nb;
			res.prime=s.prime;
			res.revenue=s.revenue;
			res.tips=s.tips;
			res.nameFile=s.nameFile;

			return res;
		}

		return null;
	}


	ShiftImpl toShiftImpl() {
		ShiftImpl res=new ShiftImpl();
		res.calendarDebut=this.calendarDebut;
		res.calendarFin=this.calendarFin;
		res.duree=this.duree;
		res.isArtificiel=this.isArtificiel;
		res.nameFile=this.nameFile;
		res.nb=this.nb;
		res.prime=this.prime;
		res.revenue=this.revenue;		
		res.tips=this.tips;		

		return res;
	}

	@Override
	public String toString() {

		String res=super.toString();
		if(distancePendantTracks!=0) {
			for(int i=0;i<this.getTabDistance().length;i++) {
				res+=formatDouble(this.getTabDistance()[i])+" ";
			}
		}
		return res;
	}

	@Override
	public Shift addition(Shift shift){
		assert shift instanceof ShiftImplGPS;
		ShiftImplGPS shiftImplGPS = (ShiftImplGPS) shift;
		super.addition(shift);		

		if(Value.isSupprimerShiftSansGPSfiable && !shiftImplGPS.hasGPSfiable()) {
			//On ne fait pas l'addition
		}
		else {
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

		// TODO 	reaffecter track puis recalculer  distance 

		return res;
	}

	public Boolean hasGPSfiable() {
		if (distancePendantTracks==0) return false;

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

	void calculDistance(Localisation locaDomicile, ArrayList<Track> listeTrack) {
		calculDistanceSansDomicile(listeTrack);
		calulDistanceDomicile(locaDomicile,listeTrack);		
	}

	private void calculDistanceSansDomicile(ArrayList<Track> listeTrack) {
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

	private void calulDistanceDomicile(Localisation locaDomicile,ArrayList<Track> listeTrack) {

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
