package com.lzp.deeplinkso.demo;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.lzp.deeplinkso.DeepLinkSoClient;
import com.lzp.deeplinkso.bean.DeepLinkSoRequest;
import com.lzp.deeplinkso.listener.IDeepLinkSoListener;

import org.jetbrains.annotations.NotNull;

/**
 * Created by li.zhipeng on 2018/12/6.
 */
public class MyApplication extends Application implements IDeepLinkSoListener {

    @Override
    public void onCreate() {
        super.onCreate();

        DeepLinkSoClient.INSTANCE.init(this, this);
        // 申请读写内存卡权限
//        AndPermission.with(this).runtime()
//                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                .onGranted(new Action<List<String>>() {
//                    @Override
//                    public void onAction(List<String> data) {
//                        DeepLinkSoClient.INSTANCE.init(MyApplication.this, Environment.getExternalStorageDirectory() + "/DeepLinkSo.xml");
//                    }
//                })
//                .start();
    }

    @Override
    public void onLaunch(@NotNull Context context) {

    }

    @Override
    public void onDeepLinkSuccess(@NotNull Context context, @NotNull DeepLinkSoRequest request) {
        Toast.makeText(context, "onDeepLinkSuccess：" + request.getUri(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeepLinkFailed(@NotNull Context context, @NotNull DeepLinkSoRequest request, @NotNull Throwable throwable) {
        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
