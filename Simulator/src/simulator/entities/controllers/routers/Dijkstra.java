/** Implements Dijkstra algorithm **/
/** Verification Test -- Done **/

package simulator.entities.controllers.routers;

import java.util.HashMap;

import simulator.entities.*;
import utility.Keywords;

public class Dijkstra extends Router {

	private HashMap<Integer, Float> distance; // <SwitchID, PropagationDelay>
	private HashMap<Integer, SDNSwitch> nodes;
	private HashMap<Integer, Integer> previous; // <SwitchID, SwitchID>
	private HashMap<Integer, Link> result; // <SwitchID, Link>
	private HashMap<Integer, Integer> unvisited; // <SwitchID, sameSwitchID>

	/* Constructor */
	public Dijkstra(HashMap<Integer, SDNSwitch> nodes) {
		this.nodes = nodes;
		distance = new HashMap<Integer, Float>();
		previous = new HashMap<Integer, Integer>();
		unvisited = new HashMap<Integer, Integer>();
		result = new HashMap<Integer, Link>();

	}

	/** Called in Class::Dijkstra.run() **/
	/* Objective::Returning the routing table result<Node,Link> for Node src */
	private SDNSwitch generateResult(int srcID, int dstID) {
		int neighborID = previous.get(dstID);
		this.result.put(neighborID, nodes.get(neighborID).networkLinks.get(dstID));
		// Check if the neighbor is connected to the src
		if (neighborID == srcID) {
			return null;
		} else {
			return generateResult(srcID, neighborID);
		}
	}

	/* Objective::Finding the optimal paths for each Node */
	public HashMap<Integer, Link> run(int srcID, int targetID) {
		distance = new HashMap<Integer, Float>();
		previous = new HashMap<Integer, Integer>();
		unvisited = new HashMap<Integer, Integer>();

		/* Initialization */
		for (SDNSwitch currentNode : nodes.values()) {
			distance.put(currentNode.getID(), Float.MAX_VALUE); // Unknown distance from source to v
			previous.put(currentNode.getID(), srcID); // Previous node in optimal path from source
			unvisited.put(currentNode.getID(), currentNode.getID()); // All nodes initially in Q (unvisited nodes)
		}

		distance.put(srcID, (float) 0); // Distance from source to source

		while (!unvisited.isEmpty()) {
			int minNodeID = -1;

			float minValue = Float.MAX_VALUE;
			for (int nodeID : unvisited.keySet()) { // Node with the least distance
				if (distance.get(nodeID) < minValue) { // will be selected first
					minValue = distance.get(nodeID);
					minNodeID = nodeID;
				}
			}
			unvisited.remove(minNodeID);

			for (int n : nodes.get(minNodeID).networkLinks.keySet()) { // For each neighbor n of minNode
				float alt = distance.get(minNodeID)
						+ nodes.get(minNodeID).networkLinks.get(n).getTotalDelay(Keywords.Segments.Sizes.DataSegSize);
				if (alt < distance.get(n)) {
					distance.put(n, alt);
					previous.put(n, minNodeID);

				}
			}
		}

		this.result = new HashMap<Integer, Link>();
		generateResult(srcID, targetID);
		return this.result;
	}
}