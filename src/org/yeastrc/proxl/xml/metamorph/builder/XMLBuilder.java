package org.yeastrc.proxl.xml.metamorph.builder;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import org.yeastrc.proxl.xml.metamorph.annotations.PSMAnnotationTypes;
import org.yeastrc.proxl.xml.metamorph.annotations.PSMDefaultVisibleAnnotationTypes;
import org.yeastrc.proxl.xml.metamorph.constants.SearchConstants;
import org.yeastrc.proxl.xml.metamorph.objects.AnalysisParameters;
import org.yeastrc.proxl.xml.metamorph.objects.MetaMorphPSM;
import org.yeastrc.proxl.xml.metamorph.objects.MetaMorphReportedPeptide;
import org.yeastrc.proxl.xml.metamorph.reader.MetaMorphResultsParser;
import org.yeastrc.proxl.xml.metamorph.utils.ModUtils;
import org.yeastrc.proxl.xml.metamorph.utils.PepXMLUtils;
import org.yeastrc.proxl_import.api.xml_dto.AnnotationSortOrder;
import org.yeastrc.proxl_import.api.xml_dto.ConfigurationFile;
import org.yeastrc.proxl_import.api.xml_dto.ConfigurationFiles;
import org.yeastrc.proxl_import.api.xml_dto.CrosslinkMass;
import org.yeastrc.proxl_import.api.xml_dto.CrosslinkMasses;
import org.yeastrc.proxl_import.api.xml_dto.DefaultVisibleAnnotations;
import org.yeastrc.proxl_import.api.xml_dto.FilterablePsmAnnotation;
import org.yeastrc.proxl_import.api.xml_dto.FilterablePsmAnnotationTypes;
import org.yeastrc.proxl_import.api.xml_dto.FilterablePsmAnnotations;
import org.yeastrc.proxl_import.api.xml_dto.LinkType;
import org.yeastrc.proxl_import.api.xml_dto.LinkedPosition;
import org.yeastrc.proxl_import.api.xml_dto.LinkedPositions;
import org.yeastrc.proxl_import.api.xml_dto.Linker;
import org.yeastrc.proxl_import.api.xml_dto.Linkers;
import org.yeastrc.proxl_import.api.xml_dto.Modification;
import org.yeastrc.proxl_import.api.xml_dto.Modifications;
import org.yeastrc.proxl_import.api.xml_dto.Peptide;
import org.yeastrc.proxl_import.api.xml_dto.Peptides;
import org.yeastrc.proxl_import.api.xml_dto.ProxlInput;
import org.yeastrc.proxl_import.api.xml_dto.Psm;
import org.yeastrc.proxl_import.api.xml_dto.PsmAnnotationSortOrder;
import org.yeastrc.proxl_import.api.xml_dto.Psms;
import org.yeastrc.proxl_import.api.xml_dto.ReportedPeptide;
import org.yeastrc.proxl_import.api.xml_dto.ReportedPeptides;
import org.yeastrc.proxl_import.api.xml_dto.SearchProgram;
import org.yeastrc.proxl_import.api.xml_dto.SearchProgram.PsmAnnotationTypes;
import org.yeastrc.proxl_import.api.xml_dto.SearchProgramInfo;
import org.yeastrc.proxl_import.api.xml_dto.SearchPrograms;
import org.yeastrc.proxl_import.api.xml_dto.StaticModification;
import org.yeastrc.proxl_import.api.xml_dto.StaticModifications;
import org.yeastrc.proxl_import.api.xml_dto.VisiblePsmAnnotations;
import org.yeastrc.proxl_import.create_import_file_from_java_objects.main.CreateImportFileFromJavaObjectsMain;

public class XMLBuilder {

