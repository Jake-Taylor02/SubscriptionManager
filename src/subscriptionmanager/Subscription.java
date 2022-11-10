package subscriptionmanager;


import java.util.Date;

/**
 * Represent a Subscription record.
 * 
 * @author b1086175 | Jake Taylor
 */
abstract class Subscription {
    private final Date sDate;
    private SubDuration sDuration;
    private final String sDiscount;
    private SubTerms sTerms;
    private int sAmount;
    private final String sName;

    /**
     * Constructor taking all attributes for a Subscription object
     * 
     * @param sDate     The date that a subscription was added
     * @param sPackage  The Subscription Package
     * @param sDuration The Subscription Duration
     * @param sDiscount The Discount code used
     * @param sTerms    The Subscription Terms (One-off or Monthly)
     * @param sAmount   The Cost (in pence) of the Subscription
     * @param sName     The Customers name
     */
    public Subscription(Date sDate, SubDuration sDuration, String sDiscount, SubTerms sTerms, int sAmount, String sName) {
        this.sDate = sDate;
        this.sDuration = sDuration;
        this.sDiscount = sDiscount;
        this.sTerms = sTerms;
        this.sAmount = sAmount;
        this.sName = sName;
    }
    
    /**
     * Overloaded constructor where Duration and Terms are provided as
     * primitive types rather than enums.
     * <p>
     * This constructor calls the regular constructor, using place holders for
     * the two enum attributes. The actual value of Package, Duration and
     * Terms are then set using their individual setters where they are
     * converted from the primitive types to enums.
     * 
     * @param sDate
     * @param sDuration Here, provided as int instead of SubDuration enum
     * @param sDiscount
     * @param sTerms    Here, provided as char instead of SubTerms enum
     * @param sAmount
     * @param sName
     */
    public Subscription(Date sDate, /*char sPackage,*/ int sDuration, String sDiscount, char sTerms, int sAmount, String sName) {
        // Construct Subscription, using place holders for enum attributes
        this(sDate, SubDuration.ONE, sDiscount, SubTerms.MONTHLY, sAmount, sName);
        
        // Use setters to set enumr attributes, converting the type
        this.setDuration(sDuration);
        this.setTerms(sTerms);
    }
    
    /** Set the Subscription duration
     * 
     * @param newDuration 
     */
    public void setDuration(SubDuration newDuration){
        this.sDuration = newDuration;
    }
    
    /** Set the Subscription duration, using int as argument
     * @param newDuration 
     */
    public void setDuration(int newDuration){
        switch (newDuration) {
            case 1:
                this.sDuration = SubDuration.ONE;
                break;
            case 3:
                this.sDuration = SubDuration.THREE;
                break;
            case 6:
                this.sDuration = SubDuration.SIX;
                break;
            default:
                this.sDuration = SubDuration.TWELVE;
      
        }
    }
    
    /** Set the Subscription terms
     * @param newTerms 
     */
    public void setTerms(SubTerms newTerms){
        this.sTerms = newTerms;
    }
    
    /** Set the Subscription terms, using char as argument.
     * @param newTerms 
     */
    public void setTerms(char newTerms) {
        if (newTerms == 'O') {
            this.sTerms = SubTerms.ONE_OFF;
        } else {
            this.sTerms = SubTerms.MONTHLY;
        }
    }
    
    /** Sets the Subscription Amount
     * @param newAmount 
     */
    void setAmount(int newAmount){
        this.sAmount = newAmount;
    }
    
    /** Returns the Date of the subscription.
     * @return String - The date in the format, dd-MMM-yyyy.
     */
    public Date getDate() {
        return sDate;
    }

    /** Returns the Subscription Duration.
     * @return SubDuration - Duration.
     * @see SubDuration
     */
    public SubDuration getDuration() {
        return sDuration;
    }

    /** Returns the Discount Code.
     * @return String - Discount Code.
     */
    public String getDiscount() {
        return sDiscount;
    }

    /** Returns the Subscription Terms.
     * @return SubTerms - Terms.
     * @see SubTerms
     */
    public SubTerms getTerms() {
        return sTerms;
    }

    /** Returns Subscription amount in pence.
     * @return Integer - Amount
     */
    public int getAmount() {
        return sAmount;
    }

    /** Returns Name of the subscriber.
     * @return String - Name
     */
    public String getName() {
        return sName;
    }
    
    /** Returns the total with discounts applied.
     *   this method will be called by Subscription Subclasses.
     * @param total The 
     * @return 
     */
    protected int applyDiscount(int total) {
        
        int disAmount = 0;
        
        if (!getDiscount().equals("-")) {
            // If discount code isn't provided, disAmount remains 0
            // Get discount percentage from discount code
            disAmount = Integer.valueOf(getDiscount().substring(5));
        }
        
        // Convert percentage to decimal - e.g. 7% becomes 0.93
        double realDiscount = (1.0 - (new Double(disAmount) / 100.0));
        
        if (getTerms() == SubTerms.ONE_OFF) {
            // For One-off terms, amount needs to be the total amount * subscription duration
            total = total * getDuration().getDurInt();
            
            // apply the discount code then apply 5% discount for One-off terms
            return (int) ((total * realDiscount) * 0.95);
        }
        
        // For Monthly terms, return total amount with discount applied:
        return (int) (total * realDiscount);
    }
    
    /** Calculates monthly subscription fee based on package and duration.
     */
    abstract void calcAmount();
    
    /** Returns the character corresponding to the package type.
     * @return char
     */
    abstract char getPackageChar();
    
    /** Returns the string word corresponding to the package type.
     * @return string
     */
    abstract String getPackageString();
    
    /** Returns the Subscription object in the form that is appended to the file.
     * @return String
     */
    public String toRecord() {
        return (
                DateHelper.dateToStr(getDate())
                + "\t" + getPackageChar()
                + "\t" + getDuration().getDurInt()
                + "\t" + getDiscount()
                + "\t" + getTerms().getTermChar()
                + "\t" + getAmount()
                + "\t" + getName()
                );
    }
}
