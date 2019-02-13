package com.example.administrator.guidersystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {
    public int a=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void onBackPressed() {
        if (a==0){
            Toast.makeText(this,"再按一次退出植物博览系统",Toast.LENGTH_SHORT).show();
        }else if (a>0){
            ActivityCollector.finishAll();
        }
        a++;
    }
}
