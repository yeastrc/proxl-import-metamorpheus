package org.yeastrc.proxl.xml.metamorph.utils;

import java.math.BigDecimal;
import java.util.Map;

import org.yeastrc.proxl.xml.metamorph.linkers.MetaMorphLinker;
import org.yeastrc.proxl.xml.metamorph.objects.MetaMorphPeptide;

import net.systemsbiology.regis_web.pepxml.MsmsPipelineAnalysis.MsmsRunSummary.SpectrumQuery.SearchResult.SearchHit;

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

	/**
	 * Given the supplied "massDiff", if it corresponds to a known monolink mass
	 * for the given linker, return that known monolink mass. Otherwise return null.
	 * 
	 * @param massDiff
	 * @param linker
	 * @return
	 */
	public static Double getDeadEndModMass( Double massDiff, MetaMorphLinker linker ) {
				
		if( linker.getMonolinkMasses() != null ) {
			
			for( Double d : linker.getMonolinkMasses() ) {
								
				if( Math.abs( d - massDiff ) < 0.01 ) {
					return d;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Return true if this search hit is a deadend.
	 * 
	 * @param searchHit
	 * @param linker
	 * @return
	 */
	public static boolean isDeadEndHit( SearchHit searchHit, MetaMorphLinker linker ) {
				
		Double reportedMassDiff = Double.valueOf( searchHit.getMassdiff() );
		if( getDeadEndModMass( reportedMassDiff, linker ) != null )
			return true;
		
		return false;		
	}
	
	
}
