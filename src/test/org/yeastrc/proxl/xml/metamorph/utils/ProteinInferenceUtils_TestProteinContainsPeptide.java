package org.yeastrc.proxl.xml.metamorph.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ProteinInferenceUtils_TestProteinContainsPeptide {

	@Test
	public void _test() throws Exception {

		{
			String proteinSequence = "PEPTIDEVMKGVDDLDFFIGDEAIEKPTYATKPEPTIDE";
			String peptideSequence = "VMKGVDDLDFFIGDEAIEKPTYATK";
		
			assertTrue( ProteinInferenceUtils.proteinContainsReportedPeptide( proteinSequence, peptideSequence ) );
		}
		
		{
			String proteinSequence = "VMKGVDDLDFFIGDEAIEKPTYATKPEPTIDE";
			String peptideSequence = "VMKGVDDLDFFIGDEAIEKPTYATK";
		
			assertTrue( ProteinInferenceUtils.proteinContainsReportedPeptide( proteinSequence, peptideSequence ) );
		}
		
		{
			String proteinSequence = "PEPTIDEVMKGVDDLDFFIGDEAIEKPTYATK";
			String peptideSequence = "VMKGVDDLDFFIGDEAIEKPTYATK";
		
			assertTrue( ProteinInferenceUtils.proteinContainsReportedPeptide( proteinSequence, peptideSequence ) );
		}
		
		{
			String proteinSequence = "VMKGVDDLDFFIGDEAIEKPTYATK";
			String peptideSequence = "VMKGVDDLDFFIGDEAIEKPTYATK";
		
			assertTrue( ProteinInferenceUtils.proteinContainsReportedPeptide( proteinSequence, peptideSequence ) );
		}
		
		
		// I/L substitution
		{
			String proteinSequence = "PEPTIDEVMKGVDDLDFFIGDEAIEKPTYATKPEPTIDE";
			String peptideSequence = "VMKGVDDLDFFLGDEAIEKPTYATK";
		
			assertTrue( ProteinInferenceUtils.proteinContainsReportedPeptide( proteinSequence, peptideSequence ) );
		}
		{
			String proteinSequence = "PEPTIDEVMKGVDDLDFFIGDEALEKPTYATKPEPTIDE";
			String peptideSequence = "VMKGVDDLDFFLGDEAIEKPTYATK";
		
			assertTrue( ProteinInferenceUtils.proteinContainsReportedPeptide( proteinSequence, peptideSequence ) );
		}

	
		
		
		
		// should all be false
		{
			String proteinSequence = "PEPTIDEVMKGVDDLDIGDEAIEKPTYATKPEPTIDE";
			String peptideSequence = "VMKGVDDLDFFIGDEAIEKPTYATK";
		
			assertFalse( ProteinInferenceUtils.proteinContainsReportedPeptide( proteinSequence, peptideSequence ) );
		}
		{
			String proteinSequence = "";
			String peptideSequence = "VMKGVDDLDFFIGDEAIEKPTYATK";
		
			assertFalse( ProteinInferenceUtils.proteinContainsReportedPeptide( proteinSequence, peptideSequence ) );
		}
		
		
		{
			String proteinSequence = "PEPTIDEVMKGVDDLDIGDEAIEKPTYATKPEPTIDE";
			String peptideSequence = "";
		
			try {
				ProteinInferenceUtils.proteinContainsReportedPeptide( proteinSequence, peptideSequence );
				fail( "Should have failed." );
			} catch( Exception e ) { ; }
			
		}
		
		
	}
	
}
