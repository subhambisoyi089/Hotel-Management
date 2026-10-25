package com.hotel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** Hotel details from application.properties (prefix "hotel"). */
@Component
@ConfigurationProperties(prefix = "hotel")
public class HotelProperties {

    private String name = "Hotel";
    private String address = "";
    private String phone = "";
    private String gstin = "";
    private int gstPercent = 12;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getGstin() { return gstin; }
    public void setGstin(String gstin) { this.gstin = gstin; }
    public int getGstPercent() { return gstPercent; }
    public void setGstPercent(int gstPercent) { this.gstPercent = gstPercent; }
}
