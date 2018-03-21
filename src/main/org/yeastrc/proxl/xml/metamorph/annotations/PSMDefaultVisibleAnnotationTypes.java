package org.yeastrc.proxl.xml.metamorph.annotations;

import java.util.ArrayList;
import java.util.List;

import org.yeastrc.proxl.xml.metamorph.constants.*;
import org.yeastrc.proxl_import.api.xml_dto.SearchAnnotation;

public class PSMDefaultVisibleAnnotationTypes {

	/**
	 * Get the default visibile annotation types for metamorpheus data
	 * @return
	 */
	public static List<SearchAnnotation> getDefaultVisibleAnnotationTypes() {
		List<SearchAnnotation> annotations = new ArrayList<SearchAnnotation>();
		
		
		{
			SearchAnnotation annotation = new SearchAnnotation();
			annotation.setAnnotationName( PSMAnnotationTypes.METAMORPH_ANNOTATION_TYPE_QVALUE);
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