	/**
	 * Build and save the proxl XML file
	 * 
	 * @param analysis
	 * @param outfile
	 * @throws Exception
	 */
	public void buildAndSaveXML(
								 AnalysisParameters analysis,
			                     File outfile
			                    ) throws Exception {

		
		// root node of the XML
		ProxlInput proxlInputRoot = new ProxlInput();

		proxlInputRoot.setFastaFilename( analysis.getFASTADatabase() );
		
		SearchProgramInfo searchProgramInfo = new SearchProgramInfo();
		proxlInputRoot.setSearchProgramInfo( searchProgramInfo );
		
		//
		// Define the sort order
		//
		AnnotationSortOrder annotationSortOrder = new AnnotationSortOrder();
		searchProgramInfo.setAnnotationSortOrder( annotationSortOrder );
		
		PsmAnnotationSortOrder psmAnnotationSortOrder = new PsmAnnotationSortOrder();
		annotationSortOrder.setPsmAnnotationSortOrder( psmAnnotationSortOrder );
		
		psmAnnotationSortOrder.getSearchAnnotation().addAll( org.yeastrc.proxl.xml.metamorph.annotations.PSMAnnotationTypeSortOrder.getPSMAnnotationTypeSortOrder() );
		
		
		SearchPrograms searchPrograms = new SearchPrograms();
		searchProgramInfo.setSearchPrograms( searchPrograms );
		
		SearchProgram searchProgram = new SearchProgram();
		searchPrograms.getSearchProgram().add( searchProgram );
		
		// add metamorpheus
		searchProgram.setName( SearchConstants.SEARCH_PROGRAM_NAME_METAMORPH );
		searchProgram.setDisplayName( SearchConstants.SEARCH_PROGRAM_NAME_METAMORPH );
		searchProgram.setVersion( PepXMLUtils.getVersion( analysis ) );

		{
			PsmAnnotationTypes psmAnnotationTypes = new PsmAnnotationTypes();
			searchProgram.setPsmAnnotationTypes( psmAnnotationTypes );
			
			FilterablePsmAnnotationTypes filterablePsmAnnotationTypes = new FilterablePsmAnnotationTypes();
			psmAnnotationTypes.setFilterablePsmAnnotationTypes( filterablePsmAnnotationTypes );
			filterablePsmAnnotationTypes.getFilterablePsmAnnotationType().addAll( PSMAnnotationTypes.getFilterablePsmAnnotationTypes( SearchConstants.SEARCH_PROGRAM_NAME_METAMORPH ) );
			
		}
		
		//
		// Define which annotation types are visible by default
		//
		DefaultVisibleAnnotations xmlDefaultVisibleAnnotations = new DefaultVisibleAnnotations();
		searchProgramInfo.setDefaultVisibleAnnotations( xmlDefaultVisibleAnnotations );
		
		VisiblePsmAnnotations xmlVisiblePsmAnnotations = new VisiblePsmAnnotations();
		xmlDefaultVisibleAnnotations.setVisiblePsmAnnotations( xmlVisiblePsmAnnotations );

		xmlVisiblePsmAnnotations.getSearchAnnotation().addAll( PSMDefaultVisibleAnnotationTypes.getDefaultVisibleAnnotationTypes() );
		
		//
		// Define the linker information
		//
		Linkers linkers = new Linkers();
		proxlInputRoot.setLinkers( linkers );

		Linker linker = new Linker();
		linkers.getLinker().add( linker );
		
		linker.setName( analysis.getLinker().getProxlName() );
		
		CrosslinkMasses masses = new CrosslinkMasses();
		linker.setCrosslinkMasses( masses );
		
		for( Double mass : analysis.getLinker().getCrosslinkMasses() ) {
			CrosslinkMass xlinkMass = new CrosslinkMass();
			linker.getCrosslinkMasses().getCrosslinkMass().add( xlinkMass );
			
			// set the mass for this crosslinker to the calculated mass for the crosslinker, as defined in the properties file
			xlinkMass.setMass( BigDecimal.valueOf( mass ) );
		}
		
		//
		// Define the static mods
		//
		StaticModifications smods = new StaticModifications();
		proxlInputRoot.setStaticModifications( smods );
		
		for( String moddedResidue : analysis.getConfReader().getStaticModifications().keySet() ) {
				
				StaticModification xmlSmod = new StaticModification();
				xmlSmod.setAminoAcid( moddedResidue );
				xmlSmod.setMassChange( analysis.getConfReader().getStaticModifications().get( moddedResidue ) );
				
				smods.getStaticModification().add( xmlSmod );
		}
		
		//
		// Define the peptide and PSM data
		//
		ReportedPeptides reportedPeptides = new ReportedPeptides();
		proxlInputRoot.setReportedPeptides( reportedPeptides );
		
		// parse the data from the pepXML into a java data structure suitable for writing as ProXL XML
		Map<MetaMorphReportedPeptide, Collection<MetaMorphPSM>> resultsByReportedPeptide = 
				MetaMorphResultsParser.getInstance().getResultsFromAnalysis( analysis );
		
		// create a unique set of peptides found, to ensure each one is found in at least 
		// one of the reported proteins
		Collection<String> peptides = new HashSet<>();
		
		// iterate over each distinct reported peptide
		for( MetaMorphReportedPeptide rp : resultsByReportedPeptide.keySet() ) {
			
			peptides.add( rp.getPeptide1().getSequence() );
			if( rp.getPeptide2() != null ) peptides.add( rp.getPeptide2().getSequence() );
			
			ReportedPeptide xmlReportedPeptide = new ReportedPeptide();
			reportedPeptides.getReportedPeptide().add( xmlReportedPeptide );
			
			xmlReportedPeptide.setReportedPeptideString( rp.toString() );
			
			if( rp.getType() == SearchConstants.LINK_TYPE_CROSSLINK )
				xmlReportedPeptide.setType( LinkType.CROSSLINK );
			else if( rp.getType() == SearchConstants.LINK_TYPE_LOOPLINK )
				xmlReportedPeptide.setType( LinkType.LOOPLINK );
			else
				xmlReportedPeptide.setType( LinkType.UNLINKED );
			
			Peptides xmlPeptides = new Peptides();
			xmlReportedPeptide.setPeptides( xmlPeptides );
			
			// add in the 1st parsed peptide
			{
				Peptide xmlPeptide = new Peptide();
				xmlPeptides.getPeptide().add( xmlPeptide );
				
				xmlPeptide.setSequence( rp.getPeptide1().getSequence() );
				
				// add in the mods for this peptide
				if( rp.getPeptide1().getModifications() != null && rp.getPeptide1().getModifications().keySet().size() > 0 ) {
					
					Modifications xmlModifications = new Modifications();
					xmlPeptide.setModifications( xmlModifications );
					
					for( int position : rp.getPeptide1().getModifications().keySet() ) {
						for( BigDecimal modMass : rp.getPeptide1().getModifications().get( position ) ) {
	
							Modification xmlModification = new Modification();
							xmlModifications.getModification().add( xmlModification );
							
							xmlModification.setMass( modMass );
							xmlModification.setPosition( new BigInteger( String.valueOf( position ) ) );
							xmlModification.setIsMonolink( ModUtils.isMonolink( modMass, analysis.getConfReader() ) );
							
						}
					}
				}
				
				// add in the linked position(s) in this peptide
				if( rp.getType() == SearchConstants.LINK_TYPE_CROSSLINK || rp.getType() == SearchConstants.LINK_TYPE_LOOPLINK ) {
					
					LinkedPositions xmlLinkedPositions = new LinkedPositions();
					xmlPeptide.setLinkedPositions( xmlLinkedPositions );
					
					LinkedPosition xmlLinkedPosition = new LinkedPosition();
					xmlLinkedPositions.getLinkedPosition().add( xmlLinkedPosition );
					xmlLinkedPosition.setPosition( new BigInteger( String.valueOf( rp.getPosition1() ) ) );
					
					if( rp.getType() == SearchConstants.LINK_TYPE_LOOPLINK ) {
						
						xmlLinkedPosition = new LinkedPosition();
						xmlLinkedPositions.getLinkedPosition().add( xmlLinkedPosition );
						xmlLinkedPosition.setPosition( new BigInteger( String.valueOf( rp.getPosition2() ) ) );
						
					}
				}
				
			}
			
			
			// add in the 2nd parsed peptide, if it exists
			if( rp.getPeptide2() != null ) {
				
				Peptide xmlPeptide = new Peptide();
				xmlPeptides.getPeptide().add( xmlPeptide );
				
				xmlPeptide.setSequence( rp.getPeptide2().getSequence() );
				
				// add in the mods for this peptide
				if( rp.getPeptide2().getModifications() != null && rp.getPeptide2().getModifications().keySet().size() > 0 ) {
					
					Modifications xmlModifications = new Modifications();
					xmlPeptide.setModifications( xmlModifications );
					
					for( int position : rp.getPeptide2().getModifications().keySet() ) {
						for( BigDecimal modMass : rp.getPeptide2().getModifications().get( position ) ) {
							
							Modification xmlModification = new Modification();
							xmlModifications.getModification().add( xmlModification );
							
							xmlModification.setMass( modMass );
							xmlModification.setPosition( new BigInteger( String.valueOf( position ) ) );
							xmlModification.setIsMonolink( ModUtils.isMonolink( modMass, analysis.getConfReader() ) );
							
						}
					}
				}
				
				// add in the linked position in this peptide
				if( rp.getType() == SearchConstants.LINK_TYPE_CROSSLINK ) {
					
					LinkedPositions xmlLinkedPositions = new LinkedPositions();
					xmlPeptide.setLinkedPositions( xmlLinkedPositions );
					
					LinkedPosition xmlLinkedPosition = new LinkedPosition();
					xmlLinkedPositions.getLinkedPosition().add( xmlLinkedPosition );
					xmlLinkedPosition.setPosition( new BigInteger( String.valueOf( rp.getPosition2() ) ) );
				}
			}
			
			
			// add in the PSMs and annotations
			Psms xmlPsms = new Psms();
			xmlReportedPeptide.setPsms( xmlPsms );
			
			// iterate over all PSMs for this reported peptide
			for( MetaMorphPSM result : resultsByReportedPeptide.get( rp ) ) {
				Psm xmlPsm = new Psm();
				xmlPsms.getPsm().add( xmlPsm );
				
				xmlPsm.setScanNumber( new BigInteger( String.valueOf( result.getScanNumber() ) ) );
				xmlPsm.setPrecursorCharge( new BigInteger( String.valueOf( result.getCharge() ) ) );
				xmlPsm.setScanFileName( result.getScanFile() );
				
				if( rp.getType() == SearchConstants.LINK_TYPE_CROSSLINK || rp.getType() == SearchConstants.LINK_TYPE_LOOPLINK )
					xmlPsm.setLinkerMass( result.getLinkerMass() );
				
				// add in the filterable PSM annotations (e.g., score)
				FilterablePsmAnnotations xmlFilterablePsmAnnotations = new FilterablePsmAnnotations();
				xmlPsm.setFilterablePsmAnnotations( xmlFilterablePsmAnnotations );
				
				// handle qvalue
				{
					FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
					xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add( xmlFilterablePsmAnnotation );
					
					xmlFilterablePsmAnnotation.setAnnotationName( PSMAnnotationTypes.METAMORPH_ANNOTATION_TYPE_QVALUE );
					xmlFilterablePsmAnnotation.setSearchProgram( SearchConstants.SEARCH_PROGRAM_NAME_METAMORPH );
					xmlFilterablePsmAnnotation.setValue( result.getqValue() );
				}

				// handle total score
				{
					FilterablePsmAnnotation xmlFilterablePsmAnnotation = new FilterablePsmAnnotation();
					xmlFilterablePsmAnnotations.getFilterablePsmAnnotation().add( xmlFilterablePsmAnnotation );
					
					xmlFilterablePsmAnnotation.setAnnotationName( PSMAnnotationTypes.METAMORPH_ANNOTATION_TYPE_SCORE );
					xmlFilterablePsmAnnotation.setSearchProgram( SearchConstants.SEARCH_PROGRAM_NAME_METAMORPH );
					xmlFilterablePsmAnnotation.setValue( result.getTotalScore() );
				}
				
			}//end iterating over all PSMs for a reported peptide
			
			
		}// end iterating over distinct reported peptides


		// add in the matched proteins section
		MatchedProteinsBuilder.getInstance().buildMatchedProteins(
				                                                   proxlInputRoot,
				                                                   analysis.getFastaFile(),
				                                                   resultsByReportedPeptide.keySet()
				                                                  );		
		
		
		// add in the config file(s)
		ConfigurationFiles xmlConfigurationFiles = new ConfigurationFiles();
		proxlInputRoot.setConfigurationFiles( xmlConfigurationFiles );
		
		ConfigurationFile xmlConfigurationFile = new ConfigurationFile();
		xmlConfigurationFiles.getConfigurationFile().add( xmlConfigurationFile );
		
		xmlConfigurationFile.setSearchProgram( SearchConstants.SEARCH_PROGRAM_NAME_METAMORPH );
		xmlConfigurationFile.setFileName( ( new File( analysis.getConfFilePath() ) ).getName() );
		xmlConfigurationFile.setFileContent( Files.readAllBytes( FileSystems.getDefault().getPath( analysis.getConfFilePath() ) ) );

		
		//make the xml file
		CreateImportFileFromJavaObjectsMain.getInstance().createImportFileFromJavaObjectsMain(outfile, proxlInputRoot);
		
	}
	

	
	
}
