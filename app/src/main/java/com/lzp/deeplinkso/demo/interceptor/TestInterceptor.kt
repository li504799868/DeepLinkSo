package com.lzp.deeplinkso.demo.interceptor

import android.content.Context
import android.widget.Toast
import com.lzp.deeplinkso.bean.DeepLinkSoRequest
import com.lzp.deeplinkso.demo.DeepLinkManager
import com.lzp.deeplinkso.interceptor.IDeepLinkSoInterceptor

/**
 * Created by li.zhipeng on 2018/12/6.
 *
 *      自定义拦截器
 */
class TestInterceptor : IDeepLinkSoInterceptor {

    override fun intercept(context: Context, request: DeepLinkSoRequest): Boolean {
        Toast.makeText(context, request.option!!.page, Toast.LENGTH_LONG).show()
        // 这里拦截跳转并保存跳转信息
        if (request.option!!.page == "test") {
            DeepLinkManager.save(request)
            return true
        }

        return false
    }

}