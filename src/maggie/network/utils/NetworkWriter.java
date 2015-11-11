package maggie.network.utils;

import java.io.File;
import java.io.IOException;

import maggie.common.utils.MaggieWriter;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import edu.uci.ics.jung.algorithms.matrix.GraphMatrixOperations;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;

public class NetworkWriter {

	private MaggieWriter writer;
	private Graph network;
	private String net;
	private String usr;

	public NetworkWriter(Graph network, String net, String usr) {
		this.network = network;
		this.net = net;
		this.usr = usr;
	}

	public static void writeNetwork(Graph network, String net, String usr) {
		NetworkWriter writer = new NetworkWriter(network, net, usr);
		try {
			writer.write();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void write() throws IOException {
		// print users
		writer = new MaggieWriter(new File("./" + usr + ".usr"));
		for (Object user : network.getVertices()) {
			writer.writeln(user + "");
		}
		writer.close();

		// print edges
		writer = new MaggieWriter(new File("./" + net + ".net"));
		for (Object edge : network.getEdges()) {
			Pair nodes = network.getEndpoints(edge);
			writer.writeln(nodes.getFirst() + "\t" + nodes.getSecond());
		}
		writer.close();
	}

	public static void write2DMatrix(Graph network, String filename) {
		SparseDoubleMatrix2D mtrx = GraphMatrixOperations.graphToSparseMatrix(network);
		try {
			MaggieWriter writer = new MaggieWriter(new File(filename));
			for (int i = 0; i < mtrx.rows(); i++) {
				for (int j = 0; j < mtrx.columns(); j++) {
					writer.write((int) mtrx.get(i, j) + "\t");
				}
				writer.writeln();
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
