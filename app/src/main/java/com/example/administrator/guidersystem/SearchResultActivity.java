package com.example.administrator.guidersystem;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SearchResultActivity extends BaseActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener{
    private MediaPlayer mediaPlayer;
    private String music_num;
    private static SeekBar seekBar;
    private Boolean bl;
    private Boolean flag;
    private Button play_pause;
    private static TextView time_text;
    private Handler mHandler;
    private static final int milliseconds = 50;
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
        title.setText(name);
        content.setText(introduction);
        //音频播放器设置
        play_pause=(Button)findViewById(R.id.play_pause);
        seekBar=(SeekBar) findViewById(R.id.seekBar);
        time_text=(TextView)findViewById(R.id.time);
        play_pause.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(this);
        initMediaPlay();
         mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        //更新进度
                        int position = mediaPlayer.getCurrentPosition();
                        int time = mediaPlayer.getDuration();
                        int max = seekBar.getMax();
                        seekBar.setProgress(position * max / time);
                        time_text.setText(timeParse(position)+"/"+timeParse(time));
                        Log.d("MainActivity",""+position);
                        break;
                    default:
                        break;
                }

            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.reset();
        flag=false;
    }

    private void initMediaPlay(){
        try{
            int id=Integer.parseInt(music_num);
            mediaPlayer=MediaPlayer.create(this,id);
            bl=true;
            play_pause.setBackgroundResource(R.drawable.play_circle);
        }
        catch (Exception e){
            Log.d("MainActivity",Log.getStackTraceString(e));
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                flag = false;
                mediaPlayer.reset();
                seekBar.setProgress(0);
                initMediaPlay();
            }
        });
    }


    //音乐开始、暂停、重置
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.play_pause:
                if (bl==true){
                    if(!mediaPlayer.isPlaying()){
                        mediaPlayer.start();
                        flag=true;
                        //使用线程更新播放进度
                        new Thread() {
                            @Override
                            public void run() {
                                while (flag) {
                                    try {
                                        sleep(milliseconds);
                                    } catch (InterruptedException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }

                                    mHandler.sendEmptyMessage(0);
                                }
                            }
                        }.start();
                        bl=false;
                        play_pause.setBackgroundResource(R.drawable.time_out);
                    }
                }
                else if (bl==false) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        bl=true;
                        play_pause.setBackgroundResource(R.drawable.play_circle);
                    }
                }
                break;
            default:
                break;
        }
    }

    //数值改变
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {
    }
    //开始拖动
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    //停止拖动
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //手动调节进度
        // TODO Auto-generated method stub
        int dest = seekBar.getProgress();
        int time = mediaPlayer.getDuration();
        int max = seekBar.getMax();
        mediaPlayer.seekTo(time*dest/max);
        }
    /**
     * Android 音乐播放器应用里，读出的音乐时长为 long 类型以毫秒数为单位，例如：将 234736 转化为分钟和秒应为 03:55 （包含四舍五入）
     * @param duration 音乐时长
     * @return
     */
    public static String timeParse(long duration) {
        String time = "" ;
        long minute = duration / 60000 ;
        long seconds = duration % 60000 ;
        long second = Math.round((float)seconds/1000) ;
        if( minute < 10 ){
            time += "0" ;
        }
        time += minute+":" ;
        if( second < 10 ){
            time += "0" ;
        }
        time += second ;
        return time ;
    }
}
