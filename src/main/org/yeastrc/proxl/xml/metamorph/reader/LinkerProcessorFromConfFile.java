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
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

import org.yeastrc.proxl.xml.metamorph.linkers.MetaMorphLinker;
import org.yeastrc.proxl.xml.metamorph.linkers.MetaMorphLinkerFactory;

public class LinkerProcessorFromConfFile {

	public static LinkerProcessorFromConfFile createInstance() {
		return new LinkerProcessorFromConfFile();
	}
	
	/**
	 * Get the MetaMorphLinker for the linker defined in the toml file
	 * 
	 * @param filename The path to the toml file
	 * @param userSuppliedProxlLinkerName If the user has supplied a name of a cross-linker, this is it
	 * @return
	 * @throws IOException
	 */
	public MetaMorphLinker getLinkerFromConfFile( String filename, String userSuppliedProxlLinkerName ) throws IOException {
		
		String metaMorphLinkerName = getLinkerNameFromConfFile( filename );

		if( metaMorphLinkerName == null )
			throw new RuntimeException( "Error: Could not find a cross-linker defined in toml file: " + filename );
		
		
		if( metaMorphLinkerName.equals( "UserDefined" ) )
			return getUserDefinedLinkerFromConfFile( filename, userSuppliedProxlLinkerName );
		
		if( MetaMorphLinkerFactory.getLinker( metaMorphLinkerName ) == null )
			throw new RuntimeException( "Unable to load linker information for: " + metaMorphLinkerName );
				
		return MetaMorphLinkerFactory.getLinker( metaMorphLinkerName );
		
	}
	
	/**
	 * Get a MetaMorphLinker for a user-defined linker in a toml file
	 * 
	 * @param filename
	 * @param userSuppliedProxlLinkerName
	 * @return
	 * @throws IOException
	 */
	public MetaMorphLinker getUserDefinedLinkerFromConfFile( String filename, String userSuppliedProxlLinkerName ) throws IOException {

		Collection<String> linkerCrosslinkMassNames = new HashSet<>();
		linkerCrosslinkMassNames.add( "UdXLkerTotalMass" );
		linkerCrosslinkMassNames.add( "UdXLkerLoopMass" );
		linkerCrosslinkMassNames.add( "CrosslinkerTotalMass" );

		Collection<String> linkerDeadEndMassNames = new HashSet<>();
		linkerDeadEndMassNames.add( "UdXLkerDeadendMassH2O" );
		linkerDeadEndMassNames.add( "UdXLkerDeadendMassNH2" );
		linkerDeadEndMassNames.add( "UdXLkerDeadendMassTris" );
		linkerDeadEndMassNames.add( "CrosslinkerDeadEndMassH2O" );
		linkerDeadEndMassNames.add( "CrosslinkerDeadEndMassNH2" );
		linkerDeadEndMassNames.add( "CrosslinkerDeadEndMassTris" );

		File confFile = new File( filename );
		
		MetaMorphLinker linker = new MetaMorphLinker();
		linker.setProxlName( userSuppliedProxlLinkerName );
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader( new FileReader( confFile ) );
			String line = br.readLine();
			
			
			while( line != null ) {
				
				String[] fields = line.split( " = " );
				

				if( fields.length < 1 ) {
					line = br.readLine();
					continue;
				}

				if( fields[ 0 ].equals( "UdXLkerName" ) || fields[ 0 ].equals( "CrosslinkerName" ) )
					linker.setMetaMorphName( fields[ 1 ].replaceAll( "\"", "" ) );
				
				else if( linkerCrosslinkMassNames.contains( fields[ 0 ] ) ) {
					
					double mass = Double.valueOf( fields[ 1 ] );
					
					if( linker.getCrosslinkMasses() == null )
						linker.setCrosslinkMasses( new HashSet<>() );
					
					linker.getCrosslinkMasses().add( mass );					
				}

				else if( linkerDeadEndMassNames.contains( fields[ 0 ] ) ) {
					
					double mass = Double.valueOf( fields[ 1 ] );
					
					if( linker.getMonolinkMasses() == null )
						linker.setMonolinkMasses( new HashSet<>() );
					
					linker.getMonolinkMasses().add( mass );					
				}
				
				
				
				line = br.readLine();
			}
		} finally {
			if( br != null ) br.close();
		}
		
		if( linker.getMonolinkMasses() == null )
			System.err.println( "\tWarning: Got no monolink/deadend masses defined for linker in toml file: "  + filename );
		
		if( linker.getCrosslinkMasses() == null )
			throw new RuntimeException( "\tError: Got no cross-link massess defined for linker in toml file: " + filename );
		
		return linker;
	}

	/**
	 * Get the name of the cross-linker from the toml file. Return null if one isn't found.
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public String getLinkerNameFromConfFile( String filename ) throws IOException {
		
		File confFile = new File( filename );
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader( new FileReader( confFile ) );
			String line = br.readLine();
			
			
			while( line != null ) {
				
				String[] fields = line.split( " = " );

				if( fields.length < 1 ) {
					line = br.readLine();
					continue;
				}
				
				if( fields[ 0 ].equals( "CrosslinkerType" ) )
					return fields[ 1 ].replaceAll( "\"", "" );

				
				line = br.readLine();
			}
		} finally {
			if( br != null ) br.close();
		}
		
		return null;
	}
	
	
}
