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

package org.yeastrc.proxl.xml.metamorph.objects;

import org.yeastrc.proxl.xml.metamorph.constants.SearchConstants;

public class MetaMorphReportedPeptide {
	
	public MetaMorphReportedPeptide( MetaMorphReportedPeptideBuilder builder ) {
		
		this.type = builder.getType();
		this.position1 = builder.getPosition1();
		this.position2 = builder.getPosition2();
		this.peptide1 = builder.getPeptide1();
		this.peptide2 = builder.getPeptide2();
		
	}
	
	public int hashCode() {
		return this.toString().hashCode();
	}
	
	public boolean equals( Object o ) {

		if( !( o instanceof MetaMorphReportedPeptide) )
			return false;
		
		return this.toString().equals( ((MetaMorphReportedPeptide)o).toString() );
	}

	public String toString() {
		
		if( this.getType() == SearchConstants.LINK_TYPE_UNLINKED ) {
			return this.getPeptide1().toString();
		} else if( this.getType() == SearchConstants.LINK_TYPE_LOOPLINK ) {
			return this.getPeptide1().toString() + "(" + this.getPosition1() + "," + this.getPosition2() + ")";
		} else if( this.getType() == SearchConstants.LINK_TYPE_CROSSLINK ) {
			return this.getPeptide1().toString() + "(" + this.getPosition1() + ")" + "-" +
        		   this.getPeptide2().toString() + "(" + this.getPosition2() + ")";
		} else if( this.getType() == SearchConstants.LINK_TYPE_MONOLINK ) {
			return this.getPeptide1().toString() + "(" + this.getPosition1() + ")";
		}
		
		return "Error: unknown peptide type";
	}
	

	public int getType() {
		return type;
	}


	public MetaMorphPeptide getPeptide1() {
		return peptide1;
	}


	public MetaMorphPeptide getPeptide2() {
		return peptide2;
	}


	public int getPosition1() {
		return position1;
	}


	public int getPosition2() {
		return position2;
	}

	
	private final int type;
	private final MetaMorphPeptide peptide1;
	private final MetaMorphPeptide peptide2;
	private final int position1;
	private final int position2;
	
}
