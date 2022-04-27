package com.hk.vehicleauth.models;

public class Driver {
    String driverId, name, image, password, status, expireDate;

    public Driver() {
    }

    public Driver(String driverId, String name, String image, String password, String status, String expireDate) {
        this.driverId = driverId;
        this.name = name;
        this.image = image;
        this.password = password;
        this.status = status;
        this.expireDate = expireDate;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
}
