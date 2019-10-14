package org.yeastrc.proxl.xml.metamorph.reader;

import com.moandjiezana.toml.Toml;
import org.yeastrc.proxl.xml.metamorph.linkers.MetaMorphLinker;
import org.yeastrc.proxl.xml.metamorph.linkers.MetaMorphLinkerEnd;
import org.yeastrc.proxl.xml.metamorph.linkers.MetaMorphLinkerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class LinkerProcessFromConfFilePre301 {
    public static LinkerProcessFromConfFilePre301 createInstance() {
        return new LinkerProcessFromConfFilePre301();
    }

    /**
     * Get the MetaMorphLinker for the linker defined in the toml file
     *
     * @param filename The path to the toml file
     * @return
     * @throws IOException
     */
    public MetaMorphLinker getLinkerFromConfFile(String filename) throws Exception {

        Toml confToml = new Toml().read(new File(filename));

        String metaMorphLinkerName = getLinkerNameFromConfFile(confToml);

        if (metaMorphLinkerName == null)
            throw new RuntimeException("Error: Could not find a cross-linker defined in toml file: " + filename);

        if (metaMorphLinkerName.equals("UserDefined"))
            return getUserDefinedLinkerFromConfFile(confToml);

        if (MetaMorphLinkerFactory.getLinker(metaMorphLinkerName) == null)
            throw new RuntimeException("Unable to load linker information for: " + metaMorphLinkerName);

        return MetaMorphLinkerFactory.getLinker(metaMorphLinkerName);

    }

    /**
     * Get a MetaMorphLinker for a user-defined linker in a toml file
     *
     * @param confToml
     * @return
     * @throws IOException
     */
    public MetaMorphLinker getUserDefinedLinkerFromConfFile(Toml confToml) throws Exception {

        Toml xlSearchParamters = confToml.getTable("XlSearchParameters");

        if (xlSearchParamters == null)
            throw new Exception("Could not find XlSearchParameters in config toml. Cannot proceed.");

        Collection<String> linkerCrosslinkMassNames = new HashSet<>();
        linkerCrosslinkMassNames.add("UdXLkerTotalMass");
        linkerCrosslinkMassNames.add("UdXLkerLoopMass");
        linkerCrosslinkMassNames.add("CrosslinkerTotalMass");

        Collection<String> linkerDeadEndMassNames = new HashSet<>();
        linkerDeadEndMassNames.add("UdXLkerDeadendMassH2O");
        linkerDeadEndMassNames.add("UdXLkerDeadendMassNH2");
        linkerDeadEndMassNames.add("UdXLkerDeadendMassTris");
        linkerDeadEndMassNames.add("CrosslinkerDeadEndMassH2O");
        linkerDeadEndMassNames.add("CrosslinkerDeadEndMassNH2");
        linkerDeadEndMassNames.add("CrosslinkerDeadEndMassTris");

        Collection<String> linkerCleavedLinkerMassNames = new HashSet<>();
        linkerCleavedLinkerMassNames.add("CrosslinkerShortMass");
        linkerCleavedLinkerMassNames.add("CrosslinkerLongMass");

        MetaMorphLinker linker = new MetaMorphLinker();

        List<MetaMorphLinkerEnd> linkerEnds = new ArrayList<>(2);
        linker.setLinkerEnds(linkerEnds);

        // handle linker name
        {
            String name = null;
            if (xlSearchParamters.getString("UdXLkerName") != null) {
                name = xlSearchParamters.getString("UdXLkerName");
            } else if (xlSearchParamters.getString("CrosslinkerName") != null) {
                name = xlSearchParamters.getString("CrosslinkerName");
            } else {
                throw new Exception("Could not find linker name in config toml. Cannot proceed.");
            }

            linker.setMetaMorphName(name);
            linker.setProxlName(name.toLowerCase());
        }

        // handle cleavability
        linker.setCleavable(xlSearchParamters.getBoolean("IsCleavable"));

        // handle linker end 1
        {
            if (xlSearchParamters.getString("CrosslinkerResidues") != null) {

                String residuesField = xlSearchParamters.getString("CrosslinkerResidues");
                Collection<String> residues = new HashSet<>();

                for (int i = 0; i < residuesField.length(); i++) {
                    String residue = String.valueOf(residuesField.charAt(i));
                    residues.add(residue);
                }

                MetaMorphLinkerEnd linkerEnd = new MetaMorphLinkerEnd(residues, false, false);
                linkerEnds.add(linkerEnd);
            }
        }

        // handle linker end 2
        {
            if (xlSearchParamters.getString("CrosslinkerResidues2") != null) {

                String residuesField = xlSearchParamters.getString("CrosslinkerResidues2");
                Collection<String> residues = new HashSet<>();

                for (int i = 0; i < residuesField.length(); i++) {
                    String residue = String.valueOf(residuesField.charAt(i));
                    residues.add(residue);
                }

                MetaMorphLinkerEnd linkerEnd = new MetaMorphLinkerEnd(residues, false, false);
                linkerEnds.add(linkerEnd);
            }
        }

        // handle cross-linker mass
        for (String key : linkerCrosslinkMassNames) {
            if (xlSearchParamters.getDouble(key) != null) {

                if (linker.getCrosslinkMasses() == null)
                    linker.setCrosslinkMasses(new HashSet<>());

                linker.getCrosslinkMasses().add(xlSearchParamters.getDouble(key));
                break;    // don't need to keep looking
            }
        }

        // handle mono-links
        for (String key : linkerDeadEndMassNames) {
            if (xlSearchParamters.getDouble(key) != null) {

                if (linker.getMonolinkMasses() == null)
                    linker.setMonolinkMasses(new HashSet<>());

                linker.getMonolinkMasses().add(xlSearchParamters.getDouble(key));
            }
        }

        // handle cleaved linker masses
        for (String key : linkerCleavedLinkerMassNames) {
            if (xlSearchParamters.getDouble(key) != null) {

                if (linker.getCleavedCrosslinkMasses() == null)
                    linker.setCleavedCrosslinkMasses(new HashSet<>());

                linker.getCleavedCrosslinkMasses().add(xlSearchParamters.getDouble(key));
            }
        }

        if (linker.getMonolinkMasses() == null)
            System.err.println("\tWarning: Got no monolink/deadend masses defined for linker in config toml file.");

        if (linker.getCrosslinkMasses() == null)
            throw new RuntimeException("\tError: Got no cross-link massess defined for linker in config toml file.");

        if (linkerEnds.size() != 2) {
            throw new RuntimeException("\tError. Did not get two linkable ends of the cross-linker.");
        }

        return linker;
    }

    /**
     * Get the name of the cross-linker from the toml file. Return null if one isn't found.
     *
     * @param confToml
     * @return
     * @throws IOException
     */
    public String getLinkerNameFromConfFile(Toml confToml) throws IOException {

        Toml xlSearchParamters = confToml.getTable("XlSearchParameters");

        if (xlSearchParamters != null)
            return xlSearchParamters.getString("CrosslinkerType");

        return null;
    }

}