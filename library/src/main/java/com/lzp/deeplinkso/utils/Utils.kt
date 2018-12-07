package com.lzp.deeplinkso.utils

import android.content.Context
import com.lzp.deeplinkso.activity.DeepLinkSoActivity

/**
 * Created by li.zhipeng on 2018/12/6.
 *
 *      工具类
 */
object Utils {

    /**
     * 检查字符串是否为空 Create at 2013-6-26
     *
     * @return boolean
     */
    fun isEmpty(txt: String?): Boolean {
        return txt == null || txt.trim { it <= ' ' }.isEmpty() || "null".equals(txt, ignoreCase = true)
    }

    /**
     * 检查字符串是否为空 Create at 2013-6-26
     *
     * @return boolean
     */
    fun isNotEmpty(txt: String): Boolean {
        return !isEmpty(txt)
    }

}