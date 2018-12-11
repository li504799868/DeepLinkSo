package com.lzp.deeplinkso.handler

import android.content.Context
import com.lzp.deeplinkso.bean.DeepLinkSoRequest

/**
 * Created by li.zhipeng on 2018/12/11.
 *
 *      深度链接自定义事件Handler，启动非Activity
 *
 */
interface IDeepLinkSoEventHandler {

    fun handleDeepLinkSoRequest(context: Context, request: DeepLinkSoRequest)
}