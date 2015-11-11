/**
 * 
 */
package maggie.common.algorithm;

/**
 * @author LIU Xiaofan
 * 
 */
public class MatrixUtils {

	/**
	 * converting prices array to returns array in log scale
	 * 
	 * @param price
	 * @return logReturn
	 */
	public static float[] priceToLogReturn(float[] price) {
		// convert price to log scale
		float[] logPrice = new float[price.length];
		for (int i = 0; i < price.length; i++) {
			if (price[i] == 0)
				logPrice[i] = 0;
			else
				logPrice[i] = (float) Math.log(price[i]);
		}
		// return array
		float[] logReturn = new float[price.length - 1];
		for (int i = 0; i < logReturn.length; i++) {
			logReturn[i] = logPrice[i + 1] - logPrice[i];
		}
		return logReturn;
	}

	public static void main(String[] args) {
		float[] array = { 0.1f, 2.2f, 3.2f };
		System.out.println(sum(array) / array.length);
		System.out.println(std(array));
	}

	public static float priceReturn(float[] close) {
		return (float) (Math.log(close[close.length - 1]) - Math.log(close[0]));
	}

	/**
	 * calculate sample standard deviation of the array
	 * 
	 * @param input
	 * @return
	 */
	public static float std(float[] input) {
		float[] array = input.clone();
		float mean = sum(array) / array.length;
		for (int i = 0; i < array.length; i++) {
			array[i] -= mean;
			array[i] *= array[i];
		}
		return (float) Math.sqrt(sum(array) / (array.length - 1));
	}

	public static float sum(float[] array) {
		float sum = 0;
		for (float f : array) {
			sum += f;
		}
		return sum;
	}

	public static float[][] transposeMatrix(float[][] input) {
		float[][] output = new float[input[0].length][input.length];
		for (int i = 0; i < output.length; i++) {
			for (int j = 0; j < output[0].length; j++) {
				output[i][j] = input[j][i];
			}
		}
		return output;
	}

	/**
	 * returns a value from -1 to 1 from book: an introduction to econophysics
	 * pp.99
	 * 
	 * @param array1
	 * @param array2
	 * @return
	 * @throws Exception
	 */
	public static float xCorr(float[] array1, float[] array2) {
		// XXX check lengths
		// if (array1.length != array2.length)
		// throw new Exception();

		int length = array1.length;
		// mean of two arrays <Si> <Sj>
		float array1mean = 0, array2mean = 0;
		for (int i = 0; i < length; i++) {
			array1mean += array1[i];
			array2mean += array2[i];
		}
		array1mean /= length;
		array2mean /= length;
		// dividend <SiSj>-<Si><Sj>
		float cov12 = 0;
		// divider sqrt{ <Si2-<Si>2> * <Sj2-<Sj>2> }
		float divider = 0, cov11 = 0, cov22 = 0;
		for (int i = 0; i < length; i++) {
			cov12 += array1[i] * array2[i];
			cov11 += (array1[i] * array1[i] - array1mean * array1mean);
			cov22 += (array2[i] * array2[i] - array2mean * array2mean);
		}
		cov12 /= length;
		cov12 -= array1mean * array2mean;
		cov11 /= length;
		cov22 /= length;
		divider = (float) Math.sqrt(cov11 * cov22);
		if (cov12 == 0)
			// invalid value
			return 2;
		else
			return cov12 / divider;
	}

	public static float average(float[] fs) {
		return sum(fs) / fs.length;
	}

	public static int sum(int[] is) {
		int sum = 0;
		for (int f : is) {
			sum += f;
		}
		return sum;
	}

	public static double average(double[] oldValues) {
		return sum(oldValues) / oldValues.length;
	}

	public static double sum(double[] oldValues) {
		double sum = 0;
		for (double f : oldValues) {
			sum += f;
		}
		return sum;
	}

	public static float std(double[] input) {
		double[] array = input.clone();
		double mean = average(array);
		for (int i = 0; i < array.length; i++) {
			array[i] -= mean;
			array[i] *= array[i];
		}
		return (float) Math.sqrt(sum(array) / (array.length - 1));
	}
}
