package com.example.administrator.guidersystem.homePage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.administrator.guidersystem.BaseActivity;
import com.example.administrator.guidersystem.R;
import com.example.administrator.guidersystem.plantDB.DatabaseManager;
import com.example.administrator.guidersystem.plantDB.MyDatabaseHelper;
import com.example.administrator.guidersystem.plantDB.RecognizeResultActivity;
import com.example.administrator.guidersystem.plantDB.SearchResultActivity;
import com.google.zxing.client.android.CaptureActivity;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class HomeActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private DatabaseManager manager;
    public static MyDatabaseHelper dbHelper;
    private Uri imageUri;
    private GridView grid_photo;
    private List<Icon> iconList=new ArrayList<>();
    public ViewPager viewPager;
    private ViewGroup viewGroup;
    private ImageView imageView;
    private ImageView[] imageViews;
    private ArrayList<View> viewList;
    public ImageHandler handler=new ImageHandler(new WeakReference<HomeActivity>(this));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //初始化图标
        initIcons();
        IconAdapter adapter=new IconAdapter(HomeActivity.this,R.layout.item_grid_icon,iconList);
        grid_photo = (GridView) findViewById(R.id.grid_photo);
        grid_photo.setAdapter(adapter);
        grid_photo.setOnItemClickListener(this);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        viewGroup=(ViewGroup)findViewById(R.id.viewGroup);
        //初始化viewPager
        initViewPage();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int arg0) {
                handler.sendMessage(Message.obtain(handler,ImageHandler.MSG_PAGE_CHANGED,arg0,0));
                int j=arg0%viewList.size();
                for(int i=0;i<imageViews.length;i++){
                    imageViews[j].setBackgroundResource(R.drawable.circle_shape2);
                    if (j != i) {
                        imageViews[i].setBackgroundResource(R.drawable.circle_shape);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                switch (i) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        handler.sendEmptyMessage(ImageHandler.MSG_KEEP_SILENT);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        handler.sendEmptyMessage(ImageHandler.MSG_BREAK_SILENT);
                        break;
                    default:
                        break;
                }
            }
        });
        viewPager.setCurrentItem(Integer.MAX_VALUE/2);//让第一张图片在中间
        //开始轮播效果
        handler.sendMessage(Message.obtain(handler,ImageHandler.MSG_PAGE_CHANGED,Integer.MAX_VALUE/2,0));
        handler.sendEmptyMessage(ImageHandler.MSG_BREAK_SILENT);
        //数据库相关操作
        dbHelper=new MyDatabaseHelper(this,"PlantContent.db",null,2);
        dbHelper.getWritableDatabase();//创建数据库
        manager=new DatabaseManager(dbHelper);
        manager.insertDB();//添加数据到数据库
        //申请权限
        applyPermissions();
        //推荐植物设置
        ImageView myImage=(ImageView)findViewById(R.id.recommend_picture);
        String imageUrl="https://img.ivsky.com/img/tupian/pre/201809/18/yinghua.jpg";
        Glide.with(this).load(imageUrl).into(myImage);
        TextView myText=(TextView)findViewById(R.id.recommend_bottom);
        String text="十日樱花作意开，绕花岂惜日千回。";
        myText.setText(text);
    }
    //初始化图标
    private void initIcons(){
        iconList.add(new Icon(R.mipmap.home_introduction,"园区介绍"));
        iconList.add(new Icon(R.mipmap.home_scan,"扫一扫"));
        iconList.add(new Icon(R.mipmap.home_recognition,"植物识别"));
        iconList.add(new Icon(R.mipmap.home_search_by_num,"数字查询"));
        iconList.add(new Icon(R.mipmap.home_location,"位置信息"));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        switch (iconList.get(position).getiId()){
            case R.mipmap.home_introduction:
                startActivity(new Intent(HomeActivity.this,IntrouductionActivity.class));
                break;
            case R.mipmap.home_scan:
                //二维码识别模块
                Intent intent=new Intent(HomeActivity.this,CaptureActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.mipmap.home_recognition:
                //植物识别模块
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (Build.VERSION.SDK_INT >= 24) {
                    imageUri = FileProvider.getUriForFile(HomeActivity.this,
                            "com.example.administrator.guidersystem.fileprovider",
                            outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                Intent intent1 = new Intent("android.media.action.IMAGE_CAPTURE");
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent1, 2);
                break;
            case R.mipmap.home_search_by_num:
               // 数字查询模块
                HomeActivity.Dialog dialog=new HomeActivity.Dialog(HomeActivity.this);
                v= View.inflate(this, R.layout.alert_dialog, null);
                dialog.setView(v);
                dialog.show();
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
        viewList =new ArrayList<View>();
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        //设置点点
        imageViews=new ImageView[viewList.size()];
        for(int i =0;i<viewList.size();i++) {
            imageView = new ImageView(HomeActivity.this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(20, 20));
            LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(imageView.getLayoutParams());
            lp.setMargins(20,0,20,0);
            imageView.setLayoutParams(lp);
            imageViews[i] = imageView;

            //默认第一张图显示为选中状态
            if (i == 0) {
                imageViews[i].setBackgroundResource(R.drawable.circle_shape2);
            } else {
                imageViews[i].setBackgroundResource(R.drawable.circle_shape);
            }

            viewGroup.addView(imageViews[i]);
        }

        PagerAdapter pagerAdapter=new PagerAdapter() {
            @Override
            //获取当前窗体界面数
            public int getCount() {
                return Integer.MAX_VALUE;//设置为最大
            }

            @Override
            //断是否由对象生成界面
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view==o;
            }

            @Override
            //从ViewGroup中移出当前View
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            }

            @NonNull
            @Override
            //返回一个对象，这个对象表明了PagerAdapter适配器选择哪个对象放在当前的ViewPager中
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                position %=viewList.size();
                if (position<0){
                    position=viewList.size()+position;
                }
                View view=viewList.get(position);
                ViewParent viewParent=view.getParent();
                if (viewParent!=null){
                    ViewGroup parent=(ViewGroup)viewParent;
                    parent.removeView(view);
                }
                container.addView(view);
                return view;
            }
        };
        viewPager.setAdapter(pagerAdapter);
    }

    //数字搜索对话框内部类
    public class Dialog extends AlertDialog {
        private Button confirm;
        private Button cancel;
        private EditText input_num;
        public Dialog(Context context, int theme) {
            super(context, theme);
        }
        public Dialog(Context context) {
            super(context);
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            confirm=(Button)findViewById(R.id.confirm);
            cancel=(Button)findViewById(R.id.cancel);
            input_num=(EditText) findViewById(R.id.input_number);
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String num=input_num.getText().toString();
                    query(num);
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    //动态权限申请
    private void applyPermissions(){
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(HomeActivity.this,Manifest.permission.CAMERA)!=
                PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(HomeActivity.this, permissions, 1);
        }
    }

    //动态权限响应
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    //通过编号查询结果
    public void query(String num){
        try {
            manager.setNumber("a");//保证number不为空
            manager.queryDB(num);
            if (!manager.getNumber().equals(num)) {
                //Toast.makeText(HomeActivity.this, "不存在此编号", Toast.LENGTH_SHORT).show();
                String pattern=".*weixin.*";
                boolean isMatch=Pattern.matches(pattern,num);
                if (isMatch==true){
                    Toast.makeText(HomeActivity.this,"请使用微信打开",Toast.LENGTH_SHORT).show();
                }
                else {
                    //跳转网页
                    Intent intent = new Intent(HomeActivity.this, QRcodeResult.class);
                    intent.putExtra("link", num);
                    startActivity(intent);
                }
            }
            else {
                //在数据库中查询相关信息
                Intent intent = new Intent(HomeActivity.this, SearchResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", manager.getName());
                bundle.putString("introduction", manager.getIntroduction());
                bundle.putString("music_num", manager.getMusic_num());
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.d("MainActivity", Log.getStackTraceString(e));
        }
    }

    //二维码扫描和植物识别结果响应
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode==RESULT_OK) {
                    String returnData = data.getStringExtra("data");
                    query(returnData);
                }
                break;
            case 2:
                String path="/sdcard/Android/data/com.example.administrator.guidersystem/cache/output_image.jpg";
                File image=new File(path);
                if (image.exists()&&image.length()==0) {
                    //图片内容为空
                break;
                }
                else {
                    Intent intent = new Intent(HomeActivity.this, RecognizeResultActivity.class);
                    intent.putExtra("image", path);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }
}
