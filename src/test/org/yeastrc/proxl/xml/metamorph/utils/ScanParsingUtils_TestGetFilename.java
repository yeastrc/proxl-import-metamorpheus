package org.yeastrc.proxl.xml.metamorph.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ScanParsingUtils_TestGetFilename {

	@Test
	public void _test() throws Exception {

		String spectrumIdString = "QEP2_2016_0121_RJ_73_206_xlink05.39287";
		assertEquals( "QEP2_2016_0121_RJ_73_206_xlink05", ScanParsingUtils.getFilenameFromReportedScan( spectrumIdString ) );

		spectrumIdString = "QEP2_2016_0121_RJ_73_206_xlink0539287";
		try {
			ScanParsingUtils.getFilenameFromReportedScan( spectrumIdString );
			fail( "Should have failed." );
			
		} catch( Exception e ) { ; }
		
		spectrumIdString = "QEP2_2016_0121_RJ_73_206_xli.nk05.39287";
		try {
			ScanParsingUtils.getFilenameFromReportedScan( spectrumIdString );
			fail( "Should have failed." );
			
		} catch( Exception e ) { ; }
		
		spectrumIdString = null;
		try {
			ScanParsingUtils.getFilenameFromReportedScan( spectrumIdString );
			fail( "Should have failed." );
			
		} catch( Exception e ) { ; }
		
		spectrumIdString = "";
		try {
			ScanParsingUtils.getFilenameFromReportedScan( spectrumIdString );
			fail( "Should have failed." );
			
		} catch( Exception e ) { ; }
		
		spectrumIdString = "QEP2_2016_0121_RJ_73_206_xlink";
		try {
			ScanParsingUtils.getFilenameFromReportedScan( spectrumIdString );
			fail( "Should have failed." );
			
		} catch( Exception e ) { ; }
		
		spectrumIdString =  "QEP2_2016_0121 RJ_73_206_xlink05.39287";
		try {
			ScanParsingUtils.getFilenameFromReportedScan( spectrumIdString );
			fail( "Should have failed." );
			
		} catch( Exception e ) { ; }
		
	}
	
}
