package com.example.administrator.guidersystem;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.administrator.guidersystem.homePage.HomeActivity;

public class LaunchActivity extends AppCompatActivity {
    private ImageView imageView;
    private AnimationDrawable anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        imageView=(ImageView)findViewById(R.id.cover);
        anim=(AnimationDrawable)imageView.getBackground();//实例化动画对象
        anim.start();//开始播放动画
        Integer time = 2500;    //设置等待时间，单位为毫秒
        Handler handler = new Handler();
        //当计时结束时，跳转至主界面
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

               startActivity(new Intent(LaunchActivity.this, HomeActivity.class));
                anim.stop();//停止播放动画
                LaunchActivity.this.finish();
            }
        }, time);
    }
}
