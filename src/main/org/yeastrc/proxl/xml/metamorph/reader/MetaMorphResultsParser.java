package org.yeastrc.proxl.xml.metamorph.reader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.yeastrc.proxl.xml.metamorph.constants.SearchConstants;
import org.yeastrc.proxl.xml.metamorph.objects.AnalysisParameters;
import org.yeastrc.proxl.xml.metamorph.objects.MetaMorphPSM;
import org.yeastrc.proxl.xml.metamorph.objects.MetaMorphPSMBuilder;
import org.yeastrc.proxl.xml.metamorph.objects.MetaMorphPeptide;
import org.yeastrc.proxl.xml.metamorph.objects.MetaMorphPeptideBuilder;
import org.yeastrc.proxl.xml.metamorph.objects.MetaMorphReportedPeptide;
import org.yeastrc.proxl.xml.metamorph.objects.MetaMorphReportedPeptideBuilder;
import org.yeastrc.proxl.xml.metamorph.utils.ModUtils;
import org.yeastrc.proxl.xml.metamorph.utils.PepXMLUtils;
import org.yeastrc.proxl.xml.metamorph.utils.ScanParsingUtils;

import net.systemsbiology.regis_web.pepxml.ModInfoDataType;
import net.systemsbiology.regis_web.pepxml.ModInfoDataType.ModAminoacidMass;
import net.systemsbiology.regis_web.pepxml.MsmsPipelineAnalysis.MsmsRunSummary;
import net.systemsbiology.regis_web.pepxml.MsmsPipelineAnalysis.MsmsRunSummary.SpectrumQuery;
import net.systemsbiology.regis_web.pepxml.MsmsPipelineAnalysis.MsmsRunSummary.SpectrumQuery.SearchResult;
import net.systemsbiology.regis_web.pepxml.MsmsPipelineAnalysis.MsmsRunSummary.SpectrumQuery.SearchResult.SearchHit;
import net.systemsbiology.regis_web.pepxml.MsmsPipelineAnalysis.MsmsRunSummary.SpectrumQuery.SearchResult.SearchHit.Xlink;
import net.systemsbiology.regis_web.pepxml.MsmsPipelineAnalysis.MsmsRunSummary.SpectrumQuery.SearchResult.SearchHit.Xlink.LinkedPeptide;
import net.systemsbiology.regis_web.pepxml.NameValueType;

public class MetaMorphResultsParser {

	private static final MetaMorphResultsParser _INSTANCE = new MetaMorphResultsParser();
	public static MetaMorphResultsParser getInstance() { return _INSTANCE; }
	private MetaMorphResultsParser() { }
	
	/**
	 * Get the results of the analysis back in the form used by proxl:
	 * reported peptides are the keys, and all of the PSMs (and their scores)
	 * that reported that peptide are the values.
	 * 
	 * @param analysis
	 * @return
	 * @throws Exception
	 */
	public Map<MetaMorphReportedPeptide, Collection<MetaMorphPSM>> getResultsFromAnalysis( AnalysisParameters analysis ) throws Exception {
		
		Map<MetaMorphReportedPeptide, Collection<MetaMorphPSM>> results = new HashMap<MetaMorphReportedPeptide, Collection<MetaMorphPSM>>();		
		
		for( MsmsRunSummary runSummary : analysis.getAnalysis().getMsmsRunSummary() ) {
			for( SpectrumQuery spectrumQuery : runSummary.getSpectrumQuery() ) {
				for( SearchResult searchResult : spectrumQuery.getSearchResult() ) {
					for( SearchHit searchHit : searchResult.getSearchHit() ) {
						
						try {
							
							// skip looplinks for now.
							if( PepXMLUtils.getHitType( searchHit ) == SearchConstants.LINK_TYPE_LOOPLINK )
								continue;
							
							// skip deadends for now
							if( ModUtils.isDeadEndHit( searchHit, analysis.getLinker() ) )
								continue;							
							
							// get our result
							MetaMorphPSM result = getResult( runSummary, spectrumQuery, searchHit );
							
							// get our reported peptide
							MetaMorphReportedPeptide reportedPeptide = getReportedPeptide( searchHit, analysis );
							
							// skip this if reportedPeptide is null
							if( reportedPeptide == null )
								continue;
							
							if( reportedPeptide.getPeptide1().getSequence() == null ) {
								System.err.println( "\tWARNING: Got null sequence for peptide for: " + spectrumQuery.getSpectrum() + "!! Skipping this scan." );
								continue;
							}
							
							if( reportedPeptide.getPeptide2() != null && reportedPeptide.getPeptide2().getSequence() == null ) {
								System.err.println( "\tWARNING: Got null sequence for peptide for: " + spectrumQuery.getSpectrum() + "!! Skipping this scan." );
								continue;
							}
							
							
							if( !results.containsKey( reportedPeptide ) )
								results.put( reportedPeptide, new ArrayList<MetaMorphPSM>() );
							
							results.get( reportedPeptide ).add( result );

						} catch( Throwable t ) {
							
							System.err.println( "\n\nError processing spectrum: " + spectrumQuery.getSpectrum() );
							throw t;
							
						}
						
					}
				}
			}
		}
		
		return results;
	}
	
	
	/**
	 * Get the MetaMorphReportedPeptide for the given SearchHit
	 * 
	 * @param searchHit
	 * @return
	 * @throws Exception
	 */
	private MetaMorphReportedPeptide getReportedPeptide( SearchHit searchHit, AnalysisParameters analysis ) throws Exception {
		
		int type = PepXMLUtils.getHitType( searchHit );
		
		if( type == SearchConstants.LINK_TYPE_CROSSLINK )
			return getCrosslinkReportedPeptide( searchHit, analysis );
		
		if( type == SearchConstants.LINK_TYPE_LOOPLINK )
			return getLooplinkReportedPeptide( searchHit, analysis );
		
		return getUnlinkedReportedPeptide( searchHit, analysis );
		
	}

