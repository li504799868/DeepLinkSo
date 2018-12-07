package com.lzp.deeplinkso.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // 启动DeepLink跳转，如果之前有保存跳转信息，会打开指定的页面
        DeepLinkManager.INSTANCE.startActivity(this);
    }
}
