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

public class MetaMorphPSM {

	public MetaMorphPSM( MetaMorphPSMBuilder builder ) {
		this.scanFile = builder.getScanFile();
		this.linkerMass = builder.getLinkerMass();
		this.qValue = builder.getqValue();
		this.scanNumber = builder.getScanNumber();
		this.totalScore = builder.getTotalScore();
		this.charge = builder.getCharge();
	}
	
	private final String scanFile;
	private final int scanNumber;
	private final int charge;
	private final BigDecimal qValue;
	private final BigDecimal totalScore;
	private final BigDecimal linkerMass;
	
	public String getScanFile() {
		return scanFile;
	}

	public int getScanNumber() {
		return scanNumber;
	}

	public int getCharge() {
		return charge;
	}

	public BigDecimal getLinkerMass() {
		return linkerMass;
	}

	/**
	 * @return the qValue
	 */
	public BigDecimal getqValue() {
		return qValue;
	}

	/**
	 * @return the totalScore
	 */
	public BigDecimal getTotalScore() {
		return totalScore;
	}

	
}
