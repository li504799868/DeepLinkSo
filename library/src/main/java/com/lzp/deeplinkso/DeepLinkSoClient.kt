package com.lzp.deeplinkso

import android.content.Context
import com.lzp.deeplinkso.config.DeepLinkSoConfig
import com.lzp.deeplinkso.listener.IDeepLinkSoListener
import com.lzp.deeplinkso.parse.DeepLinkSoParser
import java.io.FileInputStream
import java.io.InputStream

/**
 * Created by li.zhipeng on 2018/12/5.
 *
 *      DeepLink客户端管理类，复杂暴露初始化和暴露服务
 */
object DeepLinkSoClient {

    val config = DeepLinkSoConfig()

    /**
     * 尽可能早的设置Listener，推荐在Application中
     * */
    fun init(context: Context, deepLinkListener: IDeepLinkSoListener) {
        // 默认加载DeepLinkSo.xml配置文件
        init(deepLinkListener, context.resources.assets.open("DeepLinkSo.xml"))
    }

    /**
     * 尽可能早的设置Listener，推荐在Application中
     * */
    fun init(deepLinkListener: IDeepLinkSoListener, path: String) {
        // 默认加载DeepLinkSo.xml配置文件
        init(deepLinkListener, FileInputStream(path))
    }

    /**
     * 尽可能早的设置Listener，推荐在Application中
     *
     * @param input 支持传递文件流，对配置进行更新
     * */
    private fun init(deepLinkListener: IDeepLinkSoListener, input: InputStream) {
        config.listener = deepLinkListener
        // 开始解析xml配置文件
        parseXml(input)

    }

    /**
     * 解析xml配置文件
     * */
    private fun parseXml(input: InputStream) {
        DeepLinkSoParser.parse(input)
    }

}