package org.yeastrc.proxl.xml.metamorph.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


import org.junit.Before;
import org.junit.Test;

public class ModUtils_TestIsStaticMod {

	private Map<String,BigDecimal> _STATIC_MODS = null;
	
	@Before
	public void setUp() {

		_STATIC_MODS = new HashMap<>();
		_STATIC_MODS.put( "C", BigDecimal.valueOf( 57.021463720689994 ) );
		_STATIC_MODS.put( "S", BigDecimal.valueOf( 19.394838388390339 ) );
	}

	
	@Test
	public void _test() {

		assertTrue( ModUtils.isStaticMod( "C", BigDecimal.valueOf( 57.021463720689994 ), _STATIC_MODS ) );
		assertTrue( ModUtils.isStaticMod( "S", BigDecimal.valueOf( 19.394838388390339 ), _STATIC_MODS ) );

		// wrong mass
		assertFalse( ModUtils.isStaticMod( "C", BigDecimal.valueOf( 57.02146372068999 ), _STATIC_MODS ) );
		assertFalse( ModUtils.isStaticMod( "C", BigDecimal.valueOf( 12.3938 ), _STATIC_MODS ) );

		// wrong residue
		assertFalse( ModUtils.isStaticMod( "T", BigDecimal.valueOf( 57.021463720689994 ), _STATIC_MODS ) );

	}
	
}
