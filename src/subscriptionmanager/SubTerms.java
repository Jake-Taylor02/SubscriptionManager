package subscriptionmanager;

/**
 *
 * @author b1086175 | Jake Taylor
 */
public enum SubTerms {
    ONE_OFF('O'),
    MONTHLY('M');
    
    private final char termChar;

    private SubTerms(char termChar) {
        this.termChar = termChar;
    }

    public char getTermChar() {
        return termChar;
    }
    
    
    
    /**
     * Overriding the toString() method so that only the first letter of the
     * constant is in uppercase.
     * 
     * @return Name of constant
     */
    @Override
    public String toString() {
        String origName = super.toString();// Calling parent method
        
        if (origName.equals("ONE_OFF")) {
            return "One-off";
        }
        
        return origName.substring(0, 1) + origName.substring(1).toLowerCase();
    }
}
