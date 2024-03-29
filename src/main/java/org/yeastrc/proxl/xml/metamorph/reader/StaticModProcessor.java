/*
 * Original author: Michael Riffle <mriffle .at. uw.edu>
 *                  
 * Copyright 2018 University of Washington - Seattle, WA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.yeastrc.proxl.xml.metamorph.reader;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.yeastrc.proxl.xml.metamorph.objects.MetaMorphPSM;
import org.yeastrc.proxl.xml.metamorph.objects.MetaMorphPeptide;
import org.yeastrc.proxl.xml.metamorph.objects.MetaMorphReportedPeptide;

/**
 * Use experimental results to infer static mods
 * 
 * @author mriffle
 *
 */
public class StaticModProcessor {

	public static StaticModProcessor createInstance() { return new StaticModProcessor(); }
	
	/**
	 * For the given MetaMorpheus results, find all mods that are present on every instance of
	 * an amino acid--and return these as static mods.
	 * 
	 * @param metaMorphResults
	 * @return
	 */
	public Map<String, BigDecimal> processStaticModsFromResults( Map<MetaMorphReportedPeptide, Collection<MetaMorphPSM>> metaMorphResults ) {
		
		Map<String,BigDecimal> staticMods = new HashMap<>();
		
		Map<String, Collection<BigDecimal>> foundMods = new HashMap<>();
		
		// first find all mods and residues they're on
		for( MetaMorphReportedPeptide reportedPeptide : metaMorphResults.keySet() ) {
			
			addModsToFoundMods( reportedPeptide.getPeptide1(), foundMods );
			if( reportedPeptide.getPeptide2() != null )
				addModsToFoundMods( reportedPeptide.getPeptide2(), foundMods );
		}
		
		// now check to see if any of these mods are on every instance of a given residue
		// if so, that is a static/fixed mod.
		for( MetaMorphReportedPeptide reportedPeptide : metaMorphResults.keySet() ) {
			
			if( foundMods.keySet().size() < 1 )
				break;	// no need to keep looking, there are no static mods.
			
			{
				Map<String, Collection<BigDecimal>> modsToDelete = getModsToDelete( reportedPeptide.getPeptide1(), foundMods );
				removeModsToDelete( modsToDelete, foundMods );
			}
			
			if( reportedPeptide.getPeptide2() != null ) {
				Map<String, Collection<BigDecimal>> modsToDelete = getModsToDelete( reportedPeptide.getPeptide2(), foundMods );
				removeModsToDelete( modsToDelete, foundMods );
			}
			
		}
		
		
		// add the remaining mods (which are all static mods) to the static mods data structure
		for( String residue : foundMods.keySet() ) {
			
			if( foundMods.get( residue ).size() > 1 )
				throw new RuntimeException( "Found more than 1 static mod for: " + residue + ": " + StringUtils.join( foundMods.get( residue ), "," ) );

			for( BigDecimal mod : foundMods.get( residue ) )
				staticMods.put( residue, mod );
			
		}
		
		return staticMods;
	}
	
	
	private Map<String, Collection<BigDecimal>> getModsToDelete( MetaMorphPeptide peptide, Map<String, Collection<BigDecimal>> foundMods ) {
		
		try {				
			
			Map<String, Collection<BigDecimal>> modsToDelete = new HashMap<>();
			
			if( peptide.getModifications() == null )
				return modsToDelete;
			
			for( String residue : foundMods.keySet() ) {
				
				String sequence = peptide.getSequence();
	
				if( !sequence.contains( residue ) )
					continue;
				
				int index = sequence.indexOf( residue );
				while (index >= 0) {
	
					int position = index + 1;
	
					// this position of the residue in this peptide has no mods defined.
					// this means that there are no static mods on this amino acid in this search
					if( !peptide.getModifications().containsKey( position ) ) {
						if( !modsToDelete.containsKey( residue ) )
							modsToDelete.put( residue, new HashSet<>() );
						
						modsToDelete.get( residue ).addAll( foundMods.get( residue ) );
						
						break;	// don't have to keep looking for more positions of this amino acid
					}
					
					// look at all mod values found for this amino acid and see if any of them
					// aren't mod masses for this residue in this peptide.
					for( BigDecimal modMass : foundMods.get( residue ) ) {
						
						if( !peptide.getModifications().get( position ).contains( modMass ) ) {
							
							if( !modsToDelete.containsKey( residue ) )
								modsToDelete.put( residue, new HashSet<>() );
							
							modsToDelete.get( residue ).add( modMass );
						}
						
					}
					
				    index = sequence.indexOf(residue, index + 1);
				}
				
				
			}
			
			
			return modsToDelete;
		} catch( Throwable t ) {
			
			System.err.println( "\n\nError processing peptide: " + peptide );
			throw t;
			
		}
	}
	
	
	/**
	 * Take in a data structure for the mods to remove, and remove them from "foundMods"
	 * @param modsToDelete
	 * @param foundMods
	 */
	private void removeModsToDelete( Map<String, Collection<BigDecimal>> modsToDelete, Map<String, Collection<BigDecimal>> foundMods ) {

		for( String residue : modsToDelete.keySet() ) {
			
			if( !foundMods.containsKey( residue ) )
				continue;
			
			for( BigDecimal mod : modsToDelete.get( residue ) )
				foundMods.get( residue ).remove( mod );
				
			if( foundMods.get( residue ).size() < 1 )
				foundMods.remove( residue );
			
		}
	}
	
	
	/**
	 * Add all mods found on the given peptide to the "foundMods" data structure
	 * 
	 * @param peptide
	 * @param foundMods
	 */
	private void addModsToFoundMods( MetaMorphPeptide peptide, Map<String, Collection<BigDecimal>> foundMods ) {

		if( peptide.getModifications() == null || peptide.getModifications().keySet().size() < 1 )
			return;

		for( int position : peptide.getModifications().keySet() ) {
			String residue = null;

			if(position == 0) {
				residue = "n";
			} else if(position == peptide.getSequence().length() + 1) {
				residue = "c";
			} else {
				residue = peptide.getSequence().substring( position - 1, position );
			}

			// assume there are no static mods defined for n- or c-terminus
			if(!(residue.equals("n")) && !(residue.equals("c"))) {
				if (!foundMods.containsKey(residue))
					foundMods.put(residue, new HashSet<>());

				foundMods.get(residue).addAll(peptide.getModifications().get(position));
			}
		}
	}
	
	
}
