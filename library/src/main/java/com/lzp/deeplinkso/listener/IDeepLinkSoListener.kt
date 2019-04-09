package com.lzp.deeplinkso.listener

import android.content.Context
import com.lzp.deeplinkso.bean.DeepLinkSoRequest

/**
 * Created by li.zhipeng on 2018/12/5.
 *
 *      DeepLink跳转回调监听
 */
interface IDeepLinkSoListener {

    /**
     * 第一次初始化
     * */
    fun onFirstInit()

    /**
     * 版本号发生变化的回调，第一个也会被执行
     * */
    fun onVersionChanged(oldVersion: String, newVersion: String)

    /**
     * app被启动
     * */
    fun onLaunch(context: Context)

    /**
     * 打开指定的页面成功
     * */
    fun onDeepLinkSuccess(context: Context, request: DeepLinkSoRequest)

    /**
     * 打开指定的页面失败
     * */
    fun onDeepLinkFailed(context: Context, request: DeepLinkSoRequest, throwable: Throwable)

}