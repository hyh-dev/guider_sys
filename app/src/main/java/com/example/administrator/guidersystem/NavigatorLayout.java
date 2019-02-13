package com.example.administrator.guidersystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NavigatorLayout extends RelativeLayout implements View.OnClickListener{
    private TextView txt_topbar;
    private TextView txt_homePage;
    private TextView txt_navigator;
    private TextView txt_discovery;
    private TextView txt_mine;
    public NavigatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.navigator_bar,this);
        bindViews();
        if (getContext().getClass().getSimpleName().equals("MainActivity")){
            txt_navigator.setSelected(true);
            txt_topbar.setText(R.string.tab_menu_navigator);
        }
        else if (getContext().getClass().getSimpleName().equals("HomeActivity")){
            txt_homePage.setSelected(true);
            txt_topbar.setText(R.string.tab_menu_home);
        }
        else if (getContext().getClass().getSimpleName().equals("DiscoveryActivity")){
            txt_discovery.setSelected(true);
            txt_topbar.setText(R.string.tab_menu_discovery);
        }
        else if (getContext().getClass().getSimpleName().equals("MineActivity")){
            txt_mine.setSelected(true);
            txt_topbar.setText(R.string.tab_menu_mine);
        }
        else if (getContext().getClass().getSimpleName().equals("SearchResultActivity")){
            txt_navigator.setSelected(true);
            txt_topbar.setText(R.string.introduction);
        }
    }
    private void bindViews() {
        txt_topbar=(TextView)findViewById(R.id.txt_topbar);
        txt_homePage = (TextView) findViewById(R.id.txt_home);
        txt_navigator = (TextView) findViewById(R.id.txt_navigator);
        txt_discovery = (TextView) findViewById(R.id.txt_discovery);
        txt_mine = (TextView) findViewById(R.id.txt_mine);
        txt_homePage.setOnClickListener(this);
        txt_navigator.setOnClickListener(this);
        txt_discovery.setOnClickListener(this);
        txt_mine.setOnClickListener(this);
    }
    //重置所有文本的选中状态
    private void setSelected(){
        txt_homePage.setSelected(false);
        txt_navigator.setSelected(false);
        txt_discovery.setSelected(false);
        txt_mine.setSelected(false);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_home:
                setSelected();
                txt_homePage.setSelected(true);
                Intent intent = new Intent((Activity)getContext(), HomeActivity.class);
                getContext().startActivity(intent);
                ((Activity) getContext()).overridePendingTransition(0,0);
                break;
            case R.id.txt_navigator:
                setSelected();
                txt_navigator.setSelected(true);
                Intent intent1 = new Intent((Activity)getContext(), MainActivity.class);
                getContext().startActivity(intent1);
                ((Activity) getContext()).overridePendingTransition(0,0);
                break;
            case R.id.txt_discovery:
                setSelected();
                txt_discovery.setSelected(true);
                Intent intent2 = new Intent((Activity)getContext(), DiscoveryActivity.class);
                getContext().startActivity(intent2);
                ((Activity) getContext()).overridePendingTransition(0,0);
                break;
            case R.id.txt_mine:
                setSelected();
                txt_mine.setSelected(true);
                Intent intent3 = new Intent((Activity)getContext(), MineActivity.class);
                getContext().startActivity(intent3);
                ((Activity) getContext()).overridePendingTransition(0,0);
                break;
        }
    }
}
