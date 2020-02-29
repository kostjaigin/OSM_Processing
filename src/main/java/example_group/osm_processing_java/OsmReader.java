package example_group.osm_processing_java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.container.v0_6.RelationContainer;
import org.openstreetmap.osmosis.core.container.v0_6.WayContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

public class OsmReader implements Sink {
	
	public ArrayList<MapWay> ways = new ArrayList<MapWay>();
	// Reference by id:
	public Map<Long, MapObject> objects = new HashMap<Long, MapObject>();
	
	// following methods are a requirement of the Sink interface //
	public void initialize(Map<String, Object> metaData) {}

	public void complete() {}

	public void release() {}
	
	/**
	 * Method called on each entity from original XML file
	 * @param entityContainer incoming entity for procession
	 * Method separates entities in different collections
	 */
	public void process(EntityContainer entityContainer) {
		
		if (entityContainer instanceof NodeContainer) { 
			Node node = ((NodeContainer) entityContainer).getEntity();
			MapObject temp;
			// if object was already created through ways
			// (usually not the case, if .pbf file formatted correctly)
			if (objects.get(node.getId()) != null) {
				// expand it:
				temp = objects.get(node.getId());
				temp.setLatitude(node.getLatitude());
				temp.setLongitude(node.getLongitude());
				temp.addAllTags(node.getTags());
			} 
			else {
				// else create it:
				temp = new MapObject(node.getLatitude(), node.getLongitude(), node.getId(), node.getTags());
				objects.put(temp.getID(), temp);
			}
		}
		else if (entityContainer instanceof WayContainer) {
			Way way = ((WayContainer) entityContainer).getEntity();
			// you can filter ways/nodes //
			if (this.isAppropriate(way)) {
				
				MapWay mway = new MapWay(way.getId(), way.getTags());
				// process all nodes contained in the way //
				for(WayNode wn: way.getWayNodes()) {
					MapObject temp;
					// if object was already created through nodes:
					if (objects.get(wn.getNodeId()) != null) {
						temp = objects.get(wn.getNodeId());
					}
					else {
						temp = new MapObject(wn.getNodeId());
						objects.put(temp.getID(), temp);
					}
					mway.addObject(temp);
					// linkCounter of node counts on how many ways the node appears //
					temp.linkCounter++;
					temp.addWay(mway);
				}
				this.ways.add(mway);
			}
		}
		else if (entityContainer instanceof RelationContainer) {
			// i did not need relations for my project
		}
		
	}
	
	/**
	 * Checks, whether a way is appropriate for our graph
	 * @param way from pbf file to check
	 * @return true if
	 * (1) way contains highway tag
	 * (2) way not for only pedestrians, not a footway
	 * (3) way should have a name (street name)
	 */
	private boolean isAppropriate(Way way) {
		// use Object-Boolean variable to track, if value changed
		Boolean carAllowed = null;
		Boolean namedStreet = null;
		
		for (Tag tag : way.getTags()) {
			if (tag.getKey().equals("name")) {
				namedStreet = true;
			}
			if (tag.getKey().equals("highway")) {
				carAllowed = this.noVehicleValues.contains(tag.getValue()) ? false : true;
			}
			// ... no need to iterate over all tags if both values already changed
			if (namedStreet != null && carAllowed != null) {
				break;
			}
		}
		// final decision:
		carAllowed = carAllowed == null ? false : carAllowed;
		namedStreet = namedStreet == null ? false : namedStreet;
		return (carAllowed && namedStreet);
	}
	
	/**
	 * Values of tag 'highway' of ways in OSM where no cars are allowed
	 * Feel free to fill the list: https://wiki.openstreetmap.org/wiki/Key:highway
	 * TODO use more efficient collection
	 */
	private final List<String> noVehicleValues = Arrays.asList("trunk", "motorway", "pedestrian", "footway", "bridleway", "steps", "path", "cycleway",
			"construction", "proposed", "bus_stop", "elevator", "street_lamp", "stop", "traffic_signals", "service", "track", "platform", "raceway",
			"abandoned", "road" , "escape" , "proposed" , "construction", "corridor", "bridleway" , "bus_guideway", "none", "motorway_link", "unclassified");
	
	/**
	 * Check if a node from pbf file is appropriate for your purposes
	 * TODO you can add required properties
	 * @param node
	 * @return
	 */
	private boolean isAppropriate(Node node) {
		return false;
	}

}
