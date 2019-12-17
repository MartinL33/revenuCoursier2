/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.modele;



import static fr.modele.Text.messPasdeFacture;

import java.io.File;
import java.lang.Thread.State;

import fr.algorithmes.ScannerFolder;
import fr.gui.FrameChart;
import fr.gui.GUI;


/**
 * <p>Action class.</p>
 *
 * @author martin
 * @version 1.0
 */
public class Action{


	private static File fileImport;
	private static File fileExport;	    
	static DataFromDirectory dataSource=new DataFromDirectory();
	static DataRegroupe dataRegroupe=new DataRegroupe();
	private final static Action action=new Action();
	private static RunImpl actionThread=null;

	private static void reset() {
		dataSource=new DataFromDirectory();
		GUI.setLancer();
	}

	static File getFileExport() {	
		return fileExport;
	}

	public static void setFileImport(File fileImport) {
		Action.fileImport = fileImport;	
		reset();
	} 
	
	public static void setFileExport(File fileExport) {
		Action.fileExport = fileExport;
	}

	public static boolean hasFileImport() {
		return Action.fileImport!=null&&Action.fileImport.exists()&&fileImport.isDirectory();
	}	
	
	public static boolean detectFilesMaps(File f) {		
		ScannerFolder scannerFolder=new ScannerFolder();
		scannerFolder.setDirectory(f);
		scannerFolder.setExtention(".json");
		return scannerFolder.hasResult();		
	}	

	public static void execute() {

		if(!hasFileImport()){
			GUI.updateUI(Text.messSelectionDossier);
		}

		else if(actionThread==null) {
			actionThread=action.new RunImpl();
			actionThread.start();
		}

		else if(actionThread.getState()==State.RUNNABLE) {
			actionThread.arreter();
			actionThread=null;
			reset();
		}

		else if(actionThread.getState()==State.TERMINATED) {
			actionThread=action.new RunImpl();
			actionThread.start();
		}	

	}

	

	class RunImpl extends Thread {


		public RunImpl() {
			super();
		}

		public void arreter() {
			Value.runImport = false;
		}

		@Override
		public void run(){

			GUI.setStop();
			if(dataSource.isEmpty()) {					
				
				dataSource.importDataFromDirectory(fileImport);				
			}
			if(!Value.runImport) {
				GUI.updateUI("Import stoppé");
			}
			else {
				if(dataSource.isEmpty())    {
					GUI.updateUI(messPasdeFacture);
					

				}

				else {
					
					dataRegroupe.getResult(dataSource);
					setFileExport(fileImport);		
					dataRegroupe.ecrireResult(fileExport);
					//ouvertureDossierResult();				

					if(!Value.debug) {
						new FrameChart(dataRegroupe.createDataset());
					}			

					GUI.updateUI("résultats détaillés dans le fichier "+"statDeliveroo-"+Value.regroupageSelected.textCombo+".csv");

					
				}
			}
			Value.runImport = true;
			GUI.setLancer();
		}

	}
}

