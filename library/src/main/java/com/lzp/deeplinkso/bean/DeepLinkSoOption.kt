package com.lzp.deeplinkso.bean

import com.lzp.deeplinkso.interceptor.IDeepLinkSoInterceptor

/**
 * Created by li.zhipeng on 2018/12/6.
 *
 * DeepLink配置项
 */
open class DeepLinkSoOption {

    var className: Class<*>? = null

    var page: String? = null

    /**
     * 保存配置的参数信息
     * */
    var params: ArrayList<DeepLinkSoParam>? = null

    /**
     * 拦截器
     * */
    var interceptors: ArrayList<IDeepLinkSoInterceptor>? = null

    /**
     * 是否跳过公有Interceptor，默认是false
     * */
    var skipCommonInterceptors = false

}
