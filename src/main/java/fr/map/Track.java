package fr.map;

import static fr.map.ValueMap.DISTANCE_LONGUE;
import static fr.map.ValueMap.DUREE_MIN_FIN;
import static fr.map.ValueMap.DUREE_MIN_SAUVEGARDE_PAS;
import static fr.map.ValueMap.IND_ARRET_INCONNU;
import static fr.map.ValueMap.IND_DEFAUT;
import static fr.map.ValueMap.IND_DEPLACEMENT_INCONNU;
import static fr.map.ValueMap.IND_DEPLACEMENT_LIGNE_DROITE;
import static fr.map.ValueMap.IND_REMOVE;
import static fr.map.ValueMap.IND_START;
import static fr.map.ValueMap.MAX_DISTANCE_ABERANT;
import static fr.map.ValueMap.MAX_DISTANCE_DOUGLAS;
import static fr.map.ValueMap.MIN_DISTANCE_ABERANT;
import static fr.map.ValueMap.MIN_DISTANCE_MOYENNE;
import static fr.map.ValueMap.MIN_DISTANCE_MOYENNE2;
import static fr.map.ValueMap.MIN_DISTANCE_MOYENNE3;
import static fr.map.ValueMap.RAYONTERRE;
import static fr.map.ValueMap.coordonneToString;
import static fr.map.ValueMap.modeDivisionDistance;
import static fr.map.ValueMap.modeDivisionDuree;
import static fr.map.ValueMap.rayonPetitCercle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.algorithmes.Trieur;
import fr.algorithmes.TrieurBulle;
import fr.modele.Shift;
import fr.modele.ShiftImplGPS;
import fr.modele.Value;

public class Track implements Cloneable{

	private ShiftImplGPS shiftGPS=null;
	private ArrayList<Localisation> listeLoca;
	private int tailleOctet=0;


	//public Track() {
	//listeLoca=new ArrayList<>();		
	//   }

	Track(Shift shift){
		assert shift instanceof ShiftImplGPS;
		listeLoca=new ArrayList<>();
		this.shiftGPS=(ShiftImplGPS) shift;	
	}

	Track(ShiftImplGPS s){
		listeLoca=new ArrayList<>();
		this.shiftGPS=s;
	}

	public Localisation getLastLoca() {
		return listeLoca.get(listeLoca.size()-1);
	}

	public Localisation getFirstLoca() {
		return listeLoca.get(0);
	}

	public boolean isEmpty() {
		return listeLoca.isEmpty();
	}	

	public void add(Localisation newlocalisation){
		listeLoca.add(newlocalisation);
	}

	public ShiftImplGPS getShiftImplGPS() {	
		return this.shiftGPS;
	}

	public Track clone() {

		Track clone=new Track(this.shiftGPS);    
		for(Localisation l:listeLoca) {
			clone.add(l.clone());
		}

		return clone;
	}


	public List<Localisation> getList(){
		return listeLoca;
	}

	public void corrigerTrack(){

		pointAberrant();		
		moyennePointProche(MIN_DISTANCE_MOYENNE, 1);		
		moyennePointProche(MIN_DISTANCE_MOYENNE, 2);		
		moyennePointProche(MIN_DISTANCE_MOYENNE2, 4);		
		moyennePointProche(MIN_DISTANCE_MOYENNE2, 2);		
		moyennePointProche(MIN_DISTANCE_MOYENNE3, 3);
		enlevePointLongueDistance();
		removePoint();
		indication();				

	}	

	private void enlevePointLongueDistance() {

		boolean dist1;
		// boucle pour enlever les points arret-fin
		boolean condition=true;
		while(condition){
			condition=false;

			boolean dist2;
			for (int i =  0; i < listeLoca.size() - 2; i++) {

				dist1 =distance2(i, i + 1) > DISTANCE_LONGUE * DISTANCE_LONGUE;
				dist2 =distance2(i+1, i + 2) < DISTANCE_LONGUE * DISTANCE_LONGUE;

				if(dist1&&!dist2){

					listeLoca.get(i + 1).setIndication(IND_REMOVE);
					condition=true;
				}
			}


			removePoint();
		}
		removePoint();		
	}



