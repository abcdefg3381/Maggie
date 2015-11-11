package maggie.network.parser;

import java.io.File;
import java.io.IOException;

import maggie.common.utils.MaggieReader;
import maggie.common.utils.MaggieWriter;

public class AdjacencyMatrix2EdgeList {
	public static void main(String[] args) throws IOException {
		// write input and output files here
		File inputFile = new File("E:\\workspace\\java\\MusicBox\\clarinet\\stats\\adj.txt");
		File outputFile = new File("edge.csv");
		// read file
		MaggieReader reader = new MaggieReader(inputFile);
		String line;
		int size = 222;
		int[][] adjMtx = new int[222][222];
		int row = 0;
		while ((line = reader.readLine()) != null) {
			String[] sgmt = line.split(",");
			int column = 0;
			for (String string : sgmt) {
				adjMtx[row][column] = Integer.parseInt(string);
				column++;
			}
			row++;
		}
		// write file
		MaggieWriter writer = new MaggieWriter(outputFile);
		for (int i = 0; i < adjMtx.length; i++) {
			for (int j = 0; j < adjMtx[i].length; j++) {
				if (adjMtx[i][j] == 1)
					writer.write(i + "," + j + "\n");
			}
		}
		writer.close();
	}
}
