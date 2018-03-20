package org.yeastrc.proxl.xml.metamorph.linkers;

import java.util.Collection;
import java.util.HashSet;

public class MetaMorphLinker {
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MetaMorphLinker [crosslinkMasses=" + crosslinkMasses + ", monolinkMasses=" + monolinkMasses
				+ ", metaMorphName=" + metaMorphName + ", proxlName=" + proxlName + "]";
	}
	/**
	 * @return the crosslinkMasses
	 */
	public Collection<Double> getCrosslinkMasses() {
		return crosslinkMasses;
	}
	/**
	 * @param crosslinkMasses the crosslinkMasses to set
	 */
	public void setCrosslinkMasses(Collection<Double> crosslinkMasses) {
		this.crosslinkMasses = crosslinkMasses;
	}
	/**
	 * @return the monolinkMasses
	 */
	public Collection<Double> getMonolinkMasses() {
		return monolinkMasses;
	}
	/**
	 * @param monolinkMasses the monolinkMasses to set
	 */
	public void setMonolinkMasses(Collection<Double> monolinkMasses) {
		this.monolinkMasses = monolinkMasses;
	}
	/**
	 * @return the metaMorphName
	 */
	public String getMetaMorphName() {
		return metaMorphName;
	}
	/**
	 * @param metaMorphName the metaMorphName to set
	 */
	public void setMetaMorphName(String metaMorphName) {
		this.metaMorphName = metaMorphName;
	}
	/**
	 * @return the proxlName
	 */
	public String getProxlName() {
		return proxlName;
	}
	/**
	 * @param proxlName the proxlName to set
	 */
	public void setProxlName(String proxlName) {
		this.proxlName = proxlName;
	}
	
	private Collection<Double> crosslinkMasses = new HashSet<>();
	private Collection<Double> monolinkMasses = new HashSet<>();
	private String metaMorphName;
	private String proxlName;
	
}
