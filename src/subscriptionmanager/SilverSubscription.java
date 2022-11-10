package subscriptionmanager;

import java.util.Date;

/** Represents a Silver Subscription.
 *
 * @author b1086175 | Jake Taylor
 */
public class SilverSubscription extends Subscription {

    public SilverSubscription(Date sDate, SubDuration sDuration, String sDiscount, SubTerms sTerms, int sAmount, String sName) {
        super(sDate, sDuration, sDiscount, sTerms, sAmount, sName);
    }
    
    public SilverSubscription(Date sDate, int sDuration, String sDiscount, char sTerms, int sAmount, String sName) {
        super(sDate, sDuration, sDiscount, sTerms, sAmount, sName);
    }
    
    @Override
    public void calcAmount() {
        
        int total = 0;
        switch (getDuration()){
            case ONE:
                total = 800;
                break;
            case THREE:
                total = 700;
                break;
            case SIX:
                total = 600;
                break;
            case TWELVE:
                total = 500;
                break;
            default:
                System.out.println("Error calculating monthly figure (BRONZE)");
        }
        
        setAmount(this.applyDiscount(total));
    }
    
    @Override
    public char getPackageChar() {
        return 'S';
    }
    
    @Override
    public String getPackageString() {
        return "Silver";
    }
}
