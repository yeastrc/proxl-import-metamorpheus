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
