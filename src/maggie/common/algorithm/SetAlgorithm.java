package maggie.common.algorithm;

import java.util.Arrays;
import java.util.Comparator;

/**
 * This class is able to calculate union and intersection of integer intervals.
 * 
 * @author LIU Xiaofan
 * 
 */
public class SetAlgorithm {

	private static Interval intersect(Interval i1, Interval i2) {
		if (i1 == null || i2 == null) {
			return null;
		} else {
			int min, max;
			min = Math.max(i1.getMin(), i2.getMin());
			max = Math.min(i1.getMax(), i2.getMax());
			if (min < max) {
				return new Interval(min, max);
			} else {
				return null;
			}
		}
	}

	/**
	 * This algorithm intersects the interval with each interval in intarray and
	 * merge them together.
	 * 
	 * @param intarray
	 * @param interval
	 * @return the intersection Interval[] of input parameters.
	 */
	public static Interval[] intersect(Interval[] intarray, Interval interval) {
		if (intarray == null || intarray.length == 0) {
			return new Interval[0];
		}
		// 1. get union of interval array
		Interval[] result = union(intarray);
		// 2. get intersection
		for (int i = 0; i < result.length; i++) {
			result[i].setMin(Math.max(result[i].getMin(), interval.getMin()));
			result[i].setMax(Math.min(result[i].getMax(), interval.getMax()));
			if (result[i].getMin() >= result[i].getMax()) {
				result[i] = null;
			}
		}
		// 3. validate result
		int size = result.length;
		for (Interval element : result) {
			if (element == null) {
				size--;
			}
		}
		Interval[] finalresult = new Interval[size];
		for (int i = 0, j = 0; i < result.length; i++) {
			if (result[i] != null) {
				finalresult[j] = result[i];
				j++;
			}
		}
		return finalresult;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// SetAlgorithm sa = new SetAlgorithm();
		Interval d1 = new Interval(7, 8);
		Interval d2 = new Interval(5, 7);
		Interval d3 = new Interval(-1, 4);
		Interval d4 = new Interval(8, 12);
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

		// Interval[] inter = {};
		// Interval[] inter = { d1, d2, d5 };
		// Interval[] inter = { d1, d2, d3, d4, d5, d6, d7, d8, d9, c1, c2, c3,
		// c4, c5 };
		Interval[] inter = { d1, d2, d3, d4, d5, d6, d7, d8, d9 };

		long sc = System.currentTimeMillis();

		// for (int i = 0; i < 1000000; i++) {
		// sa.
		union(inter);
		// sa.
		// intersect(inter, d4);
		// }

		sc = System.currentTimeMillis() - sc;

		System.out.println("input:\t" + Arrays.asList(inter));
		System.out.println("union:\t" + Arrays.asList(
		// sa.
				union(inter)));
		System.out.println("intersect:\t" + Arrays.asList(
		// sa.
				intersect(inter, d4)));
		System.out.println("cost:\t\t" + sc + " ms");
	}

	/**
	 * The union algorithm is implemented upon the concept of <br>
	 * A(union)B = A + [B- A(intersect)B]
	 * 
	 * @param origin
	 * @return the union array Interval[] of input parameter.
	 */
	public static Interval[] union(Interval[] origin) {
		// 1. verify original intervals and make a copy
		int size = origin.length;
		Interval[] temp = new Interval[origin.length];
		for (int i = 0; i < origin.length; i++) {
			temp[i] = new Interval(origin[i]);
		}
		for (int i = 0; i < temp.length; i++) {
			if (temp[i].getMin() >= temp[i].getMax()) {
				temp[i] = null;
				size--;
			}
		}
		Interval[] sets = new Interval[size];
		for (int i = 0, j = 0; i < origin.length; i++) {
			if (temp[i] != null) {
				sets[j] = temp[i];
				j++;
			}
		}

		// 2. sort the intervals by their min value
		Arrays.sort(sets, new Comparator<Interval>() {
			@Override
			public int compare(Interval o1, Interval o2) {
				return o1.getMin() < o2.getMin() ? 0 : 1;
			}
		});

		// 3. -(intersect)= each interval with previous ones
		// the bigO of the algorithm is O(n^2)
		Interval tempin = null;
		for (int i = 1; i < sets.length; i++) {
			for (int j = 0; j < i; j++) {
				// logic of -intersect=
				tempin = intersect(sets[i], sets[j]);
				if (tempin != null) {
					sets[i].setMin(tempin.getMax());
				}
				if (sets[i] == null || sets[i].getMin() >= sets[i].getMax()) {
					sets[i] = null;
				}
			}
		}

		// 4. verify result
		size = sets.length;
		for (int i = 1; i < sets.length; i++) {
			if (sets[i] != null && sets[i - 1] != null
					&& sets[i].getMin() == sets[i - 1].getMax()) {
				sets[i].setMin(sets[i - 1].getMin());
				sets[i - 1] = null;
			}
		}
		for (Interval set : sets) {
			if (set == null) {
				size--;
			}
		}
		Interval[] result = new Interval[size];
		for (int i = 0, j = 0; i < sets.length; i++) {
			if (sets[i] != null) {
				result[j] = sets[i];
				j++;
			}
		}
		return result;
	}

	/**
	 * The union algorithm is implemented upon the concept of <br>
	 * A(union)B = A + [B- A(intersect)B]<br>
	 * This is modified version on step 3.
	 * 
	 * @param origin
	 * @return the union array Interval[] of input parameter.
	 */
	public static Interval[] union2(Interval[] origin) {
		// 1. verify original intervals and make a copy
		int size = origin.length;
		Interval[] temp = new Interval[origin.length];
		for (int i = 0; i < origin.length; i++) {
			temp[i] = new Interval(origin[i]);
		}
		for (int i = 0; i < temp.length; i++) {
			if (temp[i].getMin() >= temp[i].getMax()) {
				temp[i] = null;
				size--;
			}
		}
		Interval[] sets = new Interval[size];
		for (int i = 0, j = 0; i < origin.length; i++) {
			if (temp[i] != null) {
				sets[j] = temp[i];
				j++;
			}
		}

		// 2. sort the intervals by their min value
		Arrays.sort(sets, new Comparator<Interval>() {
			@Override
			public int compare(Interval o1, Interval o2) {
				return o1.getMin() < o2.getMin() ? 0 : 1;
			}
		});

		// 3. -(intersect)= each interval with the last non-null one
		// the bigO of the algorithm is O(n)
		Interval tempin = null;
		for (int i = 1; i < sets.length; i++) {
			for (int j = i - 1; j >= 0; j--) {
				// logic of -intersect=
				if (sets[j] != null) {
					tempin = intersect(sets[i], sets[j]);
					if (tempin != null) {
						sets[i].setMin(tempin.getMax());
					}
					if (sets[i] == null || sets[i].getMin() >= sets[i].getMax()) {
						sets[i] = null;
					}
					continue;
				}
			}
		}

		// 4. verify result
		size = sets.length;
		for (int i = 1; i < sets.length; i++) {
			if (sets[i] != null && sets[i - 1] != null
					&& sets[i].getMin() == sets[i - 1].getMax()) {
				sets[i].setMin(sets[i - 1].getMin());
				sets[i - 1] = null;
			}
		}
		for (Interval set : sets) {
			if (set == null) {
				size--;
			}
		}
		Interval[] result = new Interval[size];
		for (int i = 0, j = 0; i < sets.length; i++) {
			if (sets[i] != null) {
				result[j] = sets[i];
				j++;
			}
		}
		return result;
	}

}
