package com.lzp.deeplinkso.demo.interceptor

import android.content.Context
import android.widget.Toast
import com.lzp.deeplinkso.bean.DeepLinkSoOption
import com.lzp.deeplinkso.demo.DeepLinkManager
import com.lzp.deeplinkso.interceptor.IDeepLinkSoInterceptor

/**
 * Created by li.zhipeng on 2018/12/6.
 *
 *      自定义拦截器
 */
class TestInterceptor : IDeepLinkSoInterceptor {

    override fun intercept(context: Context, option: DeepLinkSoOption, params: HashMap<String, Any>): Boolean {
        Toast.makeText(context, option.page, Toast.LENGTH_LONG).show()
        // 这里拦截跳转并保存跳转信息
        if (option.page == "test") {
            DeepLinkManager.save(option, params)
            return true
        }

        return false
    }

}