	/**
	 * Get the MetaMorphReportedPeptide for a crosslink result
	 * @param searchHit
	 * @return
	 * @throws Exception
	 */
	private MetaMorphReportedPeptide getCrosslinkReportedPeptide( SearchHit searchHit, AnalysisParameters analysis ) throws Exception {
		
		//System.out.println( searchHit.getPeptide() );
		//System.out.println( "\t" + searchHit.getXlinkType() );
		
		MetaMorphReportedPeptideBuilder reportedPeptideBuilder = new MetaMorphReportedPeptideBuilder();
		reportedPeptideBuilder.setType( SearchConstants.LINK_TYPE_CROSSLINK );
				
		for( LinkedPeptide linkedPeptide : searchHit.getXlink().getLinkedPeptide() ) {
			
			int peptideNumber = 0;
			if( reportedPeptideBuilder.getPeptide1() == null ) {
				peptideNumber = 1;
			} else if( reportedPeptideBuilder.getPeptide2() == null ) {
				peptideNumber = 2;
			} else {
				throw new Exception( "Got more than two linked peptides." );
			}
			
			
			//System.out.println( "\t\t" + linkedPeptide.getPeptide() );
			//System.out.println( "\t\tpeptide num: " + peptideNumber );
			
			MetaMorphPeptide peptide = getPeptideFromLinkedPeptide( linkedPeptide, analysis );
			int position = 0;
			
			for( NameValueType nvt : linkedPeptide.getXlinkScore() ) {
								
				if( nvt.getName().equals( "link" ) ) {
					
					//System.out.println( "\t\t" + nvt.getValueAttribute() );
					
					if( position == 0 )
						position = Integer.valueOf( nvt.getValueAttribute() );
					else
						throw new Exception( "Got more than one linked position in peptide." );
				}
			}
			
			if( position == 0 )
				throw new Exception( "Could not find linked position in peptide." );
			
			
			if( peptideNumber == 1 ) {
				reportedPeptideBuilder.setPeptide1( peptide );
				reportedPeptideBuilder.setPosition1( position );
			} else {
				reportedPeptideBuilder.setPeptide2( peptide );
				reportedPeptideBuilder.setPosition2( position );
			}
			
			
		}
		
		
		// ensure peptides and positions are consistently ordered so that any two reported peptides containing the same
		// two peptides and linked positions are recognized as the same
		
		if( reportedPeptideBuilder.getPeptide1().toString().compareTo( reportedPeptideBuilder.getPeptide2().toString() ) > 0 ) {

			// swap them
			MetaMorphPeptide tpep = reportedPeptideBuilder.getPeptide1();
			int tpos = reportedPeptideBuilder.getPosition1();
			
			reportedPeptideBuilder.setPeptide1( reportedPeptideBuilder.getPeptide2() );
			reportedPeptideBuilder.setPosition1( reportedPeptideBuilder.getPosition2() );
			
			reportedPeptideBuilder.setPeptide2( tpep );
			reportedPeptideBuilder.setPosition2( tpos );
		} else if( reportedPeptideBuilder.getPeptide1().toString().compareTo( reportedPeptideBuilder.getPeptide2().toString() ) == 0 ) {
			
			// peptides are the same, should we swap positions?
			if( reportedPeptideBuilder.getPosition1() > reportedPeptideBuilder.getPosition2() ) {
				int tpos = reportedPeptideBuilder.getPosition1();
				
				reportedPeptideBuilder.setPosition1( reportedPeptideBuilder.getPosition2() );
				reportedPeptideBuilder.setPosition2( tpos );
			}
			
		}
		
		return new MetaMorphReportedPeptide( reportedPeptideBuilder );
	}
	
