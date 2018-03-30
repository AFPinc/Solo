package artyfartyparty.solo.Model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Class that maintains the data of rides
 */

public class Ride {
    private long id;                    // ID of every
    private User user;                  // Driver of
    private Location locationFrom;      // Departure location
    private Location locationTo;        // Arrival location
    private Date dateFrom;              // Departure timing
    private Date dateTo;                // Arrival timing
    private boolean deleted;

    private Set<Stopover> stopovers = new HashSet<Stopover>();

    public Ride(User user, Location locationFrom, Location locationTo, Date dateFrom, Date dateTo) {
        this.user = user;
        this.locationFrom = locationFrom;
        this.locationTo = locationTo;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public Ride() {}

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setLocationFrom(Location locationFrom){
        this.locationFrom = locationFrom;
    }

    public void setLocationTo(Location locationTo) {
        this.locationTo = locationTo;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Location getLocationFrom() {
        return locationFrom;
    }

    public Location getLocationTo() {
        return locationTo;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
