package com.example.administrator.guidersystem;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DiscoveryActivity extends BaseActivity {
    private  List<Discovery> discoveryList=new ArrayList<>();
    private String responseData;
    private ListView listView;
    private DiscoveryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);
        initDiscoveries();
        adapter = new DiscoveryAdapter(DiscoveryActivity.this, R.layout.discovery_item, discoveryList);
        listView = (ListView) findViewById(R.id.list_view);
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
            Discovery discovery=new Discovery(plantBean.name,plantBean.coverURL);
            discoveryList.add(discovery);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    listView.setAdapter(adapter);
                    break;
                default:
                    break;
            }
        }
    };
}