	/**
	 * Get the MetaMorphReportedPeptide for a looplink result
	 * @param searchHit
	 * @return
	 * @throws Exception
	 */
	private MetaMorphReportedPeptide getLooplinkReportedPeptide( SearchHit searchHit, AnalysisParameters analysis ) throws Exception {
		
		MetaMorphReportedPeptideBuilder reportedPeptideBuilder = new MetaMorphReportedPeptideBuilder();
		
		reportedPeptideBuilder.setPeptide1( getPeptideFromSearchHit( searchHit, analysis ) );
		reportedPeptideBuilder.setType( SearchConstants.LINK_TYPE_LOOPLINK );
		
		// add in the linked positions
		Xlink xl = searchHit.getXlink();
		
		for( NameValueType nvt : xl.getXlinkScore() ) {
			if( nvt.getName().equals( "link" ) ) {
				
				//System.out.println( "\t\t" + nvt.getValueAttribute() );
				
				if( reportedPeptideBuilder.getPosition1() == 0 )
					reportedPeptideBuilder.setPosition1( Integer.valueOf( nvt.getValueAttribute() ) );
				else if( reportedPeptideBuilder.getPosition2() == 0 )
					reportedPeptideBuilder.setPosition2( Integer.valueOf( nvt.getValueAttribute() ) );
				else
					throw new Exception( "Got more than 2 linked positions for looplink." );
			}
		}
		
		if( reportedPeptideBuilder.getPosition1() == 0 || reportedPeptideBuilder.getPosition2() == 0 )
			throw new Exception( "Did not get two positions for looplink." );
		
		if( reportedPeptideBuilder.getPosition1() > reportedPeptideBuilder.getPosition2() ) {
			int tpos = reportedPeptideBuilder.getPosition1();
			
			reportedPeptideBuilder.setPosition1( reportedPeptideBuilder.getPosition2() );
			reportedPeptideBuilder.setPosition2( tpos );
		}
		
		
		return new MetaMorphReportedPeptide( reportedPeptideBuilder );
	}

	/**
	 * Get the MetaMorphReportedPeptide for an unlinked result
	 * @param searchHit
	 * @return
	 * @throws Exception
	 */
	private MetaMorphReportedPeptide getUnlinkedReportedPeptide( SearchHit searchHit, AnalysisParameters analysis ) throws Exception {
		
		MetaMorphReportedPeptideBuilder reportedPeptideBuilder = new MetaMorphReportedPeptideBuilder();
		
		reportedPeptideBuilder.setPeptide1( getPeptideFromSearchHit( searchHit, analysis ) );
		reportedPeptideBuilder.setType( SearchConstants.LINK_TYPE_UNLINKED );
		
		return new MetaMorphReportedPeptide( reportedPeptideBuilder );
	}
	
