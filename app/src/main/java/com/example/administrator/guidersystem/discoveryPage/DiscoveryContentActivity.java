package com.example.administrator.guidersystem.discoveryPage;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.administrator.guidersystem.R;

public class DiscoveryContentActivity extends AppCompatActivity {
    public static final String PLANT_NAME="plant_name";
    public static final String PLANT_IMAGE_URL="plant_image_url";
    public static final String PLANT_AREA="plant_area";
    public static final String PLANT_ENGNAME="plant_engName";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_content);
        //接收Intent的数据
        Intent intent=getIntent();
        String plantName=intent.getStringExtra(PLANT_NAME);
        String plantImageUrl=intent.getStringExtra(PLANT_IMAGE_URL);
        String plantArea=intent.getStringExtra(PLANT_AREA);
        String plantEngName=intent.getStringExtra(PLANT_ENGNAME);
        //初始化控件
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        ImageView plantImageView=(ImageView)findViewById(R.id.plant_image_view);
        TextView plantContentText=(TextView)findViewById(R.id.plant_content_text);

        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(plantName);
        Glide.with(this).load(plantImageUrl).into(plantImageView);
        String introduction="英文名:"+plantEngName+"\n"+"地区："+plantArea;
        plantContentText.setText(introduction);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