	// calcule la moyenne des points entre indexDebut et indexFin (non compris) pondérée par l'inverse de la précision de la mesure,
	private void moyenne (int indexDebut, int indexFin){

		if (indexFin>indexDebut+1){

			double moyenneLat=0;
			double moyenneLon=0;
			double ponderation=0.0;


			for(int i=indexDebut; i<indexFin;i++){

				double p=listeLoca.get(i).getPrecision();
				double lat=listeLoca.get(i).getLatitudeRadian();
				double lon=listeLoca.get(i).getLongitudeRadian();

				ponderation=(1.0/p)+ponderation;
				moyenneLat = moyenneLat + (lat / p);
				moyenneLon = moyenneLon + (lon / p);
			}
			moyenneLat= moyenneLat/ponderation;
			moyenneLon=moyenneLon/ponderation;

			Localisation l =listeLoca.get(indexDebut);
			l.setLatitude((float) moyenneLat);
			l.setLongitude((float) moyenneLon);
			l.setPrecision((float) (1.0/ponderation));
			l.setDuree((int) (listeLoca.get(indexFin-1).getTime()-l.getTime()));


			for(int i= indexDebut+1; i<indexFin;i++){
				listeLoca.get(i).setIndication(-1);
			}
		}
	}

	private double distance2(int indexD, int indexF) {
		Localisation lD=listeLoca.get(indexD);
		Localisation lF=listeLoca.get(indexF);
		return ValueMap.distance2(lD,lF);
	}

	private boolean moyennePointProche(int distanceMax,int mode){
		boolean result=false;

		//moyenne des points proches

		int indexF;

		for(int i=0;i<listeLoca.size()-1;i++) {
			indexF = i + 1;
			while ((indexF < listeLoca.size()-1)  && conditionDistance(mode, i, indexF, distanceMax)) {
				result = true;
				indexF++;
			}

			if (result) {
				moyenne(i, indexF);
				i = indexF - 1;
			}
		}

		if(result){
			removePoint();
		}

		return result;
	}

	private boolean conditionDistance(int mode,int indexD,int indexF,int distanceMax){
		if(listeLoca.get(indexF).getIndication()>=IND_START
				||listeLoca.get(indexD).getIndication()>=IND_START) return false;

		switch (mode) {
		case 1:
			return distance2(indexD, indexF) < ((distanceMax + listeLoca.get(indexF).getPrecision()) * (distanceMax + listeLoca.get(indexF).getPrecision()));

		case 3:
			return  listeLoca.get(indexF).getDuree()> DUREE_MIN_SAUVEGARDE_PAS&& listeLoca.get(indexD).getDuree()> DUREE_MIN_SAUVEGARDE_PAS && (distance2(indexD, indexF) < (distanceMax * distanceMax));

		case 4: return (indexF<listeLoca.size()-2) && listeLoca.get(indexF).getDuree()<DUREE_MIN_SAUVEGARDE_PAS&&distance2(indexD,indexF+1)<(distanceMax * distanceMax);

		default:
			return distance2(indexD, indexF) < (distanceMax * distanceMax);
		}
	}

	private void indication(){

		for(Localisation l:listeLoca){
			l.setIndication(IND_DEFAUT);			
		}
		//calcul pas
		for(int i=0;i<listeLoca.size()-1;i++){

			listeLoca.get(i).setDuree((int) (listeLoca.get(i+1).getTime() - listeLoca.get(i).getTime()));
		}
		listeLoca.get(listeLoca.size()-1).setDuree(0);
		/*
			if(distance2(i, i + 1) > DISTANCE_LONGUE * DISTANCE_LONGUE) {
				listeLoca.get(i).setIndication(IND_END);
				listeLoca.get(i + 1).setIndication(IND_START);		
			}
		 */

		/*
		boolean condition=true;
		while(condition) {
			condition=false;
			for(int i=2;i<listeLoca.size();i++){
				if(listeLoca.get(i).getIndication()==IND_START
						&&listeLoca.get(i+1).getIndication()==IND_END
						&&listeLoca.get(i+2).getIndication()==IND_START) {

					listeLoca.get(i).setIndication(IND_REMOVE);
					listeLoca.get(i + 1).setIndication(IND_REMOVE);
					condition=true;

				}

			}
			removePoint();
		}
		 */

		for(Localisation l: listeLoca) {

			if(l.getIndication()==IND_DEFAUT) {

				if (l.getDuree() < DUREE_MIN_SAUVEGARDE_PAS) l.setIndication(IND_DEPLACEMENT_INCONNU);
				else l.setIndication(IND_ARRET_INCONNU) ;
			}
		}


	}

