package com.lzp.deeplinkso.interceptor

import android.content.Context
import com.lzp.deeplinkso.bean.DeepLinkSoOption

/**
 * Created by li.zhipeng on 2018/12/6.
 *
 *      拦截器接口
 */
interface IDeepLinkSoInterceptor {

    /**
     * @param context 中转页面Context
     * @param option xml中的跳转配置
     * @param params 跳转所需的参数
     *
     * @return true表示拦截，不会发生页面跳转
     * */
    fun intercept(context: Context, option: DeepLinkSoOption, params: HashMap<String, Any>): Boolean
}