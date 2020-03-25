package fr.modele;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fr.algorithmes.ScannerFolder;

import fr.gui.GUI;

public class Factures {

	private ArrayList<Facture> arrayListFacture =new ArrayList<Facture>();

	public static Factures createFromDirectory(File fileDirectory) {
		assert fileDirectory!=null;	
		assert fileDirectory.isDirectory();
		return new Factures(fileDirectory);
	}

	Factures(File fileDirectory){

		ScannerFolder scanner=new ScannerFolder();
		scanner.setExtention(".pdf");
		scanner.setDirectory(fileDirectory);
		List<File> files=scanner.find();
		for (File file : files) {
			if(!Value.runImport) break;
			GUI.updateUI(file.getName());				
			MonteurFacture monteur=new MonteurFacture(file);			
			Facture facture=monteur.getFacture();
			this.addFacture(facture);

		}

		scanner.setExtention("txt");
		files=scanner.find();

		for (File file : files) {			
			if(!Value.runImport) break;
			if(!file.getName().equalsIgnoreCase(fr.map.ValueMap.nameFileLocaDomicile)) {
				importFromTxt(file);
			}
			
		}
	}

	private Boolean importFromTxt(File file) {

		if(!file.isFile())  return false;
		if(!file.canRead()) return false;

		ArrayList<String> arrayString=new ArrayList<String>();
		String[] proto= {""};

		try{
			InputStream flux=new FileInputStream(file); 
			InputStreamReader lecture=new InputStreamReader(flux);
			BufferedReader buff=new BufferedReader(lecture);

			String ligne;
			while ((ligne=buff.readLine())!=null){
				if(!Value.runImport) break;

				if(ligne.startsWith("Facture:")) {

					if(!arrayString.isEmpty()) {
						MonteurFacture monteur=new MonteurFacture(arrayString.toArray(proto),file);			
						
						Facture facture=monteur.getFacture();						
						addFacture(facture);
						arrayString=new ArrayList<String>();		
					}						
				}

				else if(!ligne.isEmpty()){
					arrayString.add(ligne);
				}

			}
			buff.close(); 		
		}		
		catch (Exception e){
			System.out.println(e.toString());	
			return false;
		}

		MonteurFacture monteur=new MonteurFacture(arrayString.toArray(proto),file);	
		
		Facture facture=monteur.getFacture();			
		addFacture(facture);

		return true;
	}    

	boolean isEmpty() {
		return arrayListFacture.isEmpty();
	}

	private void addFacture(Facture f) {

		if(f!=null&&!f.isEmpty()) {
			Boolean isAlready=false;
			for (Facture facture:arrayListFacture) {
				if(facture.equals(f)) {
					isAlready=true;
					break;
				}
			}
			if(!isAlready)	this.arrayListFacture.add(f);
		}
	}

	public int getNbFacture() {
		return arrayListFacture.size();
	}

	ArrayList<Biker> getListBiker(String type){

		ArrayList<Biker> res=new ArrayList<Biker>();

		for (Facture f:this.arrayListFacture) {

			if(res.isEmpty()) {
				BikerImp b=new BikerImp();
				b.setNameBiker(f.getNameRegroupage(type));				
				b.addFacture(f);
				res.add(b);
			}
			else {
				boolean newBiker=true;


				for (Biker b:res) {					
					if(b.getNameBiker().equals(f.getNameRegroupage(type))) {
						newBiker=false;
						b.addFacture(f);
						break;
					}
				}

				if(newBiker) {				
					BikerImp b=new BikerImp();
					b.setNameBiker(f.getNameRegroupage(type));					
					b.addFacture(f);
					res.add(b);
				}

			}

		}


		return res;
	}	

	public String toString() {
		String res="";
		for (Facture f:this.arrayListFacture) {
			res+=f.toString();
		}
		return res;
	}

	ArrayList<Facture> getFacture(){
		return arrayListFacture;
	}

	public Facture getFacture(String nameFile) {
		
		for (Facture f:this.arrayListFacture) {			
			if(f.getNameFile().contentEquals(nameFile)) return f;
		}
		return null;
	}
}
