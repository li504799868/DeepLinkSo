package com.lzp.deeplinkso.demo

import android.content.Context
import com.lzp.deeplinkso.bean.DeepLinkSoOption
import com.lzp.deeplinkso.utils.DeepLinkSoNavigator

/**
 * Created by li.zhipeng on 2018/12/6.
 */
object DeepLinkManager {

    private var option: DeepLinkSoOption? = null

    private var params: HashMap<String, Any>? = null

    fun save(option: DeepLinkSoOption, params: HashMap<String, Any>) {
        this.option = option
        this.params = params

    }

    fun startActivity(context: Context) {
        if (option != null && params != null) {
            DeepLinkSoNavigator.startActivity(context, option!!, params!!)
            this.option = null
            this.params = null
        }
    }


}