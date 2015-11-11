package maggie.network.parser;

import maggie.network.entity.Edge;
import maggie.network.entity.Network;
import maggie.network.entity.Node;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;

public class MaggieNetworkToJungGraphParser {

	public static Graph MaggieNetworkToJungGraph(Network network) {
		Graph graph = new DirectedSparseGraph<>();
		for (Node node : network.getNodeList()) {
			graph.addVertex(node);
		}
		for (Edge edge : network.getEdgeList()) {
			graph.addEdge(edge, edge.getFrom(), edge.getTo());
		}
		return graph;
	}

}
