package com.lzp.deeplinkso.parse

import android.util.Log
import com.lzp.deeplinkso.DeepLinkSoClient
import com.lzp.deeplinkso.bean.DeepLinkSoActivityOption
import com.lzp.deeplinkso.bean.DeepLinkSoEventOption
import com.lzp.deeplinkso.bean.DeepLinkSoOption
import com.lzp.deeplinkso.bean.DeepLinkSoParam
import com.lzp.deeplinkso.constants.DeepLinkSoConstant
import com.lzp.deeplinkso.interceptor.IDeepLinkSoInterceptor
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream

/**
 * Created by li.zhipeng on 2018/12/11.
 *
 *      DeepLinkSo配置文件解析器
 */
object DeepLinkSoParser {

    fun parse(input: InputStream) {
        val start = System.currentTimeMillis()
        try {
            val factory = XmlPullParserFactory.newInstance()
            val xmlPullParser = factory.newPullParser()
            //设置输入的内容
            xmlPullParser.setInput(input, "utf-8")
            //获取当前解析事件，返回的是数字
            var eventType = xmlPullParser.eventType

            // 清除之前的配置
            DeepLinkSoClient.config.reset()

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
                            // 设置版本号
                            DeepLinkSoConstant.VERSION -> parseVersionCode(xmlPullParser)
                            // 公共拦截器
                            DeepLinkSoConstant.COMMON_INTERCEPTORS -> {
                                interceptors = ArrayList()
                            }
                            // 公共拦截器
                            DeepLinkSoConstant.COMMON_INTERCEPTOR -> {
                                try {
                                    interceptors!!.add(Class.forName(xmlPullParser.nextText()).newInstance() as IDeepLinkSoInterceptor)
                                } catch (e: Exception) {
                                    Log.e("DeepLinkSoClient", "${xmlPullParser.nextText()} is not found!!!")
                                }
                            }
                            // 解析Activity
                            DeepLinkSoConstant.ACTIVITY -> {
                                option = DeepLinkSoActivityOption()
                            }
                            // 解析Event
                            DeepLinkSoConstant.EVENT -> {
                                option = DeepLinkSoEventOption()
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
                            // 是否跳过公共拦截器
                            DeepLinkSoConstant.SKIP_COMMON_INTERCEPTOR -> {
                                option!!.skipCommonInterceptors = xmlPullParser.nextText()!!.toBoolean()
                            }
                            // 拦截器
                            DeepLinkSoConstant.INTERCEPTORS -> {
                                interceptors = ArrayList()
                            }
                            // 拦截器
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
                            // 公共拦截器
                            DeepLinkSoConstant.COMMON_INTERCEPTORS -> {
                                DeepLinkSoClient.config.interceptors = interceptors
                            }
                            // 配置项
                            DeepLinkSoConstant.ACTIVITY, DeepLinkSoConstant.EVENT -> {
                                // 过滤掉page为空的配置
                                if (option!!.page?.trim() ?: "" != "") {
                                    DeepLinkSoClient.config.addOption(option.page!!, option)
                                }
                            }
                            // 设置参数
                            DeepLinkSoConstant.PRAMS -> {
                                option!!.params = params
                            }
                            // 私有拦截器
                            DeepLinkSoConstant.INTERCEPTORS -> {
                                option!!.interceptors = interceptors
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
    /**
     * 解析版本号
     * */
    private fun parseVersionCode(xmlPullParser: XmlPullParser) {
        DeepLinkSoClient.config.setVersionCode(xmlPullParser.getAttributeValue("", DeepLinkSoConstant.VALUE))
    }
}