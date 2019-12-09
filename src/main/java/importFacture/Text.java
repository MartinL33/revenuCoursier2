/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package importFacture;

import static importFacture.Value.pathResult;

/**
 *
 * @author martin
 */
public class Text {
     
//text dans fichier result
    
	
	
    static final String nameResult="result";
    static final String nameListFacturesTextDebut="factures";
    static final String nameListFacturesTextFin=".csv";
    
    static final String global="global";    
   
    //message
    static final String messEcritureFichier="Ecriture fichier : "; 
    static final String resultat="Résultats dans le";
     static final String fichier="fichier";
      static final String dossier="dossier";
      static final String sep=" ";
      static final String extentionFichier=".csv";
     static final String[] messFinish={resultat+sep+ fichier +sep+nameResult+extentionFichier,resultat+sep+ dossier +sep+nameResult};
    
   
     public static final String textDossierChoisi="Dossier choisi : " ; 
     static final String messPasdeFacture="Pas de Facture!" ;
     public static final String messSelectionDossier="selectionner le dossier facture";
     static final String erreurOuvertureDossier="Erreur: impossible d'ouvrir le dossier ";
     
     static String messOuvertureImpossible=erreurOuvertureDossier+pathResult;
      
}
