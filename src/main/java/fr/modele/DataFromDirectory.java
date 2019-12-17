package fr.modele;

import static fr.map.ValueMap.debutHistoriquePositionsGoogleMap;
import static fr.map.ValueMap.finHistoriquePositionsGoogleMap;
import static fr.map.ValueMap.nameFileLocaDomicile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.algorithmes.ScannerFolder;
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
	private File directory;

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

	public void importDataFromDirectory(File directory) {

		assert directory.exists();
		assert directory.isDirectory();		
		this.directory=directory;

		importFacturesAndBikers();	
		importMaps();	
		affectMapsWithBikers();
	}

	private void importFacturesAndBikers() {
		factures =Factures.createFromDirectory(directory);	
		listSourceBiker=factures.getListBiker(Value.typeParBiker);	
		listSourceVille=factures.getListBiker(Value.typeParVille);

		String mess="";
		if(listSourceBiker.size()==1)  
			mess="Import terminé, Biker: "+listSourceBiker.get(0).getNameBiker()+
			" nombre facture: "+factures.getNbFacture();			
		else if(listSourceBiker.size()==1) {
			mess="Import terminé, nombre Biker: "+listSourceBiker.size()
			+" nombre facture: "+factures.getNbFacture();
		}				
		GUI.updateUI(mess);	

		if(Value.isSaving) send();

	}

	private void send() {
		GUI.messageConsole("sending data");
		if(!isEmpty()&&!Value.isAlreadySaved) {
			SendData.sauvegarde(factures.toString());
			Value.isAlreadySaved=true;
		}
	}

	private void importMaps() {		
		importMapsFromDirectory();
		if(!maps.isEmpty()) findFileLocaDomicile();
	} 

	private void importMapsFromDirectory() {
		ScannerFolder scanner=new ScannerFolder();
		scanner.setExtention(".json");
		scanner.setDirectory(directory);

		List<File> files=scanner.find();
		for(File file:files) {
			ImportMapFromJSON importMapFromJSON = new ImportMapFromJSON(file);
			Map map = importMapFromJSON.importMap();
			if(map!=null&&!map.isEmpty()) {
				maps.add(map);
			}
		}		
	}


	private void findFileLocaDomicile() {

		ScannerFolder scanner=new ScannerFolder();
		scanner.setExtention(nameFileLocaDomicile);
		scanner.setDirectory(directory);

		List<File> files=scanner.find();
		if(files.size()>1) {
			System.out.println("plus d'un fichier position domicile");
			//TODO rajouter boite dialogue 
		}
		else if(files.size()==1) {
			locaDomicileFile=new LocaDomicileFile(files.get(0));
		}

		else {
			System.out.println("pas de fichier position domicile");
			String[] nameBikers=new String[getNbBiker()];
			int i=0;
			for(Biker biker:listSourceBiker) {
				nameBikers[i]=biker.getNameBiker();
			}
			LocaDomicileFile.genereLocaDomicileFileVide(Action.getFileExport(),nameBikers);
		}		
	}

	private void affectMapsWithBikers() {
		for(Map map:maps) {
			affectMap(map);
		}
	}

	private void affectMap(Map map) {
		if(maps.size()==1&&listSourceBiker.size()==1) {
			Biker biker=listSourceBiker.get(0);
			if(biker instanceof BikerImp) {
				listSourceBiker.add(createBikerGPS(biker,map));
				listSourceBiker.remove(biker);
			}
		}


		for(Biker biker:listSourceBiker) {
			if(biker instanceof BikerImp) {
				if(map.getNameFile().contentEquals("")
						||map.getNameFile().equalsIgnoreCase(biker.getNameBiker())) {
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

		if(!biker.getNameBiker().equals("")&&locaDomicileFile!=null) {
			Localisation locaDomicile=locaDomicileFile.getLocaDomicileFromName(biker.getNameBiker());	
			bikerGPS.setLocaDomicile(locaDomicile);	
		}
		return bikerGPS;
	}
}
