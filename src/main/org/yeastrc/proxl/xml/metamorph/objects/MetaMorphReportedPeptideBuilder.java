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

public class MetaMorphReportedPeptideBuilder {
	
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return the peptide1
	 */
	public MetaMorphPeptide getPeptide1() {
		return peptide1;
	}
	/**
	 * @param peptide1 the peptide1 to set
	 */
	public void setPeptide1(MetaMorphPeptide peptide1) {
		this.peptide1 = peptide1;
	}
	/**
	 * @return the peptide2
	 */
	public MetaMorphPeptide getPeptide2() {
		return peptide2;
	}
	/**
	 * @param peptide2 the peptide2 to set
	 */
	public void setPeptide2(MetaMorphPeptide peptide2) {
		this.peptide2 = peptide2;
	}
	/**
	 * @return the position1
	 */
	public int getPosition1() {
		return position1;
	}
	/**
	 * @param position1 the position1 to set
	 */
	public void setPosition1(int position1) {
		this.position1 = position1;
	}
	/**
	 * @return the position2
	 */
	public int getPosition2() {
		return position2;
	}
	/**
	 * @param position2 the position2 to set
	 */
	public void setPosition2(int position2) {
		this.position2 = position2;
	}
	
	private int type;
	private MetaMorphPeptide peptide1;
	private MetaMorphPeptide peptide2;
	private int position1;
	private int position2;
	
}
