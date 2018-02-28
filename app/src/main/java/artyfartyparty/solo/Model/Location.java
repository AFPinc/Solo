package artyfartyparty.solo.Model;

/**
 * Ása Júlía
 * Melkorka Mjöll
 * Sigurlaug
 * Valgerður
 *
 * Class that maintains the data of rides
 */

public class Location {
    private long id;                    // ID of location
    private String name;                // Name of location

    public Location(String name) {
        this.name = name;
    }

    public Location() {}

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
