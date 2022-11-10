package subscriptionmanager;

import java.util.Date;

/** Represents a Bronze Subscription
 *
 * @author b1086175 | Jake Taylor
 */
public class BronzeSubscription extends Subscription{
    
    
    public BronzeSubscription(Date sDate, SubDuration sDuration, String sDiscount, SubTerms sTerms, int sAmount, String sName) {
        super(sDate, sDuration, sDiscount, sTerms, sAmount, sName);
    }
    
    public BronzeSubscription(Date sDate, int sDuration, String sDiscount, char sTerms, int sAmount, String sName) {
        super(sDate, sDuration, sDiscount, sTerms, sAmount, sName);
    }
    
    @Override
    public void calcAmount() {
        
        int total = 0;
        switch (getDuration()){
            case ONE:
                total = 600;
                break;
            case THREE:
                total = 500;
                break;
            case SIX:
                total = 400;
                break;
            case TWELVE:
                total = 300;
                break;
            default:
                System.out.println("Error calculating monthly figure (BRONZE)");
        }
        
        setAmount(this.applyDiscount(total));
    }
    
    @Override
    public char getPackageChar() {
        return 'B';
    }
    
    @Override
    public String getPackageString() {
        return "Bronze";  
    }
}
