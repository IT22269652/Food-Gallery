package com.nuradha.hello.model;

public class FoodPost {
    private Long id;
    private String title;
    private String description;
    private String[] imagePaths;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String[] getImagePaths() { return imagePaths; }
    public void setImagePaths(String[] imagePaths) { this.imagePaths = imagePaths; }
}