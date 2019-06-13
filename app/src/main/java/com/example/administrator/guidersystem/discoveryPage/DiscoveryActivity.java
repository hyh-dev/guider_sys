package com.example.administrator.guidersystem.discoveryPage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.guidersystem.BaseActivity;
import com.example.administrator.guidersystem.R;
import com.example.administrator.guidersystem.homePage.HomeActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DiscoveryActivity extends BaseActivity {
    private  List<Discovery> discoveryList=new ArrayList<>();
    private String responseData;
    private RecyclerView recyclerView;
    private DiscoveryAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private SQLiteDatabase db;
    private Discovery discovery;
    private int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //刷新页面内容
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                discoveryList.clear();
                int page=(int)(1+Math.random()*10);
                initDiscoveries(page,30);
                swipeRefresh.setRefreshing(false);
            }
        });
        //设置两列的网格视图
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DiscoveryAdapter(discoveryList);
        //判断网络是否可用
        if (isNetworkConnected(this)==false){
            Toast.makeText(this,"网络连接不可用",Toast.LENGTH_SHORT).show();
        }
        //获取数据库实例
        db=HomeActivity.dbHelper.getWritableDatabase();
        Log.d("DiscoveryActivity",""+db.query("plantOnline",null,null,null,
                null,null,null).getCount());
       //数据数量
        count=db.query("plantOnline",null,null,null,
                null,null,null).getCount();
        if (count==0)//若数据库为空获取植物内容
        {
            Log.d("DiscoveryActivity","数据库为空");
            initDiscoveries(1, 30);
        }
        else{
            Log.d("DiscoveryActivity","数据库不为空");
            queryDB();  //数据库不为空，查询数据库
            recyclerView.setAdapter(adapter);
        }
    }


    private void initDiscoveries(final int page,final int pageSize){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client=new OkHttpClient();
                    Request request=new Request.Builder()
                            .url("https://api.apishop.net/common/plantFamily/queryPlantList?" +
                                    "apiKey=laUuwV4e99fe7400a5ea670e5c6cb78b74c84eeccbe3af4&page="+page+"&pageSize="+pageSize)
                            .build();
                    Response response=client.newCall(request).execute();
                    responseData=response.body().string();
                    parseJSONWithGSON(responseData);
                    mHandler.sendEmptyMessage(0);
                    Log.d("DiscoveryActivity","send message");
                }catch (Exception e){
                    Log.d("DiscoveryActivity",Log.getStackTraceString(e));
                }
            }
        }).start();
    }
    private void parseJSONWithGSON(String jsonData){
        Gson gson=new Gson();
        List<Bean.PlantBean> resultList=gson.fromJson(jsonData,Bean.class).result.plantList;
        for (Bean.PlantBean plantBean:resultList){
            Discovery discovery=new Discovery(plantBean.name,plantBean.coverURL,plantBean.area,plantBean.engName);
            discoveryList.add(discovery);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    recyclerView.setAdapter(adapter);
                    if (count==0) {
                        insertDB(discoveryList);    //把获取到的数据保存在数据库中
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isConnected();
            }
        }
        return false;
    }
    private void insertDB(List<Discovery> discoveryList){
        for (Discovery discovery:discoveryList){
            ContentValues values = new ContentValues();
            values.put("name", discovery.getName());
            values.put("imageID", discovery.getImageId());
            values.put("area", discovery.getArea());
            values.put("engName", discovery.getEngName());
            db.insert("plantOnline", null, values);
            values.clear();
        }
    }
    private void queryDB(){
        Cursor cursor=db.query("plantOnline",null,null,null,null,null,null);
        if (cursor.moveToFirst()) {
            do {
                Discovery discovery=new Discovery(cursor.getString(cursor.getColumnIndex("name")),
                        cursor.getString(cursor.getColumnIndex("imageID")),
                        cursor.getString(cursor.getColumnIndex("area")),
                        cursor.getString(cursor.getColumnIndex("engName"))
                );
                discoveryList.add(discovery);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