	public static double getDureeEntreTrack(Track t1,Track t2) {
		Localisation l1=t1.getList().get(t1.size()-1);
		Localisation l2=t2.getList().get(0);
		return l2.getTime()-l1.getTime();
	}

	public static double getDistanceEntreTrack(Track t1,Track t2) {
		Localisation l1=t1.getList().get(t1.size()-1);
		Localisation l2=t2.getList().get(0);
		return Math.sqrt(ValueMap.distance2(l1, l2));
	}


	private int distanceDroite2(int indexPoint,int indexDebut, int indexFin){

		if (indexDebut<indexPoint&&indexPoint<indexFin){
			Localisation Ld=listeLoca.get(indexDebut);
			Localisation Lf=listeLoca.get(indexFin);
			Localisation Lp=listeLoca.get(indexPoint);

			if(rayonPetitCercle==0) rayonPetitCercle = (int) (RAYONTERRE * Math.cos(Lp.getLatitudeRadian()));

			float xDF=rayonPetitCercle*(Lf.getLongitudeRadian()-Ld.getLongitudeRadian());
			float yDF=RAYONTERRE*(Lf.getLatitudeRadian()-Ld.getLatitudeRadian());

			float xDP=rayonPetitCercle*(Lp.getLongitudeRadian()-Ld.getLongitudeRadian());
			float yDP=RAYONTERRE*(Lp.getLatitudeRadian()-Ld.getLatitudeRadian());

			return (int)( (xDP*yDF-yDP*xDF)*(xDP*yDF-yDP*xDF)/(xDF*xDF+yDF*yDF));
		}
		else return 0;

	}

	private void douglasPeucker(int indexDebut, int indexFin){

		if (indexDebut+1<indexFin) {
			int dmax = 0;
			boolean pointInterressant = false;
			int index = -1;

			for (int i = indexDebut + 1; i < indexFin; i++) {

				if (listeLoca.get(i).getIndication()!=IND_DEPLACEMENT_INCONNU) {
					index = i;
					pointInterressant = true;
					break; //si le point est interressant, on le sauve en arretant la boucle
				}
			}
			//si on n'a pas trouvé de point interressant, on cherche la distance max
			if (!pointInterressant) {
				for (int i = indexDebut + 1; i < indexFin; i++) {
					int distance = distanceDroite2(i, indexDebut, indexFin);
					if (dmax < distance) {
						dmax = distance;
						index = i;
					}
				}
			}

			if (!pointInterressant && dmax < MAX_DISTANCE_DOUGLAS * MAX_DISTANCE_DOUGLAS) {


				projeterSurDroiteBetween(indexDebut,indexFin);					

			}
			else if (indexDebut < index && index < indexFin) {
				douglasPeucker(indexDebut, index);
				douglasPeucker(index, indexFin);
			}
		}

	}

	private void projeterSurDroiteBetween(int indexDebut,int indexFin) {

		if (indexDebut<indexFin){
			Localisation Lb=listeLoca.get(indexDebut);
			Localisation Lc=listeLoca.get(indexFin);			
			if(rayonPetitCercle==0) rayonPetitCercle = (int) (RAYONTERRE * Math.cos(Lb.getLatitudeRadian()));


			float xV=rayonPetitCercle*(Lc.getLongitudeRadian()-Lb.getLongitudeRadian());
			float yV=RAYONTERRE*(Lc.getLatitudeRadian()-Lb.getLatitudeRadian());
			double nV=Math.sqrt(xV*xV+yV*yV);
			if(nV>0) {
				xV=(float) (xV/nV);
				yV=(float) (yV/nV);

				for (int i = indexDebut + 1; i < indexFin; i++) {
					float xBA=rayonPetitCercle*(listeLoca.get(i).getLongitudeRadian()-Lb.getLongitudeRadian());
					float yBA=RAYONTERRE*(listeLoca.get(i).getLatitudeRadian()-Lb.getLatitudeRadian());
					float produitScalaire=xBA*xV+yBA*yV;

					listeLoca.get(i).setLongitude(Lb.getLongitudeRadian()+(produitScalaire*xV)/rayonPetitCercle);
					listeLoca.get(i).setLatitude(Lb.getLatitudeRadian()+(produitScalaire*yV)/RAYONTERRE);
					listeLoca.get(i).setIndication(IND_DEPLACEMENT_LIGNE_DROITE);

				}
			}
		}


	}

