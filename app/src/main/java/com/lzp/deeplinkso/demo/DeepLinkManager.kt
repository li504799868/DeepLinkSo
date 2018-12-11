package com.lzp.deeplinkso.demo

import android.content.Context
import com.lzp.deeplinkso.bean.DeepLinkSoRequest
import com.lzp.deeplinkso.utils.DeepLinkSoNavigator

/**
 * Created by li.zhipeng on 2018/12/6.
 */
object DeepLinkManager {

    private var request: DeepLinkSoRequest? = null

    fun save(request: DeepLinkSoRequest) {
        this.request = request

    }

    fun startActivity(context: Context) {
        if (request != null) {
            DeepLinkSoNavigator.startActivity(context, request!!)
            this.request = null
        }
    }


}