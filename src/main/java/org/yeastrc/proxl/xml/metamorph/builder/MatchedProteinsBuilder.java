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


package org.yeastrc.proxl.xml.metamorph.builder;

import java.io.File;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.yeastrc.proxl.xml.metamorph.constants.SearchConstants;
import org.yeastrc.proxl.xml.metamorph.objects.MetaMorphReportedPeptide;
import org.yeastrc.proxl.xml.metamorph.utils.*;
import org.yeastrc.proteomics.fasta.*;
import org.yeastrc.proxl_import.api.xml_dto.MatchedProteins;
import org.yeastrc.proxl_import.api.xml_dto.Protein;
import org.yeastrc.proxl_import.api.xml_dto.ProteinAnnotation;
import org.yeastrc.proxl_import.api.xml_dto.ProxlInput;


/**
 * Build the MatchedProteins section of the emozi XML docs. This is done by finding all proteins in the FASTA
 * file that contains any of the peptide sequences found in the experiment. 
 * 
 * This is generalized enough to be usable by any pipeline
 * 
 * @author mriffle
 *
 */
public class MatchedProteinsBuilder {

	public static MatchedProteinsBuilder getInstance() { return new MatchedProteinsBuilder(); }

	/**
	 * Add all target proteins from the FASTA file that contain any of the peptides found in the experiment
	 * to the emozi xml document in the matched proteins section.
	 *
	 * @param proxlInputRoot
	 * @param fastaFile
	 * @param reportedPeptides
	 * @throws Exception
	 */
	public void buildMatchedProteins( ProxlInput proxlInputRoot, File fastaFile, Collection<MetaMorphReportedPeptide> reportedPeptides ) throws Exception {
		

		// the proteins we've found
		Map<String, Collection<FastaProteinAnnotation>> proteins = getProteins( reportedPeptides, fastaFile );
		
		// create the XML and add to root element
		buildAndAddMatchedProteinsToXML( proxlInputRoot, proteins );
		
	}
	
	/**
	 * Do the work of building the matched peptides element and adding to emozi xml root
	 * 
	 * @param proxlInputRoot
	 * @param proteins
	 * @throws Exception
	 */
	private void buildAndAddMatchedProteinsToXML( ProxlInput proxlInputRoot, Map<String, Collection<FastaProteinAnnotation>> proteins ) throws Exception {
		
		MatchedProteins xmlMatchedProteins = new MatchedProteins();
		proxlInputRoot.setMatchedProteins( xmlMatchedProteins );
		
		for( String sequence : proteins.keySet() ) {
			
			if( proteins.get( sequence ).isEmpty() ) continue;
			
			Protein xmlProtein = new Protein();
        	xmlMatchedProteins.getProtein().add( xmlProtein );
        	
        	xmlProtein.setSequence( sequence );
        	        	
        	for( FastaProteinAnnotation anno : proteins.get( sequence ) ) {
        		ProteinAnnotation xmlMatchedProteinLabel = new ProteinAnnotation();
        		xmlProtein.getProteinAnnotation().add( xmlMatchedProteinLabel );
        		
        		xmlMatchedProteinLabel.setName( anno.getName() );
        		
        		if( anno.getDescription() != null )
        			xmlMatchedProteinLabel.setDescription( anno.getDescription() );
        			
        		if( anno.getTaxonomId() != null )
        			xmlMatchedProteinLabel.setNcbiTaxonomyId( new BigInteger( anno.getTaxonomId().toString() ) );
        	}
		}
	}


	/**
	 * Get a map of the distinct target protein sequences mapped to a collection of target annotations for that sequence
	 * from the given fasta file, where the sequence contains any of the supplied peptide sequences
	 *
	 * @param percolatorPeptides
	 * @param fastaFile
	 * @return
	 * @throws Exception
	 */
	private Map<String, Collection<FastaProteinAnnotation>> getProteins( Collection<MetaMorphReportedPeptide> percolatorPeptides, File fastaFile ) throws Exception {
		
		// get a unique set of naked peptide sequence
		Collection<String> nakedPeptideSequences = getNakedPeptideSequences( percolatorPeptides );
		
		Map<String, Collection<FastaProteinAnnotation>> proteinAnnotations = new HashMap<>();

		try ( FASTAFileParser parser = FASTAFileParserFactory.getInstance().getFASTAFileParser(  fastaFile ) ) {

			for( FASTAEntry entry = parser.getNextEntry(); entry != null; entry = parser.getNextEntry() ) {

				for( String nakedSequence : nakedPeptideSequences ) {
					
					if( ProteinInferenceUtils.proteinContainsReportedPeptide( entry.getSequence(), nakedSequence ) ) {
						
						// this protein has a matching peptide
						
						for( FASTAHeader header : entry.getHeaders() ) {
							
							if( !proteinAnnotations.containsKey( entry.getSequence() ) )
								proteinAnnotations.put( entry.getSequence(), new HashSet<FastaProteinAnnotation>() );
							
							FastaProteinAnnotation anno = new FastaProteinAnnotation();
							anno.setName( header.getName() );
							anno.setDescription( header.getDescription() );
		            		
							proteinAnnotations.get( entry.getSequence() ).add( anno );

						}//end iterating over fasta headers
						
						break;// no need to check more peptides for this protein, we found one						

					} // end if statement for protein containing peptide

				} // end iterating over peptide sequences
				
			}// end iterating over fasta entries

		}
		
		return proteinAnnotations;
	}
	
	

	private Collection<String> getNakedPeptideSequences( Collection< MetaMorphReportedPeptide > percolatorPeptides ) {
		
		Collection<String> nakedSequences = new HashSet<>();
		
		for( MetaMorphReportedPeptide percolatorPeptide : percolatorPeptides ) {
			
			nakedSequences.add( percolatorPeptide.getPeptide1().getSequence() );
			
			if( percolatorPeptide.getType() == SearchConstants.LINK_TYPE_CROSSLINK )
				nakedSequences.add( percolatorPeptide.getPeptide2().getSequence() );
		}
		
		
		return nakedSequences;
	}

	
	
	/**
	 * An annotation for a protein in a Fasta file
	 * 
	 * @author mriffle
	 *
	 */
	private class FastaProteinAnnotation {
		
		public int hashCode() {
			
			String hashString = this.getName();
			
			if( this.getDescription() != null )
				hashString += this.getDescription();
			
			if( this.getTaxonomId() != null )
				hashString += this.getTaxonomId().intValue();
			
			return hashString.hashCode();
		}
		
		/**
		 * Return true if name, description and taxonomy are all the same, false otherwise
		 */
		public boolean equals( Object o ) {
			try {
				
				FastaProteinAnnotation otherAnno = (FastaProteinAnnotation)o;
				
				if( !this.getName().equals( otherAnno.getName() ) )
					return false;
				
				
				if( this.getDescription() == null ) {
					if( otherAnno.getDescription() != null )
						return false;
				} else {
					if( otherAnno.getDescription() == null )
						return false;
				}
				
				if( !this.getDescription().equals( otherAnno.getDescription() ) )
					return false;
				
				
				if( this.getTaxonomId() == null ) {
					if( otherAnno.getTaxonomId() != null )
						return false;
				} else {
					if( otherAnno.getTaxonomId() == null )
						return false;
				}
				
				if( !this.getTaxonomId().equals( otherAnno.getTaxonomId() ) )
					return false;
				
				
				return true;
				
			} catch( Exception e ) {
				return false;
			}
		}

		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public Integer getTaxonomId() {
			return taxonomId;
		}

		private String name;
		private String description;
		private Integer taxonomId;

	}
	
}
