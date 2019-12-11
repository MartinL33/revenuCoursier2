package fr.map;

import static fr.map.ValueMap.debutHistoriquePositionsGoogleMap;
import static fr.map.ValueMap.finHistoriquePositionsGoogleMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import fr.modele.Biker;
import fr.modele.Shift;


public class Map implements Cloneable{

	private ArrayList<Track> tracks=new ArrayList<Track>();
	private File file=null;

	public Map clone() {
		Map clone=new Map();
		clone.setFile(this.file);
		for(Track t:this.tracks){
			clone.tracks.add(t.clone());
		}	
		return clone;
	}

	public void setFile(File file) {
		this.file=file;
	}

	public File getFile() {
		return file;
	}

	public void addTrack(Track t) {
		tracks.add(t);
	}

	public  ArrayList<Track> getTracks(){
		return tracks;
	}

	public   Track getFirstTrack() {
		return tracks.get(0);
	}

	public Boolean isEmpty() {
		return tracks.isEmpty();
	}

	public int getNbTrack() {
		return tracks.size();
	}

	public long getNbLocalisation() {
		long res=0;
		for(Track t:tracks) {
			res+=t.size();
		}
		return res;
	}

	public String getNameFile() {
		return file.getName().replace(debutHistoriquePositionsGoogleMap, "").replace(finHistoriquePositionsGoogleMap, "");
	}    

	public void fixTracks() {
		
		Long time1 = System.currentTimeMillis();
		for(Track t:tracks){
			t.corrigerTrack();
		}
		diviseTrack(ValueMap.modeDivisionDistance);

		for(Track t:tracks){	       	
			t.douglasPeucker();	     
		}	 
		Long time2 = System.currentTimeMillis();
	//	System.out.println("time fix track "+String.valueOf(((float)(time2-time1))/1000)+" s");
	}




	public void diviseTrack(byte mode) {
		ArrayList<Track> res=new ArrayList<Track>();

		for(Track track:tracks) {
			res.addAll(track.diviseTrack(mode));
		}
		tracks=res;
	}

	@Override
	public String toString() {
		StringBuilder res=new StringBuilder();
		res.append(file.getName()+System.getProperty("line.separator"));
		for(Track t:tracks){
			res.append(System.getProperty("line.separator"));
			for(Localisation l:t.getList()) {
				res.append(l.toString()+System.getProperty("line.separator"));
			}
		}
		return res.toString();
	}

	public void intersectionWithBiker(Biker biker) {

		ArrayList<Track> tracksTemp=new ArrayList<Track>();

		Shift shift=null;
		Track trackTemp=null;

		for(Track t:tracks){	
			for(Localisation localisation:t.getList()) {
				if(shift==null) {
					shift=localisation.findShift(biker);			
				}

				if(shift!=null) {

					if(localisation.isUnderShift(shift)) {
						if(trackTemp==null) {
							trackTemp=new Track(shift);
						}
						trackTemp.add(localisation.getCloneCorrected(shift));		

					}

					// le shift est fini 
					else {
						if(trackTemp.size()>2) {
							
							tracksTemp.add(trackTemp);
							trackTemp=null;
						}			
						shift=localisation.findShift(biker);

						//un autre shift commence
						if(shift!=null) {
							trackTemp =new Track(shift);
							trackTemp.add(localisation.getCloneCorrected(shift));
						}
					}
				}
			}
			if(trackTemp!=null&&trackTemp.size()>2) {

				tracksTemp.add(trackTemp);
			}
		}
		tracks=tracksTemp;

	}

	
	public Map subMap(Date dateDebut,Date dateFin) {
		
		assert (dateFin.getTime()>dateDebut.getTime());
		Track track=new Track(null);
		for(Track t:tracks){		
			for(Localisation l:t.getList()) {
				if(l.getTime()>dateDebut.getTime()
						&&l.getTime()<dateFin.getTime()) {
					track.add(l);
				}
			}
		}
		
		Map res=new Map();
		res.addTrack(track);
		return res;
	}


}
