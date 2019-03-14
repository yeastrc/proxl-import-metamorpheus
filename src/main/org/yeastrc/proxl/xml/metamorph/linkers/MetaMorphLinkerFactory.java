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

package org.yeastrc.proxl.xml.metamorph.linkers;

import java.util.HashMap;
import java.util.Map;

public class MetaMorphLinkerFactory {

	private static Map<String, MetaMorphLinker> _LINKER_MAP;
	
	static {
		
		_LINKER_MAP = new HashMap<>();
		
		{
			MetaMorphLinker linker = new MetaMorphLinker();
			
			linker.setMetaMorphName( "DSSO" );
			linker.setProxlName( "dsso" );
			linker.setCleavable( true );

			linker.getCrosslinkMasses().add( 158.0038 );

			linker.getCleavedCrosslinkMasses().add( 54.01056);
			linker.getCleavedCrosslinkMasses().add( 103.9932 );

			linker.getMonolinkMasses().add( 176.0143 );
			linker.getMonolinkMasses().add( 175.0303 );
			linker.getMonolinkMasses().add( 279.0777 );
			
			_LINKER_MAP.put( "DSSO", linker );
		}
		
		{
			MetaMorphLinker linker = new MetaMorphLinker();
			
			linker.setMetaMorphName( "DSS" );
			linker.setProxlName( "dss" );
			linker.setCleavable( false );
			linker.getCrosslinkMasses().add( 138.06808 );
			linker.getMonolinkMasses().add( 156.0786 );
			linker.getMonolinkMasses().add( 155.0946 );
			linker.getMonolinkMasses().add( 259.142 );
			
			_LINKER_MAP.put( "DSS", linker );
		}

		{
			MetaMorphLinker linker = new MetaMorphLinker();

			linker.setMetaMorphName( "DisulfideBond" );
			linker.setProxlName( "DisulfideBond" );
			linker.setCleavable( true );

			linker.getCrosslinkMasses().add( -2.01565 );

			linker.getCleavedCrosslinkMasses().add( -33.98772 );
			linker.getCleavedCrosslinkMasses().add( 31.97207 );

			_LINKER_MAP.put( "DisulfideBond", linker );
		}

		{
			MetaMorphLinker linker = new MetaMorphLinker();

			linker.setMetaMorphName( "DSBU" );
			linker.setProxlName( "dsbu" );
			linker.setCleavable( true );

			linker.getCrosslinkMasses().add( 196.0848 );

			linker.getCleavedCrosslinkMasses().add( 85.05276);
			linker.getCleavedCrosslinkMasses().add( 111.0320 );

			linker.getMonolinkMasses().add( 214.0954 );
			linker.getMonolinkMasses().add( 213.1113 );
			linker.getMonolinkMasses().add( 317.1587 );

			_LINKER_MAP.put( "DSBU", linker );
		}
		
		
	}
	
	public static MetaMorphLinker getLinker( String name ) {
		return _LINKER_MAP.get( name );		
	}
	
}
