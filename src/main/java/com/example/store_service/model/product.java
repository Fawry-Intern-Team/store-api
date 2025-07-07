package com.example.store_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

"rstock---->order-id,p-id,quantity"
public class product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private int id;


    private String name;


    private String description;


    private Double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public product(int id, Double price, String description, String name) {
        this.id = id;
        this.price = price;
        this.description = description;
        this.name = name;
    }
}
