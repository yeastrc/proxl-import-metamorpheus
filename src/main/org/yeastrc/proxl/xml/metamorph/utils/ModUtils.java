package org.yeastrc.proxl.xml.metamorph.utils;

import java.math.BigDecimal;
import java.util.Map;

import org.yeastrc.proxl.xml.metamorph.objects.MetaMorphPeptide;

public class ModUtils {

	public static boolean isStaticMod( String residue, BigDecimal modMass, Map<String,BigDecimal> staticMods ) {
		
		if( staticMods.containsKey( residue ) && staticMods.get( residue ).equals( modMass ) )
			return true;
		
		return false;
	}
	
	
	public static String getResidueAtPosition( MetaMorphPeptide peptide, int position ) {
		return peptide.getSequence().substring( position - 1, position );
	}
	
	public static boolean peptideHasNonStaticMods( MetaMorphPeptide peptide, Map<String,BigDecimal> staticMods ) {
		
		if( peptide.getModifications() == null || peptide.getModifications().keySet().size() < 1 )
			return false;
		
		for( int position : peptide.getModifications().keySet() ) {
			
			String residue = getResidueAtPosition( peptide, position );
			if( !staticMods.containsKey( residue ) )
				return true;	// not a static mod
			
			for( BigDecimal modMass : peptide.getModifications().get( position ) ) {
				if( !staticMods.get( residue ).equals( modMass ) )
					return true;	// not a static mod
			}
		}
		
		return false;	// only found static mods
		
	}
	
	
}
