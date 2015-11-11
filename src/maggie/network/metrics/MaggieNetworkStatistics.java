package maggie.network.metrics;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import maggie.common.utils.ReportWriter;
import maggie.network.entity.Edge;
import maggie.network.entity.Network;
import maggie.network.entity.Node;

/**
 * Calculates the properties of Maggie.network. Features include:
 * <ul>
 * <li>translate Maggie network to adjacency matrix</li>
 * <li>number of nodes</li>
 * <li>number of edges</li>
 * <li>mean degree</li>
 * <li>mean shortest distance and network diameter</li>
 * <li>clustering coefficient</li>
 * <li>degree distribution</li>
 * <li>edge weight distribution</li>
 * <li>node strength distribution</li>
 * <li>sum of edge weight in the network</li>
 * <li>sum of edge weight (above certain threshold) in the network</li>
 * <li>correlation (edge weight in stock network) distribution</li>
 * <li>sum of edge weight of each index (node strengths in stock network)</li>
 * <li>Calls mfinder1.2 to calculate motifs.</li>
 * <li>output all edges</li>
 * <li>output degree and strength of each node and weight of each edge</li>
 * 
 * @author LIU Xiaofan
 * 
 */
public abstract class MaggieNetworkStatistics extends ReportWriter {

	private int[][] adjacencyMatrix;
	protected int[][] adjacencyMatrixWeighted;

	protected Network network;

	protected float networkWeightSum = 0.0f;

	public MaggieNetworkStatistics(Network network) {
		super();
		this.network = network;
		setPrintWriter(System.out);
	}

	public MaggieNetworkStatistics(Network network, File file) {
		super();
		this.network = network;
		setPrintWriter(file);
	}

	protected void formConnectivityTable(List<? extends Edge> edgeList,
			List<? extends Node> nodeList) {

		adjacencyMatrix = new int[nodeList.size()][nodeList.size()];
		adjacencyMatrixWeighted = new int[nodeList.size()][nodeList.size()];

		for (int i = 0; i < adjacencyMatrix.length; i++) {
			for (int j = 0; j < adjacencyMatrix[i].length; j++) {
				if (i == j) {
					adjacencyMatrix[i][j] = 0;
				} else {
					adjacencyMatrix[i][j] = 512;
				}
			}
		}

		for (Edge edge : edgeList) {
			adjacencyMatrix[nodeList.indexOf(edge.getPair().getFirst())][nodeList.indexOf(edge
					.getPair().getSecond())] = 1;
			adjacencyMatrix[nodeList.indexOf(edge.getPair().getSecond())][nodeList.indexOf(edge
					.getPair().getFirst())] = 1;
			adjacencyMatrixWeighted[(Integer) edge.getPair().getFirst().getID() - 1][(Integer) edge
					.getPair().getSecond().getID() - 1] += 1;
		}
	}

