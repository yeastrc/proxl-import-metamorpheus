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

package org.yeastrc.proxl.xml.metamorph.utils;

import java.math.BigDecimal;
import java.util.Map;

import org.yeastrc.proxl.xml.metamorph.linkers.MetaMorphLinker;
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
	
	
	public static boolean isDeadEndMod( Double modMass, MetaMorphLinker linker ) {
		
		// linker has no monolink masses defined, not a deadend
		if( linker.getMonolinkMasses() == null || linker.getMonolinkMasses().size() < 1 )
			return false;
		
		// test the mass of this mod against the known deadend masses. if it is a deadend, this is a deadend hit
		for( double linkerMonolinkMass : linker.getMonolinkMasses() ) {
			
			// if they are within 0.01 of each other, consider them the same
			if( Math.abs( linkerMonolinkMass - modMass ) < 0.01 )
				return true;
			
		}
		
		return false;
	}
	
	
}
