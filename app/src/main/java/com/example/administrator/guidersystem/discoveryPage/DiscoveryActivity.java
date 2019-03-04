package com.example.administrator.guidersystem.discoveryPage;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.administrator.guidersystem.BaseActivity;
import com.example.administrator.guidersystem.R;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);
        initDiscoveries();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DiscoveryAdapter(discoveryList);
        if (isNetworkConnected(this)==false){
            Toast.makeText(this,"网络连接不可用",Toast.LENGTH_SHORT).show();
        }
    }


    private void initDiscoveries(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    OkHttpClient client=new OkHttpClient();
                    Request request=new Request.Builder()
                            .url("https://api.apishop.net/common/plantFamily/queryPlantList?apiKey=laUuwV4e99fe7400a5ea670e5c6cb78b74c84eeccbe3af4&page=1&pageSize=30")
                            .build();
                    Response response=client.newCall(request).execute();
                    responseData=response.body().string();
                    parseJSONWithGSON(responseData);
                    mHandler.sendEmptyMessage(0);
                }catch (Exception e){
                    e.printStackTrace();
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
}
