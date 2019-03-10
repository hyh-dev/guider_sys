package com.example.administrator.guidersystem.minePage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.aip.client.BaseClient;
import com.baidu.aip.imageclassify.AipImageClassify;
import com.baidu.navisdk.ui.routeguide.mapmode.subview.B;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.example.administrator.guidersystem.BaseActivity;
import com.example.administrator.guidersystem.R;
import com.example.administrator.guidersystem.discoveryPage.Bean;
import com.example.administrator.guidersystem.discoveryPage.Discovery;
import com.example.administrator.guidersystem.homePage.LocationActivity;
import com.example.administrator.guidersystem.navigationPage.MainActivity;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.baidu.mapframework.commonlib.utils.IOUitls.readFile;

public class MineActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
    }

}
