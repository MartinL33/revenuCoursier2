package fr.modele;


import static fr.modele.Value.regroupageSelected;

import java.io.File;

import org.jfree.data.category.DefaultCategoryDataset;

import fr.gui.GUI;
import fr.map.EcrireMap;

public class DataRegroupe {

	static private Biker[] bikersRegroupe;
	static private BikerImp paysRegroupe;
	static final public String nameTotal="total";


	public Biker getBiker(String nameBiker) {
		if(nameBiker.equalsIgnoreCase(nameTotal)) {
			return paysRegroupe;
		}
		for(Biker biker:bikersRegroupe) {
			if(biker.getNameBiker().equalsIgnoreCase(nameBiker)) {
				return biker;
			}
		}

		return null;
	}

	public void getResult(DataFromDirectory dataSource) {
		
		
		BikerImp paysARegroupe=new BikerImp();
		paysARegroupe.setNameBiker(nameTotal);
		for(int iBiker=0;iBiker<dataSource.getNbBiker();iBiker++){			
			paysARegroupe.addCloneList(dataSource.getBiker(iBiker).getList());
		}
		
		paysRegroupe=(BikerImp) paysARegroupe.bikerCloneWithShiftRegroupe();
	

		if(regroupageSelected.isParAnnee) {		
			GUI.updateUI("get tableau année");
			bikersRegroupe=paysARegroupe.getTabBikerParAnnee();

			for(int iBiker=0;iBiker<bikersRegroupe.length;iBiker++){				
				bikersRegroupe[iBiker]=bikersRegroupe[iBiker].bikerCloneWithShiftRegroupe();					
			}			
		}	
		else {
			bikersRegroupe=  new Biker[dataSource.getNbBiker()];
			
			for(int iBiker=0;iBiker<bikersRegroupe.length;iBiker++){					
				bikersRegroupe[iBiker]=dataSource.getBiker(iBiker).bikerCloneWithShiftRegroupe();			
			}
		}
		
		

	}

	public void ecrireResult(File fileExport) {


		EcrireBikers ecrireBikers=new EcrireBikers();
		ecrireBikers.setBikers(bikersRegroupe);
		ecrireBikers.setDossier(fileExport);
		ecrireBikers.setBikerTotal(paysRegroupe);
		ecrireBikers.write();
	
		ecrireMaps(fileExport);	    
	}

	DefaultCategoryDataset createDataset() {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
		if(bikersRegroupe.length>1) {
			for(Shift shift:paysRegroupe.getList()){
				regroupageSelected.setShift(shift); 
				dataset.addValue( regroupageSelected.getCAhoraire(), "CA/h (€/h)" , regroupageSelected.getDateChart() );
				dataset.addValue( regroupageSelected.getDureeChart() , regroupageSelected.getTitleDureeChart() , regroupageSelected.getDateChart() );
			}
		}
		else {
			for(Shift shift:bikersRegroupe[0].getList()){
				regroupageSelected.setShift(shift); 
				dataset.addValue( regroupageSelected.getCAhoraire(), "CA/h (€/h)" , regroupageSelected.getDateChart() );
				dataset.addValue( regroupageSelected.getDureeChart() , regroupageSelected.getTitleDureeChart() , regroupageSelected.getDateChart() );
			}
		}
		return dataset;
	}



	private void ecrireMaps(File fileExport) {

		if(Value.isExportCarte) {
			for(Biker biker:bikersRegroupe){			
				if(biker instanceof BikerImpGPS) {
					BikerImpGPS bikerGPS = (BikerImpGPS) biker;
					EcrireMap ecrireMap = new EcrireMap();
					ecrireMap.setMap(bikerGPS.getMap());
					ecrireMap.setCouleur(Value.couleurSelected);
					ecrireMap.setIsSupprimerShiftSansGPSfiable(Value.isSupprimerShiftSansGPSfiable);
					ecrireMap.setDossier(Action.getFileExport());
					
					ecrireMap.setFormat(Value.formatSelected);		
					ecrireMap.ecrireCarte();
				}
			}
		}	
	}

}
