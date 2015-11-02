package com.unibratec.livia.polishcollection.Model;

import java.io.Serializable;

/**
 * Created by Livia on 31/10/2015.
 */
public class Polish implements Serializable{
    public Polish() {
    }

    public Polish(long dbId, int id, String brand, String color, String name, String imageUrl) {
        this.dbId = dbId;
        this.id = id;
        this.brand = brand;
        this.color = color;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public Polish(String brand, String color, String name, String imageUrl) {
        this.brand = brand;
        this.color = color;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public long dbId;
    public int id;
    public String brand;
    public String color;
    public String name;
    public String imageUrl;

    @Override
    public String toString() {
        return brand + ": " + name;
    }
}
