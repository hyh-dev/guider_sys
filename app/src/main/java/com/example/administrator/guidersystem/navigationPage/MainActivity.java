package com.example.administrator.guidersystem.navigationPage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.guidersystem.BaseActivity;
import com.example.administrator.guidersystem.R;
import com.example.administrator.guidersystem.homePage.LocationActivity;
import com.google.zxing.client.android.CaptureActivity;

import org.apache.log4j.chainsaw.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private Button scan;
    private Button searchByNumber;
    private Button recognize;
    private DatabaseManager manager;
    private MyDatabaseHelper dbHelper;
    private Uri imageUri;
    //数字搜索对话框内部类
    public class Dialog extends AlertDialog{
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scan=(Button)findViewById(R.id.scan_QRcode);
        searchByNumber=(Button)findViewById(R.id.searchByNumber);
        searchByNumber.setOnClickListener(this);
        recognize=(Button)findViewById(R.id.recognize);
        dbHelper=new MyDatabaseHelper(this,"PlantContent.db",null,1);
        dbHelper.getWritableDatabase();//创建数据库
        manager=new DatabaseManager(dbHelper);
        manager.insertDB();//添加数据到数据库
        //动态权限申请
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA)!=
                PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            scan.setOnClickListener(this);
            recognize.setOnClickListener(this);
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
                    scan.setOnClickListener(this);
                    recognize.setOnClickListener(this);
                } else {
                        Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                break;
            default:
                break;
        }
    }
    //点击事件处理
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.scan_QRcode:
                Intent intent=new Intent(MainActivity.this,CaptureActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.searchByNumber:
                Dialog dialog=new Dialog(MainActivity.this);
                v= View.inflate(this, R.layout.alert_dialog, null);
                dialog.setView(v);
                dialog.show();
                break;
            case R.id.recognize:
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
                    imageUri = FileProvider.getUriForFile(MainActivity.this, "com.example.administrator.guidersystem.fileprovider",
                            outputImage);
                } else {
                    imageUri = Uri.fromFile(outputImage);
                }
                Intent intent1 = new Intent("android.media.action.IMAGE_CAPTURE");
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent1, 2);
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
                Toast.makeText(MainActivity.this, "不存在此编号", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
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

    //二维码扫描结果
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
                Intent intent=new Intent(MainActivity.this,RecognizeResultActivity.class);
                intent.putExtra("image","/sdcard/Android/data/com.example.administrator.guidersystem/cache/output_image.jpg");
                startActivity(intent);
                break;
                default:
                    break;
        }
    }
}