	/**
	 * Get the MetaMorphPeptide from the searchHit. Includes the peptide sequence and any mods.
	 * 
	 * @param searchHit
	 * @return
	 * @throws Exception
	 */
	private MetaMorphPeptide getPeptideFromSearchHit( SearchHit searchHit, AnalysisParameters analysis ) throws Exception {
		
		MetaMorphPeptideBuilder peptideBuilder = new MetaMorphPeptideBuilder();
		
		peptideBuilder.setSequence( searchHit.getPeptide() );
				
		ModInfoDataType modInfo = searchHit.getModificationInfo();
		
		if( modInfo!= null && modInfo.getModAminoacidMass() != null && modInfo.getModAminoacidMass().size() > 0 ) {
			Map<Integer, Collection<BigDecimal>> mods = new HashMap<>();
			
			for( ModAminoacidMass mam : modInfo.getModAminoacidMass() ) {
				
				int position = mam.getPosition().intValue() - 1;				
				double massDifferenceDouble = mam.getMass();
				
				if( !mods.containsKey( position ) )
					mods.put( position, new HashSet<BigDecimal>() );
				
				mods.get( position ).add( BigDecimal.valueOf( massDifferenceDouble ) );				
			}
			
			peptideBuilder.setModifications( mods );			
		}
				
		return new MetaMorphPeptide( peptideBuilder );
	}
	
	/**
	 * Get the MetaMorphPeptide from the searchHit. Includes the peptide sequence and any mods.
	 * 
	 * @param searchHit
	 * @return
	 * @throws Exception
	 */
	private MetaMorphPeptide getPeptideFromLinkedPeptide( LinkedPeptide linkedPeptide, AnalysisParameters analysis ) throws Exception {
		
		MetaMorphPeptideBuilder peptideBuilder = new MetaMorphPeptideBuilder();
		
		peptideBuilder.setSequence( linkedPeptide.getPeptide() );		
		
		ModInfoDataType modInfo = linkedPeptide.getModificationInfo();
		
		if( modInfo!= null && modInfo.getModAminoacidMass() != null && modInfo.getModAminoacidMass().size() > 0 ) {
			Map<Integer, Collection<BigDecimal>> mods = new HashMap<>();
			
			for( ModAminoacidMass mam : modInfo.getModAminoacidMass() ) {
				
				int position = mam.getPosition().intValue() - 1;				
				double massDifferenceDouble = mam.getMass();
				
				if( !mods.containsKey( position ) )
					mods.put( position, new HashSet<BigDecimal>() );
				
				mods.get( position ).add( BigDecimal.valueOf( massDifferenceDouble ) );				
			}
			
			peptideBuilder.setModifications( mods );			
		}
				
		return new MetaMorphPeptide( peptideBuilder );
	}
	
	
	/**
	 * Get the PSM result for the given spectrum query and search hit.
	 * 
	 * @param spectrumQuery
	 * @param searchHit
	 * @return
	 * @throws Exception If any of the expected scores are not found
	 */
	private MetaMorphPSM getResult( MsmsRunSummary runSummary, SpectrumQuery spectrumQuery, SearchHit searchHit ) throws Exception {
		
		MetaMorphPSMBuilder psmBuilder = new MetaMorphPSMBuilder();
		
		psmBuilder.setScanFile( ScanParsingUtils.getFilenameFromReportedScan( spectrumQuery.getSpectrum() ) + runSummary.getRawData() );
		
		
		
		psmBuilder.setScanNumber( (int)spectrumQuery.getStartScan() );
		psmBuilder.setCharge( spectrumQuery.getAssumedCharge().intValue() );
		
		// if this is a crosslink or looplink, get the mass of the linker
		int type = PepXMLUtils.getHitType( searchHit );
		if( type == SearchConstants.LINK_TYPE_CROSSLINK || type == SearchConstants.LINK_TYPE_LOOPLINK ) {
			Xlink xl = searchHit.getXlink();
			psmBuilder.setLinkerMass( xl.getMass() );
		}
		
		for( NameValueType score : searchHit.getSearchScore() ) {
			if( score.getName().equals( "xlTotalScore" ) ) {
				psmBuilder.setTotalScore( new BigDecimal( score.getValueAttribute() ) );
			}
			
			else if( score.getName().equals( "Qvalue" ) ) {
				psmBuilder.setqValue( new BigDecimal( score.getValueAttribute() ) );
			}
					
		}
		
		
		if( psmBuilder.getqValue() == null )
			throw new Exception( "Missing qvalue score for result: " + spectrumQuery.getSpectrum() );
		
		if( psmBuilder.getTotalScore() == null )
			throw new Exception( "Missing TotalScore error for result: " + spectrumQuery.getSpectrum() );
		
		
		return new MetaMorphPSM( psmBuilder );
		
	}
	
}
