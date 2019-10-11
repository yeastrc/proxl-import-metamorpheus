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
