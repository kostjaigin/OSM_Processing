package example_group.osm_processing_java;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * The class stands for a crossing of ways
 */

public class OsmNode implements Comparable<OsmNode> {
	
	/**
	 * PROPERTIES:
	 */
	private LinkedList<OsmEdge> edges = new LinkedList<OsmEdge>();
	
	private Double latitude;
	private Double longitude;
	private Long id;
	
	//degree = weight
	private Integer degree;
	
	public OsmNode(MapObject object) {
		this.id = object.getID();
		this.latitude = object.getLatitude();
		this.longitude = object.getLongitude();
	}
	
	// GETTERS:
	
	public Long getID() {
		return this.id;
	}
	
	public Double getLatitude() {
		return this.latitude;
	}
	
	public Double getLongitude() {
		return this.longitude;
	}
	
	//instead of getWeight
	public Integer getDegree() {
		return this.degree == null ? 0 : this.degree;
	}
	
	public ArrayList<OsmNode> getAdjacentNodes() {
		ArrayList<OsmNode> adjacentNodes = new ArrayList<OsmNode>();
		for(OsmEdge edge : edges) {
			adjacentNodes.add(edge.getEndNode());
		} 
		return adjacentNodes;
	}
	
	public ArrayList<OsmEdge> getIncidentEdges() {
		return new ArrayList<OsmEdge>(edges);
	}
	
	// SETTERS:
	public void addEdge(OsmEdge edge) {
		this.edges.add(edge);
	}
	
	public void setDegree(int degree) {
		this.degree = degree;
	}
	
	public boolean isConnected(OsmNode targetNode) {
		for (OsmEdge e : this.edges) {
			if (e.getStartNode().getID() == targetNode.getID() ||
					e.getEndNode().getID() == targetNode.getID()) { 
				return true; 
			}
		}
		return false;
	}

	public int compareTo(OsmNode node) {
		return this.degree.compareTo(node.getDegree());
	}

}