	private void pointAberrant(){

		if(listeLoca.size()>3) {
			for (int k = 0; k < listeLoca.size() - 3; k++) {

				boolean dist = distance2(k, k + 1) > MAX_DISTANCE_ABERANT * MAX_DISTANCE_ABERANT;
				boolean dist2 = distance2(k, k + 2) < MIN_DISTANCE_ABERANT * MIN_DISTANCE_ABERANT;
				boolean dist3 = distance2(k, k + 3) < MIN_DISTANCE_ABERANT * MIN_DISTANCE_ABERANT;


				if (dist && dist3) {
					listeLoca.get(k + 1).fixPosition(listeLoca.get(k));
					listeLoca.get(k + 2).fixPosition(listeLoca.get(k));
				}

				else if (dist && dist2) {
					listeLoca.get(k + 1).fixPosition(listeLoca.get(k));
				}

			}

		}
		removePoint();
	}

	private void removePoint(){

		for(int i=0;i<listeLoca.size();i++){
			Localisation l=listeLoca.get(i);
			if(l.getIndication()==ValueMap.IND_REMOVE) {				
				listeLoca.remove(i);
				i--;
			}
		}


	}



	public void douglasPeucker() {		 
		if(listeLoca.size()>500) {
			for(int i=0;i<listeLoca.size()-500;i=i+500) {
				douglasPeucker(i, i+500);			
			}
		} 
		else {
			douglasPeucker(0, listeLoca.size()-1);	

		}
	}

	public List<Track> diviseTrack(byte mode) {
		if(!this.isEmpty()) {
			List<Track> resTracks=new ArrayList<>();

			Track t=new Track(this.shiftGPS);
			t.add(this.listeLoca.get(0));	

			for (int i =  1; i < listeLoca.size(); i++) {

				if(isAdiviser(mode,i-1,i)) {
					if(t.size()>2) {
						t.listeLoca.get(t.listeLoca.size()-1).setDuree(0);
						resTracks.add(t);				
					}
					t=new Track(this.shiftGPS);

				}			
				t.add(this.listeLoca.get(i));
			}
			if(t.size()>2) {
				t.listeLoca.get(t.listeLoca.size()-1).setDuree(0);
				resTracks.add(t);
			}    

			return resTracks;

		}else {
			return null;
		}
	}

	private boolean isAdiviser(byte mode,int i,int j) {
		if(j>=listeLoca.size()) return false;

		if(mode==modeDivisionDuree) {
			return (listeLoca.get(i).getTime()-listeLoca.get(j).getTime())>DUREE_MIN_FIN;
		}
		else if(mode==modeDivisionDistance){
			return distance2(i, j) > DISTANCE_LONGUE * DISTANCE_LONGUE;
		}
		else return false;
	}

	public int size() {		
		return listeLoca.size();
	}

	String GPXString() {
		String retourchariot=System.getProperty("line.separator");
		StringBuilder mess=new StringBuilder("<trk>" + retourchariot+
				"    <name>"+ValueMap.getStringDate(new Date(listeLoca.get(0).getTime()))+"</name>" + retourchariot+		
				"    <trkseg>"+retourchariot
				);				


		for(Localisation l:listeLoca) {
			mess.append(" <trkpt lat=\""+coordonneToString(l.getLatitudeRadian()) +"\" lon=\""+coordonneToString(l.getLongitudeRadian())+"\">");
			mess.append("</trkpt>"+retourchariot);		 

		}		
		mess.append("</trkseg>" + "</trk>");

		return mess.toString();
	}


	String KMLLineString(String couleur,Boolean isSupprimerShiftSansGPSfiable) {

		if(couleur.equals("vitesse")) {
			return kMLLingStringVitesse();
		}
		assert shiftGPS!=null;

		if(isSupprimerShiftSansGPSfiable
				&&!shiftGPS.hasGPSfiable()) return "";
		else {
			return kMLLingStringCA();
		}
	}

