package com.hk.vehicleauth.models;

public class Vehicle {
    String id, name, status, ownerName;

    public Vehicle() {
    }

    public Vehicle(String id, String name, String status, String ownerName) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.ownerName = ownerName;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
