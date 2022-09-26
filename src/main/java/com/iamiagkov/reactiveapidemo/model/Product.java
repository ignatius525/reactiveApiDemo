package com.iamiagkov.reactiveapidemo.model;


import lombok.Data;


@Data
public class Product {

    private Integer id;

    private String name;
    private Integer quantity;
    private Double price;
}
