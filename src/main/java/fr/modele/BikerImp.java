package fr.modele;

public class BikerImp extends Biker implements Cloneable {

    void addShift(Shift shift){
	assert(shift!=null);
	assert(shift.getDuree()>0);	

	if(shift instanceof ShiftImplGPS) {
	    shift=(ShiftImpl) shift;
	}		    
	(this.getList()).add(shift);	    
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
	    clone.addShift(shift.clone2());
	}
	return clone;
    }   

    




}
