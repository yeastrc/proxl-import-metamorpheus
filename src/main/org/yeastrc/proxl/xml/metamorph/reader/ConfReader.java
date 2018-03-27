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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.yeastrc.proxl.xml.metamorph.linkers.MetaMorphLinker;
import org.yeastrc.proxl.xml.metamorph.linkers.MetaMorphLinkerFactory;

/**
 * A class for reading the values from a MetaMorpheus conf file. Once a value is
 * queried, it will read the file once and store the values for future
 * reference.
 * 
 * @author mriffle
 *
 */
public class ConfReader {

	public static ConfReader getInstance( String filename ) {
		return new ConfReader( filename );
	}
	
	private ConfReader( String filename ) {
		this.file = new File( filename );
	}	
	private ConfReader() { }
	
	private void parseFile() throws Exception {
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader( new FileReader( this.file ) );
			String line = br.readLine();
			
			this.staticModifications = new HashMap<>();
			
			while( line != null ) {
				
				String[] fields = line.split( " = " );
				
				if( fields[ 0 ].equals( "ListOfModsFixed" ) ) {
					this.staticModifications = getStaticModsFromConf( fields[ 1 ] );
				}
				
				else if( fields[ 0 ].equals( "CrosslinkerType" ) ) {
					this.metaMorphLinker = getMetaMorphLinkerFromConf( fields[ 1 ] );
				}
				
				line = br.readLine();
			}
		} finally {
			if( br != null ) br.close();
		}
	}
	
	public MetaMorphLinker getMetaMorphLinkerFromConf( String line ) throws Exception {
		
		line = line.replaceAll( "\"", "" );
		if( MetaMorphLinkerFactory.getLinker( line ) == null )
			throw new Exception( "Unable to load linker information for: " + line );
				
		return MetaMorphLinkerFactory.getLinker( line );
	}
	
	public Map<String, BigDecimal> getStaticModsFromConf( String line ) throws Exception {
		
		Map<String, BigDecimal> mods = new HashMap<>();
		
		if( line.equals( "\"Common Fixed\\tCarbamidomethyl of C\\t\\tCommon Fixed\\tCarbamidomethyl of U\"" ) ||
			line.equals( "\"Common Fixed\\tCarbamidomethyl of C\"") ) {
			
			mods.put( "C", BigDecimal.valueOf( 57.02146372068994 ) );

		} else {
			
			throw new Exception( "Unexpected syntax of static/fixed mod line: " + line );
			
		}
		
		
		return mods;
	}


	public Map<String, BigDecimal> getStaticModifications()throws Exception {
		if( this.staticModifications == null )
			this.parseFile();
		
		return staticModifications;
	}

	
	
	public File getFile() {
		return file;
	}



	/**
	 * @return the metaMorphLinker
	 * @throws Exception 
	 */
	public MetaMorphLinker getMetaMorphLinker() throws Exception {
		
		if( this.metaMorphLinker == null )
			this.parseFile();
		
		return metaMorphLinker;
	}



	private File file;
	private MetaMorphLinker metaMorphLinker;
	private Map<String, BigDecimal> staticModifications;
	
}
