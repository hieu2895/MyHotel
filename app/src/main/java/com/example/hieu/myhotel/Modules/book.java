package com.example.hieu.myhotel.Modules;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by ThuHuong on 26/06/2017.
 */

public class book implements Serializable {
    private String nameHotel;
    private String address;
    private Date dateIn;
    private Date dateOut;
    private int cost;
    private int quantity;
    private double total;

    public book() {
    }

    public book(String nameHotel, String address, Date dateIn, Date dateOut, int cost, Double total, int quantity) {

        this.nameHotel = nameHotel;
        this.address = address;
        this.dateIn = dateIn;
        this.dateOut = dateOut;
        this.cost = cost;
        this.total = total;
        this.quantity = quantity;
    }


    public String getNameHotel() {
        return nameHotel;
    }

    public void setNameHotel(String nameHotel) {
        this.nameHotel = nameHotel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDateIn() {
        return dateIn;
    }

    public void setDateIn(Date dateIn) {
        this.dateIn = dateIn;
    }

    public Date getDateOut() {
        return dateOut;
    }

    public void setDateOut(Date dateOut) {
        this.dateOut = dateOut;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;

    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