	public float[] getClusteringCoefficient(List<? extends Node> lst) {

		int n = lst.size();
		int[][] tt = new int[n][2];

		// iterate graph table to find triangles
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (adjacencyMatrix[i][j] == 1) {
					for (int k = 0; k < n; k++) {
						if (adjacencyMatrix[j][k] == 1) {
							tt[j][1]++;
							if (adjacencyMatrix[k][i] == 1) {
								tt[j][0]++;
							}
						}
					}
				}
			}
		}

		float c1 = 0, c2 = 0, t1 = 0, t2 = 0;
		for (int[] element : tt) {
			int triangle = element[0];
			int triple = element[1];
			if (triple != 0) {
				c1 += (float) triangle / triple;
			}
			t1 += triangle;
			t2 += triple;
		}
		c1 /= tt.length;
		c2 = t1 / t2;
		return new float[] { c1, c2 };
	}

	public int[] getCorrelationDistribution() {
		int bins = 21;

		int[] weightDist = new int[bins];

		for (int i = 0; i < network.getAdjMatrix().length; i++) {
			for (int j = 0; j < network.getAdjMatrix()[i].length; j++) {
				weightDist[(int) ((network.getAdjMatrix()[i][j] + 1) / 0.1f)] += 1;
			}
		}

		for (int i : weightDist) {
			i = i / 2;
		}

		return weightDist;
	}

	public int[] getDegreeDistribution() {
		// find largest degree
		int largestDe = 0;
		for (Node s : network.getNodeList()) {
			if (s.getDegree() > largestDe) {
				largestDe = s.getDegree();
			}
		}
		// initialize distribution vector
		int[] dist = new int[largestDe + 1];
		// count frequency
		for (Node s : network.getNodeList()) {
			dist[s.getDegree()] += 1;
		}
		return dist;

	}

	public int[] getEdgeWeightDistribution(List<? extends Edge> list) {
		// find maximum edge weight
		float maxWeight = 0;
		for (Edge e : list) {
			if (e.getWeight() > maxWeight) {
				maxWeight = e.getWeight();
			}
		}
		// initialize distribution vector
		int[] weightDist = new int[(int) maxWeight];
		// count frequency
		for (Edge e : list) {
			weightDist[(int) (e.getWeight() - 1)] += 1;
		}
		return weightDist;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public float[] getIndexWeightSum() {
		float[] ws = new float[network.getNodeList().size()];
		for (int i = 0; i < network.getNodeList().size(); i++) {
			for (int j = 0; j < network.getNodeList().size(); j++) {
				ws[i] += network.getAdjMatrix()[i][j];
			}
		}
		return ws;
	}

	protected float getMeanDegree() {
		return (float) network.getEdgeList().size() / network.getNodeList().size();
	}

	public float[] getMeanDistanceAndDiameter() {

		int i, j, k;

		// System.out.println("start Floyd's Algorithm");
		/* Run Floyd's Algorithm */
		for (i = 0; i < adjacencyMatrix.length; i++) {
			for (j = 0; j < adjacencyMatrix.length; j++) {
				int[] subTable = adjacencyMatrix[i];
				if (i != j) // skip over the current row
				{
					for (k = 0; k < subTable.length; k++) {
						if (k != i) // skip over the current column of
						// iteration
						{
							adjacencyMatrix[j][k] =
									Math.min(adjacencyMatrix[j][k], adjacencyMatrix[j][i]
											+ adjacencyMatrix[i][k]);
						}
					}
				}
			}
		}

		// count diameter
		int dia = 0;
		float l = 0;
		int count = 0;
		// System.out.println("Floyd's Algorithm finished");
		for (int[] elements : adjacencyMatrix) {
			for (int element : elements) {
				if (element != 0 && element != 512) {
					count++;
					l += element;
					if (element > dia) {
						dia = element;
					}
				}
			}
		}
		return new float[] { l / count, dia };
	}

	public void getMotif(File statFile, String args) {
		// print edge list for mfinder
		printEdgeList(network.getEdgeList());
		closePrintWriter();
		try {
			String line;
			Process p =
					Runtime.getRuntime().exec(
							"mfinder/mfinder1.2.exe " + statFile.getPath() + " " + args);
			BufferedReader input =
					new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				System.out.println(line);
			}
			input.close();
			Desktop.getDesktop().open(new File("mfinder/motif_OUT.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	public float getNetworkWeightSum() {
		networkWeightSum = 0.0f;
		// matrix method
		for (int i = 0; i < network.getNodeList().size(); i++) {
			for (int j = 0; j < network.getNodeList().size(); j++) {
				networkWeightSum += network.getAdjMatrix()[i][j];
			}
		}
		return networkWeightSum / 2.0f;
	}

	/**
	 * @return
	 */
	public float getNetworkWeightSumAboveThreshold() {
		networkWeightSum = 0.0f;
		// edge method
		for (Edge myEdge : network.getEdgeList()) {
			networkWeightSum += myEdge.getWeight();
		}
		return networkWeightSum;
	}

	public int[] getNodeStrengthDistribution() {
		// find largest node strength
		int largestDe = 0;
		for (Node noteInfo : network.getNodeList()) {
			if (noteInfo.getStrength() > largestDe) {
				largestDe = (int) noteInfo.getStrength();
			}
		}
		// initialize distribution vector
		int[] dist = new int[largestDe + 1];
		// count frequency
		for (Node noteInfo : network.getNodeList()) {
			dist[(int) noteInfo.getStrength()] += 1;
		}
		return dist;
	}

	protected int getNetworkM() {
		return network.getEdgeList().size();
	}

	protected int getNetworkN() {
		return network.getNodeList().size();
	}

	/**
	 * 
	 */
	protected void initialize() {
		// form graph table
		formConnectivityTable(network.getEdgeList(), network.getNodeList());
	}

	/**
	 * In the form of From \t To \t Weight
	 * @param list
	 */
	protected void printEdgeList(List<? extends Edge> list) {
		for (Edge myEdge : list) {
			println(network.getNodeList().indexOf(myEdge.getPair().getFirst()) + "\t"
					+ network.getNodeList().indexOf(myEdge.getPair().getSecond()) + "\t"
					+ myEdge.getWeight());
		}
	}

	private void printRawData(List<? extends Edge> list) {
		println("% degree/weight/strength series:");

		// degree
		print("degree=[");
		for (Node s : network.getNodeList()) {
			if (s.getDegree() != 0) {
				print(s.getID() + ":" + s.getDegree() + " ");
			}
		}
		println("];");

		// edge
		print("edgeweight=[");
		for (Edge myEdge : list) {
			print(myEdge.getWeight() + " ");
		}
		println("];");

		// strength
		print("strength=[");
		for (Node s : network.getNodeList()) {
			if (s.getStrength() != 0) {
				print(s.getStrength() + " ");
			}
		}
		println("];");
		println();
	}

	/**
	 * A statistical report includes:
	 * 
	 * <ul>
	 * <li>Stock List</li>
	 * <li>Network Size</li>
	 * <li>Edge Distribution</li>
	 * <li>Clustering Coefficient</li>
	 * </ul>
	 * 
	 */
	public void printReport() {
		// TODO print node list
		// println("network contains nodes:");
		// for (Node s : network.getNodeList()) {
		// print(s.getName() + "\t");
		// }
		// println();
		initialize();

		// n sum(k) mean(k)
		println("total number of network nodes = " + getNetworkN());
		println("total number of edges = " + getNetworkM());
		println("mean co-occurrent degree = " + getMeanDegree());
		println();

		// mean distance and diameter
		float[] mdd = getMeanDistanceAndDiameter();
		println("mean shortest distance: " + mdd[0]);
		println("network diameter: " + mdd[1]);
		println();

		// count clustering coefficient
		float[] c = getClusteringCoefficient(network.getNodeList());
		println("Clustering Coefficient 1 = " + c[0]);
		println("Clustering Coefficient 2 = " + c[1]);
		println();

		// print degree list
		println("% Degree distribution (starting from dgr=0):");
		print("netdegree=[");
		for (int i : getDegreeDistribution()) {
			print(i + " ");
		}
		println("];");
		println();

		// get edge weight distribution
		println("% Edge weight distribution (starting from edgw=1): ");
		print("edge=[");
		for (int i : getEdgeWeightDistribution(network.getEdgeList())) {
			print(i + " ");
		}
		println("];");
		println();

		// // calculate assortativity
		// float assort1 = calcAssort(coEdgeList);
		// // print assortativity
		// println(
		// "Assortativity Coefficient of Index Degree: " + assort1);
		// println();

		// print music note with in and out degrees
		printRawData(network.getEdgeList());
		println();

		// print adjacency matrix
		// println("adjcancy matrix");
		// for (int i = 0; i < network.getAdjMatrix().length; i++) {
		// for (int j = 0; j < network.getAdjMatrix()[i].length; j++) {
		// print(network.getAdjMatrix()[i][j] + " ");
		// }
		// println();
		// }
		// println();

		// print connectivity
		// println("connectivity");
		// println("from\tto");
		// for (Edge e : network.getEdgeList()) {
		// println(e.getPair().getFirst().getId()+"\t"+e.getPair().getSecond().getId());
		// }
		// println();

	}

}