package fr.modele;

import static fr.modele.Text.messEcritureFichier;
import static fr.modele.Value.regroupageSelected;
import static fr.algorithmes.Utilitaire.getMultiSeparateur;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import fr.gui.GUI;

public class EcrireBikers {

	private Biker[] bikers = null;  
	private Biker bikerTotal= null;
	private File dossier = null;

	public void setBikers(Biker[] bikers) {
		assert  bikers != null; 
		this.bikers = bikers;
	}

	public void setBikerTotal(Biker bikerTotal) {
		assert  bikerTotal != null; 
		this.bikerTotal = bikerTotal;
	}

	public void setDossier(File dossier) {
		assert  dossier != null; 
		assert dossier.isDirectory();
		assert dossier.canWrite();
		this.dossier = dossier;
	}

	public void write() {

		String pathFileToWrite = getPathtoWrite();

		try {
			BufferedWriter bw=new BufferedWriter(new FileWriter(pathFileToWrite));
			writeEntete(bw);

			if(bikers.length>1)  {
				ecrireMultiBker(bw);
			}
			else {
				writeOneBiker(bw);
			}
			bw.close();

		} catch (IOException e) {			
			GUI.messageConsole(e.toString());
		} 
	}

	private String getPathtoWrite() {

		String pathFileToWrite = dossier.getPath()+File.separator+"statDeliveroo-"+regroupageSelected.toString();
		if(Value.typeRegroupageSelected.equals(Value.typeParVille)) {
			pathFileToWrite+="-"+Value.typeRegroupageSelected;
		}
		pathFileToWrite+=".csv";
		String mess=messEcritureFichier+"statDeliveroo-"+regroupageSelected.toString()+".csv";		
		GUI.updateUI(mess);
		return pathFileToWrite;
	}

	private void writeEntete(BufferedWriter bw) throws IOException {

		String line= "Biker"+ regroupageSelected.separateurEnteteDebut;
		if(bikers.length>1) line+="total"+regroupageSelected.separateurEnteteEnd+getMultiSeparateur(7);
		for(Biker b:bikers) {
			line+=b.getNameBiker()+regroupageSelected.separateurEnteteEnd+Value.separateurCSV;
		}

		bw.write(line);
		bw.newLine();


		line= regroupageSelected.getEnteteResultDate();
		if(bikers.length>1) line+=regroupageSelected.getEnteteResultEnd(bikerTotal)+getMultiSeparateur(7);
		for(Biker biker:bikers) {
			line+=regroupageSelected.getEnteteResultEnd(biker)+Value.separateurCSV;
		}

		bw.write(line);
		bw.newLine();
	}

	private void writeOneBiker(BufferedWriter bw) throws IOException {

		Shift shiftTotalPrecedent=null;
		for(Shift shiftBiker:bikers[0].getList()){
			regroupageSelected.setShift(shiftBiker);                 
			if(regroupageSelected.isPasserUneLigneFichierResult(shiftTotalPrecedent)) {
				bw.newLine();
			}
			shiftTotalPrecedent=shiftBiker;

			String line=regroupageSelected.getStringRegroupageDate()+
					regroupageSelected.lineBiker();

			bw.write(line); 
			bw.newLine();

		}
	}

	private void ecrireMultiBker(BufferedWriter bw) throws IOException{

		String line;
		Shift shiftBiker;
		Shift shiftTotalPrecedent=null;

		for(Shift shiftTotal:bikerTotal.getList()){

			regroupageSelected.setShift(shiftTotal);  

			if(regroupageSelected.isPasserUneLigneFichierResult(shiftTotalPrecedent)) {
				bw.newLine();
			}
			shiftTotalPrecedent=shiftTotal;

			line=regroupageSelected.getStringRegroupageDate();	    
			line+=regroupageSelected.lineBikerCA()+getMultiSeparateur(6);

			for(Biker biker:bikers) {		
				
				
				line+=biker.getLineToWrite(shiftTotal);	
			}				

			bw.write(line); 
			bw.newLine();	
		}		
	}


}
