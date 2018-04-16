package org.yeastrc.proxl.xml.metamorph.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.yeastrc.proxl.xml.metamorph.linkers.MetaMorphLinker;
import org.yeastrc.proxl.xml.metamorph.linkers.MetaMorphLinkerFactory;

public class ModUtils_TestIsDeadEndMod {

	public MetaMorphLinker _LINKER = MetaMorphLinkerFactory.getLinker( "DSS" );

	
	@Test
	public void _test() {

		/*
		    linker.getCrosslinkMasses().add( 138.06808 );
			linker.getMonolinkMasses().add( 156.0786 );
			linker.getMonolinkMasses().add( 155.0946 );
			linker.getMonolinkMasses().add( 259.142 );
		 */
		
		assertTrue( ModUtils.isDeadEndMod( 156.0786, _LINKER ) );
		assertTrue( ModUtils.isDeadEndMod( 155.0946, _LINKER ) );
		assertTrue( ModUtils.isDeadEndMod( 259.142, _LINKER ) );
		assertTrue( ModUtils.isDeadEndMod( 156.079, _LINKER ) );

		assertFalse( ModUtils.isDeadEndMod( 138.06808, _LINKER ) );
		assertFalse( ModUtils.isDeadEndMod( 12.33, _LINKER ) );
		
		assertFalse( ModUtils.isDeadEndMod( 156.0887, _LINKER ) );	// different by more than 0.01


	}
	
}
