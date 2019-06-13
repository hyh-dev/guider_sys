package com.example.administrator.guidersystem.plantDB;

import java.util.List;

public class Flower {
    public String log_id;
    public List<Flowerresult>result;

    public class Flowerresult{
        public String score;
        public String name;
        public Baike baike_info;
    }

    public class Baike{
        public String baike_url;
        public String description;
        public String image_url;
    }
}