	private String kMLLingStringCA() {
		String retourchariot=System.getProperty("line.separator");
		float CAHoraire=(float) ((shiftGPS.getRevenue()+shiftGPS.getPrime()+shiftGPS.getTips())/shiftGPS.getDuree());

		StringBuilder mess=new StringBuilder("<Placemark>"
				+ "<name>"+ValueMap.getStringDate(shiftGPS.getcDebut().getTime())+"</name>\n" 
				+"<description>"+String.format(Value.localeResult,"%.2f", CAHoraire)+"€/h</description>" 
				+"<styleUrl>#"
				+MapCouleur.getStyleFromCA(CAHoraire)
				+"</styleUrl><LineString><coordinates>"+retourchariot);	

		for(Localisation l:listeLoca) {
			mess.append(coordonneToString(l.getLongitudeRadian())+","+coordonneToString(l.getLatitudeRadian())+",0"+retourchariot);
		}		
		mess.append("</coordinates></LineString></Placemark>");
		tailleOctet= 22*listeLoca.size()+139;
		return mess.toString();
	}

	private String kMLLingStringVitesse() {

		String retourchariot=System.getProperty("line.separator");

		StringBuilder mess=new StringBuilder("");
		for(int i=0; i<listeLoca.size()-1; i++) {

			Localisation l1=listeLoca.get(i);
			Localisation l2=listeLoca.get(i+1);
			double distance=Math.sqrt(ValueMap.distance2(l1, l2));
			if(distance>1) {
				float vitesse=(float) (distance*3600/(l2.getTime()-l1.getTime()));

				mess.append("<Placemark>"
						+ "<name>"+ValueMap.getStringDate(new Date(l1.getTime()))+"</name>\n" 	
						+"<description>"+String.format(Value.localeResult,"%.2f", vitesse)+"km/h</description>" 
						+"<styleUrl>#"
						+MapCouleur.getStyleFromVitesse(vitesse)
						+"</styleUrl><LineString><coordinates>"+retourchariot);

				mess.append(coordonneToString(l1.getLongitudeRadian())+","+coordonneToString(l1.getLatitudeRadian())+",0"+retourchariot);
				mess.append(coordonneToString(l2.getLongitudeRadian())+","+coordonneToString(l2.getLatitudeRadian())+",0"+retourchariot);
				mess.append("</coordinates></LineString></Placemark>");
				tailleOctet+=210;
			}
		}


		return mess.toString();
	}


	String KMLgxMultiTrack() {
		String retourchariot=System.getProperty("line.separator");


		StringBuilder mess=new StringBuilder("<gx:Track><altitudeMode>clampToGround</altitudeMode>"+retourchariot);	

		for(Localisation l:listeLoca) {
			mess.append("<gx:coord>"+l.getLongitudeDegres()+" "+l.getLatitudeDegres()+" 0</gx:coord>"+retourchariot);
		}		
		mess.append("</gx:Track>");

		return mess.toString();
	}

	int getTailleMapOctet() {		
		return tailleOctet;		
	}

	
	String json() {
		String retourchariot=System.getProperty("line.separator");

		StringBuilder mess=new StringBuilder();	
		boolean first=true;
		
		for(Localisation l:listeLoca) {
			if(first) first=false;
			else 	mess.append(", ");
			
			
			mess.append(
					
					"{"+retourchariot
					+"    \"timestampMs\" : \"" +l.getTime()+"\","+retourchariot
					+"    \"latitudeE7\" : " +((int)(l.getLatitudeDegres()*10000000))+","+retourchariot
					+"    \"longitudeE7\" : " +((int)(l.getLongitudeDegres()*10000000))+","+retourchariot
					+"    \"accuracy\" : " +(int)(l.getPrecision())+retourchariot
					+"  }");
					
		}		
		

		return mess.toString();
	}	

	void sortTrack() {
		// tri à bulle car liste déjà presque trié
		Trieur<Localisation> trieur = new TrieurBulle<Localisation>();
		trieur.setArray(listeLoca);
		trieur.tri();
	}

	String getTitleCalqueMap() {
		Date d=new Date(listeLoca.get(0).getTime());
		return ValueMap.dateFormatTitle.format(d);		

	}

	@Override
	public String toString() {
		StringBuilder res=new StringBuilder();
		for(Localisation l:listeLoca) {
			res.append(l.toString()+"\n");
		}
		return res.toString();
	}

}
