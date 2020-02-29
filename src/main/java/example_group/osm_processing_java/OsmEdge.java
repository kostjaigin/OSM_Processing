package example_group.osm_processing_java;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;
import java.util.NoSuchElementException;

public class OsmEdge {
	
	/**
	 * PROPERTIES:
	 */
	private OsmNode startNode;
	private OsmNode endNode;
	
	private Double distance;
	private Double weight; 
	
	// The name of the street: 
	private String name;
	// Highway type of the Edge:
	private String highwayType;
	
	/* read from the map constructor: */
	public OsmEdge(MapWay way, OsmNode start, OsmNode target) {
		this.startNode = start;
		this.endNode = target;
		this.name = way.getTag("name");
		this.highwayType = way.getTag("highway");
	}
	
	/**
	 * GETTERS
	 */
	public Double getDistance() {
		return this.distance;
	}
	
	public Double getWeight() {
		return this.weight == null ? 0 : this.weight;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getHighwayType() {
		return this.highwayType;
	}

	public OsmNode getStartNode() {
		return this.startNode;
	}

	public OsmNode getEndNode() {
		return this.endNode;
	}
	
	/**
	 * @param
	 * @return
	 */
	public Long getEdgeId() {
		return (long)(this.getStartNode().getID()+this.getEndNode().getID());
	}
	
	/**
	 * SETTERS
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setHighwayType(String highwayType) {
		this.highwayType = highwayType;
	}
	
	/**
	 * calculate distance between two geo points
	 * using Haversine Method and store it in distance
	 * attribute of the object
	 */
	public void calculateDistance() {
		
		double lat1 = this.getStartNode().getLatitude();
		double lat2 = this.getEndNode().getLatitude();
		double lon1 = this.getStartNode().getLongitude();
		double lon2 = this.getEndNode().getLongitude();
		
		this.distance = Options.getInstance().getDistance(lat1, lon1, lat2, lon2);
	}
	

}
