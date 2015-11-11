package maggie.common.algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 
 * 
 * </pre>
 * 
 * @author luoxian
 * @since Sep 5, 2008 4:38:09 PM
 * @version 1.0
 */
public class UnionAlgorithm {
	/**
	 */
	private static List value = new ArrayList();

	/**
	 * @param duan
	 * @return
	 */
	public static Interval[] check(Interval[] temp) {
		Interval[] interval = new Interval[temp.length];
		for (int i = 0; i < interval.length; i++) {
			interval[i] = new Interval(temp[i]);
		}
		// :-
		int length = interval.length;
		for (int i = 0; i < interval.length; i++) {
			if (interval[i].getMax() < interval[i].getMin()) {
				interval[i] = null;
				length--;
			}
		}
		Interval[] result = new Interval[length];
		int index = 0;
		for (Interval element : interval) {
			if (element != null) {
				result[index] = element;
				index++;
			}
		}
		return result;
	}

	/**
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * @param value
	 * @param from
	 * @param point
	 * @return
	 */
	public static int[] getPosition(List value, int from, int point) {
		if (from >= value.size()) {
			return new int[] { 0, value.size() };
		}

		if (point < ((Interval) value.get(from)).getMin()) {
			return new int[] { 0, from };
		}

		for (int i = from; i < value.size() - 1; i++) {
			Interval tmp = (Interval) value.get(i);
			if (tmp.isContain(point)) {
				return new int[] { 1, i };
			}
			Interval tmpLater = (Interval) value.get(i + 1);
			if (point > tmp.getMax() && point < tmpLater.getMin()) {
				return new int[] { 0, i + 1 };
			}
		}

		Interval last = (Interval) value.get(value.size() - 1);
		if (last.isContain(point)) {
			return new int[] { 1, value.size() - 1 };
		} else {
			return new int[] { 0, value.size() };
		}
	}

	public static List intersect(Interval[] intarray, Interval interval) {
		value.clear();
		if (intarray == null || intarray.length == 0) {
			return value;
		}
		// get union of interval array
		Interval[] result =
				((List<Interval>) union(intarray)).toArray(new Interval[value.size()]);
		// get intersection
		for (int i = 0; i < result.length; i++) {
			result[i].setMin(Math.max(result[i].getMin(), interval.getMin()));
			result[i].setMax(Math.min(result[i].getMax(), interval.getMax()));
			if (result[i].getMin() >= result[i].getMax()) {
				result[i] = null;
			}
		}
		// validate result
		int size = result.length;
		for (Interval element : result) {
			if (element == null) {
				size--;
			}
		}
		value.clear();
		for (int i = 0, j = 0; i < result.length; i++) {
			if (result[i] != null) {
				value.add(result[i]);
			}
		}
		// return validate(sets);
		return value;
	}

	public static void main(String[] args) {
		Interval[] v = { new Interval(1, 3), new Interval(4, 2) };

		UnionAlgorithm ua = new UnionAlgorithm();
		Interval d1 = new Interval(7, 9);
		Interval d2 = new Interval(5, 7);
		Interval d3 = new Interval(-1, 4);
		Interval d4 = new Interval(8, 10);
		Interval d5 = new Interval(10, 12);
		Interval d6 = new Interval(4, 1);
		Interval d7 = new Interval(8, 10);
		Interval d8 = new Interval(3, 14);
		Interval d9 = new Interval(15, 17);
		Interval c1 = new Interval(0, 4545);
		Interval c2 = new Interval(32, 54);
		Interval c3 = new Interval(123, 456);
		Interval c4 = new Interval(34, 54);
		Interval c5 = new Interval(12, 23);

		// Interval[] duan = {d1,d2,d3};
		// Interval[] duan = {d1,d2,d3,d4,d5,d6,d7,d8,d9,c1,c2,d1, d2, d3, d4,
		// d5, d6, d7, d8, d9, c1, c2, c3,c3,c4,c5};
		Interval[] duan = { d1, d2, d3, d4, d5, d6, d7, d8, d9 };
		long sc = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {

			// ua.
			union(duan);
		}
		// Thread.sleep(1);
		sc = System.currentTimeMillis() - sc;

		System.out.println(ua.getValue() + "\ncost:" + sc + "ms");

	}

	public static void refresh(int index) {
		Interval temp = (Interval) value.get(index);
		int[] index_right_position = getPosition(value, index + 1, temp.getMax());
		if (index_right_position[0] == 1) {
			Interval include_right = (Interval) value.get(index_right_position[1]);
			temp.setMax(include_right.getMax());

			for (int i = index + 1; i <= index_right_position[1]; i++) {
				value.remove(i);
			}
		} else if (index_right_position[0] == 0) {
			for (int i = index + 1; i < index_right_position[1]; i++) {
				value.remove(i);
			}
		}
	}

	/**
	 * 
	 * @param param
	 * @return
	 */
	public static List union(Interval[] param) {
		Interval[] interval = check(param);
		if (interval == null || interval.length == 0) {
			return null;
		}
		if (value.size() == 0 && interval.length >= 1) {
			value.add(interval[0]);
		}
		for (int i = 1; i < interval.length; i++) {
			int[] left_position = getPosition(value, 0, interval[i].getMin());
			if (left_position[0] == 1) {
				int[] right_position =
						getPosition(value, left_position[1], interval[i].getMax());
				if (right_position[0] == 1) {
					if (left_position[1] == right_position[1]) {
						continue;
					}

					((Interval) value.get(left_position[1])).setMax(((Interval) value
							.get(right_position[1])).getMax());

					for (int j = left_position[1] + 1; j <= right_position[1]; j++) {
						value.remove(j);
					}
				} else {
					((Interval) value.get(left_position[1])).setMax(interval[i].getMax());
					for (int j = left_position[1] + 1; j < right_position[1]; j++) {
						value.remove(j);
					}
				}
			} else {
				value.add(left_position[1], interval[i]);
				refresh(left_position[1]);

			}
		}

		return value;
	}

	public List getValue() {
		return value;
	}
}
