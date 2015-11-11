package maggie.network.entity;

import edu.uci.ics.jung.graph.util.Pair;

public abstract class Edge {

	protected Pair<? extends Node> pair;
	protected float weight;

	public void addWeight() {
		weight++;
	}

	public Node getFrom() {
		return pair.getFirst();
	}

	public abstract Pair<? extends Node> getPair();

	public Node getTo() {
		return pair.getSecond();
	}

	public abstract boolean getType();

	public float getWeight() {
		return weight;
	}

	public void setPair(Pair<Node> pair) {
		this.pair = pair;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public void subWeight() {
		weight -= 1;
	}

}
