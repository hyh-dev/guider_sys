package com.example.administrator.guidersystem;

import java.util.List;

public class Bean {

    public String statusCode;
    public String desc;
    public ResultBean result;
    public int totalCount;

    public  class ResultBean{
        public List<PlantBean> plantList;
    }

    public class PlantBean{
        public String area;
        public String coverURL;
        public String engName;
        public String name;
        public int plantID;
    }

}
