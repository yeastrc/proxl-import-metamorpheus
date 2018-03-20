package org.yeastrc.proxl.xml.metamorph.utils;

import org.yeastrc.proxl.xml.metamorph.constants.SearchConstants;
import org.yeastrc.proxl.xml.metamorph.objects.AnalysisParameters;

import net.systemsbiology.regis_web.pepxml.MsmsPipelineAnalysis.MsmsRunSummary.SpectrumQuery.SearchResult.SearchHit;

public class PepXMLUtils {

	public static final String XLINK_TYPE_LOOPLINK = "loop";
	public static final String XLINK_TYPE_CROSSLINK = "xl";
	public static final String XLINK_TYPE_UNLINKED = "na";
	
	/**
	 * Get the type of link represented by the search hit
	 * 
	 * @param searchHit
	 * @return
	 * @throws Exception
	 */
	public static int getHitType( SearchHit searchHit ) throws Exception {
		
		if( searchHit.getXlinkType().equals( PepXMLUtils.XLINK_TYPE_CROSSLINK ) ) {
			return SearchConstants.LINK_TYPE_CROSSLINK;
		}
		
		if( searchHit.getXlinkType().equals( PepXMLUtils.XLINK_TYPE_LOOPLINK ) ) {
			return SearchConstants.LINK_TYPE_LOOPLINK;
		}
		
		if( searchHit.getXlinkType().equals( PepXMLUtils.XLINK_TYPE_UNLINKED ) ) {
			return SearchConstants.LINK_TYPE_UNLINKED;
		}
		
		throw new Exception( "Unknown link type in pepxml: " + searchHit.getXlinkType() );
		
	}
	
	/**
	 * Get the version number associated with this metamorpheus search
	 * 
	 * @param analysis
	 * @return
	 * @throws Exception
	 */
	public static String getVersion( AnalysisParameters analysis ) throws Exception {
		
		String version = "Unknown";
		
		try {
			version = analysis.getAnalysis().getMsmsRunSummary().get( 0 ).getSearchSummary().get( 0 ).getSearchEngineVersion();
		} catch (Exception e ) { ; }
		
		return version;		
	}
	
}
