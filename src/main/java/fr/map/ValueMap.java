package fr.map;

import java.text.SimpleDateFormat;
import java.util.Date;

import fr.modele.Value;

public class ValueMap {


	final static int IND_REMOVE=-1;
	final static int IND_DEFAUT=1;
	//deplacement
	final static int IND_DEPLACEMENT_INCONNU=2;
	final static int IND_DEPLACEMENT_LIGNE_DROITE=3;
	final static int IND_DEPLACEMENT_VERS_RESTO=4;
	final static int IND_DEPLACEMENT_VERS_CLIENT=5;
	//attente
	final static int IND_ATTENTE=6;
	final static int IND_ATTENTE_CONFIRME=7;
	//arrêt
	public final static int IND_ARRET_INCONNU=8;
	final static int IND_RESTO=9;
	final static int IND_CLIENT=10;
	final static int IND_HYPO_RESTO=11;
	final static int IND_HYPO_CLIENT=12;
	final static int IND_RESTO_CONFIRME=13;
	final static int IND_CLIENT_CONFIRME=14;

	//autre
	final static int IND_START=15;
	final static int IND_END=16;

	//plateforme

	final static int[] IND_PLATEFORME={16,17,18,19,20};


	final static int DUREE_DEFAUT=0;  //en ms
	final static int ID_RESTO_DEFAUT=-1;
	final static int PRECISION_DEFAUT=1;




	static final int MIN_TIME_UPDATE_LOCATION = 1000;   //en ms
	static final int MIN_DISTANCE_UPDATE_LOCATION = 2;  //en m
	// suppression oncreate ondestroy
	static final int DUREE_MAX_INTERRUPTION = 100000;    //en ms

	//si le pas si supérieur, on considere que le shift 'est arreté puis redemarré
	static final int DUREE_MIN_FIN = 60000;    //en ms  soit 10min 

	static final int MIN_DISTANCE_MOYENNE = 5;     //en m
	static final int MIN_DISTANCE_MOYENNE2 = 10;     //en m
	static final int MIN_DISTANCE_MOYENNE3 = 100;     //en m


	
	static final int MAX_DISTANCE_DOUGLAS = 10;   //en m
	// on garde tous les points pour lesquels on est resté DUREE_MIN_SAUVEGARDE_PAS à la même postion
	// (à MIN_DISTANCE_MOYENNE2 metre près)
	static final int DUREE_MIN_SAUVEGARDE_PAS = 50000; //en ms
	//si le coursier attend plus que DUREE_MIN_RESTO à proximité d'un resto, on considere que c'est un pickup
	static final int DUREE_MIN_RESTO=50000;//en ms   : 50s
	/*point aberant:
     si la distance entre un point et le point suivant est sup à MAX_DISTANCE_ABERANT
     et que la distance entre le premier point
    et le 2e point suivant est inférieur à MIN_DISTANCE_ABERANT on le supprime
	 */
	static final int MAX_DISTANCE_ABERANT= 200;  //en m
	static final int MIN_DISTANCE_ABERANT=100;  //en m
	static final int MIN_DISTANCE_ABERANT2=1000;  //en m
	static final int DISTANCE_LONGUE=300;  //en m


	static final int SEUILZONE=10000; //en m
	static final int SEUILRESTO =20; //en m
	static final int SEUILSELECTRESTO =50; //en m



	static final int RAYONTERRE = 6378137; //en m
	static int rayonPetitCercle=0;			//en m

	/**
	 *
	 * @param latRad1 latitude position1 en radian
	 * @param latRad2 latitude position2 en radian
	 * @param lonRad1 longitude position1 en radian
	 * @param lonRad2 longitude position2 en radian
	 * @return la distance au carré en m2 entre la position 1 et la position 2
	 */
	private static double distance2(double latRad1,double latRad2,double lonRad1,double lonRad2){
		if (rayonPetitCercle==0) rayonPetitCercle = (int) (RAYONTERRE * Math.cos(latRad1));
		return  ((RAYONTERRE * (latRad2 - latRad1)) *(RAYONTERRE *(latRad2 - latRad1) ) +
				(rayonPetitCercle *(lonRad2 - lonRad1)) *( (lonRad2 - lonRad1) * rayonPetitCercle));
	}
	public static double distance2(Localisation l1, Localisation l2) {		
		return distance2(l1.getLatitudeRadian(),l2.getLatitudeRadian(),
				l1.getLongitudeRadian(),l2.getLongitudeRadian());
	}
	
	private static SimpleDateFormat dateFormatMap  =new SimpleDateFormat("dd/MM/yy",Value.localeResult);
	static String getStringDate(Date d) {
		return dateFormatMap.format(d);
	}
	
	static SimpleDateFormat dateFormatTitle  =new SimpleDateFormat("yyyy",Value.localeResult);
	

	static String coordonneToString(float f) {
		return (String.format(Value.localeResult,"%.6f", Math.toDegrees(f))).replace(",", ".");
	}


	static String intToString(int i){

		if (i < 10) return "0" + String.valueOf(i);
		return String.valueOf(i);
	}

	static String[] textPays={"france","paris"};

	static int[] ACTIVITY={0,1,2,3,4};
	static String[] NAMEACTIVITY={"Still",""};

	static final int tailleMaxCarte=5000000;   //environ 5Mo  
	public static final String debutHistoriquePositionsGoogleMap="Historique des positions";
	public static final String finHistoriquePositionsGoogleMap=".json";
	
	
	public static final String nameFileLocaDomicile="position GPS domicile.txt";
	
	public static final byte modeDivisionDuree=0;
	public static final byte modeDivisionDistance=1;
	
}





