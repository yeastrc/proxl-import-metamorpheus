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
import java.util.*;

import org.yeastrc.proxl.xml.metamorph.linkers.MetaMorphLinker;
import org.yeastrc.proxl.xml.metamorph.linkers.MetaMorphLinkerEnd;
import org.yeastrc.proxl.xml.metamorph.linkers.MetaMorphLinkerFactory;

import com.moandjiezana.toml.*;

public class LinkerProcessorFromConfFile {

	public static LinkerProcessorFromConfFile createInstance() {
		return new LinkerProcessorFromConfFile();
	}
	
	/**
	 * Get the MetaMorphLinker for the linker defined in the toml file
	 * 
	 * @param filename The path to the toml file
	 * @return
	 * @throws IOException
	 */
	public MetaMorphLinker getLinkerFromConfFile( String filename ) throws Exception {

		Toml confToml = new Toml().read( new File( filename ) );

		// if it is the "old style", parse it appropriately
		if( getIsConfFilePre301( confToml ) ) {
			return LinkerProcessFromConfFilePre301.createInstance().getLinkerFromConfFile( filename );
		}

		Toml XlSearchParameters = confToml.getTable( "XlSearchParameters" );
		if( XlSearchParameters == null )
			throw new Exception( "Could not find XlSearchParameters section in toml config file." );

		Toml CrosslinkerParams = XlSearchParameters.getTable( "Crosslinker" );
		if( CrosslinkerParams == null )
			throw new Exception( "Could not find Crosslinker subsection in XlkSearchParameters in toml config file." );


		MetaMorphLinker linker = new MetaMorphLinker();

		linker.setMetaMorphName( CrosslinkerParams.getString( "CrosslinkerName" ) );
		linker.setProxlName( CrosslinkerParams.getString( "CrosslinkerName" ) );
		linker.getCrosslinkMasses().add( CrosslinkerParams.getDouble( "TotalMass" ) );

		// add in both linked ends
		List<MetaMorphLinkerEnd> linkerEnds = new ArrayList<>(2);
		linker.setLinkerEnds(linkerEnds);

		for( String key : new String[] {"CrosslinkerModSites", "CrosslinkerModSites2"} ) {

			String residuesField = CrosslinkerParams.getString( key );
			Collection<String> residues = new HashSet<>();

			for (int i = 0; i < residuesField.length(); i++) {
				String residue = String.valueOf(residuesField.charAt(i));
				residues.add(residue);
			}

			MetaMorphLinkerEnd linkerEnd = new MetaMorphLinkerEnd(residues, false, false);
			linkerEnds.add(linkerEnd);
		}

		// add in mono-link masses
		for( String key : new String[] {"H2O", "Tris", "NH2"} ) {

			String shouldBeIncludedKey = "XlQuench_" + key;
			String definitionKey = "DeadendMass" + key;

			if( XlSearchParameters.getBoolean( shouldBeIncludedKey ) != null && XlSearchParameters.getBoolean( shouldBeIncludedKey ) ) {
				if( CrosslinkerParams.getDouble( definitionKey ) != null ) {
					linker.getMonolinkMasses().add( CrosslinkerParams.getDouble( definitionKey ) );
				}
			}
		}

		// add in cleavable info
		linker.setCleavable( CrosslinkerParams.getBoolean( "Cleavable" ) );

		if( linker.isCleavable() ) {
			linker.getCleavedCrosslinkMasses().add( CrosslinkerParams.getDouble( "CleaveMassShort" ) );
			linker.getCleavedCrosslinkMasses().add( CrosslinkerParams.getDouble( "CleaveMassLong" ) );
		}

		return linker;
	}


	/**
	 * Is the conf file pre 0.0.301? From 0.0.301 onward, cross-linker parameters are stored in the config
	 * file in a more consistent way.
	 *
	 * @param confToml
	 * @return
	 */
	private boolean getIsConfFilePre301(Toml confToml) {

		Toml XlSearchParameters = confToml.getTable( "XlSearchParameters" );
		if( XlSearchParameters != null && XlSearchParameters.getTable( "Crosslinker" ) != null ) {
			return false;
		}

		return true;
	}

}
