package com.example.administrator.guidersystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class DatabaseManager {
    private SQLiteDatabase db;
    private String name;
    private String introduction;
    private String number;
    private String music_num;

    public DatabaseManager(){ }
    public DatabaseManager(MyDatabaseHelper dbHelper){
        db=dbHelper.getWritableDatabase();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getNumber() {
        return number;
    }

    public String getMusic_num() {
        return music_num;
    }

    public void insertDB(){
        ContentValues values=new ContentValues();
        values.put("number","001");
        values.put("name","仙人掌");
        values.put("music_num",R.raw.inside_out);
        values.put("introduction","仙人掌（学名：Opuntia stricta (Haw.) Haw. var. dillenii (Ker-Gawl.) Benson ）是仙人掌科缩刺仙人掌的变种。丛生肉质灌木，高1.5-3米。上部分枝宽倒卵形、倒卵状椭圆形或近圆形，绿色至蓝绿色，无毛；刺黄色，有淡褐色横纹，坚硬；倒刺直立。叶钻形，绿色，早落。花辐状；花托倒卵形，基部渐狭，绿色；萼状花被黄色，具绿色中肋；花丝淡黄色；花药黄色；花柱淡黄色；柱头黄白色。浆果倒卵球形，顶端凹陷，表面平滑无毛，紫红色，倒刺刚毛和钻形刺。种子多数，扁圆形，边缘稍不规则，无毛，淡黄褐色。花期6-10（-12)月。");
        db.insert("plant",null,values);
        values.clear();
        values.put("number","002");
        values.put("name","芦荟");
        values.put("music_num",R.raw.swing);
        values.put("introduction","芦荟（学名：Aloe vera（Haw.） Berg）：为单子叶植物纲 、阿福花科（又称日光兰科、独尾草科）、芦荟属的多年生常绿草本植物。又名库拉索芦荟 [1]  、中华芦荟、油葱 [2]  、洋芦荟、翠叶芦荟、美国芦荟等。 [3]  叶簇生、大而肥厚，呈座状或生于茎顶，叶常披针形或叶短宽，边缘有尖齿状刺。花序为伞形、总状、穗状、圆锥形等，色呈红、黄或具赤色斑点，花瓣六片、雌蕊六枚。花被基部多连合成筒状。");
        db.insert("plant",null,values);
    }
    public void queryDB(String num){
        Cursor cursor=db.query("plant",null,"number=?",new String[]{num},null,null,null);
        if (cursor.moveToFirst()) {
            do {
                 name = cursor.getString(cursor.getColumnIndex("name"));
                 introduction = cursor.getString(cursor.getColumnIndex("introduction"));
                 number=cursor.getString(cursor.getColumnIndex("number"));
                 music_num=cursor.getString(cursor.getColumnIndex("music_num"));
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
