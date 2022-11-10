package subscriptionmanager;

/**
 *
 * @author b1086175 | Jake Taylor
 */
public class FieldValidation {
    
    /** Returns whether or not the name parameter is a valid name.
     * A valid name contains only letters and is 2-25 characters long.
     * @param name
     * @return 
     */
    static boolean validName(String name){
        // Test that name is 2 - 25 characters
        if (name.length() < 2 || name.length() > 25){
            return false;
        }
        char c;
        // Loop through each char in name, ensuring that there are only letters
        for(int count = 0; count < name.length(); count++){
            c = name.charAt(count);
            if (!Character.isLetter(c) && c != ' '){
                return false;
            }
        }
        return true;
    }
    
    /** Returns whether or not the pack parameter is a valid package.
     * A valid package must be 'G', 'S' or 'B'.
     * @param pack
     * @return 
     */
    static boolean validPackage(String pack) {
        if(pack.length() != 1){
            return false;
        }
        
        char d = pack.toUpperCase().charAt(0);
        
        // Return true if package is 'G', 'S' or 'B'
        return (d == 'G' || d == 'S' || d == 'B');
    }
    
    /** Returns whether or not the duration parameter is a valid duration.
     * A valid duration must be 1, 3, 6 or 12.
     * @param duration
     * @return 
     */
    static boolean validDuration(int duration){
        if (duration == 1 || duration == 3 || duration == 6 || duration == 12){
            return true;
        }
        System.out.println("Invalid duration.");
        return false;
    }
    
    /** Returns whether or not the discCode parameter is a valid discount code.
     * 
     * @param discCode
     * @return 
     */
    static boolean validDiscountCode(String discCode) {
        String reason = "";
        String[] subcode = new String[4];// Array to hold the 4 sections of the discount code
        if (discCode.length() == 6) {
            discCode = discCode.toUpperCase();
            // Seperate the sections of the code:
            subcode[0] = discCode.substring(0, 2);
            subcode[1] = discCode.substring(2, 4);
            subcode[2] = discCode.substring(4, 5);
            subcode[3] = discCode.substring(5);

            // Validating each section of the discount code
            boolean dValid = true;
            // The fist two characters need to be letters:
            if (!Character.isLetter(subcode[0].charAt(0)) || !Character.isLetter(subcode[0].charAt(1))) {
                dValid = false;
                reason = "(Invalid Issuance characters)";
                
            // The third and forth characters need to be integers:
            } else if (!Character.isDigit(subcode[1].charAt(0)) || !Character.isDigit(subcode[1].charAt(1))) {
                dValid = false;
                reason = "(Invalid Year)";
                
            // The fifth character needs to be either 'E' or 'L':
            }else if (subcode[2].charAt(0) != 'E' && subcode[2].charAt(0) != 'L') {
                dValid = false;
                reason = "(Invalid Month)";
                
            // The final character must be an integer 1-9:
            } else if (Character.isDigit(subcode[3].charAt(0))) {
                int dAmount = Integer.parseInt(String.valueOf(subcode[3].charAt(0)));// Parse the char into int
                // If Discount is NOT within allowed range:
                if (dAmount < 1 || dAmount > 9) {
                    dValid = false;
                    reason = "(Invalid Discount Percentage)";
                } else {// checking the expiration year and month are in date:
                    // Get last 2 digits of current year
                    String strYear = DateHelper.getDate().substring(9);
                    int currentYear = Integer.parseInt(strYear);

                    // Get Exp. Year of code, Souldn't throw exception as we've checked that it is numeric
                    int expYear = Integer.parseInt(subcode[1]);

                    if (expYear != currentYear) {
                        dValid = false;
                        reason = "(Invalid Year)";
                    } else if (expYear == currentYear) {
                        // Get current month
                        String strMonth = DateHelper.getDate().substring(3, 6);

                        int currentMonth = findMonthIndex(strMonth) + 1;

                        
                        int expMonth[] = new int[2];// Holds the period that the code is valid for
                        
                        // E= January to June, L = July to December
                        expMonth[0] = (subcode[2].charAt(0) == 'E') ? 1 : 7;// Lower limit
                        expMonth[1] = (subcode[2].charAt(0) == 'E') ? 6 : 12;// Upper Limit
                        if (expMonth[0] > currentMonth) {
                            dValid = false;
                            reason = "(Expiry month means that code is not yet valid)";
                        } else if ( expMonth[1] < currentMonth) {
                            dValid = false;
                            reason = "(Discount code has expired (month))";
                        }
                    }
                }
            } else {// Discount percentage is not a digit
                dValid = false;
                reason = "(Invalid Discount Percentage!!)";
            }
            
            
            if (dValid) {
                return true;
            } else {
                System.out.println("Invalid discount code " + reason);   
            }
            
        } else {
            System.out.println("Invalid discount code (Length)");
        }
        return false;
    }
    /** Returns integer corresponding to the month input.
     * 
     * @param criteria
     * @return 
     */
    static int findMonthIndex(String criteria) {
        String[] tempMonths = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                               "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        
        for (int idx = 0; idx < tempMonths.length; idx++) {
            if (tempMonths[idx].equals(criteria)){
                return idx;
            }
        }
        return 0; // this should never happen
    }
    
    /** Returns whether or not the sTerms parameter is a valid Subscription term.
     * Payment term must be either 'O' or 'M'
     * @param sTerms
     * @return 
     */
    static boolean validTerms(String sTerms) {
        if (sTerms.length() != 1) {
            return false;
        }
        
        char cTerms = sTerms.toUpperCase().charAt(0);
        
        return cTerms == 'O' || cTerms == 'M';
    }
}