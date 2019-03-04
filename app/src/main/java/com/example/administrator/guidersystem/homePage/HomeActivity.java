package com.example.administrator.guidersystem.homePage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.administrator.guidersystem.BaseActivity;
import com.example.administrator.guidersystem.navigationPage.MainActivity;
import com.example.administrator.guidersystem.R;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private GridView grid_photo;
    private List<Icon> iconList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initIcons();
        IconAdapter adapter=new IconAdapter(HomeActivity.this,R.layout.item_grid_icon,iconList);
        grid_photo = (GridView) findViewById(R.id.grid_photo);
        grid_photo.setAdapter(adapter);
        grid_photo.setOnItemClickListener(this);
    }
    private void initIcons(){
        iconList.add(new Icon(R.mipmap.home_introduction,"园区介绍"));
        iconList.add(new Icon(R.mipmap.home_navigation,"参观导览"));
        iconList.add(new Icon(R.mipmap.home_transportation,"园区交通"));
        iconList.add(new Icon(R.mipmap.home_phone,"联系方式"));
        iconList.add(new Icon(R.mipmap.home_notice,"参观须知"));
        iconList.add(new Icon(R.mipmap.home_location,"位置信息"));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (iconList.get(position).getiId()){
            case R.mipmap.home_introduction:
                startActivity(new Intent(HomeActivity.this,IntrouductionActivity.class));
                break;
            case R.mipmap.home_navigation:
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
                break;
            case R.mipmap.home_transportation:
                startActivity(new Intent(HomeActivity.this,TransportationActivity.class));
                break;
            case R.mipmap.home_phone:
                startActivity(new Intent(HomeActivity.this,PhoneActivity.class));
                break;
            case R.mipmap.home_notice:
                startActivity(new Intent(HomeActivity.this,NoticeActivity.class));
                break;
            case R.mipmap.home_location:
                startActivity(new Intent(HomeActivity.this,LocationActivity.class));
                break;
                default:
                    break;
        }
    }
}
