package com.example.administrator.guidersystem.navigationPage;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.example.administrator.guidersystem.R;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class RecognizeResultActivity extends AppCompatActivity {
    //设置APPID/AK/SK
    public static final String APP_ID = "15680776";
    public static final String API_KEY = "HWnq018EbEq0fcCtuflHhjLD";
    public static final String SECRET_KEY = "Rrf6ltxQghGbQ2d9zXrGoeRO9Bdyw9Ph";
    private AipImageClassify client;
    private HashMap<String, String> options;
    private String image;
    private TextView description;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognize_result);
        Intent intent=getIntent();
        image=intent.getStringExtra("image");
        description=(TextView)findViewById(R.id.description);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar);
        client=new AipImageClassify(APP_ID,API_KEY,SECRET_KEY);
        // 传入可选参数调用接口
        options = new HashMap<String, String>();
        options.put("baike_num", "1");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject res = client.plantDetect(image, options);
                        Log.d("image",res.toString());
                        parseJSONWithGSON(res.toString());
                    }
                }).start();
    }
    private void parseJSONWithGSON(String jsonData){
        Gson gson=new Gson();
        List<Flower.Flowerresult> resultList=gson.fromJson(jsonData,Flower.class).result;
        Message message=new Message();
        message.what=1;
        Bundle bundle=new Bundle();
        bundle.putString("name",resultList.get(0).name);
        bundle.putString("description",resultList.get(0).baike_info.description);
        message.setData(bundle);
        handler.sendMessage(message);
        Log.d("image",resultList.get(0).name+"\n"+resultList.get(0).baike_info.description);
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle bundle=msg.getData();
                    description.setText(bundle.getString("name")+"\n"+bundle.getString("description"));
                    if (description.getText()!=null){
                        progressBar.setVisibility(View.GONE);
                    }
                    break;
                    default:
                        break;
            }
        }
    };
}
