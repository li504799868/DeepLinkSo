package com.lzp.deeplinkso.listener

import android.content.Context
import com.lzp.deeplinkso.bean.DeepLinkSoOption

/**
 * Created by li.zhipeng on 2018/12/5.
 *
 *      DeepLink跳转回调监听
 */
interface IDeepLinkSoListener {

    /**
     * app被启动
     * */
    fun onLaunch(context: Context)

    /**
     * 打开指定的页面成功
     * */
    fun onDeepLinkSuccess(context: Context, option: DeepLinkSoOption, params: HashMap<String, Any>)

    /**
     * 打开指定的页面失败
     * */
    fun onDeepLinkFailed(context: Context, throwable: Throwable, option: DeepLinkSoOption)

}