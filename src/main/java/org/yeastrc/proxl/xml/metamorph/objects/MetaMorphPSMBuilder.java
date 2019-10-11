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

import java.math.BigDecimal;

public class MetaMorphPSMBuilder {
	
	/**
	 * @return the scanFile
	 */
	public String getScanFile() {
		return scanFile;
	}
	/**
	 * @param scanFile the scanFile to set
	 */
	public void setScanFile(String scanFile) {
		this.scanFile = scanFile;
	}
	/**
	 * @return the scanNumber
	 */
	public int getScanNumber() {
		return scanNumber;
	}
	/**
	 * @param scanNumber the scanNumber to set
	 */
	public void setScanNumber(int scanNumber) {
		this.scanNumber = scanNumber;
	}
	/**
	 * @return the charge
	 */
	public int getCharge() {
		return charge;
	}
	/**
	 * @param charge the charge to set
	 */
	public void setCharge(int charge) {
		this.charge = charge;
	}
	/**
	 * @return the qValue
	 */
	public BigDecimal getqValue() {
		return qValue;
	}
	/**
	 * @param qValue the qValue to set
	 */
	public void setqValue(BigDecimal qValue) {
		this.qValue = qValue;
	}
	/**
	 * @return the totalScore
	 */
	public BigDecimal getTotalScore() {
		return totalScore;
	}
	/**
	 * @param totalScore the totalScore to set
	 */
	public void setTotalScore(BigDecimal totalScore) {
		this.totalScore = totalScore;
	}
	/**
	 * @return the linkerMass
	 */
	public BigDecimal getLinkerMass() {
		return linkerMass;
	}
	/**
	 * @param linkerMass the linkerMass to set
	 */
	public void setLinkerMass(BigDecimal linkerMass) {
		this.linkerMass = linkerMass;
	}
	
	private String scanFile;
	private int scanNumber;
	private int charge;
	private BigDecimal qValue;
	private BigDecimal totalScore;
	private BigDecimal linkerMass;
	
}
