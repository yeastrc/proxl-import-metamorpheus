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

public class PepXMLUtils_TestGetHitType_UNLINKED {

	private SearchHit _XML_SEARCH_HIT = null;
	
	@Before
	public void setUp() throws JAXBException {

		String xmlString = "        <search_hit hit_rank=\"1\" peptide=\"LKPKPIDVQVITHHMQR\" peptide_prev_aa=\"R\" peptide_next_aa=\"Y\" protein=\"A_1\" num_tot_proteins=\"1\" calc_neutral_pep_mass=\"2042.16919\" massdiff=\"0.00118657567418268\" xlink_type=\"na\">\r\n          <modification_info />\r\n          <search_score name=\"xlTotalScore\" value=\"17.1515501248358\" />\r\n          <search_score name=\"Qvalue\" value=\"0\" />\r\n        </search_hit>";

		JAXBContext jaxbContext = JAXBContext.newInstance(TestSearchHit.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		
		
		StringReader reader = new StringReader( xmlString );
		_XML_SEARCH_HIT = (SearchHit) unmarshaller.unmarshal(reader);
	}

	
	@Test
	public void _test() throws Exception {

		assertEquals( SearchConstants.LINK_TYPE_UNLINKED, PepXMLUtils.getHitType( _XML_SEARCH_HIT ) );

	}
	
	@XmlRootElement(name="search_hit")
	private static class TestSearchHit extends SearchHit {
		
		
		
	}
	
}
