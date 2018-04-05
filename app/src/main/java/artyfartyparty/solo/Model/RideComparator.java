package artyfartyparty.solo.Model;

import java.util.Comparator;

public class RideComparator implements Comparator<Ride> {
    @Override
    public int compare(Ride r1, Ride r2) {
        return r1.getDateFrom().compareTo(r2.getDateFrom());
    }
}