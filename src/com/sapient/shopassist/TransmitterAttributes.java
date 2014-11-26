
package com.sapient.shopassist;

public class TransmitterAttributes {

    private String identifier;
    private String name;
    private String offerText;
    private String offerTitle;
    private String ownerId;
    private String iconUrl;
    private Integer battery;
    private Integer temperature;
    private int rssi;
    private boolean depart;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfferText() {
        return offerText;
    }

    public void setOfferText(String text) {
        this.offerText = text;
    }
    
    public String getOfferTitle() {
        return offerTitle;
    }

    public void setOfferTitle(String text) {
        this.offerTitle = text;
    }
    
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Integer getBattery() {
        return battery;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public boolean isDepart() {
        return depart;
    }

    public void setDepart(boolean depart) {
        this.depart = depart;
    }

}
