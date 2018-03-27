package org.yeastrc.proxl.xml.metamorph.utils;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.yeastrc.proxl.xml.metamorph.objects.MetaMorphPeptide;

public class ModUtils {

	public static boolean isStaticMod( String residue, BigDecimal modMass, Map<String,BigDecimal> staticMods ) {
		
		if( staticMods.containsKey( residue ) && staticMods.get( residue ).equals( modMass ) )
			return true;
		
		return false;
	}
	
	public static void removeStaticModsFromPeptide( MetaMorphPeptide peptide, Map<String,BigDecimal> staticMods ) {
		
		if( peptide.getModifications() == null ) return;
		if( peptide.getModifications().keySet().size() < 1 ) return;
		
		Map<Integer, Collection<BigDecimal>> newModMap = new HashMap<>();
		
		
		for( int position : peptide.getModifications().keySet() ) {
			String residue = getResidueAtPosition( peptide, position );
			
			for( BigDecimal modMass : peptide.getModifications().get( position ) ) {
				
				if( isStaticMod( residue, modMass, staticMods ) )
					continue;
				
				if( !newModMap.containsKey( position ) )
					newModMap.put( position, new HashSet<>() );
				
				newModMap.get( position ).add( modMass );
			}
		}
		
		peptide.setModifications( newModMap );
	}
	
	
	public static String getResidueAtPosition( MetaMorphPeptide peptide, int position ) {
		return peptide.getSequence().substring( position - 1, position );
	}
	
	
}
