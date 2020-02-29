package example_group.osm_processing_java;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import crosby.binary.osmosis.OsmosisReader;



/**
 * Simple App to produce a graph representation of the given OSM map
 * TU Berlin university project
 */
public class App 
{
    public static void main( String[] args )
    {
    	String filepath = Extras.chooseFile();
    	App.CreateGraph(filepath);
    	// now the graph is ready to use //
    }
    
    public static void CreateGraph(String pathToPBF) {
    	
    	InputStream inputStream = null;
    	
    	try {
			inputStream = new FileInputStream(pathToPBF);
    	} catch (FileNotFoundException fnfe) {
    		JOptionPane.showMessageDialog(new JFrame(), "File not found!", "ERROR",
    		        JOptionPane.ERROR_MESSAGE);	
    		// exit after error //
    		System.exit(0);
    	}
    	
    	// declare instance of our custom class //
    	OsmReader custom = new OsmReader();
    	// reader provided by osmosis //
    	OsmosisReader reader = new OsmosisReader(inputStream);
    	// set processing point //
    	reader.setSink(custom);
		// initial parsing of the .pbf file //
		reader.run();
		// create graph:
		OsmGraph graph = OsmGraph.getInstance();
		// secondary parsing of ways/creation of edges:
		graph.parseMapWays(custom.ways, custom.objects);
    }
    
}
