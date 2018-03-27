package org.yeastrc.proxl.xml.metamorph.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.yeastrc.proxl.xml.metamorph.objects.MetaMorphPeptide;
import org.yeastrc.proxl.xml.metamorph.objects.MetaMorphPeptideBuilder;

public class ModUtils_TestGetResidueAtPosition {

	private MetaMorphPeptide peptide;
	
	@Before
	public void setUp() {

		MetaMorphPeptideBuilder builder = new MetaMorphPeptideBuilder();
		builder.setSequence( "EGPGLSRTGVELSKPTHFTVNTKAAGKGKLDVQFSGPAKGDAVRDVDIIDHHDNTYTVKY" );
		
		peptide = new MetaMorphPeptide( builder );
	}

	
	@Test
	public void _test() {

		assertEquals( "E", ModUtils.getResidueAtPosition( peptide, 1 ) );
		assertEquals( "Y", ModUtils.getResidueAtPosition( peptide, 60 ) );
		assertEquals( "P", ModUtils.getResidueAtPosition( peptide, 15 ) );
		
		try {
			ModUtils.getResidueAtPosition( peptide, 0 );
			fail( "Should have failed." );
		} catch( Exception e ) {
			;
		}
		
		try {
			ModUtils.getResidueAtPosition( peptide, 61 );
			fail( "Should have failed." );
		} catch( Exception e ) {
			;
		}

	}
	
}
