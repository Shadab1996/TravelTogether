package com.stinkinsweet.traveltogether;

/**
 * Created by Funkies PC on 17-Dec-16.
 */
public class Friend {
    //name and address string
    private String Name;
    private Double Latitude;
    private Double Longitude;
    private Long timestamp;

    public Friend() {
      /*Blank default constructor essential for Firebase*/
    }
    //Getters and setters
    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getLongitude() {
        return Longitude;
    }
}
