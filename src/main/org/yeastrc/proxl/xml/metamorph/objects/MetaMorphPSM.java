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
