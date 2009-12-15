package org.springframework.samples.petclinic;

public class Surgery extends NamedEntity {

    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
