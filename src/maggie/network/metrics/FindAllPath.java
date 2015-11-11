package maggie.network.metrics;

import java.util.HashSet;
import java.util.LinkedList;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseGraph;

public class FindAllPath<V, E> {

	public static void main(String[] args) {
		DirectedGraph<String, String> graph = new DirectedSparseGraph<String, String>();
		graph.addVertex("A");
		graph.addVertex("B");
		graph.addVertex("C");
		graph.addVertex("D");
		graph.addEdge("1", "A", "B");
		graph.addEdge("2", "A", "C");
		graph.addEdge("3", "A", "D");
		graph.addEdge("4", "B", "C");
		graph.addEdge("5", "B", "D");
		graph.addEdge("6", "C", "B");
		graph.addEdge("7", "C", "D");
		graph.addEdge("8", "D", "B");
		graph.addEdge("9", "D", "C");
		findAllPaths(graph, "A", true);
	}

	public static <V, E> HashSet<LinkedList<V>> findAllPaths(DirectedGraph<V, E> graph, V root,
			boolean print) {
		HashSet<LinkedList<V>> allPaths = new HashSet<LinkedList<V>>();
		HashSet<LinkedList<V>> currentPaths;
		HashSet<LinkedList<V>> newPaths = new HashSet<LinkedList<V>>();
		// new paths add single root
		LinkedList<V> newPath = new LinkedList<V>();
		newPath.add(root);
		newPaths.add(newPath);
		while (!newPaths.isEmpty()) {
			// swap current and new paths
			currentPaths = newPaths;
			newPaths = new HashSet<LinkedList<V>>();
			// for each current path
			for (LinkedList<V> currPath : currentPaths) {
				// find all children
				HashSet<V> allChildren = new HashSet<V>();
				HashSet<V> realChildren = new HashSet<V>();
				allChildren.addAll(graph.getSuccessors(currPath.getLast()));
				// filter children, not appeared before in the path
				for (V child : allChildren) {
					if (!currPath.contains(child))
						realChildren.add(child);
				}
				// if no children, add current path to all paths
				if (realChildren.isEmpty())
					allPaths.add(currPath);
				// if not
				else {
					// for each child of current path, add new path with current
					// path + child
					for (V child : realChildren) {
						newPath = new LinkedList<V>();
						newPath.addAll(currPath);
						newPath.add(child);
						newPaths.add(newPath);
					}
				}
			}
		}
		if (print) {
			for (LinkedList<V> path : allPaths) {
				for (V v : path) {
					System.out.print(v + " ");
				}
				System.out.println();
			}
		}

		return allPaths;
	}

}
