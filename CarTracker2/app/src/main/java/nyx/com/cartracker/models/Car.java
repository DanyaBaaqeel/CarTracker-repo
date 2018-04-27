package nyx.com.cartracker.models;

/**
 * Created by Luminance on 3/14/2018.
 */

public class Car {
    private String id , number , details,name , owner_details;

    public Car(String id, String number, String details, String name, String owner_details) {
        this.id = id;
        this.number = number;
        this.details = details;
        this.name = name;
        this.owner_details = owner_details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner_details() {
        return owner_details;
    }

    public void setOwner_details(String owner_details) {
        this.owner_details = owner_details;
    }
}
