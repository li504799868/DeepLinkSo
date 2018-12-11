package com.lzp.deeplinkso.demo.handler

import android.content.Context
import android.widget.Toast
import com.lzp.deeplinkso.bean.DeepLinkSoRequest
import com.lzp.deeplinkso.handler.IDeepLinkSoEventHandler

/**
 * Created by li.zhipeng on 2018/12/11.
 *
 *      接收深度链接的自定义事件
 */
class TestEventHandler: IDeepLinkSoEventHandler {

    override fun handleDeepLinkSoRequest(context: Context, request: DeepLinkSoRequest) {
        Toast.makeText(context, request.uri.toString(), Toast.LENGTH_SHORT).show()
    }
}