package com.hk.vehicleauth.models;

public class Vehicle {
    String carNo, name, status, ownerNid, ownerName, expireDate;

    public Vehicle() {
    }

    public Vehicle(String carNo, String name, String status, String ownerNid, String ownerName, String expireDate) {
        this.carNo = carNo;
        this.name = name;
        this.status = status;
        this.ownerNid = ownerNid;
        this.ownerName = ownerName;
        this.expireDate = expireDate;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
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

    public String getOwnerNid() {
        return ownerNid;
    }

    public void setOwnerNid(String ownerNid) {
        this.ownerNid = ownerNid;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }
}
