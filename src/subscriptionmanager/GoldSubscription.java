package subscriptionmanager;

import java.util.Date;

/** Represents a Gold Subscription
 *
 * @author b1086175 | Jake Taylor
 */
public class GoldSubscription extends Subscription{

    public GoldSubscription(Date sDate, SubDuration sDuration, String sDiscount, SubTerms sTerms, int sAmount, String sName) {
        super(sDate, sDuration, sDiscount, sTerms, sAmount, sName);
    }
    
    
    public GoldSubscription(Date sDate, int sDuration, String sDiscount, char sTerms, int sAmount, String sName) {
        super(sDate, sDuration, sDiscount, sTerms, sAmount, sName);
    }
    
    @Override
    public void calcAmount() {
        
        int total = 0;
        switch (getDuration()){
            case ONE:
                total = 999;
                break;
            case THREE:
                total = 899;
                break;
            case SIX:
                total = 799;
                break;
            case TWELVE:
                total = 699;
                break;
            default:
                System.out.println("Error calculating monthly figure (BRONZE)");
        }
        
        setAmount(this.applyDiscount(total));
    }
    
    @Override
    public char getPackageChar() {
        return 'G';
    }
    
    @Override
    public String getPackageString() {
        return "Gold";
    }
}
