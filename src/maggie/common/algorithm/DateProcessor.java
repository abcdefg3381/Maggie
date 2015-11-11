/**
 * 
 */
package maggie.common.algorithm;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * This class processes simple date algorithm
 * 
 * @author LIU Xiaofan
 * 
 */
public class DateProcessor {

	private static Calendar c = new GregorianCalendar();

	/**
	 * Given an end date, the function finds the first working day starting from
	 * the end date and get its partner before the window size.</br> If
	 * getDayBefore is true, the pair is shifted one working day ahead.
	 * 
	 * @param endDate
	 * @param windowSize
	 * @param getDayBefore
	 * @return a pair of dates [endDate-10, endDate]. totally 11 days including
	 *         start and end.</br> usage: left exclusive, right inclusive.
	 */
	public Date[] processDate(Date endDate, int windowSize, boolean getDayBefore) {
		Date[] pair = new Date[2];
		c.setTime(endDate);
		if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
		} else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			c.set(Calendar.DATE, c.get(Calendar.DATE) - 2);
		}
		if (getDayBefore) {
			c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
			if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
			} else if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				c.set(Calendar.DATE, c.get(Calendar.DATE) - 2);
			}
		}
		pair[1] = c.getTime();
		c.set(Calendar.DATE, c.get(Calendar.DATE) - windowSize - windowSize / 5 * 2);
		pair[0] = c.getTime();
		return pair;
	}
}
