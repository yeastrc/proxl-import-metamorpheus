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
			linker.getCrosslinkMasses().add( 158.0038 );
			linker.getMonolinkMasses().add( 176.0143 );
			linker.getMonolinkMasses().add( 175.0303 );
			linker.getMonolinkMasses().add( 279.0777 );
			
			_LINKER_MAP.put( "DSSO", linker );
		}
		
		{
			MetaMorphLinker linker = new MetaMorphLinker();
			
			linker.setMetaMorphName( "DSS" );
			linker.setProxlName( "dss" );
			linker.getCrosslinkMasses().add( 138.06808 );
			linker.getMonolinkMasses().add( 156.0786 );
			linker.getMonolinkMasses().add( 155.0946 );
			linker.getMonolinkMasses().add( 259.142 );
			
			_LINKER_MAP.put( "DSS", linker );
		}
		
		
	}
	
	public static MetaMorphLinker getLinker( String name ) {
		return _LINKER_MAP.get( name );		
	}
	
}
