package org.yeastrc.proxl.xml.metamorph.linkers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MetaMorphLinkerFactory_TestGetLinker {

	@Test
	public void _test() throws Exception {

		{
			
			MetaMorphLinker linker = MetaMorphLinkerFactory.getLinker( "DSSO" );
			assertEquals( "dsso", linker.getProxlName() );

		}
		
		{
			
			MetaMorphLinker linker = MetaMorphLinkerFactory.getLinker( "DSS" );
			assertEquals( "dss", linker.getProxlName() );

		}

	}
	
}
