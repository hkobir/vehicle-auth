package com.hk.vehicleauth.models;

import java.util.List;

public class VehicleDriver {
    private Vehicle profile;
    private List<Driver> drivers;
    private String currentDriver;

    public VehicleDriver() {
    }

    public VehicleDriver(Vehicle profile, List<Driver> drivers, String currentDriver) {
        this.profile = profile;
        this.drivers = drivers;
        this.currentDriver = currentDriver;
    }

    public Vehicle getProfile() {
        return profile;
    }

    public void setProfile(Vehicle profile) {
        this.profile = profile;
    }

    public List<Driver> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<Driver> drivers) {
        this.drivers = drivers;
    }

    public String getCurrentDriver() {
        return currentDriver;
    }

    public void setCurrentDriver(String currentDriver) {
        this.currentDriver = currentDriver;
    }
}
