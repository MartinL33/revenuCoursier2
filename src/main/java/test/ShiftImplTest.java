package test;

import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

import importFacture.Shift;
import importFacture.ShiftImpl;
import junit.framework.TestCase;


public class ShiftImplTest extends TestCase{

	@Test
	public void testCloneWithCalendars() {
		ShiftImpl shift1 = new ShiftImpl();
		Calendar cDebut = new GregorianCalendar();
		Calendar cFin = new GregorianCalendar();
		cFin.add(Calendar.HOUR, 6);
		shift1.setCalendars(cDebut, cFin);
		shift1.setNb(4);
		shift1.setPrime(8);
		shift1.setRevenue(55);

		Calendar cFin2 = (Calendar) cDebut.clone();
		cFin2.setTimeInMillis(cDebut.getTimeInMillis() + 500000);

		ShiftImpl shiftClone = (ShiftImpl) shift1.cloneWithCalendars(cDebut, cFin2);
		int nombremillisecondeInOneHour = 1000 * 60 * 60;

		assertTrue("Duree fausse :" + shiftClone.getDuree(),
				Math.abs(shiftClone.getDuree()-500000.0/nombremillisecondeInOneHour)<0.01);

		if(shift1.getPrime()/shift1.getDuree()-shiftClone.getPrime()/shiftClone.getDuree()>0.001) {
			fail("Prime fausse");
		}

	}

	@Test
	public void testEclaterShift() {

		// test 12h04->13h05 doit donner 12h04->13h00 13h00->13h05
		ShiftImpl shift1 =  new ShiftImpl();
		Calendar cDebut = new GregorianCalendar(2000, 1, 3, 12, 4, 5);
		Calendar cFin = new GregorianCalendar(2000, 1, 3, 13, 5, 5);
		shift1.setCalendars(cDebut, cFin);
		shift1.setNb(4);
		shift1.setPrime(8);
		shift1.setRevenue(55);

		ArrayList<Shift> l = shift1.eclaterShift();
		assertTrue("Size fausse" + l.size(), l.size() == 2);
		testRevenue(shift1, l);
	}
	
	@Test
	public void testEclaterShift2() {
		// test 12h04->12h40 doit donner 12h04->12h40
		ShiftImpl shift1 = new ShiftImpl();
		Calendar cDebut = new GregorianCalendar(2000, 1, 3, 12, 4, 5);
		Calendar cFin = new GregorianCalendar(2000, 1, 3, 12, 40, 5);
		shift1.setCalendars(cDebut, cFin);
		ArrayList<Shift> l = shift1.eclaterShift();
		testRevenue(shift1, l);
		assertTrue("Size fausse" + l.size(), l.size() == 1);
	}
	
	@Test
	public void testEclaterShift3() {

		// test 13h00->14h04 doit donner 13h00->14h00 14h00->14h04
		ShiftImpl shift1 = new ShiftImpl();
		Calendar cDebut = new GregorianCalendar(2000, 1, 3, 13, 00, 5);
		Calendar cFin = new GregorianCalendar(2000, 1, 3, 14, 04, 5);
		shift1.setCalendars(cDebut, cFin);
		ArrayList<Shift> l = shift1.eclaterShift();
		testRevenue(shift1,l);
		assertTrue("Size fausse" + l.size(), l.size() == 2);
	}
	
	@Test
	public void testEclaterShift4() {
		// test 12h04->15h05 doit donner 12h04->13h00 13h00->14h00 14h00->15h00 15h00->15h05
		ShiftImpl shift1 = new ShiftImpl();
		Calendar cDebut = new GregorianCalendar(2000, 1, 3, 12, 4, 5);
		Calendar cFin = new GregorianCalendar(2000, 1, 3, 15, 5, 0);
		shift1.setCalendars(cDebut, cFin);
		ArrayList<Shift> l = shift1.eclaterShift();
		testRevenue(shift1, l);
		assertTrue("Size fausse" + l.size(), l.size() == 4);
	}
	
	@Test
	public void testEclaterShift5() {

		// test 12h04->15h00 doit donner 12h04->13h00 13h00->14h00 14h00->15h00
		ShiftImpl shift1 = new ShiftImpl();
		Calendar cDebut = new GregorianCalendar(2000, 1, 3, 12, 4, 5);
		Calendar cFin = new GregorianCalendar(2000, 1, 3, 15, 00, 0);
		shift1.setCalendars(cDebut, cFin);
		ArrayList<Shift> l = shift1.eclaterShift();
		testRevenue(shift1, l);
		assertTrue("Size fausse" + l.size(), l.size() == 3);
	}

	private void testRevenue(Shift s1, ArrayList<Shift> l) {
		double CA2 = 0;
		for (Shift s:l) {
			CA2 += s.getRevenue();
		}

		if (abs(CA2 - s1.getRevenue()) > 0.001) {
			afficheShift(s1,l);
			fail("Revenu faux");
		}
	}

	private void afficheShift(Shift s1, ArrayList<Shift> l) {
		System.out.println("shift A Faire:\n" + s1.toString());
		System.out.println("res:");
		for (Shift s:l) {
			System.out.println(s.toString());
		}
	}

}
