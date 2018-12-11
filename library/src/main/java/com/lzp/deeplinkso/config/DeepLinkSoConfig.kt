package com.lzp.deeplinkso.config

import com.lzp.deeplinkso.bean.DeepLinkSoActivityOption
import com.lzp.deeplinkso.bean.DeepLinkSoEventOption
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
     * 保存跳转Activity配置项
     * */
    private val activityOptionMap = HashMap<String, DeepLinkSoActivityOption>()

    /**
     * 保存自定义事件配置项
     * */
    private val eventOptionMap = HashMap<String, DeepLinkSoEventOption>()

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
        when (option) {
            is DeepLinkSoActivityOption -> activityOptionMap[key] = option
            is DeepLinkSoEventOption -> eventOptionMap[key] = option
        }
    }

    /**
     *  添加配置项
     * */
    internal fun getOption(key: String): DeepLinkSoOption? {
        return activityOptionMap[key] ?: eventOptionMap[key]
    }

    /**
     *  获取Activity配置项
     * */
    internal fun getActivityOption(key: String) = activityOptionMap[key]

    /**
     *  获取Event配置项
     * */
    internal fun getEventOption(key: String) = eventOptionMap[key]

    /**
     * 清楚配置项
     * */
    internal fun reset() {
        activityOptionMap.clear()
        eventOptionMap.clear()
        interceptors?.clear()
    }

}