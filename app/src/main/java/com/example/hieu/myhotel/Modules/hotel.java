package com.example.hieu.myhotel.Modules;

import java.io.Serializable;

/**
 * Created by ThuHuong on 24/06/2017.
 */

public class hotel implements Serializable{
    private String id;
    private String imgHotel;
    private String name;
    private String district;
    private String city;
    private String address;
    private int cost;
    private String detail;

    public hotel() {
    }

    public hotel(String id, String imgHotel, String name, String district, String city, String address, int cost, String detail) {
        this.id = id;
        this.imgHotel = imgHotel;
        this.name = name;
        this.district = district;
        this.city = city;
        this.address = address;
        this.cost = cost;
        this.detail = detail;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgHotel() {
        return imgHotel;
    }

    public void setImgHotel(String imgHotel) {
        this.imgHotel = imgHotel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
