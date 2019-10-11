/*
 * Original author: Michael Riffle <mriffle .at. uw.edu>
 *                  
 * Copyright 2018 University of Washington - Seattle, WA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
