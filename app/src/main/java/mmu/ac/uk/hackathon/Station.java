package mmu.ac.uk.hackathon;

import android.location.Location;


public class Station {

    String name;
    Double latitude;
    Double longitude;
    float distance;

    //setters

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param latitude
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @param longitude
     */
    public void setLongitude(Double longitude){
        this.longitude = longitude;
    }

    //find the distance between the users latitude and longitude and the latitude and longitude for the train station
    public void setDistance(){
        Location locA = new Location("User");

        locA.setLatitude(LocationVars.latitude);
        locA.setLongitude(LocationVars.longitude);

        Location locB = new Location("Station");

        locB.setLatitude(this.latitude);
        locB.setLongitude(this.longitude);
        //set the distance variable and convert from meters to km
        this.distance = locA.distanceTo(locB)/1000;
    }

    //getters

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     *
     * @return
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     *
     * @return
     */
    public float getDistance(){
        return distance;
    }

}
