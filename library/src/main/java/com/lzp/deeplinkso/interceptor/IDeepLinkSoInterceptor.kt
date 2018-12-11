package com.lzp.deeplinkso.interceptor

import android.content.Context
import com.lzp.deeplinkso.bean.DeepLinkSoRequest

/**
 * Created by li.zhipeng on 2018/12/6.
 *
 *      拦截器接口
 */
interface IDeepLinkSoInterceptor {

    /**
     * @param context 中转页面Context
     * @param request 跳转请求
     *
     * @return true表示拦截，不会发生页面跳转
     * */
    fun intercept(context: Context, request: DeepLinkSoRequest): Boolean
}