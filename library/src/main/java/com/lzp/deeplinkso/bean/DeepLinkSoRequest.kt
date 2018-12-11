package com.lzp.deeplinkso.bean

import android.net.Uri

/**
 * Created by li.zhipeng on 2018/12/11.
 *
 *      深度链接跳转请求
 */
class DeepLinkSoRequest(val uri: Uri, var option: DeepLinkSoOption?, var params: HashMap<String, Any>?) {

    fun shouldSkipCommonInterceptors() = option?.skipCommonInterceptors ?: false

}