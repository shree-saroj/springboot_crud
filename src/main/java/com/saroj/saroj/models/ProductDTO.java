package com.saroj.saroj.models;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;

public class ProductDTO {
    @NotEmpty(message = "Name is required")
    private String name;

    @NotEmpty(message = "Brand Name is required")
    private String brandName;

    @NotEmpty(message = "Category name is required")
    private String categoryName;

    @Min(value = 0, message = "Price must be non-negative")
    private double price;

    @Size(min = 10, message = "Description must be at least 10 characters long")
    @Size(max = 200, message = "Description must not exceed 200 characters")
    private String description;

    private MultipartFile image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}
