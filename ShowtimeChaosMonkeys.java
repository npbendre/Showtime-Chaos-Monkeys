import java.util.Calendar;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

public class ShowtimeChaosMonkeys {
	
	private final static int OPEN_HOUR = 9;

    private final static int CLOSE_HOUR = 15;
    
    private final static int DAYS_IN_WEEK = 7;
    
    private final static int DAYS_IN_MONTH = 30;
    
    private final static Set<Integer> holidays = new TreeSet<Integer>();
    
    private static boolean isHoliday(Calendar now) {
        if (!holidays.contains(now.get(Calendar.YEAR))) {
            loadHolidays(now.get(Calendar.YEAR));
        }
        return holidays.contains(now.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * Load holidays.
     *
     * @param year
     *            the year
     */
    protected static void loadHolidays(int year) {
        holidays.clear();
        // these aren't all strictly holidays, but days when engineers will likely
        // not be in the office to respond to rampaging monkeys

        // new years, or closest work day
        holidays.add(workDayInYear(year, Calendar.JANUARY, 1));

        // 3rd monday == MLK Day
        holidays.add(dayOfYear(year, Calendar.JANUARY, Calendar.MONDAY, 3));

        // 3rd monday == Presidents Day
        holidays.add(dayOfYear(year, Calendar.FEBRUARY, Calendar.MONDAY, 3));

        // last monday == Memorial Day
        holidays.add(dayOfYear(year, Calendar.MAY, Calendar.MONDAY, -1));

        // 4th of July, or closest work day
        holidays.add(workDayInYear(year, Calendar.JULY, 4));

        // first monday == Labor Day
        holidays.add(dayOfYear(year, Calendar.SEPTEMBER, Calendar.MONDAY, 1));

        // second monday == Columbus Day
        holidays.add(dayOfYear(year, Calendar.OCTOBER, Calendar.MONDAY, 2));

        // veterans day, Nov 11th, or closest work day
        holidays.add(workDayInYear(year, Calendar.NOVEMBER, 11));

        // 4th thursday == Thanksgiving
        holidays.add(dayOfYear(year, Calendar.NOVEMBER, Calendar.THURSDAY, 4));

        // 4th friday == "black friday", monkey goes shopping!
        holidays.add(dayOfYear(year, Calendar.NOVEMBER, Calendar.FRIDAY, 4));

        // christmas eve
        holidays.add(dayOfYear(year, Calendar.DECEMBER, 24));
        // christmas day
        holidays.add(dayOfYear(year, Calendar.DECEMBER, 25));
        // day after christmas
        holidays.add(dayOfYear(year, Calendar.DECEMBER, 26));

        // new years eve
        holidays.add(dayOfYear(year, Calendar.DECEMBER, 31));
        
        // mark the holiday set with the year, so on Jan 1 it will automatically
        // recalculate the holidays for next year
        holidays.add(year);
    }

    /**
     * Day of year.
     *
     * @param year
     *            the year
     * @param month
     *            the month
     * @param day
     *            the day
     * @return the day of the year
     */
    private static int dayOfYear(int year, int month, int day) {
        Calendar holiday = now();
        holiday.set(Calendar.YEAR, year);
        holiday.set(Calendar.MONTH, month);
        holiday.set(Calendar.DAY_OF_MONTH, day);
        return holiday.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Day of year.
     *
     * @param year
     *            the year
     * @param month
     *            the month
     * @param dayOfWeek
     *            the day of week
     * @param weekInMonth
     *            the week in month
     * @return the day of the year
     */
    private static int dayOfYear(int year, int month, int dayOfWeek, int weekInMonth) {
        Calendar holiday = now();
        holiday.set(Calendar.YEAR, year);
        holiday.set(Calendar.MONTH, month);
        holiday.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        holiday.set(Calendar.DAY_OF_WEEK_IN_MONTH, weekInMonth);
        return holiday.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Work day in year.
     *
     * @param year
     *            the year
     * @param month
     *            the month
     * @param day
     *            the day
     * @return the day of the year adjusted to the closest workday
     */
    private static int workDayInYear(int year, int month, int day) {
        Calendar holiday = now();
        holiday.set(Calendar.YEAR, year);
        holiday.set(Calendar.MONTH, month);
        holiday.set(Calendar.DAY_OF_MONTH, day);
        int doy = holiday.get(Calendar.DAY_OF_YEAR);
        int dow = holiday.get(Calendar.DAY_OF_WEEK);

        if (dow == Calendar.SATURDAY) {
            return doy - 1; // FRIDAY
        }

        if (dow == Calendar.SUNDAY) {
            return doy + 1; // MONDAY
        }

        return doy;
    }
    
    private static Calendar now() {
        return Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
    }
    
	private enum Frequency {
		Daily, Weekly, Monthly
	};
	
	private static boolean isWeekend(Calendar calendar) {
        int dow = calendar.get(Calendar.DAY_OF_WEEK);
        return dow == Calendar.SATURDAY
                || dow == Calendar.SUNDAY;
    }
	
	public static int getRandom(int seed) {
		Random r = new Random();
		int value =  r.nextInt(seed);
		System.out.println("Random Value is " + value);
		return value;
	}
	
	
	/** This is an utility to decide whether to deploy Chaos Monkeys or not. */
	public static boolean unleashChaosMonkey(Date date, Frequency frequency) {
		//current date
		Calendar calendar = now();
		calendar.setTime(date);
		
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		System.out.println("Hour of the day is " + hour);
		
		
		//check time
		if (hour < OPEN_HOUR || hour > CLOSE_HOUR) {
			System.out.println("Sorry, can unleash monkey only between 9 am to 3 pm.");
		    return false;
	    } 
        
        switch(frequency) {
			case Monthly:
				calendar.add(Calendar.DATE, getRandom(DAYS_IN_MONTH));
				break;
			case Weekly:
				calendar.add(Calendar.DATE, getRandom(DAYS_IN_WEEK));
				break;
			default:
				break;
        }
        
        //check if its not a weekend
        System.out.println("Target Date trying to deploy chaos monkey is "+ calendar.getTime());
        if(isWeekend(calendar) || isHoliday(calendar)) {
        	System.out.println("Sorry, cannot unleash monkey on the weekend or a holiday.");
        	return false;
        }
        
        System.out.println("Hurray, chaos monkey is going to test you.! ");
		return true;
	}
	
	public static void main(String[] args) {
		Date date = new Date();
		boolean unleashMonkey = unleashChaosMonkey(date, Frequency.Monthly);
		System.out.println("Is Chaos Monkey Unleased ? - "+ unleashMonkey);
	}

}
