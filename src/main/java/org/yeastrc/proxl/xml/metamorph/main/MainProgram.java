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

package org.yeastrc.proxl.xml.metamorph.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.yeastrc.proxl.xml.metamorph.builder.XMLBuilder;
import org.yeastrc.proxl.xml.metamorph.constants.Constants;
import org.yeastrc.proxl.xml.metamorph.objects.AnalysisParameters;
import org.yeastrc.proxl.xml.metamorph.reader.LinkerProcessorFromConfFile;

import picocli.CommandLine;

@CommandLine.Command(name = "java -jar " + Constants.CONVERSION_PROGRAM_NAME,
		mixinStandardHelpOptions = true,
		version = Constants.CONVERSION_PROGRAM_NAME + " " + Constants.CONVERSION_PROGRAM_VERSION,
		sortOptions = false,
		synopsisHeading = "%n",
		descriptionHeading = "%n@|bold,underline Description:|@%n%n",
		optionListHeading = "%n@|bold,underline Options:|@%n",
		description = "Convert the results of a MetaMorpheus XL analysis to a ProXL XML file suitable for import into ProXL.\n\n" +
				"More info at: " + Constants.CONVERSION_PROGRAM_URI,
		footer = {
				"",
				"@|bold,underline Examples|@:",
				"java -jar " + Constants.CONVERSION_PROGRAM_NAME + " ^\n" +
						"-x .\\Task1-XLSearchTask\\my-edc-data.pep.XML ^\n" +
						"-f \"c:\\fastas\\yeast.fa\" ^\n" +
						"-c \".\\Task Settings\\Task1-XLSearchTaskconfig.toml\" ^\n" +
						"-o my-edc-data.proxl.xml",
				""
		}
)
public class MainProgram implements Runnable {

	@CommandLine.Option(names = { "-x", "--pepxml" }, required = true, description = "Full path to the pepXML file generated by MetaMorpheus.")
	private File pepXMLFile;

	@CommandLine.Option(names = { "-o", "--out-file" }, required = true, description = "Full path to use for the ProXL XML output file (including file name).")
	private File outFile;

	@CommandLine.Option(names = { "-f", "--fasta-file" }, required = true, description = "Full path to FASTA file used in the experiment.")
	private File fastaFile;

	@CommandLine.Option(names = { "-c", "--conf" },  required = true, description = "The full path to the configuration file used in the MetaMorpheus search (e.g. XLSearchTaskconfig.toml).")
	private File confFile;

	public void convertSearch( String pepXMLFilePath,
			                   String outFilePath,
			                   String fastaFilePath,
			                   String confFilePath
			                  ) throws Exception {
		
		AnalysisParameters analysis = AnalysisParameters.loadAnalysis( pepXMLFilePath );
		
		analysis.setConfFilePath( confFilePath );
		analysis.setFastaFile( new File( fastaFilePath ) );
		analysis.setLinker( LinkerProcessorFromConfFile.createInstance().getLinkerFromConfFile( confFilePath )  );
		
		System.err.println( "\tGot linker: " + analysis.getLinker() );
		
		XMLBuilder builder = new XMLBuilder();
		builder.buildAndSaveXML(analysis, new File( outFilePath ) );		
	}

	public void run()  {

		printRuntimeInfo();

		if( !pepXMLFile.exists() ) {
        	System.err.println( "The pepXML file: " + pepXMLFile.getAbsolutePath() + " does not exist." );
        	System.exit( 1 );
        }
        
        if( !pepXMLFile.canRead() ) {
        	System.err.println( "Can not read pepXML file: " + pepXMLFile.getAbsolutePath() );
        	System.exit( 1 );
        }
        
        
        /*
         * Parse the output file option
         */
        if( outFile.exists() ) {
        	System.err.println( "The output file: " + outFile.getAbsolutePath() + " already exists." );
        	System.exit( 1 );
        }
        
        
        /*
         * Parse the conf files options
         */
        if( !confFile.exists() ) {
        	System.err.println( "The conf file: " + confFile.getAbsolutePath() + " does not exist." );
        	System.exit( 1 );
        }
        
        if( !confFile.canRead() ) {
        	System.err.println( "Can not read conf file: " + confFile.getAbsolutePath() );
        	System.exit( 1 );
        }
        
        /*
         * Parse the fasta file option
         */

        if( !fastaFile.exists() ) {
        	System.err.println( "The fasta file: " + fastaFile.getAbsolutePath() + " does not exist." );
        	System.exit( 1 );
        }
        
        if( !fastaFile.canRead() ) {
        	System.err.println( "Can not read fasta file: " + fastaFile.getAbsolutePath() );
        	System.exit( 1 );
        }
        
        // get the user supplied linker name

        System.err.println( "Converting MetaMorpheus to ProXL XML with the following parameters:" );
        System.err.println( "\tpepXML path: " + pepXMLFile.getAbsolutePath() );
        System.err.println( "\toutput file path: " + outFile.getAbsolutePath() );
        System.err.println( "\tfasta file path: " + fastaFile.getAbsolutePath() );
        System.err.println( "\tconf file path: " + confFile.getAbsolutePath() );

        /*
         * Run the conversion
         */
        try {
			MainProgram mp = new MainProgram();
			mp.convertSearch(pepXMLFile.getAbsolutePath(),
					outFile.getAbsolutePath(),
					fastaFile.getAbsolutePath(),
					confFile.getAbsolutePath()
			);


			System.err.println("Done.");
			System.exit(0);
		} catch( Throwable t ) {

        	System.out.println( "\nError encountered:" );
        	System.out.println( t.getMessage() );
        	System.exit( 1 );

		}
	}

	public static void main( String[] args ) {

		CommandLine.run(new MainProgram(), args);

	}

	/**
	 * Print runtime info to STD ERR
	 * @throws Exception
	 */
	public void printRuntimeInfo() {

		try( BufferedReader br = new BufferedReader( new InputStreamReader( MainProgram.class.getResourceAsStream( "run.txt" ) ) ) ) {

			String line = null;
			while ( ( line = br.readLine() ) != null ) {

				line = line.replace( "{{URL}}", Constants.CONVERSION_PROGRAM_URI );
				line = line.replace( "{{VERSION}}", Constants.CONVERSION_PROGRAM_VERSION );

				System.err.println( line );

			}

			System.err.println( "" );

		} catch ( Exception e ) {
			System.out.println( "Error printing runtime information." );
		}
	}
}
