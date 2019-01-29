package com.example.administrator.guidersystem;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button scan;
    private Button searchByNumber;
    private DatabaseManager manager;
    private MyDatabaseHelper dbHelper;
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
                    manager.queryDB(num);
                    if(!manager.getNumber().equals(num)){
                        Toast.makeText(MainActivity.this, "不存在此编号", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                        Bundle bundle=new Bundle();
                        bundle.putString("name",manager.getName());
                        bundle.putString("introduction",manager.getIntroduction());
                        bundle.putString("music_num",manager.getMusic_num());
                        intent.putExtra("data",bundle);
                        startActivity(intent);
                    }
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
        dbHelper=new MyDatabaseHelper(this,"PlantContent.db",null,1);
        dbHelper.getWritableDatabase();//创建数据库
        manager=new DatabaseManager(dbHelper);
        manager.insertDB();//添加数据到数据库
        //动态权限申请
        if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA)!=
                PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA},1);
        }
        else {
            scan.setOnClickListener(this);
        }
    }
    //动态权限响应
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    scan.setOnClickListener(this);
                }
                else {
                    Toast.makeText(this,"拒绝权限将无法使用程序",Toast.LENGTH_SHORT).show();
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
                startActivity(intent);
                break;
            case R.id.searchByNumber:
                Dialog dialog=new Dialog(MainActivity.this);
                v= View.inflate(this, R.layout.alert_dialog, null);
                dialog.setView(v);
                dialog.show();
                break;
                default:
                    break;
        }
    }
}
