package com.lzp.deeplinkso

import android.content.Context
import android.util.Log
import com.lzp.deeplinkso.bean.DeepLinkSoOption
import com.lzp.deeplinkso.bean.DeepLinkSoParam
import com.lzp.deeplinkso.config.DeepLinkSoConfig
import com.lzp.deeplinkso.constants.DeepLinkSoConstant
import com.lzp.deeplinkso.interceptor.IDeepLinkSoInterceptor
import com.lzp.deeplinkso.listener.IDeepLinkSoListener
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
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
        val start = System.currentTimeMillis()
        try {
            val factory = XmlPullParserFactory.newInstance()
            val xmlPullParser = factory.newPullParser()
            //设置输入的内容
            xmlPullParser.setInput(input, "utf-8")
            //获取当前解析事件，返回的是数字
            var eventType = xmlPullParser.eventType

            // 清除之前的配置
            config.reset()

            // 拦截器
            var interceptors: ArrayList<IDeepLinkSoInterceptor>? = null
            // 创建DeepLinkSoOption
            var option: DeepLinkSoOption? = null
            // 每一个DeepLinkSoOption的参数结合
            var params: ArrayList<DeepLinkSoParam>? = null

            // 开始解析
            while (eventType != (XmlPullParser.END_DOCUMENT)) {
                val nodeName = xmlPullParser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (nodeName) {
                            //设置版本号
                            DeepLinkSoConstant.VERSION -> parseVersionCode(xmlPullParser)
                            // 创建DeepLinkSoOption
                            DeepLinkSoConstant.OPTION -> {
                                option = DeepLinkSoOption()
                            }
                            // 解析Class类名
                            DeepLinkSoConstant.CLAZZ -> {
                                option!!.className = Class.forName(xmlPullParser.nextText())
                            }
                            //解析page
                            DeepLinkSoConstant.PAGE -> {
                                option!!.page = xmlPullParser.nextText()
                            }
                            //解析参数
                            DeepLinkSoConstant.PRAMS -> {
                                params = ArrayList()
                            }
                            //解析参数的key
                            DeepLinkSoConstant.KEY -> {
                                val deepLinkParam = DeepLinkSoParam(
                                        xmlPullParser.getAttributeValue("", DeepLinkSoConstant.TYPE)
                                                ?: DeepLinkSoConstant.STRING,
                                        xmlPullParser.getAttributeValue("", DeepLinkSoConstant.VALUE)
                                )
                                params!!.add(deepLinkParam)
                            }
                            // 拦截器
                            DeepLinkSoConstant.INTERCEPTORS -> {
                                interceptors = ArrayList()
                            }
                            // 解析拦截器
                            DeepLinkSoConstant.INTERCEPTOR -> {
                                try {
                                    interceptors!!.add(Class.forName(xmlPullParser.nextText()).newInstance() as IDeepLinkSoInterceptor)
                                } catch (e: Exception) {
                                    Log.e("DeepLinkSoClient", "${xmlPullParser.nextText()} is not found!!!")
                                }
                            }
                        }
                    }

                    XmlPullParser.END_TAG -> {
                        when (nodeName) {
                            // 设置参数
                            DeepLinkSoConstant.PRAMS -> {
                                option!!.params = params
                            }
                            // 配置项
                            DeepLinkSoConstant.OPTION -> {
                                // 过滤掉page为空的配置
                                if (option!!.page?.trim() ?: "" != "") {
                                    config.addOption(option.page!!, option)
                                }
                            }
                            // 拦截器
                            DeepLinkSoConstant.INTERCEPTORS -> {
                                config.interceptors = interceptors
                            }
                        }
                    }

                }
                //下一个
                eventType = xmlPullParser.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.e("lzp", "${System.currentTimeMillis() - start}")
    }

    /**
     * 解析版本号
     * */
    private fun parseVersionCode(xmlPullParser: XmlPullParser) {
        config.setVersionCode(xmlPullParser.getAttributeValue("", DeepLinkSoConstant.VALUE))
    }

}