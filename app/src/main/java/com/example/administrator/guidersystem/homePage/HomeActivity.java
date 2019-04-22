package com.example.administrator.guidersystem.homePage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private ViewPager viewPager;
    private ArrayList<View> pageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initIcons();
        IconAdapter adapter=new IconAdapter(HomeActivity.this,R.layout.item_grid_icon,iconList);
        grid_photo = (GridView) findViewById(R.id.grid_photo);
        grid_photo.setAdapter(adapter);
        grid_photo.setOnItemClickListener(this);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        initViewPage();
    }
    //初始化图标
    private void initIcons(){
        iconList.add(new Icon(R.mipmap.home_introduction,"园区介绍"));
        iconList.add(new Icon(R.mipmap.home_navigation,"参观导览"));
        iconList.add(new Icon(R.mipmap.home_phone,"联系方式"));
        iconList.add(new Icon(R.mipmap.home_notice,"参观须知"));
        iconList.add(new Icon(R.mipmap.home_location,"位置信息"));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (iconList.get(position).getiId()){
            case R.mipmap.home_introduction:
                //startActivity(new Intent(HomeActivity.this,IntrouductionActivity.class));
                break;
            case R.mipmap.home_navigation:
                startActivity(new Intent(HomeActivity.this,MainActivity.class));
                break;
            case R.mipmap.home_phone:
                //startActivity(new Intent(HomeActivity.this,PhoneActivity.class));
                break;
            case R.mipmap.home_notice:
               // startActivity(new Intent(HomeActivity.this,NoticeActivity.class));
                break;
            case R.mipmap.home_location:
                startActivity(new Intent(HomeActivity.this,LocationActivity.class));
                break;
                default:
                    break;
        }
    }
    //加载viewPage
    private void initViewPage(){
        //查找布局文件用LayoutInflater.inflate
        LayoutInflater inflater =getLayoutInflater();
        View view1 = inflater.inflate(R.layout.item01, null);
        View view2 = inflater.inflate(R.layout.item02, null);
        View view3 = inflater.inflate(R.layout.item03, null);
        //将view装入数组
        pageview =new ArrayList<View>();
        pageview.add(view1);
        pageview.add(view2);
        pageview.add(view3);

        PagerAdapter pagerAdapter=new PagerAdapter() {
            @Override
            //获取当前窗体界面数
            public int getCount() {
                return pageview.size();
            }

            @Override
            //断是否由对象生成界面
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view==o;
            }

            @Override
            //是从ViewGroup中移出当前View
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView(pageview.get(position));
            }

            @NonNull
            @Override
            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
             container.addView(pageview.get(position));
             return pageview.get(position);
            }
        };
        viewPager.setAdapter(pagerAdapter);
    }
}
