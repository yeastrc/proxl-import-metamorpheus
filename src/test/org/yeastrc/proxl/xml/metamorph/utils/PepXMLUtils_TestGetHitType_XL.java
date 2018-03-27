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

public class PepXMLUtils_TestGetHitType_XL {

	private SearchHit _XML_SEARCH_HIT = null;
	
	@Before
	public void setUp() throws JAXBException {

		String xmlString = "<search_hit hit_rank=\"1\" peptide=\"-\" peptide_prev_aa=\"-\" peptide_next_aa=\"-\" protein=\"-\" num_tot_proteins=\"1\" calc_neutral_pep_mass=\"2524.41113\" massdiff=\"-0.00105924906074506\" xlink_type=\"xl\">\r\n          <xlink identifier=\"DSS\" mass=\"138.068085\">\r\n            <linked_peptide peptide=\"TSDFLKVLNR\" peptide_prev_aa=\"K\" peptide_next_aa=\"A\" protein=\"A_3\" num_tot_proteins=\"7\" calc_neutral_pep_mass=\"1191.66113\" complement_mass=\"1329.72815\" designation=\"alpha\">\r\n              <xlink_score name=\"xlscore\" value=\"19.0738712937268\" />\r\n              <xlink_score name=\"link\" value=\"6\" />\r\n            </linked_peptide>\r\n            <linked_peptide peptide=\"TSDFLKVLNR\" peptide_prev_aa=\"K\" peptide_next_aa=\"A\" protein=\"A_3\" num_tot_proteins=\"14\" calc_neutral_pep_mass=\"1191.66113\" complement_mass=\"1329.72815\" designation=\"beta\">\r\n              <xlink_score name=\"xlscore\" value=\"19.0738712937268\" />\r\n              <xlink_score name=\"link\" value=\"6\" />\r\n            </linked_peptide>\r\n          </xlink>\r\n          <search_score name=\"xlTotalScore\" value=\"38.1477425874537\" />\r\n          <search_score name=\"Qvalue\" value=\"0\" />\r\n        </search_hit>";

		JAXBContext jaxbContext = JAXBContext.newInstance(TestSearchHit.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		
		
		StringReader reader = new StringReader( xmlString );
		_XML_SEARCH_HIT = (SearchHit) unmarshaller.unmarshal(reader);
	}

	
	@Test
	public void _test() throws Exception {

		assertEquals( SearchConstants.LINK_TYPE_CROSSLINK, PepXMLUtils.getHitType( _XML_SEARCH_HIT ) );

	}
	
	@XmlRootElement(name="search_hit")
	private static class TestSearchHit extends SearchHit {
		
		
		
	}
	
	
}
