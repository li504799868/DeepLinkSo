package com.lzp.deeplinkso.demo;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.lzp.deeplinkso.bean.DeepLinkSoOption;
import com.lzp.deeplinkso.listener.IDeepLinkSoListener;
import com.lzp.deeplinkso.DeepLinkSoClient;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

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
    public void onDeepLinkSuccess(@NotNull Context context, @NotNull DeepLinkSoOption option, @NotNull HashMap<String, Object> params) {
        Toast.makeText(context, "onDeepLinkSuccess：" + option.getPage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeepLinkFailed(@NotNull Context context, @NotNull Throwable throwable, @NotNull DeepLinkSoOption option) {
        Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }


}
