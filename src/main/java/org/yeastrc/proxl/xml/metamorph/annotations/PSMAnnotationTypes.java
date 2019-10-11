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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.yeastrc.proxl.xml.metamorph.constants.SearchConstants;
import org.yeastrc.proxl_import.api.xml_dto.FilterDirectionType;
import org.yeastrc.proxl_import.api.xml_dto.FilterablePsmAnnotationType;

public class PSMAnnotationTypes {

	// metamorpheus scores
	public static final String METAMORPH_ANNOTATION_TYPE_SCORE = "Total score";
	public static final String METAMORPH_ANNOTATION_TYPE_QVALUE = "q-value";

	/**
	 * Get the list of filterable PSM annotation types in StavroX data
	 * @return
	 */
	public static List<FilterablePsmAnnotationType> getFilterablePsmAnnotationTypes( String programName ) {
		List<FilterablePsmAnnotationType> types = new ArrayList<FilterablePsmAnnotationType>();

		if( programName.equals( SearchConstants.SEARCH_PROGRAM_NAME_METAMORPH ) ) {
			{
				FilterablePsmAnnotationType type = new FilterablePsmAnnotationType();
				type.setName( METAMORPH_ANNOTATION_TYPE_SCORE );
				type.setDescription( "Total score for peptide" );
				type.setFilterDirection( FilterDirectionType.ABOVE );
				type.setDefaultFilter( false );
	
				types.add( type );
			}
			
			{
				FilterablePsmAnnotationType type = new FilterablePsmAnnotationType();
				type.setName( METAMORPH_ANNOTATION_TYPE_QVALUE );
				type.setDescription( "Q-value." );
				type.setDefaultFilterValue( new BigDecimal( "0.05" ) );
				type.setDefaultFilter( true );
				type.setFilterDirection( FilterDirectionType.BELOW );
				
				types.add( type );
			}
		}
		
		return types;
	}
	
}
