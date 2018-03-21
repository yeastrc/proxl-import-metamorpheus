package org.yeastrc.proxl.xml.metamorph.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Some utility methods for parsing scan variables from the reported scan information in plink results files.
 * 
 * @author Michael Riffle
 * @date Mar 23, 2016
 *
 */
public class ScanParsingUtils {
	
	/**
	 * Get name of scan file from reported scan string in pepXML for MetaMorpheus data
	 * E.g., QEP2_2016_0121_RJ_73_206_xlink05.39287 would return QEP2_2016_0121_RJ_73_206_xlink05
	 * 
	 * @param reportedScan
	 * @return
	 * @throws Exception
	 */
	public static String getFilenameFromReportedScan( String reportedScan ) throws Exception {
		
		Pattern r = Pattern.compile( "^([^\\.\\s]+)\\.\\d+$" );
		Matcher m = r.matcher( reportedScan );
		
		if( m.matches() ) {
			return m.group( 1 );
		} else {
			throw new Exception( "Invalid syntax for spectrum id: " + reportedScan );
		}
		
	}
	
}
