package subscriptionmanager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author b1086175 | Jake Taylor
 */
public class DateHelper {
    
    /** Returns the current date in string format
     * 
     * @return 
     */
    public static String getDate()
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        return sdf.format(cal.getTime());
        
    }
    
    /** Returns an integer corresponding to the month in the given date
     * 
     * @param inDate
     * @return 
     */
    public static int getMonthInt(Date inDate){
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        int result;
        result = Integer.parseInt(sdf.format(inDate));
        return result;
    }
    
    /** Returns the month section of a given date as a string
     * 
     * @param inDate
     * @return 
     */
    public static String getMonthWord(Date inDate){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM");
        return sdf.format(inDate);
    }
    
    /** Takes a date in string format and converts in into a Date object
     * 
     * @param inDate
     * @return 
     */
    public static Date strToDate(String inDate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            return sdf.parse(inDate);
        } catch (ParseException ex) {
            // If exception occurs, return current date instead
            System.out.println("strDate() ParseException. Date set to todays date.");
            Calendar cal = Calendar.getInstance();
            return cal.getTime();
        }
    }
    
    /** Takes a Date and returns it as a string
     * 
     * @param inDate
     * @return 
     */
    public static String dateToStr(Date inDate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        return sdf.format(inDate);
    }
}
