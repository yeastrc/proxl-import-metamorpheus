MetaMorpheus to Proxl XML Converter
=============================

Use this program to convert the results of a MetaMorpheus cross-linking analysis to
proxl XML suitable for import into the proxl web application.

**Note:** When running MetaMorpheus, you must enable output as PepXML when defining your XL Search taslk.

How To Run
-------------
1. Download the [latest release](https://github.com/yeastrc/proxl-import-metamorpheus/releases).
2. Run the program ``java -jar metaMorph2ProxlXML.jar`` with no arguments to see the possible parameters.

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

 Example:
 
  java -jar metamorph2ProxlXML.jar -x ./results.pep.xml -o ./results.proxl.xml\
  -f /data/mass_spec/yeast.fa -c ./XLSearchTaskconfig.toml

  java -jar metamorph2ProxlXML.jar -x ./results.pep.xml -o ./results.proxl.xml\
  -f /data/mass_spec/yeast.fa -c ./XLSearchTaskconfig.toml

  java -jar metamorph2ProxlXML.jar -x .\Task1-XLSearchTask\my-edc-data.pep.XML
  -c ".\Task Settings\Task1-XLSearchTaskconfig.toml"
  -f ..\fastas\small_fasta.fasta
  -o my-edc-data.proxl.xml

  More information: https://github.com/yeastrc/proxl-import-metamorpheus

