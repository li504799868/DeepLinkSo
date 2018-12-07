package com.lzp.deeplinkso.activity

import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lzp.deeplinkso.DeepLinkSoClient
import com.lzp.deeplinkso.R
import com.lzp.deeplinkso.bean.DeepLinkSoOption
import com.lzp.deeplinkso.constants.DeepLinkSoConstant
import com.lzp.deeplinkso.exception.DeepLinkSoFailedException
import com.lzp.deeplinkso.utils.DeepLinkSoNavigator
import com.lzp.deeplinkso.utils.Utils

/**
 * DeepLink中转页面
 * */
class DeepLinkSoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deep_link)

        // 首先判断是否是从深度链接跳转过来的
        val uri = intent.data
        if (uri != null) {
            val page = uri.getQueryParameter("page")
            // 启动app
            if (Utils.isEmpty(page)) {
                DeepLinkSoNavigator.launchApp(this)
                return
            }

            // 得到指定的配置信息
            val option = DeepLinkSoClient.config.getOption(page!!)
            val params = queryParams(option!!, uri)

            if (params == null) {
                DeepLinkSoClient.config.listener?.onDeepLinkFailed(this,
                        DeepLinkSoFailedException(DeepLinkSoFailedException.PARAMS_NULL, "param can not be null"),
                        option)
                deepLinkFailed()
                return
            }

            // 判断是否拦截此次跳转
            if (needIntercept(option, params)) {
                DeepLinkSoClient.config.listener?.onDeepLinkFailed(this,
                        DeepLinkSoFailedException(DeepLinkSoFailedException.PARAMS_NULL, "failed by intercept"),
                        option)
                deepLinkFailed()
                return
            }

            DeepLinkSoNavigator.startActivity(this, option, params)
            finish()
        }

    }

    private fun queryParams(option: DeepLinkSoOption, uri: Uri): HashMap<String, Any>? {
        val params = HashMap<String, Any>()
        if (option.params != null && option.params!!.size > 0) {
            for (param in option.params!!) {
                val value = uri.getQueryParameter(param.key)
                // 目前不允许参数为空
                if (Utils.isEmpty(value)) {
                    // 返回null
                    return null
                }
                // 把参数加入到集合中
                if (param.type == DeepLinkSoConstant.INT) {
                    params[param.key] = value!!.toInt()
                } else if (param.type == DeepLinkSoConstant.LONG) {
                    params[param.key] = value!!.toLong()
                } else if (param.type == DeepLinkSoConstant.FLOAT) {
                    params[param.key] = value!!.toFloat()
                } else if (param.type == DeepLinkSoConstant.DOUBLE) {
                    params[param.key] = value!!.toDouble()
                }
                // 默认是字符串
                else {
                    params[param.key] = value!!
                }
            }
        }
        return params
    }

    private fun needIntercept(option: DeepLinkSoOption, params: HashMap<String, Any>): Boolean {
        // 开始调用拦截器
        val interceptors = DeepLinkSoClient.config.interceptors
        if (interceptors != null && interceptors.size > 0) {
            for (interceptor in interceptors) {
                // 如果某个拦截器需要拦截，之后的拦截器也不会得到执行，页面直接销毁
                if (interceptor.intercept(this, option, params)) {
                    return true
                }
            }
        }
        return false
    }

    private fun deepLinkFailed() {
        // 把app回到前台
        DeepLinkSoNavigator.moveMainTaskToFront(this)
        finish()
    }
}
