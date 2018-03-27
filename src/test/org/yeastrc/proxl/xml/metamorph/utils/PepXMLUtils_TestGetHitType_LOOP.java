package org.yeastrc.proxl.xml.metamorph.utils;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Before;
import org.junit.Test;
import org.yeastrc.proxl.xml.metamorph.constants.SearchConstants;

import net.systemsbiology.regis_web.pepxml.MsmsPipelineAnalysis.MsmsRunSummary.SpectrumQuery.SearchResult.SearchHit;

public class PepXMLUtils_TestGetHitType_LOOP {

	private SearchHit _XML_SEARCH_HIT = null;
	
	@Before
	public void setUp() throws JAXBException {

		String xmlString = "        <search_hit hit_rank=\"1\" peptide=\"NPPINTKSQAVKDR\" peptide_prev_aa=\"K\" peptide_next_aa=\"A\" protein=\"A_6\" num_tot_proteins=\"1\" calc_neutral_pep_mass=\"1708.94189\" massdiff=\"0.00672069323670144\" xlink_type=\"loop\">\r\n          <modification_info />\r\n          <xlink identifier=\"DSS\" mass=\"138.068085\">\r\n            <linked_peptide num_tot_proteins=\"0\" calc_neutral_pep_mass=\"0\" complement_mass=\"0\">\r\n              <xlink_score name=\"xlscore\" value=\"0\" />\r\n            </linked_peptide>\r\n          </xlink>\r\n          <search_score name=\"xlTotalScore\" value=\"7\" />\r\n          <search_score name=\"Qvalue\" value=\"0.0487804878048781\" />\r\n        </search_hit>";

		JAXBContext jaxbContext = JAXBContext.newInstance(TestSearchHit.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		
		
		StringReader reader = new StringReader( xmlString );
		_XML_SEARCH_HIT = (SearchHit) unmarshaller.unmarshal(reader);
	}

	
	@Test
	public void _test() throws Exception {

		assertEquals( SearchConstants.LINK_TYPE_LOOPLINK, PepXMLUtils.getHitType( _XML_SEARCH_HIT ) );

	}
	
	@XmlRootElement(name="search_hit")
	private static class TestSearchHit extends SearchHit {
		
		
		
	}
	
}
