package com.lzp.deeplinkso

import android.content.Context
import com.lzp.deeplinkso.config.DeepLinkSoConfig
import com.lzp.deeplinkso.listener.IDeepLinkSoListener
import com.lzp.deeplinkso.parse.DeepLinkSoParser
import java.io.FileInputStream
import java.io.InputStream
import java.lang.IllegalArgumentException

/**
 * Created by li.zhipeng on 2018/12/5.
 *
 *      DeepLink客户端管理类，复杂暴露初始化和暴露服务
 */
object DeepLinkSoClient {

    val config = DeepLinkSoConfig()

    @JvmStatic
    fun builder() = Builder()

    /**
     * 尽可能早的设置Listener，推荐在Application中
     * */
    private fun init(context: Context, deepLinkListener: IDeepLinkSoListener?) {
        // 默认加载DeepLinkSo.xml配置文件
        init(deepLinkListener, context.resources.assets.open("DeepLinkSo.xml"))
    }

    /**
     * 尽可能早的设置Listener，推荐在Application中
     * */
    private fun init(path: String, deepLinkListener: IDeepLinkSoListener?) {
        // 默认加载DeepLinkSo.xml配置文件
        init(deepLinkListener, FileInputStream(path))
    }

    /**
     * 尽可能早的设置Listener，推荐在Application中
     *
     * @param input 支持传递文件流，对配置进行更新
     * */
    private fun init(deepLinkListener: IDeepLinkSoListener?, input: InputStream) {
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

    /**
     * 构造方法不对外开放
     * */
    class Builder internal constructor() {

        private var context: Context? = null
        private var deepLinkListener: IDeepLinkSoListener? = null
        private var path: String? = null

        fun setContext(context: Context): Builder {
            this.context = context
            return this
        }

        fun setDeepLinkSoListener(deepLinkListener: IDeepLinkSoListener): Builder {
            this.deepLinkListener = deepLinkListener
            return this
        }

        fun setXMLPath(path: String): Builder {
            this.path = path
            return this
        }

        fun build() {
            // 如果没有设置外部的XML路径，使用默认的xml
            if (path.isNullOrEmpty()) {
                // 必须要设置context，否则抛出异常
                if (context == null) {
                    throw IllegalArgumentException("if path is null, you must set context!!!")
                }
                // 使用默认路径XML初始化
                DeepLinkSoClient.init(context!!, deepLinkListener)
            } else {
                // 使用指定路径XML初始化
                DeepLinkSoClient.init(path!!, deepLinkListener)
            }


        }

    }

}