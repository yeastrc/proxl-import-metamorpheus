package org.yeastrc.proxl.xml.metamorph.annotations;

import java.util.ArrayList;
import java.util.List;

import org.yeastrc.proxl.xml.metamorph.constants.SearchConstants;
import org.yeastrc.proxl_import.api.xml_dto.SearchAnnotation;

/**
 * The default order by which to sort the results.
 * 
 * @author mriffle
 *
 */
public class PSMAnnotationTypeSortOrder {

	public static List<SearchAnnotation> getPSMAnnotationTypeSortOrder() {
		List<SearchAnnotation> annotations = new ArrayList<SearchAnnotation>();
		
		{
			SearchAnnotation annotation = new SearchAnnotation();
			annotation.setAnnotationName( PSMAnnotationTypes.METAMORPH_ANNOTATION_TYPE_QVALUE );
			annotation.setSearchProgram( SearchConstants.SEARCH_PROGRAM_NAME_METAMORPH );
			annotations.add( annotation );
		}
		
		
		{
			SearchAnnotation annotation = new SearchAnnotation();
			annotation.setAnnotationName( PSMAnnotationTypes.METAMORPH_ANNOTATION_TYPE_SCORE );
			annotation.setSearchProgram( SearchConstants.SEARCH_PROGRAM_NAME_METAMORPH );
			annotations.add( annotation );
		}
		
		return annotations;
	}
}
