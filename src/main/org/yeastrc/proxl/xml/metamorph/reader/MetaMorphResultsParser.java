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
import org.yeastrc.proxl.xml.metamorph.objects.MetaMorphPeptide;
import org.yeastrc.proxl.xml.metamorph.objects.MetaMorphReportedPeptide;
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
						
						
						// get our result
						MetaMorphPSM result = getResult( runSummary, spectrumQuery, searchHit );
						
						// get our reported peptide
						MetaMorphReportedPeptide reportedPeptide = getReportedPeptide( searchHit, analysis );
						
						if( !results.containsKey( reportedPeptide ) )
							results.put( reportedPeptide, new ArrayList<MetaMorphPSM>() );
						
						results.get( reportedPeptide ).add( result );
						
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
		
		MetaMorphReportedPeptide reportedPeptide = new MetaMorphReportedPeptide();
		reportedPeptide.setType( SearchConstants.LINK_TYPE_CROSSLINK );
				
		for( LinkedPeptide linkedPeptide : searchHit.getXlink().getLinkedPeptide() ) {
			
			int peptideNumber = 0;
			if( reportedPeptide.getPeptide1() == null ) {
				peptideNumber = 1;
			} else if( reportedPeptide.getPeptide2() == null ) {
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
				reportedPeptide.setPeptide1( peptide );
				reportedPeptide.setPosition1( position );
			} else {
				reportedPeptide.setPeptide2( peptide );
				reportedPeptide.setPosition2( position );
			}
			
			
		}
		
		
		// ensure peptides and positions are consistently ordered so that any two reported peptides containing the same
		// two peptides and linked positions are recognized as the same
		
		if( reportedPeptide.getPeptide1().toString().compareTo( reportedPeptide.getPeptide2().toString() ) > 0 ) {

			// swap them
			MetaMorphPeptide tpep = reportedPeptide.getPeptide1();
			int tpos = reportedPeptide.getPosition1();
			
			reportedPeptide.setPeptide1( reportedPeptide.getPeptide2() );
			reportedPeptide.setPosition1( reportedPeptide.getPosition2() );
			
			reportedPeptide.setPeptide2( tpep );
			reportedPeptide.setPosition2( tpos );
		} else if( reportedPeptide.getPeptide1().toString().compareTo( reportedPeptide.getPeptide2().toString() ) == 0 ) {
			
			// peptides are the same, should we swap positions?
			if( reportedPeptide.getPosition1() > reportedPeptide.getPosition2() ) {
				int tpos = reportedPeptide.getPosition1();
				
				reportedPeptide.setPosition1( reportedPeptide.getPosition2() );
				reportedPeptide.setPosition2( tpos );
			}
			
		}
		
		return reportedPeptide;
	}
	
	/**
	 * Get the MetaMorphReportedPeptide for a looplink result
	 * @param searchHit
	 * @return
	 * @throws Exception
	 */
	private MetaMorphReportedPeptide getLooplinkReportedPeptide( SearchHit searchHit, AnalysisParameters analysis ) throws Exception {
		
		MetaMorphReportedPeptide reportedPeptide = new MetaMorphReportedPeptide();
		
		reportedPeptide.setPeptide1( getPeptideFromSearchHit( searchHit, analysis ) );
		reportedPeptide.setType( SearchConstants.LINK_TYPE_LOOPLINK );
		
		// add in the linked positions
		Xlink xl = searchHit.getXlink();
		
		for( NameValueType nvt : xl.getXlinkScore() ) {
			if( nvt.getName().equals( "link" ) ) {
				
				//System.out.println( "\t\t" + nvt.getValueAttribute() );
				
				if( reportedPeptide.getPosition1() == 0 )
					reportedPeptide.setPosition1( Integer.valueOf( nvt.getValueAttribute() ) );
				else if( reportedPeptide.getPosition2() == 0 )
					reportedPeptide.setPosition2( Integer.valueOf( nvt.getValueAttribute() ) );
				else
					throw new Exception( "Got more than 2 linked positions for looplink." );
			}
		}
		
		if( reportedPeptide.getPosition1() == 0 || reportedPeptide.getPosition2() == 0 )
			throw new Exception( "Did not get two positions for looplink." );
		
		if( reportedPeptide.getPosition1() > reportedPeptide.getPosition2() ) {
			int tpos = reportedPeptide.getPosition1();
			
			reportedPeptide.setPosition1( reportedPeptide.getPosition2() );
			reportedPeptide.setPosition2( tpos );
		}
		
		
		return reportedPeptide;
	}

	/**
	 * Get the MetaMorphReportedPeptide for an unlinked result
	 * @param searchHit
	 * @return
	 * @throws Exception
	 */
	private MetaMorphReportedPeptide getUnlinkedReportedPeptide( SearchHit searchHit, AnalysisParameters analysis ) throws Exception {
		
		MetaMorphReportedPeptide reportedPeptide = new MetaMorphReportedPeptide();
		
		reportedPeptide.setPeptide1( getPeptideFromSearchHit( searchHit, analysis ) );
		reportedPeptide.setType( SearchConstants.LINK_TYPE_UNLINKED );
		
		return reportedPeptide;
	}
	
	/**
	 * Get the MetaMorphPeptide from the searchHit. Includes the peptide sequence and any mods.
	 * 
	 * @param searchHit
	 * @return
	 * @throws Exception
	 */
	private MetaMorphPeptide getPeptideFromSearchHit( SearchHit searchHit, AnalysisParameters analysis ) throws Exception {
		
		MetaMorphPeptide peptide = new MetaMorphPeptide();
		
		peptide.setSequence( searchHit.getPeptide() );
				
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
			
			peptide.setModifications( mods );			
		}
				
		return peptide;
	}
	
	/**
	 * Get the MetaMorphPeptide from the searchHit. Includes the peptide sequence and any mods.
	 * 
	 * @param searchHit
	 * @return
	 * @throws Exception
	 */
	private MetaMorphPeptide getPeptideFromLinkedPeptide( LinkedPeptide linkedPeptide, AnalysisParameters analysis ) throws Exception {
		
		MetaMorphPeptide peptide = new MetaMorphPeptide();
		
		peptide.setSequence( linkedPeptide.getPeptide() );		
		
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
			
			peptide.setModifications( mods );			
		}
				
		return peptide;
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
		
		MetaMorphPSM result = new MetaMorphPSM();
		
		result.setScanFile( ScanParsingUtils.getFilenameFromReportedScan( spectrumQuery.getSpectrum() ) + runSummary.getRawData() );
		
		
		
		result.setScanNumber( (int)spectrumQuery.getStartScan() );
		result.setCharge( spectrumQuery.getAssumedCharge().intValue() );
		
		// if this is a crosslink or looplink, get the mass of the linker
		int type = PepXMLUtils.getHitType( searchHit );
		if( type == SearchConstants.LINK_TYPE_CROSSLINK || type == SearchConstants.LINK_TYPE_LOOPLINK ) {
			Xlink xl = searchHit.getXlink();
			result.setLinkerMass( xl.getMass() );
		}
		
		for( NameValueType score : searchHit.getSearchScore() ) {
			if( score.getName().equals( "xlTotalScore" ) ) {
				result.setTotalScore( new BigDecimal( score.getValueAttribute() ) );
			}
			
			else if( score.getName().equals( "Qvalue" ) ) {
				result.setqValue( new BigDecimal( score.getValueAttribute() ) );
			}
					
		}
		
		
		if( result.getqValue() == null )
			throw new Exception( "Missing qvalue score for result: " + spectrumQuery.getSpectrum() );
		
		if( result.getTotalScore() == null )
			throw new Exception( "Missing TotalScore error for result: " + spectrumQuery.getSpectrum() );
		
		
		return result;
		
	}
	
}
