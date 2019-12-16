package fr.map;


import static fr.map.ValueMap.DUREE_DEFAUT;
import static fr.map.ValueMap.ID_RESTO_DEFAUT;
import static fr.map.ValueMap.IND_DEFAUT;
import static fr.map.ValueMap.PRECISION_DEFAUT;


import fr.gui.GUI;
import fr.modele.Biker;
import fr.modele.Shift;


/**
 *
 * @author martin
 */
public class Localisation implements Comparable<Localisation>,Cloneable{



	private int id;
	private long time;
	private float latitude;
	private float longitude;

	/**
	 * Duree en ms
	 */	
	private long duree;    
	private int indication;
	private int idResto;
	private float precision;





	public Localisation(){
		duree=DUREE_DEFAUT;
		idResto=ID_RESTO_DEFAUT;
		indication=IND_DEFAUT;
		precision=PRECISION_DEFAUT;
	}



	public Localisation clone() {
		Localisation clone=null;  

		try {
			clone =(Localisation) super.clone();			
		} catch (CloneNotSupportedException e) {			
			GUI.messageConsole(e.toString());
		}  
		return clone;
	}

	

	void fixPosition(Localisation l){
		this.latitude=l.getLatitudeRadian();
		this.longitude=l.getLongitudeRadian();
	}

	void setId(int id){
		this.id=id;
	}

	void setPrecision(float precision){
		this.precision=precision;
	}

	float getPrecision(){
		return precision;
	}

	int getId(){
		return id;
	}

	int getIdResto() {
		return idResto;
	}

	public long getTime(){
		return time;
	}
	float getLatitudeRadian(){ 
		return latitude;
		}

	float getLongitudeRadian() {
		return longitude;
	}
	
	float getLatitudeDegres(){ 
		return (float) Math.toDegrees(latitude);
		}

	float getLongitudeDegres() {
		return (float) Math.toDegrees(longitude);
	}
	

	public int getIndication(){
		return indication;
	}
	public long getDuree(){return duree;}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	
	public void setLatitudeDegres(float latDeg) {
		this.latitude = (float) Math.toRadians(latDeg);
	}
	
	public void setLongitudeDegres(float lonDeg) {
		this.longitude = (float) Math.toRadians(lonDeg);
	}
	
	void setIndication(int indication){
		this.indication=indication;
	}

	void setIdResto(int idResto) {
		this.idResto = idResto;
	}

	public void setTime(long time) {
		this.time = time;
	}

	void setDuree(long duree) {this.duree=duree;}

	@Override
	public String toString() {


		String res ="time : ";
		res += String.valueOf(time);
		res +=" lat : ";
		res += String.valueOf(latitude);
		res +=" long : ";
		res += String.valueOf(longitude);
		res +=" duree : ";
		res += String.valueOf(duree);
		res +=" indication: ";
		res += String.valueOf(indication);
		res +=" precision: ";
		res += String.valueOf(precision);
		return res;
	}



	@Override
	public boolean equals(Object o){
		if(!(o instanceof Localisation)) return false;
		return this.equals((Localisation) o);
	}

	public boolean equals(Localisation o){
		if(this==o) return true;
		if(o==null) return false;
		else 	return time==o.time;

	}

	@Override
	public int hashCode(){
		return (int) (time);
	}

	/**
	 *
	 * @param shift
	 * @return true if la localisation est pendant le shift
	 */
	Boolean isUnderShift(Shift shift) {
		//si la localisation est  avant ou après le shift
		if(time>(shift.getcDebut().getTimeInMillis())
				&&time<(shift.getcFin().getTimeInMillis())){

			return true;
		}

		else return false;

	}

	Shift findShift(Biker biker) {
		if(biker.isEmpty()) return null;			

		for (Shift shift:biker.getList()) {

			if(this.isUnderShift(shift)) {
				return shift;
			}
		}
		return null;
	}

	

	@Override
	public int compareTo(Localisation l) {
		if (this.time==l.time) return 0;			
		else return (int) (this.time-l.time)/1000;

	}

}








