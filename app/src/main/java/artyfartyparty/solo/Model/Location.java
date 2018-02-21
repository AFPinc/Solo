package artyfartyparty.solo.Model;

/**
 * Created by melkorkamj1 on 21/02/2018.
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
