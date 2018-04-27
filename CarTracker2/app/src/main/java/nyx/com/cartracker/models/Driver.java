package nyx.com.cartracker.models;

/**
 * Created by Luminance on 3/14/2018.
 */

public class Driver {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String id  , name ;

    public Driver(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
