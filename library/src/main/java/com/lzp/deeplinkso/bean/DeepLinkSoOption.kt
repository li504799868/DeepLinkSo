package com.lzp.deeplinkso.bean

/**
 * Created by li.zhipeng on 2018/12/6.
 *
 * DeepLink配置项
 */
class DeepLinkSoOption {

    var className: Class<*>? = null

    var page: String? = null

    var params: ArrayList<DeepLinkSoParam>? = null

}
