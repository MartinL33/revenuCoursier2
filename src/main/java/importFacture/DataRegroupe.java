package importFacture;


import static importFacture.Value.regroupageSelected;

import java.io.File;

import org.jfree.data.category.DefaultCategoryDataset;

import GUI.GUI;

public class DataRegroupe {
    
    static private Biker[] bikersRegroupe;
    
    static private BikerImp paysRegroupe;
    //static private Map map;
    static final public String nameTotal="total";
    
    
    public Biker getBiker(String nameBiker) {
	if(nameBiker.contentEquals(nameTotal)) {
	    return paysRegroupe;
	}
	for(Biker biker:bikersRegroupe) {
	    if(biker.getNameBiker().contentEquals(nameBiker)) {
		return biker;
	    }
	}
	
	return null;
    }
    
    public void getResult(DataFromDirectory dataSource) {
	BikerImp paysARegroupe=new BikerImp();
	 paysARegroupe.setNameBiker(nameTotal);
	for(int iBiker=0;iBiker<dataSource.getNbBiker();iBiker++){			
	    paysARegroupe.addList(dataSource.getBiker(iBiker).getList());
	}

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

	if(bikersRegroupe.length>1) {					
	    paysRegroupe=(BikerImp) paysARegroupe.bikerCloneWithShiftRegroupe();
	}	    
    }
    
    public void ecrireResult(File fileExport) {
	
	
	EcrireBikers ecrireBikers=new EcrireBikers();
	ecrireBikers.setBikers(bikersRegroupe);
	ecrireBikers.setDossier(fileExport);
	ecrireBikers.setBikerTotal(paysRegroupe);
	ecrireBikers.write();
	
	
 	//ecrireBikers(fileExport);
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

	/*TODO

		if(Value.isExportCarte&&DataBikersSource.getBikerMap()!=null&&Value.historiquePositionFile!=null) {
			Map map= new Map(DataBikersSource.getBikerMap());
			GUI.updateUI("écriture carte en format "+Value.formatSelected +" couleur en fonction "+Value.couleurSelected);
			map.ecrireCarte(fileExport);
		}
	 */

	// TODO Auto-generated method stub

    }

}
