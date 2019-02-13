package com.example.administrator.guidersystem;

public class Discovery {
    private String name;
    private String imageId;
    public  Discovery(String name,String imageId)
    {
        this.name=name;
        this.imageId=imageId;
    }
    public String getName(){
        return name;
    }

    public String getImageId() {
        return imageId;
    }
}
