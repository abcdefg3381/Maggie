package maggie.network.metrics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import maggie.common.utils.MaggieWriter;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.algorithms.metrics.Metrics;
import edu.uci.ics.jung.algorithms.shortestpath.UnweightedShortestPath;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * Graph metrics not implemented in the JUNG2 package. Includes:
 * <ul>
 * <li>output (in-/out-) degree distributions of the network.</li>
 * <li>find number of disconnected components in the network.</li>
 * <li>average clustering coefficient of the network.</li>
 * </ul>
 * 
 * @author GDI
 * 
 */
public class JungGraphStatistics {

	public static float[] printAssortativity(Graph network) {
		System.out.println("calculate assortativity of the network");
		float[] assort = new float[4];
		// for undirected network, see Newman, M. E. J. (2002).
		// "Assortative mixing in networks." Physical Review Letters 89(20):
		// 208701.
		if (network instanceof UndirectedGraph) {
			int j, k, m = network.getEdgeCount();
			float jk = 0, jak = 0, j2ak2 = 0;
			for (Object e : network.getEdges()) {
				Pair pair = network.getEndpoints(e);
				j = network.degree(pair.getFirst());
				k = network.degree(pair.getSecond());
				jk += j * k;
				jak += (j + k);
				j2ak2 += (j * j + k * k);
			}
			assort[0] =
					(jk / m - jak / 2 / m * jak / 2 / m)
							/ (j2ak2 / 2 / m - jak / 2 / m * jak / 2 / m);
		}
		// for directed network, see Foster, J. G., D. V. Foster, et al. (2010).
		// "Edge direction and the structure of networks." Proceedings of the
		// National Academy of Sciences 107(24): 10815-10820. and
		// TODO: http://en.wikipedia.org/wiki/Assortativity
		else if (network instanceof DirectedGraph) {
			int jIn, jOut, kIn, kOut, m = network.getEdgeCount();
			float jInMean = 0, jOutMean = 0, kInMean = 0, kOutMean = 0;
			// prepare jAlphaMean and kBetaMean
			for (Object e : network.getEdges()) {
				Pair pair = network.getEndpoints(e);
				jIn = network.inDegree(pair.getFirst());
				jOut = network.outDegree(pair.getFirst());
				kIn = network.inDegree(pair.getSecond());
				kOut = network.outDegree(pair.getSecond());
				// j*k
				jInMean += jIn;
				jOutMean += jOut;
				kInMean += kIn;
				kOutMean += kOut;
			}
			jInMean /= network.getEdgeCount();
			jOutMean /= network.getEdgeCount();
			kInMean /= network.getEdgeCount();
			kOutMean /= network.getEdgeCount();
			// let (jAlphaI-jAlphaMean)=xAlpha,(kBetaI-kBetaMean)=yBeta,
			float xIn, xOut, yIn, yOut, sumXInSq = 0, sumXOutSq = 0, sumYInSq = 0, sumYOutSq =
					0, sumXInYIn = 0, sumXOutYIn = 0, sumXInYOut = 0, sumXOutYOut = 0;
			for (Object e : network.getEdges()) {
				Pair pair = network.getEndpoints(e);
				jIn = network.inDegree(pair.getFirst());
				jOut = network.outDegree(pair.getFirst());
				kIn = network.inDegree(pair.getSecond());
				kOut = network.outDegree(pair.getSecond());
				xIn = jIn - jInMean;
				xOut = jOut - jOutMean;
				yIn = kIn - kInMean;
				yOut = kOut - kOutMean;
				sumXInSq += xIn * xIn;
				sumXOutSq += xOut * xOut;
				sumYInSq += yIn * yIn;
				sumYOutSq += yOut * yOut;
				sumXInYIn += xIn * yIn;
				sumXOutYIn += xOut * yIn;
				sumXInYOut += xIn * yOut;
				sumXOutYOut += xOut * yOut;
			}
			// in-in assortativity
			assort[0] = (float) (sumXInYIn / (Math.sqrt(sumXInSq) * Math.sqrt(sumYInSq)));
			// in-out assortativity
			assort[1] = (float) (sumXInYOut / (Math.sqrt(sumXInSq) * Math.sqrt(sumYOutSq)));
			// out-in assortativity
			assort[2] = (float) (sumXOutYIn / (Math.sqrt(sumXOutSq) * Math.sqrt(sumYInSq)));
			// out-out assortativity
			assort[3] = (float) (sumXOutYOut / (Math.sqrt(sumXOutSq) * Math.sqrt(sumYOutSq)));
		}

		// print out results
		System.out.println("assortativity: " + assort[0] + "\t" + assort[1] + "\t" + assort[2]
				+ "\t" + assort[3] + "\t");

		return assort;
	}

	public static void printClusteringCoefficient(Graph network) {
		double cc = 0;
		Map ccs = Metrics.clusteringCoefficients(network);
		for (Object vertex : ccs.keySet()) {
			cc += (Double) ccs.get(vertex);
		}
		cc /= ccs.size();
		System.out.println("clustering coefficient:" + cc);
	}

	/**
	 * Find the number of disconnected components in the network.
	 * 
	 * @param network
	 */
	public static void findClusters(Graph network) {
		WeakComponentClusterer finder = new WeakComponentClusterer();
		Set<Set> clusters = finder.transform(network);
		System.out.println(clusters.size() + " cluster found.");
		for (Set set : clusters) {
			System.out.println(set.size());
		}
	}

	/**
	 * Calls the
	 * {@link #printDegreeDistribution(Graph, String, String, String, String)
	 * printDegreeDistribution(Graph network, String comment, String
	 * line_prefix, String line_prefix2, String line_suffix)} method with no
	 * line prefix and suffix.
	 * 
	 * @param network
	 *            a Jung Graph
	 */
	public static void printDegreeDistribution(Graph network) {
		printDegreeDistribution(network, "", "", "", "");
	}

