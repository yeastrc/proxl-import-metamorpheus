package org.yeastrc.proxl.xml.metamorph.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.yeastrc.proxl.xml.metamorph.objects.MetaMorphPeptide;
import org.yeastrc.proxl.xml.metamorph.objects.MetaMorphPeptideBuilder;

public class ModUtils_TestPeptideHasNonStaticMods {

	private Map<String,BigDecimal> _STATIC_MODS = null;
	
	@Before
	public void setUp() {

		_STATIC_MODS = new HashMap<>();
		_STATIC_MODS.put( "C", BigDecimal.valueOf( 57.021463720689994 ) );
		_STATIC_MODS.put( "S", BigDecimal.valueOf( 19.394838388390339 ) );
	}

	
	@Test
	public void _test() {

		// has no mods
		{
			MetaMorphPeptideBuilder builder = new MetaMorphPeptideBuilder();
			builder.setSequence( "EGPGLSRTGVELSKPTHFTVNTKAAGKGKLDVQFSGPAKGDAVRDVDIIDHHDNTYTVKY" );
			
			MetaMorphPeptide peptide = new MetaMorphPeptide( builder );
			
			
			assertFalse( ModUtils.peptideHasNonStaticMods( peptide, _STATIC_MODS ) );
		}
		
		// has two non static mods, first nod is on a residue that can have a static mod
		{
			MetaMorphPeptideBuilder builder = new MetaMorphPeptideBuilder();
			builder.setSequence( "EGPGLSRTGVELSKPTHFTVNTKAAGKGKLDVQFSGPAKGDAVRDVDIIDHHDNTYTVKY" );
		
			Map<Integer,Collection<BigDecimal>> mods = new HashMap<>();
			mods.put( 6, new HashSet<>() );
			mods.get( 6 ).add( BigDecimal.valueOf( 12.8383902038 ) );
			
			mods.put( 15, new HashSet<>() );
			mods.get( 15 ).add( BigDecimal.valueOf( 57.021463720689994 ) );
			
			builder.setModifications( mods );
			
			MetaMorphPeptide peptide = new MetaMorphPeptide( builder );		
			
			assertTrue( ModUtils.peptideHasNonStaticMods( peptide, _STATIC_MODS ) );
		}
		
		// has one non static mod out of two
		{
			MetaMorphPeptideBuilder builder = new MetaMorphPeptideBuilder();
			builder.setSequence( "EGPGLSRTGVELSKPTHFTVNTKAAGKGKLDVQFSGPAKGDAVRDVDIIDHHDNTYTVKY" );
		
			Map<Integer,Collection<BigDecimal>> mods = new HashMap<>();
			mods.put( 6, new HashSet<>() );
			mods.get( 6 ).add( BigDecimal.valueOf( 19.394838388390339 ) );
			
			mods.put( 15, new HashSet<>() );
			mods.get( 15 ).add( BigDecimal.valueOf( 57.021463720689994 ) );
			
			builder.setModifications( mods );

			
			MetaMorphPeptide peptide = new MetaMorphPeptide( builder );		
			
			assertTrue( ModUtils.peptideHasNonStaticMods( peptide, _STATIC_MODS ) );
		}
		
		// only mod is one static mod
		{
			MetaMorphPeptideBuilder builder = new MetaMorphPeptideBuilder();
			builder.setSequence( "EGPGLSRTGVELSKPTHFTVNTKAAGKGKLDVQFSGPAKGDAVRDVDIIDHHDNTYTVKY" );
		
			Map<Integer,Collection<BigDecimal>> mods = new HashMap<>();
			mods.put( 6, new HashSet<>() );
			mods.get( 6 ).add( BigDecimal.valueOf( 19.394838388390339 ) );
			
			builder.setModifications( mods );

			
			MetaMorphPeptide peptide = new MetaMorphPeptide( builder );		
			
			assertFalse( ModUtils.peptideHasNonStaticMods( peptide, _STATIC_MODS ) );
		}
		
		// only mods are two static mods
		{
			MetaMorphPeptideBuilder builder = new MetaMorphPeptideBuilder();
			builder.setSequence( "EGPGLSRTGVELSKPCTHFTVNTKAAGKGKLDVQFSGPAKGDAVRDVDIIDHHDNTYTVKY" );
		
			Map<Integer,Collection<BigDecimal>> mods = new HashMap<>();
			mods.put( 6, new HashSet<>() );
			mods.get( 6 ).add( BigDecimal.valueOf( 19.394838388390339 ) );
			
			mods.put( 16, new HashSet<>() );
			mods.get( 16 ).add( BigDecimal.valueOf( 57.021463720689994 ) );
			
			builder.setModifications( mods );

			
			MetaMorphPeptide peptide = new MetaMorphPeptide( builder );		
			
			assertFalse( ModUtils.peptideHasNonStaticMods( peptide, _STATIC_MODS ) );
		}


	}
	
}
