package com.example.administrator.guidersystem.homePage;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

//viewPager轮播的控制类
public class ImageHandler extends Handler {
    public static final int MSG_UPDATE_IMAGE=1; //更新显示的view
    public static final int MSG_KEEP_SILENT=2;  //暂停轮播
    public static final int MSG_BREAK_SILENT=3; //恢复轮播
    public static final int MSG_PAGE_CHANGED=4; //记录最新页号
    public static final long MSG_DELAY=3000; //轮播间隔时间
    private WeakReference<HomeActivity> weakReference;  //使用弱引用避免handler泄露
    private int currentItem=Integer.MAX_VALUE/2;
    public ImageHandler(WeakReference<HomeActivity> wr){
                weakReference=wr;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        HomeActivity homeActivity=weakReference.get();
        if (homeActivity==null){
            return;
        }
        //检查消息队列并移除未发送的消息，这主要是避免在复杂环境下消息出现重复等问题。
        if (homeActivity.handler.hasMessages(MSG_UPDATE_IMAGE)){
            homeActivity.handler.removeMessages(MSG_UPDATE_IMAGE);
        }
        switch (msg.what) {
            case MSG_UPDATE_IMAGE:
                currentItem++;
                homeActivity.viewPager.setCurrentItem(currentItem);
                //准备下次播放
                homeActivity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                break;
            case MSG_KEEP_SILENT:
                //只要不发送消息就暂停了
                break;
            case MSG_BREAK_SILENT:
                homeActivity.handler.sendEmptyMessageDelayed(MSG_UPDATE_IMAGE, MSG_DELAY);
                break;
            case MSG_PAGE_CHANGED:
                //记录当前的页号，避免播放的时候页面显示不正确。
                currentItem = msg.arg1;
                break;
            default:
                break;
        }
    }
}
