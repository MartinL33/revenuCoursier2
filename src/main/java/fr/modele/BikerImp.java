package fr.modele;

public class BikerImp extends Biker implements Cloneable {

	void addCloneShift(Shift shift){
		assert(shift!=null);
		assert(shift.getDuree()>0);	
		Shift shiftArajouter=shift.clone2();
		if(shiftArajouter instanceof ShiftImplGPS) {			
			shiftArajouter= ((ShiftImplGPS)shiftArajouter).toShiftImpl();
		}		    
		(this.getList()).add(shiftArajouter);	    
	}

	@Override
	Biker clone2() {	
		return clone();
	}

	@Override
	public BikerImp clone() {	
		BikerImp clone=new BikerImp();
		clone.setProprity(this);
		for(Shift shift:this.getList()){
			clone.addCloneShift(shift);
		}
		return clone;
	}   






}
