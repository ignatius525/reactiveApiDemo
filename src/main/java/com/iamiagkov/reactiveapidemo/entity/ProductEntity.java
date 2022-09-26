package com.iamiagkov.reactiveapidemo.entity;


import lombok.Data;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
@Data
public class ProductEntity {

    @PrimaryKey
    private Integer id;

    private String name;
    private Integer quantity;
    private Double price;

}
