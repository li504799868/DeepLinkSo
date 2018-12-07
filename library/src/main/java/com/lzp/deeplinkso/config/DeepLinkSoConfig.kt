package com.lzp.deeplinkso.config

import com.lzp.deeplinkso.listener.IDeepLinkSoListener
import com.lzp.deeplinkso.bean.DeepLinkSoOption
import com.lzp.deeplinkso.interceptor.IDeepLinkSoInterceptor

/**
 * Created by li.zhipeng on 2018/12/4.
 *
 *      深度链接配置文件
 */
class DeepLinkSoConfig {

    /**
     * 当前版本号
     *
     *  无实际作用，仅仅是为了区别xml的版本
     * */
    private var version = "0"

    internal var listener: IDeepLinkSoListener? = null

    /**
     * 保存DeepLink配置项
     * */
    private val optionMap = HashMap<String, DeepLinkSoOption>()

    /**
     * 自定义拦截器
     * */
    internal var interceptors: ArrayList<IDeepLinkSoInterceptor>? = null

    internal fun setVersionCode(version: String) {
        this.version = version
    }

    fun getVersionCode() = this.version

    /**
     *  添加配置项
     * */
    internal fun addOption(key: String, option: DeepLinkSoOption) {
        optionMap[key] = option
    }

    /**
     *  添加配置项
     * */
    internal fun getOption(key: String) = optionMap[key]

    /**
     * 移除配置项
     * */
    internal fun removeOption(key: String) {
        optionMap.remove(key)
    }

    /**
     * 清楚配置项
     * */
    internal fun reset(){
        optionMap.clear()
        interceptors?.clear()
    }

}