	/**
	 * Calls the
	 * {@link #printDegreeDistribution(Graph, String, String, String, String)
	 * printDegreeDistribution(Graph network, String comment, String
	 * line_prefix, String line_prefix2, String line_suffix)} method with Matlab
	 * line prefixes and suffix.
	 * 
	 * @param network
	 *            a Jung Graph
	 */
	public static void printDegreeDistributionToMatlab(Graph network) {
		printDegreeDistribution(network, "% ", "k=[", "pk=[", "];");
	}

	/**
	 * Count the degree frequency in a network, in the form of degree k[] and
	 * frequency pk[]. Automatically determines the direction of the edges. If
	 * undirected network, output degree distribution. If directed network,
	 * output in- and out- degree distributions.
	 * 
	 * @param network
	 *            a Jung Graph
	 * @param comment
	 *            prefix to non data lines
	 * @param line_prefix
	 *            prefix to k[] lines
	 * @param line_prefix2
	 *            prefix to pk[] lines
	 * @param line_suffix
	 *            suffix to k[] and pk[] lines
	 */
	public static void printDegreeDistribution(Graph network, String comment,
			String line_prefix, String line_prefix2, String line_suffix) {
		System.out.println(comment + "network size");
		System.out.println(comment + "number of nodes: " + network.getVertexCount());
		System.out.println(comment + "number of edges: " + network.getEdgeCount());

		if (network instanceof UndirectedGraph) {
			HashMap<Integer, Integer> degreeDistribution = new HashMap<Integer, Integer>();
			for (Object vertex : network.getVertices()) {
				int degree = network.degree(vertex);
				if (degreeDistribution.containsKey(degree))
					degreeDistribution.put(degree, (degreeDistribution.get(degree) + 1));
				else
					degreeDistribution.put(degree, 1);
			}
			List<Integer> sortedKeys = new ArrayList<Integer>(degreeDistribution.keySet());
			Collections.sort(sortedKeys);
			System.out.println(comment + "degree distribution: ");
			System.out.print(line_prefix);
			for (Integer degree : sortedKeys) {
				System.out.print(degree + "\t");
			}
			System.out.println(line_suffix);
			System.out.print(line_prefix2);
			for (Integer degree : sortedKeys) {
				System.out.print(degreeDistribution.get(degree) + "\t");
			}
			System.out.println(line_suffix);
		} else if (network instanceof DirectedGraph) {
			HashMap<Integer, Integer> inDegreeDistribution = new HashMap<Integer, Integer>();
			HashMap<Integer, Integer> outDegreeDistribution = new HashMap<Integer, Integer>();
			int inDegree, outDegree;
			for (Object vertex : network.getVertices()) {
				inDegree = network.inDegree(vertex);
				outDegree = network.outDegree(vertex);
				if (inDegreeDistribution.containsKey(inDegree))
					inDegreeDistribution
							.put(inDegree, (inDegreeDistribution.get(inDegree) + 1));
				else
					inDegreeDistribution.put(inDegree, 1);
				if (outDegreeDistribution.containsKey(outDegree))
					outDegreeDistribution.put(outDegree,
							(outDegreeDistribution.get(outDegree) + 1));
				else
					outDegreeDistribution.put(outDegree, 1);
			}
			System.out.println(comment + "indegree distribution:");
			System.out.print(line_prefix);
			for (Integer degree : inDegreeDistribution.keySet()) {
				System.out.print(degree + "\t");
			}
			System.out.println(line_suffix);
			System.out.print(line_prefix2);
			for (Integer count : inDegreeDistribution.values()) {
				System.out.print(count + "\t");
			}
			System.out.println(line_suffix);
			System.out.println(comment + "outdegree distribution:");
			System.out.print(line_prefix);
			for (Integer degree : outDegreeDistribution.keySet()) {
				System.out.print(degree + "\t");
			}
			System.out.println(line_suffix);
			System.out.print(line_prefix2);
			for (Integer count : outDegreeDistribution.values()) {
				System.out.print(count + "\t");
			}
			System.out.println(line_suffix);
		}
	}

	public static double printApproximateMeanDistance(Graph network) {
		System.out.println("calculate approximate mean distance...");
		double distance = 0;
		UnweightedShortestPath dd = new UnweightedShortestPath(network);
		for (int i = 0; i < 200;) {
			Object[] vertices = network.getVertices().toArray();
			Object v1 = vertices[(int) (Math.random() * vertices.length)];
			Object v2 = vertices[(int) (Math.random() * vertices.length)];
			if (v1.equals(v2))
				continue;

			else {
				distance += dd.getDistance(v1, v2).doubleValue();
			}
			i++;
		}
		distance /= 200;
		System.out.println("approximate mean distance: " + distance);
		return distance;
	}

	public static void printDegree(Graph network) throws IOException {
		if (network instanceof UndirectedGraph) {
			MaggieWriter writer = new MaggieWriter(new File("degrees"));
			for (Object vertex : network.getVertices()) {
				writer.write(network.degree(vertex) + ",");
			}
			writer.close();
		} else if (network instanceof DirectedGraph) {
			MaggieWriter writer = new MaggieWriter(new File("degrees"));
			MaggieWriter writerIn = new MaggieWriter(new File("indegrees"));
			MaggieWriter writerOut = new MaggieWriter(new File("outdegrees"));
			for (Object vertex : network.getVertices()) {
				writer.write(network.degree(vertex) + ",");
				writerIn.write(network.inDegree(vertex) + ",");
				writerOut.write(network.outDegree(vertex) + ",");
			}
			writer.close();
			writerIn.close();
			writerOut.close();
		}
	}
}
