package subscriptionmanager;

import java.util.HashMap;

/** Contains the duration of a subscription
 *
 * @author b1086175 | Jake Taylor
 */
public enum SubDuration {
    ONE(1),
    THREE(3),
    SIX(6),
    TWELVE(12);
    
    private final int durInt;
    private static HashMap<Integer, SubDuration> durLookup = new HashMap<>();

    private SubDuration(int durInt) {
        this.durInt = durInt;
    }

    public int getDurInt() {
        return durInt;
    }
    
    static{
        for (SubDuration dur : SubDuration.values()) {
            durLookup.put(dur.durInt, dur);
        }
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
        return origName.substring(0, 1) + origName.substring(1).toLowerCase();
    }
    
    
}