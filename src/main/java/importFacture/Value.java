/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package importFacture;



import java.util.Locale;

/**
 * <p>Value class.</p>
 *
 * @author martin
 * @version $Id: $Id
 */
public class Value {

    
   static volatile boolean runImport = true;
    
    static double version=1.5;

    public static boolean debug=false;

    public static boolean isSaving=false;

    static boolean isAlreadySaved=false;
    
   
    
    //Local
    // static Locale localeResult=Locale.getDefault();
    public static Locale localeResult=Locale.FRANCE;

    static String formatNumber(double doubleAFormater) {
	return String.format(localeResult,"%.2f", doubleAFormater);
    }

    static final String separateurCSV=";";

    //  static String pathFactures="";
    static String pathResult="";
    static String pathFileLocation="";

    //regroupage shift	

    public static Regroupement[] tabRegroupageDebug= {
	    RegroupementParMois.getInstance(),
	    RegroupementParSemaine.getInstance(),
	    RegroupementParJour.getInstance(),
	    RegroupementParHeure.getInstance(),
	    RegroupementParHeureEtAnnee.getInstance(),
	    RegroupementToutLesShifts.getInstance()
    };

    public static Regroupement[] tabRegroupageNormal= {
	    RegroupementParMois.getInstance(),
	    RegroupementParSemaine.getInstance(),
	    RegroupementParJour.getInstance(),
	    RegroupementParHeure.getInstance(),  			
	    RegroupementToutLesShifts.getInstance()
    };

    public static Regroupement[] tabRegroupage;

    public static Regroupement regroupageSelected=tabRegroupageNormal[0];

    
    static LocaDomicileFile locaDomicileFile=null;

   
    public static String formatSelected=Map.EcrireMap.formatCarte[0];

    
    public static String couleurSelected=Map.EcrireMap.couleurMap[0];

    public static boolean isExportCarte=true;
    public static boolean isLimiteTailleCarte=true;
    public static boolean isSupprimerShiftSansGPSfiable=true;
  //GUI 
    static final String typeParVille="par ville";
    static final String typeParBiker="par biker";
    public static final String[] typeRegroupage= {typeParBiker,typeParVille};
    
    public static String typeRegroupageSelected=typeRegroupage[0];

   
    
    
    
    
 
    
}

