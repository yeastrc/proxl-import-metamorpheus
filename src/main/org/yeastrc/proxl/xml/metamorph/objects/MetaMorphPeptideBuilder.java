package org.yeastrc.proxl.xml.metamorph.objects;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Map;

public class MetaMorphPeptideBuilder {

	
	
	/**
	 * @return the sequence
	 */
	public String getSequence() {
		return sequence;
	}
	/**
	 * @param sequence the sequence to set
	 */
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	/**
	 * @return the modifications
	 */
	public Map<Integer, Collection<BigDecimal>> getModifications() {
		return modifications;
	}
	/**
	 * @param modifications the modifications to set
	 */
	public void setModifications(Map<Integer, Collection<BigDecimal>> modifications) {
		this.modifications = modifications;
	}
	private String sequence;
	private Map<Integer, Collection<BigDecimal>> modifications;
	
}
