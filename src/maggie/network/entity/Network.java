package maggie.network.entity;

import java.util.List;

public abstract class Network {

	protected String name;
	protected float[][] adjMatrix;
	protected List<? extends Edge> edgeList;
	protected List<? extends Node> nodeList;

	public float[][] getAdjMatrix() {
		return adjMatrix;
	}

	public abstract List<? extends Edge> getEdgeList();

	public String getName() {
		return name;
	}

	public abstract List<? extends Node> getNodeList();

	public void setAdjMatrix(float[][] adjMatrix) {
		this.adjMatrix = adjMatrix;
	}

	public void setEdgeList(List<? extends Edge> edgeList) {
		this.edgeList = edgeList;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNodeList(List<? extends Node> nodeList) {
		this.nodeList = nodeList;
	}

}