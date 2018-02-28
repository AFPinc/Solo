package artyfartyparty.solo.Model;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Class that maintains the data of stopovers
 */

public class Stopover {
    private long id;                    // ID of stopover
    private Location location;          // Location of stopover
    private Ride ride;                  // Ride that stopover belongs to

    public Stopover(Location location, Ride ride) {
        this.location = location;
        this.ride = ride;
    }

    public Stopover() {}

    public void setId(Long id) {
        this.id = id;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public Long getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public Ride getRide() {
        return ride;
    }
}
