package org.yeastrc.proxl.xml.metamorph.utils;

public class ProteinInferenceUtils {

	/**
	 * Returns true if the protein sequence contains the peptide sequence. No check is made
	 * for tryptic ends. I/L are treated as the same residue for matching.
	 * 
	 * @param proteinSequence
	 * @param reportedPeptide
	 * @return
	 */
	public static boolean proteinContainsReportedPeptide( String proteinSequence, String reportedPeptide ) {
		
		// handle leucine, iso-leucine
		reportedPeptide = reportedPeptide.replaceAll( "L", "I" );
		proteinSequence = proteinSequence.replaceAll( "L",  "I" );
		
		if( proteinSequence.contains( reportedPeptide ) )
			return true;
		
		return false;
	}
	
}
