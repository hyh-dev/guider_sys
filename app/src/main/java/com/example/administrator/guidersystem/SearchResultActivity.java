package com.example.administrator.guidersystem;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SearchResultActivity extends AppCompatActivity implements View.OnClickListener{
    private MediaPlayer mediaPlayer;
    private String music_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        //获取植物文字介绍信息
        TextView title=(TextView) findViewById(R.id.title);
        TextView content=(TextView)findViewById(R.id.introduction);
        Intent intent=getIntent();
        Bundle bundle=intent.getBundleExtra("data");
        String name=bundle.getString("name");
        String introduction=bundle.getString("introduction");
        music_num=bundle.getString("music_num");
        Log.d("MainActivity",""+music_num);
        title.setText(name);
        content.setText(introduction);
        //音频播放器设置
        Button play=(Button)findViewById(R.id.play);
        Button pause=(Button)findViewById(R.id.pause);
        Button reset=(Button)findViewById(R.id.reset);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        reset.setOnClickListener(this);
        initMediaPlay();
    }
    private void initMediaPlay(){
        try{
            int id=Integer.parseInt(music_num);
            mediaPlayer=MediaPlayer.create(this,id);
        }
        catch (Exception e){
            Log.d("MainActivity",Log.getStackTraceString(e));
        }
    }
    //音乐开始、暂停、重置
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.play:
                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                }
                break;
            case R.id.pause:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                break;
            case R.id.reset:
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.reset();
                    initMediaPlay();
                }
                break;
            default:
                break;
        }
    }

}
