package fr.modele;

import static fr.map.ValueMap.debutHistoriquePositionsGoogleMap;
import static fr.map.ValueMap.finHistoriquePositionsGoogleMap;
import static fr.map.ValueMap.nameFileLocaDomicile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import fr.gui.GUI;
import fr.map.ImportMapFromJSON;
import fr.map.Localisation;
import fr.map.Map;


public class DataFromDirectory {


    private ArrayList<Biker> listSourceBiker = new ArrayList<>();   
    private ArrayList<Biker> listSourceVille = new ArrayList<>();


    private ArrayList<Map> maps = new ArrayList<>();    
    private Factures factures = null;
    private LocaDomicileFile locaDomicileFile=null;

    public int getNbBiker() {
	return getList().size();
    }

    public int getNbFacture() {
	return factures.getNbFacture();
    }

    public Biker getBiker(int i) {		
	return getList().get(i);
    }
    
    public ArrayList<Map> getMaps() {
	return maps;
    }
    //TODO pas joli
    private ArrayList<Biker> getList(){
	if(Value.typeRegroupageSelected.equals(Value.typeParBiker)) return listSourceBiker;
	else return listSourceVille;
    }


    public boolean isEmpty() {
	return listSourceBiker.isEmpty()||factures==null||factures.getNbFacture()==0;
    }

    void ecrireFacture(File f,Factures l) {
	try {
	    BufferedWriter bw=new BufferedWriter(new FileWriter(f.getAbsoluteFile()+File.separator+Text.nameListFacturesTextDebut+System.currentTimeMillis()+Text.nameListFacturesTextFin));

	    bw.write(l.toString());
	    bw.newLine();

	    bw.close();
	} catch (IOException e) {			
	    e.printStackTrace();
	}
    }

    private static boolean isDirectoryValid(File directory) {
	return directory.exists()&&
		directory.isDirectory();				
    }

    public void importDataFromDirectory(File directory) {

	importFacturesAndBikers(directory);	
	importMaps(directory);	
	affectMapWithBikers();
    }

    private void importFacturesAndBikers(File directory) {
	factures =Factures.createFromDirectory(directory);	
	listSourceBiker=factures.getListBiker(Value.typeParBiker);	
	listSourceVille=factures.getListBiker(Value.typeParVille);

	String mess;

	if(listSourceBiker.size()<2)  
	    mess="Import terminé, Biker: "+listSourceBiker.get(0).getNameBiker()+
	    " nombre facture: "+factures.getNbFacture();			
	else mess="Import terminé, nombre Biker: "+listSourceBiker.size()+
		" nombre facture: "+factures.getNbFacture();
	GUI.updateUI(mess);	

	if(Value.isSaving) send();

    }

    void send() {
	GUI.messageConsole("sending data");
	if(!isEmpty()&&!Value.isAlreadySaved) {
	    SendData.sauvegarde(factures.toString());
	    Value.isAlreadySaved=true;
	}
    }

    private void importMaps(File directory) {
	findFileLocaDomicile(directory);
	importMapsFromDirectory(directory);
    } 



    private void findFileLocaDomicile(File f) {
	if(f.isFile()&&f.getName().equals(nameFileLocaDomicile))  
	    locaDomicileFile=new LocaDomicileFile(f);

	else if(isDirectoryValid(f)){
	    File[] listeFichier=f.listFiles();                
	    for (File Fichier : listeFichier) {
		findFileLocaDomicile(Fichier);
	    }                
	}
    }

    private void importMapsFromDirectory(File f) {
	if(f.isFile()
		&&f.getName().startsWith(debutHistoriquePositionsGoogleMap)
		&&f.getName().endsWith(finHistoriquePositionsGoogleMap)
		) {
	    ImportMapFromJSON importMapFromJSON = new ImportMapFromJSON(f);
	    Map map = importMapFromJSON.importMap();
	    if(map!=null&&!map.isEmpty()) {
		maps.add(map);
	    }
	}	    

	else if(isDirectoryValid(f)){
	    File[] listeFichier=f.listFiles();                
	    for (File Fichier : listeFichier) {
		importMapsFromDirectory(Fichier);
	    }                
	}
    }

    private void affectMapWithBikers() {
	for(Map map:maps) {
	    affectMap(map);
	}
    }

    private void affectMap(Map map) {
	for(Biker biker:listSourceBiker) {
	    if(biker instanceof BikerImp) {
		if(map.getNameFile().contentEquals("")
			||map.getNameFile().contentEquals(biker.getNameBiker())) {
		    listSourceBiker.add(createBikerGPS(biker,map));
		    listSourceBiker.remove(biker);
		    return;
		}
	    }
	}
    }

    Biker createBikerGPS(Biker biker,Map map){

	BikerImpGPS bikerGPS=BikerImpGPS.createFromBiker(biker);
	bikerGPS.setMap(map);	

	if(!biker.getNameBiker().equals("")) {
	    if(locaDomicileFile!=null) {
		Localisation locaDomicile=locaDomicileFile.getLoca(biker.getNameBiker());	
		bikerGPS.setLocaDomicile(locaDomicile);	
	    }
	    else {
		LocaDomicileFile.genereLocaDomicileFileVide(Action.getFileExport(),biker.getNameBiker());
	    }

	}
	return bikerGPS;
    }
}
