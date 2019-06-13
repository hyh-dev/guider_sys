package com.example.administrator.guidersystem.homePage;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.guidersystem.plantDB.RecognizeResultActivity;

import java.lang.ref.WeakReference;

public class RecognitionHandler extends Handler {
    private TextView description;
    private ProgressBar progressBar;
    private ImageView resultImage;
    private Context mContext;
    private WeakReference<RecognizeResultActivity> weakReference;  //使用弱引用避免handler泄露
    public RecognitionHandler(WeakReference<RecognizeResultActivity> wr,TextView description,ProgressBar progressBar,
                              ImageView resultImage, Context mContext){
        weakReference=wr;
        this.description=description;
        this.progressBar=progressBar;
        this.resultImage=resultImage;
        this.mContext=mContext;
    }

    @Override
    public void handleMessage(Message msg) {
        switch(msg.what){
            case 1:
                Bundle bundle=msg.getData();
                Glide.with(mContext).load(bundle.getString("picture")).into(resultImage);
                description.setText("名称："+bundle.getString("name")+"\n"+
                        "相似度："+String.format("%.2f",Double.parseDouble(bundle.getString("score"))));
                if (description.getText()!=null){
                    progressBar.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }
}
