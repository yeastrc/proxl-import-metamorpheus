package org.yeastrc.proxl.xml.metamorph.objects;

import java.math.BigDecimal;

public class MetaMorphPSM {

	private String scanFile;
	private int scanNumber;
	private int charge;
	private BigDecimal qValue;
	private BigDecimal totalScore;
	private BigDecimal linkerMass;
	
	public String getScanFile() {
		return scanFile;
	}
	public void setScanFile(String scanFile) {
		this.scanFile = scanFile;
	}
	public int getScanNumber() {
		return scanNumber;
	}
	public void setScanNumber(int scanNumber) {
		this.scanNumber = scanNumber;
	}
	public int getCharge() {
		return charge;
	}
	public void setCharge(int charge) {
		this.charge = charge;
	}
	public BigDecimal getLinkerMass() {
		return linkerMass;
	}
	public void setLinkerMass(BigDecimal linkerMass) {
		this.linkerMass = linkerMass;
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
	
	
	
}
