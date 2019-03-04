package com.example.administrator.guidersystem.discoveryPage;

public class Discovery {
    private String name;
    private String imageId;
    private String area;
    private String engName;

    public Discovery(String name, String imageId, String area, String engName) {
        this.name = name;
        this.imageId = imageId;
        this.area = area;
        this.engName = engName;
    }

    public String getName(){
        return name;
    }

    public String getImageId() {
        return imageId;
    }

    public String getArea() {
        return area;
    }

    public String getEngName() {
        return engName;
    }
}
