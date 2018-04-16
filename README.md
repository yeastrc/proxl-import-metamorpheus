MetaMorpheus to Proxl XML Converter
=============================

Use this program to convert the results of a MetaMorpheus cross-linking analysis to
proxl XML suitable for import into the proxl web application.

As of this writing, only DSS and DSSO cross-linkers are supported. Loop-link results
are not supported because of a bug in the pepXML output for loop-links. Deadends/mono-link
hits are also skipped because of:

	"Deadend hits are reported in a less-than-optimal way. The mass of the cross-linker
	is reported as the "massDiff" of the hit (which is usually an indicator of error of
	the precursor match), and the position of the linker is reported as the "score" for
	the hit--instead of the actual score for the hit, which isn't reported for
	deadends."

	"There is no way to know what the "score" was for deadend hits. Since proxl requires
	that this score be available for all hits, and it's not available for deadends, I've
	opted to skip deadends for now."


How To Run
-------------
1. Download the [latest release](https://github.com/yeastrc/proxl-import-metamorpheus/releases).
2. Run the program ``java -jar metaMorpheus2ProxlXML.jar`` with no arguments to see the possible parameters.

For more information on importing data into Proxl, please see the [Proxl Import Documentation](http://proxl-web-app.readthedocs.io/en/latest/using/upload_data.html).

More Information About Proxl
-----------------------------
For more information about Proxl, visit http://proxl-ms.org/.


Command line documentation
---------------------------
Usage:
  java -jar metamorph2ProxlXML.jar -x pepXML file path -o output file path
                               -f fasta file path -c conf file (toml file)
  
 Options:
  
     -x or --pepxml       : Required. The full path to the pepXML file
     
     -o or --out-file     : Required. Full path (including file name) to which
                            to write the Proxl XML
  
     -c or --conf         : Required. Full path to  configuration file
                            used for the MetaMorpheus cross-linking search.
                            (e.g. XLSearchTaskconfig.toml)
                          
     -f or --fasta-file   : Required. Full path to the fasta file used in
                            the search.
     
     -l or --linker       : Optional. If the linker was not set to the
                            built-in "DSS" or "DSSO" linkers using the GUI
                            and a user-defined linker was used in this search
                            (such as BS3 or EDC), use this to specify the
                            linker name that proxl will recognize. See below
                            for currently-supported values.
 
 Example:
 
  java -jar metamorph2ProxlXML.jar -x ./results.pep.xml -o ./results.proxl.xml\
  -f /data/mass_spec/yeast.fa -c ./XLSearchTaskconfig.toml

  java -jar metamorph2ProxlXML.jar -x ./results.pep.xml -o ./results.proxl.xml\
  -f /data/mass_spec/yeast.fa -c ./XLSearchTaskconfig.toml -l edc

  More information: https://github.com/yeastrc/proxl-import-metamorpheus

 Currently-supported linkers:
 	dss
	bs3
	edc
	bs2
	sulfo-smcc
	dsso
	
	bs3.sty (supports links from K/nterm-K/S/T/Y/nterm)
	dss.sty (supports links from K/nterm-K/S/T/Y/nterm)

  
