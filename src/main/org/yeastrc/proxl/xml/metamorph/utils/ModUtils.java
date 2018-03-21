package org.yeastrc.proxl.xml.metamorph.utils;

import java.math.BigDecimal;

import org.yeastrc.proxl.xml.metamorph.reader.ConfReader;

public class ModUtils {

	/**
	 * Always false.
	 * 
	 * @param modMass
	 * @param kojakConf
	 * @return
	 * @throws Exception
	 */
	public static boolean isMonolink( BigDecimal modMass, ConfReader confReader ) throws Exception {
		
		return false;
	}
	
	/**
	 * Checks to see if the reported modMass matches a static mod. Does this by rounding the mod mass to
	 * three decimal places and comparing to the static mods in the Kojak conf file (also rounded to three
	 * decimal places).
	 * 
	 * @param residue
	 * @param modMass
	 * @param confReader
	 * @return
	 * @throws Exception
	 */
	public static boolean isStaticMod( String residue, BigDecimal modMass, ConfReader confReader ) throws Exception {
		
		if( confReader.getStaticModifications() == null || confReader.getStaticModifications().keySet().size() < 1 ||
				!confReader.getStaticModifications().containsKey( residue ) )
			return false;
		
		modMass = modMass.setScale( 3, BigDecimal.ROUND_HALF_UP );
		
		BigDecimal staticModMass = confReader.getStaticModifications().get( residue );
		staticModMass = staticModMass.setScale( 3, BigDecimal.ROUND_HALF_UP );
		
		if( staticModMass.equals( modMass ) )
			return true;
		
				
		return false;
	}
	
}
