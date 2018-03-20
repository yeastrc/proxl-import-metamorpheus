package org.yeastrc.proxl.xml.metamorph.objects;

import java.io.File;
import java.util.Collection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.yeastrc.proxl.xml.metamorph.linkers.MetaMorphLinker;
import org.yeastrc.proxl.xml.metamorph.reader.ConfReader;

import net.systemsbiology.regis_web.pepxml.MsmsPipelineAnalysis;

public class AnalysisParameters {

	/**
	 * Load the data contained in the supplied pepXML file.
	 * 
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static AnalysisParameters loadAnalysis( String filename ) throws Exception {
		
		File pepXMLFile = new File( filename );
		JAXBContext jaxbContext = JAXBContext.newInstance(MsmsPipelineAnalysis.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		MsmsPipelineAnalysis msAnalysis = (MsmsPipelineAnalysis)jaxbUnmarshaller.unmarshal(pepXMLFile);
		
		AnalysisParameters ipa = new AnalysisParameters();
		ipa.setAnalysis( msAnalysis );
		return ipa;
		
	}
	
	
	private MsmsPipelineAnalysis analysis;
	private ConfReader confReader;
	private File fastaFile;
	private String confFilePath;
	private MetaMorphLinker linker;
	
	/**
	 * Get the root element of the pepXML file as a JAXB object
	 * 
	 * @return
	 */
	public MsmsPipelineAnalysis getAnalysis() {
		return analysis;
	}

	private void setAnalysis(MsmsPipelineAnalysis analysis) {
		this.analysis = analysis;
	}

	public File getFastaFile() {
		return fastaFile;
	}

	public void setFastaFile(File fastaFile) {
		this.fastaFile = fastaFile;
	}

	public ConfReader getConfReader() {
		return confReader;
	}

	public void setConfReader(ConfReader kojakConfReader) {
		this.confReader = kojakConfReader;
	}
	/**
	 * @return the linker
	 */
	public MetaMorphLinker getLinker() {
		return linker;
	}
	/**
	 * @param linker the linker to set
	 */
	public void setLinker(MetaMorphLinker linker) {
		this.linker = linker;
	}

	/**
	 * @return the confFilePath
	 */
	public String getConfFilePath() {
		return confFilePath;
	}

	/**
	 * @param confFilePath the confFilePath to set
	 */
	public void setConfFilePath(String confFilePath) {
		this.confFilePath = confFilePath;
	}

	/**
	 * Get the name of the FASTA file used in this search. It is assumed that, if multiple
	 * msms run summary elements are present, that they all use the same FASTA file. Only
	 * the first one is looked at.
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getFASTADatabase() throws Exception {		
		return this.getFastaFile().getName();
	}

	
}
