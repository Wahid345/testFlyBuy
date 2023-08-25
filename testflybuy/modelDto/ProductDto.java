package com.example.testflybuy.modelDto;



import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String name;

    private long categoryId;
    private double price;
    private double weight;
    private String description;
    private String imageName;
}
