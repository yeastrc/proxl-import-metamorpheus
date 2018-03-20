package org.yeastrc.proxl.xml.metamorph.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.yeastrc.proxl.xml.metamorph.builder.XMLBuilder;
import org.yeastrc.proxl.xml.metamorph.objects.AnalysisParameters;
import org.yeastrc.proxl.xml.metamorph.reader.ConfReader;

import jargs.gnu.CmdLineParser;
import jargs.gnu.CmdLineParser.IllegalOptionValueException;
import jargs.gnu.CmdLineParser.UnknownOptionException;


/**
 * Run the program.
 * @author Michael Riffle
 * @date July, 2016
 *
 */
public class MainProgram {
	
	public void convertSearch( String pepXMLFilePath,
			                   String outFilePath,
			                   String fastaFilePath,
			                   String confFilePath
			                  ) throws Exception {
		
		AnalysisParameters analysis = AnalysisParameters.loadAnalysis( pepXMLFilePath );
		
		analysis.setConfReader( ConfReader.getInstance( confFilePath ) );

		analysis.setConfFilePath( confFilePath );
		analysis.setFastaFile( new File( fastaFilePath ) );
		
		
		analysis.setLinker( analysis.getConfReader().getMetaMorphLinker() );		
		System.err.println( "\tGot linker: " + analysis.getLinker() );
		
		
		XMLBuilder builder = new XMLBuilder();
		builder.buildAndSaveXML(analysis, new File( outFilePath ) );		
	}
	
	public static void main( String[] args ) throws Exception {
		
		if( args.length < 1 || args[ 0 ].equals( "-h" ) ) {
			printHelp();
			System.exit( 0 );
		}
		
		CmdLineParser cmdLineParser = new CmdLineParser();
        
		CmdLineParser.Option pepXMLOpt = cmdLineParser.addStringOption( 'x', "pepxml" );	
		CmdLineParser.Option outfileOpt = cmdLineParser.addStringOption( 'o', "out-file" );	
		CmdLineParser.Option confOpt = cmdLineParser.addStringOption( 'c', "conf" );	
		CmdLineParser.Option fastaOpt = cmdLineParser.addStringOption( 'f', "fasta-file" );

        // parse command line options
        try { cmdLineParser.parse(args); }
        catch (IllegalOptionValueException e) {
        	printHelp();
            System.exit( 1 );
        }
        catch (UnknownOptionException e) {
           printHelp();
           System.exit( 1 );
        }
        
		/*
		 * Parse the pepXML file option
		 */
        String pepXMLFilePath = (String)cmdLineParser.getOptionValue( pepXMLOpt );
        if( pepXMLFilePath == null || pepXMLFilePath.equals( "" ) ) {
        	System.err.println( "Must specify a pepXML file. See help:\n" );
        	printHelp();
        	
        	System.exit( 1 );
        }
        
        File pepXMLFile = new File( pepXMLFilePath );
        if( !pepXMLFile.exists() ) {
        	System.err.println( "The pepXML file: " + pepXMLFilePath + " does not exist." );
        	System.exit( 1 );
        }
        
        if( !pepXMLFile.canRead() ) {
        	System.err.println( "Can not read pepXML file: " + pepXMLFilePath );
        	System.exit( 1 );
        }
        
        
        /*
         * Parse the output file option
         */
        String outFilePath = (String)cmdLineParser.getOptionValue( outfileOpt );
        if( outFilePath == null || outFilePath.equals( "" ) ) {
        	System.err.println( "Must specify an output file. See help:\n" );
        	printHelp();
        	
        	System.exit( 1 );
        }
        File outFile = new File( outFilePath );
        if( outFile.exists() ) {
        	System.err.println( "The output file: " + outFilePath + " already exists." );
        	System.exit( 1 );
        }
        
        
        /*
         * Parse the conf files options
         */
        String confFilePath = (String)cmdLineParser.getOptionValue( confOpt );
        if( confFilePath == null || confFilePath.equals( "" ) ) {
        	System.err.println( "Must specify a conf file. See help:\n" );
        	printHelp();
        	
        	System.exit( 1 );
        }
        
        File confFile = new File( confFilePath );
        if( !confFile.exists() ) {
        	System.err.println( "The conf file: " + confFilePath + " does not exist." );
        	System.exit( 1 );
        }
        
        if( !confFile.canRead() ) {
        	System.err.println( "Can not read conf file: " + confFilePath );
        	System.exit( 1 );
        }
        
        /*
         * Parse the fasta file option
         */
        String fastaFilePath = (String)cmdLineParser.getOptionValue( fastaOpt );
        if( fastaFilePath == null || fastaFilePath.equals( "" ) ) {
        	System.err.println( "Must specify a fasta file. See help:\n" );
        	printHelp();
        	
        	System.exit( 1 );
        }
        
        File fastaFile = new File( fastaFilePath );
        if( !fastaFile.exists() ) {
        	System.err.println( "The fasta file: " + fastaFilePath + " does not exist." );
        	System.exit( 1 );
        }
        
        if( !fastaFile.canRead() ) {
        	System.err.println( "Can not read fasta file: " + fastaFilePath );
        	System.exit( 1 );
        }
        
        
        System.err.println( "Converting MetaMorpheus to ProXL XML with the following parameters:" );
        System.err.println( "\tpepXML path: " + pepXMLFilePath );
        System.err.println( "\toutput file path: " + outFilePath );
        System.err.println( "\tfasta file path: " + fastaFilePath );
        System.err.println( "\tconf file path: " + confFilePath );
             
        /*
         * Run the conversion
         */
        MainProgram mp = new MainProgram();
        mp.convertSearch( pepXMLFilePath,
        		          outFilePath,
        		          fastaFilePath,
        		          confFilePath
        		         );

        
        System.err.println( "Done." );        
        System.exit( 0 );        
	}
	
	public static void printHelp() {
		
		try( BufferedReader br = new BufferedReader( new InputStreamReader( MainProgram.class.getResourceAsStream( "help.txt" ) ) ) ) {
			
			String line = null;
			while ( ( line = br.readLine() ) != null )
				System.out.println( line );				
			
		} catch ( Exception e ) {
			System.out.println( "Error printing help." );
		}
	}
}
