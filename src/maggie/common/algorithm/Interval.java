package maggie.common.algorithm;

/**
 * [min, max]
 * 
 * @author luoxian
 * @since Sep 5, 2008 4:48:15 PM
 * @version 1.0
 */
public class Interval {
	private int max;
	private int min;

	public Interval(int min, int max) {
		this.min = min;
		this.max = max;
	}

	public Interval(Interval interval) {
		this.min = interval.getMin();
		this.max = interval.getMax();
	}

	public int getLength() {
		return max - min;
	}

	public int getMax() {
		return max;
	}

	public int getMin() {
		return min;
	}

	public boolean isContain(int point) {
		if (point <= max && point >= min) {
			return true;
		} else {
			return false;
		}
	}

	public void setMax(int right) {
		this.max = right;
	}

	public void setMin(int left) {
		this.min = left;
	}

	@Override
	public String toString() {
		return "[" + min + " , " + max + "]";
	}
}
