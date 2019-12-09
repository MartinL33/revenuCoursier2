package fr.modele;

import java.util.Calendar;

import fr.gui.GUI;


/**
 * <p>Shift class.</p>
 * répresente un shift
 *
 * @author ML
 * @version 1.0
 * @since 2018
 */
public class ShiftImpl extends Shift implements Cloneable {

    @Override
    public Shift clone() {	  

	ShiftImpl clone=null;
	try{	  
	    clone = (ShiftImpl) super.clone();
	    clone.setCalendars((Calendar)this.calendarDebut.clone(), (Calendar)this.calendarFin.clone());
	}
	catch(CloneNotSupportedException e) { 
	    GUI.messageConsole(e.toString());
	}
	return clone;
    }   

    @Override
    public Shift clone2() {	
	return clone();
    }

    @Override
    protected Shift cloneForRegroupage() {	
	return clone();
    }

    

    


    

}
